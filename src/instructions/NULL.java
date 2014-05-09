package instructions;

import java.util.List;

public class NULL extends Instruction
{

    public NULL()
    {
        super();
        this.iType = InstructionType.NULL;
    }

    public NULL(NULL obj)
    {
        super(obj);
        setPrintableInstruction(obj.printableInstruction);
    }

    @Override
    public List<SourceObject> getSourceRegister()
    {
        // Do nothing here
        return null;
    }

    @Override
    public WriteBackObject getDestinationRegister()
    {
        // Do nothing here
        return null;
    }

    @Override
    public void executeInstruction()
    {
        // Do nothing here

    }

    @Override
    public String toString()
    {
        return "NULL";
    }

}
