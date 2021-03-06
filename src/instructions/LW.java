package instructions;

public class LW extends TwoRegImmediateInstruction
{

    public LW(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
        this.fu = FunctionalUnitType.IU;
        this.iType = InstructionType.MEMORY_REG;
    }

    public LW(LW obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "LW " + dest.getDestinationLabel() + ", " + immediate + "("
                + src1.getSourceLabel() + ")";
    }

    @Override
    public void executeInstruction()
    {
        this.address = immediate + src1.getSource();
    }

}
