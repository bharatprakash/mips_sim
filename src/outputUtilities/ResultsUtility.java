package outputUtilities;

import inputUtilities.ProgramUtility;
import instructions.Instruction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import cache.ICacheUtility;
import stages.StageType;

public class ResultsUtility
{

    public static final ResultsUtility          instance       = new ResultsUtility();
    private final TreeMap<Integer, Instruction> instructionMap = new TreeMap<Integer, Instruction>();
    private BufferedWriter                      resultsWriter  = null;
    private boolean                             HALT           = false;

    private ResultsUtility()
    {
    }
    
    public void setResultsPath(String path) throws IOException
    {
        if (resultsWriter != null)
            return;
        resultsWriter = new BufferedWriter(new FileWriter(new File(path)));
    }

    public void printResults()
    {

        System.out.println(String.format(
                Utils.Constants.iFormatString1, "INST",
                "FT", "ID", "EX", "WB", "RAW", "WAR", "WAW", "Struct"));
        System.out.print("------------------------------------------");
        System.out.println("------------------------------------------");
        for (int key : instructionMap.keySet())
        {
            Instruction inst = instructionMap.get(key);
           
            System.out.println(inst.getOutputString());
        }
        System.out.println();
        System.out.println(ICacheUtility.instance.getICacheStatistics());
        
   }

    public void writeResults()
    {
        try
        {
            resultsWriter.write(String.format(
                    Utils.Constants.iFormatString1,
                    "Instruction", "FT", "ID", "EX", "WB", "RAW", "WAR", "WAW",
                    "Struct"));
            resultsWriter.newLine();
            resultsWriter.write("------------------------------------------");            
            resultsWriter.write("------------------------------------------");
            resultsWriter.newLine();
            for (int key : instructionMap.keySet())
            {
                Instruction inst = instructionMap.get(key);
                resultsWriter.write(inst.getOutputString());
                resultsWriter.newLine();
            }            
            resultsWriter.newLine();
            resultsWriter.write(ICacheUtility.instance.getICacheStatistics());
            resultsWriter.newLine();
            resultsWriter.flush();
            resultsWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addInstruction(Instruction instruction)
    {
        int key = instruction.enterTime[StageType.IF.getId()];
        instructionMap.put(key, instruction);

    }

    public void testPrintWithDummyData() throws Exception
    {
        int count = 0;
        for (Integer address : ProgramUtility.instance.InstructionList.keySet())
        {
            Instruction inst = ProgramUtility.instance
                    .getInstructionAtAddress(address);
            inst.enterTime[0] = count++;
            inst.exitTime[0] = count;
            inst.STRUCT = (count % 2 == 0) ? true : false;
            addInstruction(inst);
        }

        printResults();

    }

    public boolean isHALT()
    {
        return HALT;
    }

    public void setHALT(boolean halt)
    {
        this.HALT = halt;
    }
}
