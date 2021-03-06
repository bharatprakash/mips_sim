package instructions;

public class LD extends TwoRegImmediateInstruction
{
    public LD(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
        this.fu = FunctionalUnitType.IU;
        this.iType = InstructionType.MEMORY_FPREG;
    }

    public LD(LD obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "LD " + dest.getDestinationLabel() + ", " + immediate + "("
                + src1.getSourceLabel() + ")";
    }

    @Override
    public void executeInstruction()
    {
        this.address = immediate + src1.getSource();
    }

}
