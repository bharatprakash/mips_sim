package stages;

import outputUtilities.ResultsUtility;
import cache.ICacheUtility;
import instructions.Instruction;
import instructions.NULL;
import stages.Processor;
import stages.IDStage;
import stages.StageType;

public class FetchUnit extends FunctionalUnit
{

    static volatile FetchUnit instance = new FetchUnit();

    private FetchUnit()
    {
        super();
        this.isPipelined = false;
        this.cyclesRequired = 1;
        this.pipeSize = 1;
        this.stage = StageType.IF;
        createPipelineQueue(pipeSize);
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return cyclesRequired;
    }

    @Override
    public void execute() throws Exception
    {       
        Instruction inst = iQueue.peekFirst();
        boolean isNULL = inst instanceof NULL; 
        
        if (!isNULL)
        {
        	//System.out.println(Processor.CLOCK + " Fetch  " + inst.debugString());

            if (IDStage.decodeObj.isStageFree(inst))
            {
                IDStage.decodeObj.acceptInstruction(inst);
                inst.exitTime[this.stage.getId()] = Processor.CLOCK;
                rotateQueue();
            }
        }

        fetchNextInstruction();
    }

    
    
    public void flushUnit() throws Exception
    {
        checkQueue();

        Instruction inst = iQueue.peekFirst();
        
        if (inst instanceof NULL)
            return;

        inst.exitTime[this.stage.getId()] = Processor.CLOCK;        
        ResultsUtility.instance.addInstruction(inst);        
        rotateQueue();
        checkQueue();
    }

    private void fetchNextInstruction() throws Exception
    {
        if (isQFree(null))
        {
            boolean checkInst = false;

            Instruction newInst = null;
            
            newInst = ICacheUtility.getInstance().getInstructionFromCache(
					Processor.PC);
			if (newInst != null)
				checkInst = true;

            if (checkInst && isQFree(null))
            {
                acceptInstruction(newInst);
                Processor.PC++;
            }

        } // end ifStage.checkIfFree

    }
}
