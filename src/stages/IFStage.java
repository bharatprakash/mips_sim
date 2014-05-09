package stages;

import instructions.Instruction;

public class IFStage extends Stage
{

    public static volatile IFStage fetchObj = new IFStage();

    
    // TO DELETE
//    public static FetchStage getInstance()
//    {
//
//        if (null == instance)
//            synchronized (FetchStage.class)
//            {
//                if (null == instance)
//                    instance = new FetchStage();
//            }
//
//        return instance;
//    }

    private FetchUnit fetch;

    private IFStage()
    {
        super();
        fetch = FetchUnit.instance;
        this.stageType = StageType.IF;
    }

    @Override
    public void execute() throws Exception
    {
        fetch.execute();
    }

    @Override
    public boolean isStageFree(Instruction instruction) throws Exception
    {
        return fetch.isQFree(instruction);
    }

    @Override
    public boolean acceptInstruction(Instruction instruction) throws Exception
    {
        if (!fetch.isQFree(instruction))
            throw new Exception("FetchStage: Illegal state exception "
                    + instruction.toString());

        fetch.acceptInstruction(instruction);
        return true;
    }

    public void flushStage() throws Exception
    {
        fetch.flushUnit();
    }

}
