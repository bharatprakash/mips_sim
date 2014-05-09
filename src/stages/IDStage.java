package stages;

import instructions.Instruction;

public class IDStage extends Stage
{

    public static volatile IDStage decodeObj = new IDStage();

    // TO  - DELETE
    /*public static DecodeStage getInstance()
    {

        if (null == instance)
            synchronized (DecodeStage.class)
            {
                if (null == instance)
                    instance = new DecodeStage();
            }

        return instance;
    }*/

    private DecodeUnit decode;

    private IDStage()
    {
        super();
        decode = DecodeUnit.instance;
        this.stageType = StageType.ID;
    }

    @Override
    public void execute() throws Exception
    {
        decode.execute();
    }

    @Override
    public boolean isStageFree(Instruction instruction) throws Exception
    {
        return decode.isQFree(instruction);
    }

    @Override
    public boolean acceptInstruction(Instruction instruction) throws Exception
    {
        if (!decode.isQFree(instruction))
            throw new Exception("DECODESTAGE: Illegal state exception "
                    + instruction.toString());

        decode.acceptInstruction(instruction);

        return true;
    }

}
