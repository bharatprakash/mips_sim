package stages;

import instructions.Instruction;
import instructions.NULL;
import stages.StageType;

public class IntegerUnit extends FunctionalUnit
{

    static volatile IntegerUnit instance = new IntegerUnit();  
    
    private IntegerUnit()
    {
        super();
        isPipelined = false;
        cyclesRequired = 1;
        pipeSize = 1;
        stage = StageType.EX;
        createPipelineQueue(pipeSize);

    }

    @Override
    public void execute() throws Exception
    {
        checkQueue();

        Instruction inst = iQueue.peekFirst();

        if (inst instanceof NULL)
            return;

        inst.executeInstruction();

        if (MemoryUnit.instance.isQFree(inst))
        {
            MemoryUnit.instance.acceptInstruction(inst);
            inst.exitTime[this.stage.getId()] = Processor.CLOCK;
            rotateQueue();
        }
        else
        {
        	iQueue.peekFirst().STRUCT = true;
        }

    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return cyclesRequired;
    }
}
