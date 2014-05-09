package stages;

import instructions.Instruction;

public abstract class Stage
{
    protected StageType stageType;

    public abstract void execute() throws Exception;

    public abstract boolean isStageFree(Instruction instruction)
            throws Exception;

    public boolean isStageFree() throws Exception
    {
        return isStageFree(null); 
    }
    
    public abstract boolean acceptInstruction(Instruction instruction)
            throws Exception;
}
