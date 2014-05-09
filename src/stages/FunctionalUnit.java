package stages;

import instructions.Instruction;
import instructions.NULL;

import java.util.ArrayDeque;

import stages.Processor;
import stages.StageType;

public abstract class FunctionalUnit
{

    public boolean                  isPipelined;
    public int                      cyclesRequired;
    public int                      pipeSize;
    public StageType                stage;
    ArrayDeque<Instruction> iQueue;

    public abstract void execute() throws Exception;

    public abstract int getClockCyclesRequiredForNonPipeLinedUnit()
            throws Exception;

    public void acceptInstruction(Instruction instruction) throws Exception
    {
    	checkQueue();
        boolean check1 = (iQueue.peekLast() instanceof NULL) ? true : false;

        if (!check1)
            throw new Exception("FUnit - queue error");

        iQueue.removeLast();

        iQueue.addLast(instruction);
        
        instruction.enterTime[this.stage.getId()] = Processor.CLOCK;
        
    }

    protected void checkQueue() throws Exception
    {
        if (iQueue.size() != pipeSize)
            throw new Exception("FUnit - queue size error");
    }


    public boolean isQFree(Instruction instruction) throws Exception
    {
        checkQueue();
        return (iQueue.peekLast() instanceof NULL) ? true : false;

    }

    public boolean stageCompleted() throws Exception
    {
        if (isPipelined)
        {
            if (!(iQueue.peekFirst() instanceof NULL))
            {
                return true;
            }
        }
        else
        {
            if (!(iQueue.peekFirst() instanceof NULL)
                    && ((Processor.CLOCK - iQueue.peekFirst().enterTime[stage.getId()]) >= getClockCyclesRequiredForNonPipeLinedUnit()))
            {
                return true;
            }
        }

        return false;
    }

    protected Instruction[] iQueueToArray()
    {
        return (Instruction[]) iQueue
                .toArray(new Instruction[iQueue.size()]);
    }

    protected void createPipelineQueue(int size)
    {
        iQueue = new ArrayDeque<Instruction>();
        for (int i = 0; i < size; i++)
            iQueue.addLast(new NULL());
    }

    protected void rotateQueue() throws Exception
    {
        checkQueue();
        iQueue.removeFirst();
        iQueue.addLast(new NULL());
    }

   
}
