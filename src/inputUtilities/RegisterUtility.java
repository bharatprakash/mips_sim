package inputUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

public class RegisterUtility
{

    public static final RegisterUtility instance = new RegisterUtility();

    private RegisterUtility()
    {
        initializeRegisters();
    }

    Map<String, RegisterState>  registerStateMap = new TreeMap<String, RegisterState>();
    Map<String, Register<Long>> registerMap      = new TreeMap<String, Register<Long>>();

    public void initializeRegisters()
    {
        for (int i = 0; i < 32; i++)
        {
            String intRKey = "R" + i;
            String FPRKey = "F" + i;

            Register<Long> intR = new Register<Long>(intRKey);
            intR.value = 0L;
            Register<Long> fpR = new Register<Long>(FPRKey);
            fpR.value = 0L;

            registerMap.put(intRKey, intR);
            registerMap.put(FPRKey, fpR);

            registerStateMap.put(intRKey, RegisterState.IDLE);
            registerStateMap.put(FPRKey, RegisterState.IDLE);
        }
    }

    public void setRegisterValue(String label, long val) throws Exception
    {
        label = isCorrectRegName.getValidRegisterName(label);
        Register<Long> reg = registerMap.get(label);
        reg.value = (long) val;
    }

    public long getRegisterValue(String label) throws Exception
    {
        label = isCorrectRegName.getValidRegisterName(label);
        Register<Long> reg = registerMap.get(label);
        return reg.value;
    }

    public boolean isRegisterFree(String label) throws Exception
    {
        label = isCorrectRegName.getValidRegisterName(label);
        RegisterState reg = registerStateMap.get(label);
        return reg.equals(RegisterState.IDLE);
    }

    public void setRegisterFree(String label) throws Exception
    {
        label = isCorrectRegName.getValidRegisterName(label);
        registerStateMap.put(label, RegisterState.IDLE);
    }

    public void setRegisterBusy(String label) throws Exception
    {
        label = isCorrectRegName.getValidRegisterName(label);
        registerStateMap.put(label, RegisterState.BUSY);
    }

    public void dumpAllRegisters()
    {
        String leftAlignFormat = "| %-5s | %-5s | %-10d |%n";
        for (String label : registerMap.keySet())
        {
            Register<Long> reg = registerMap.get(label);
            System.out.format(leftAlignFormat, label,
                    registerStateMap.get(label), reg.value);
        }
    }
    
    

    public static void parseRegister(String fileName) throws Exception
    {
        BufferedReader bfread = null;
        try
        {

            bfread = new BufferedReader(new FileReader(new File(fileName)));

            String line = null;
            int count = 0;
            while ((line = bfread.readLine()) != null)
            {
                line = line.trim();
                if (line.length() == 0)
                    throw new Exception(
                            "Integer register data "
                                    + count);
                int value = Integer.parseInt(line, 2);
                RegisterUtility.instance.setRegisterValue("R" + count, value);

                count++;

                if (count == 32)
                    break;
            }

        }
        finally
        {
            if (bfread != null)
                bfread.close();
        }

    }

    
    //Inner Classes for registers
    public class Register<T>
    {
        String label;

        T      value;

        public Register(String label)
        {
            this.label = label;
        }

    }
    
    public enum RegisterType
    {
        INT, FP
    }

    public enum RegisterState
    {
        IDLE, BUSY
    }
   
    

}
