package instructions;

public class SW extends StoreInstruction
{

    public SW(String sourceLabel, String sourceLabel2, int immediate)
    {
        super(sourceLabel, sourceLabel2, immediate);
        this.fu = FunctionalUnitType.IU;
        this.iType = InstructionType.MEMORY_REG;
    }

    public SW(SW obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "SW " + src1.getSourceLabel() + ", " + immediate + "("
                + src2.getSourceLabel() + ")";
    }
}
