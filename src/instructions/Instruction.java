package instructions;

import java.util.Arrays;

public abstract class Instruction implements InstructionI
{
    
    public boolean            RAW;
    public boolean            WAR;
    public boolean            WAW;
    public boolean            STRUCT;
    public int[]              enterTime;
    public int[]              exitTime;
    public FunctionalUnitType fu;
    public InstructionType    iType;
    public String             printableInstruction;
    public long               address;         

    public Instruction()
    {
        super();
        this.enterTime = new int[4];
        this.exitTime = new int[4];
        this.RAW = false;
        this.WAR = false;
        this.WAW = false;
        this.STRUCT = false;
        this.fu = FunctionalUnitType.UNKNOWN;
        this.iType = InstructionType.UNKNOWN;
    }

    public Instruction(Instruction obj)
    {
        super();
        this.RAW = obj.RAW;
        this.WAR = obj.WAR;
        this.WAW = obj.WAW;
        this.STRUCT = obj.STRUCT;
        this.enterTime = new int[obj.enterTime.length];
        this.exitTime = new int[obj.exitTime.length];
        System.arraycopy(obj.enterTime, 0, this.enterTime, 0,
                this.enterTime.length);
        System.arraycopy(obj.exitTime, 0, this.exitTime, 0,
                this.exitTime.length);
        this.fu = obj.fu;
        this.iType = obj.iType;
    }
    
    public void setPrintableInstruction(String str)
    {
        this.printableInstruction = str;
    }

    public String debugString()
    {
        return String.format(Utils.Constants.iFormatString2,
                printableInstruction, Arrays.toString(enterTime),
                Arrays.toString(exitTime), RAW ? 'Y' : 'N', WAR ? 'Y' : 'N',
                WAW ? 'Y' : 'N', STRUCT ? 'Y' : 'N');
    }

    public String getOutputString()
    {
        return String.format(Utils.Constants.iFormatString1,
                printableInstruction, exitTime[0] != 0 ? exitTime[0] : "",
                exitTime[1] != 0 ? exitTime[1] : "",
                exitTime[2] != 0 ? exitTime[2] : "",
                exitTime[3] != 0 ? exitTime[3] : "", RAW ? 'Y' : 'N',
                WAR ? 'Y' : 'N', WAW ? 'Y' : 'N', STRUCT ? 'Y' : 'N');
    }

    public static boolean isLoadStore(Instruction inst)
    {
        return (inst.iType.equals(InstructionType.MEMORY_FPREG) || inst.iType
                .equals(InstructionType.MEMORY_REG)) ? true : false;
    }

    public static boolean isStore(Instruction inst)
    {
        return (isLoadStore(inst) && (inst instanceof StoreInstruction));
    }

}
