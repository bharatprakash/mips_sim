package instructions;

public class ORI extends TwoRegImmediateInstruction
{

    public ORI(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
        this.fu = FunctionalUnitType.IU;
        this.iType = InstructionType.ARITHMETIC_IMM;
    }

    public ORI(ORI obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "ORI " + dest.getDestinationLabel() + ", "
                + src1.getSourceLabel() + ", " + immediate;
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() | immediate);
    }

}
