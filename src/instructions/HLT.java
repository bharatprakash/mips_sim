package instructions;

import java.util.List;

public class HLT extends Instruction
{
    public HLT()
    {
        super();
        this.iType = InstructionType.HALT;
    }

    public HLT(HLT obj)
    {
        super(obj);
        setPrintableInstruction(obj.printableInstruction);
    }

    @Override
    public List<SourceObject> getSourceRegister()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WriteBackObject getDestinationRegister()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString()
    {
        return "HLT";
    }

    @Override
    public void executeInstruction()
    {
        // Do nothing here

    }

}
