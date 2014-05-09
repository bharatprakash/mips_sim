package stages;

import instructions.DADDI;
import instructions.HLT;
import instructions.Instruction;
import instructions.J;
import instructions.NULL;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;

import stages.WBStage;

public abstract class FPFunctionalUnit extends FunctionalUnit
{

    @Override
    public void execute() throws Exception
    {
        checkQueue();

        Instruction inst = iQueue.peekFirst();
        inst.executeInstruction();
        
        if (!(inst instanceof NULL))
        {
            if (stageCompleted())
            {
                if (!WBStage.writeBackObj.isStageFree(inst))
                    throw new Exception(this.getClass().getSimpleName()
                            + "WB Stage not ready");

                WBStage.writeBackObj.acceptInstruction(inst);
                inst.exitTime[this.stage.getId()] = Processor.CLOCK;
            }
            else if (!isPipelined)
            {
                return;
            }
        }
        
        rotateQueue();
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return cyclesRequired;
    }

    public void rotatePipelineOnHazard() throws Exception
    {
        checkQueue();
        if (!isPipelined)
            return;
      
        Instruction objects[] = iQueueToArray();

        for (int i = 0; i < objects.length - 1; i++)
        {
            if (objects[i] instanceof NULL)
            {
                Instruction temp = objects[i];
                objects[i] = objects[i + 1];
                objects[i + 1] = temp;
            }
        }

        createPipelineQueue(0);
        for (int i = 0; i < objects.length; i++)
        {
        	iQueue.addLast(objects[i]);
        }
        checkQueue();
    }

    public static void main(String[] args)
    {
        ArrayDeque<Instruction> deque = new ArrayDeque<Instruction>();
        deque.add(new HLT());
        deque.add(new NULL());
        deque.add(new J("Jump"));
        deque.add(new NULL());
        deque.add(new NULL());
        deque.add(new DADDI("src1", "src2", 123));

        Instruction[] objects = (Instruction[]) deque
                .toArray(new Instruction[deque.size()]);

        System.out.println(Arrays.toString(objects));

        for (int i = 0; i < objects.length - 1; i++)
        {
            if (objects[i] instanceof NULL)
            {
                Instruction temp = objects[i];
                objects[i] = objects[i + 1];
                objects[i + 1] = temp;
            }
        }

        System.out.println(Arrays.toString(objects));

        deque = new ArrayDeque<Instruction>();
        for (Instruction instruction : objects)
        {
            deque.add(instruction);
        }

        for (Iterator<Instruction> itr = deque.iterator(); itr.hasNext();)
        {
            System.out.print(itr.next().toString() + " ");
        }

    }
}
