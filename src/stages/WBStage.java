package stages;

import instructions.Instruction;

public class WBStage extends Stage
{

    public static volatile WBStage writeBackObj =  new WBStage();

    private WriteBackUnit writeBack;

    private WBStage()
    {
        super();

        this.stageType = StageType.WB;

        writeBack = WriteBackUnit.instance;
    }

    @Override
    public void execute() throws Exception
    {
        writeBack.execute();
    }

    @Override
    public boolean acceptInstruction(Instruction instruction) throws Exception
    {
        writeBack.acceptInstruction(instruction);
        return true;
    }

    @Override
    public boolean isStageFree(Instruction instruction) throws Exception
    {
        return writeBack.isQFree(instruction);
    }
}
