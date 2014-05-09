package simulator;

import outputUtilities.ResultsUtility;
import inputUtilities.ConfigUtility;
import inputUtilities.DataMemoryUtility;
import inputUtilities.ProgramUtility;
import inputUtilities.RegisterUtility;
import stages.Processor;
import stages.IDStage;
import stages.EXStage;
import stages.IFStage;
import stages.WBStage;

public class Simulator
{
    /**
     * 
     * @param args
     *            inst.txt data.txt reg.txt config.txt result.txt
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {

    	ProgramUtility.parse(args[0]);
        
        DataMemoryUtility.parseMemoryFile(args[1]);
        
        RegisterUtility.parseRegister(args[2]);
       
        ConfigUtility.parseConfigFile(args[3]);
        
        ResultsUtility.instance.setResultsPath(args[4]);
        
        Processor.CLOCK = 0;
        Processor.PC = 0;
      
        WBStage WB = WBStage.writeBackObj;
        EXStage EX = EXStage.excuteObj;
        IDStage ID = IDStage.decodeObj;
        IFStage IF = IFStage.fetchObj;

        try
        {
            
            int extraCLKCount = 4000;
            while (extraCLKCount != 0)
            {

                WB.execute();
                EX.execute();
                
                if (!ResultsUtility.instance.isHALT())
                {
                    ID.execute();

                    if (!ResultsUtility.instance.isHALT())
                    {
                        IF.execute();
                    }
                }
                else
                    extraCLKCount--;

                Processor.CLOCK++;
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR at time" + Processor.CLOCK);
            e.printStackTrace();
        }
        finally
        {
        }
        
        Thread.sleep(1000L);
        
        ResultsUtility.instance.printResults();
        ResultsUtility.instance.writeResults();
        
    }
}
