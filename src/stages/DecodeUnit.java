package stages;

import inputUtilities.ProgramUtility;
import inputUtilities.RegisterUtility;
import instructions.BEQ;
import instructions.BNE;
import instructions.ConditionalBranchInstruction;
import instructions.FunctionalUnitType;
import instructions.HLT;
import instructions.Instruction;
import instructions.J;
import instructions.NULL;
import instructions.SourceObject;
import instructions.WriteBackObject;

import java.util.List;

import outputUtilities.ResultsUtility;
import stages.Processor;
import stages.EXStage;
import stages.IFStage;
import stages.StageType;

public class DecodeUnit extends FunctionalUnit
{

    static volatile DecodeUnit instance = new DecodeUnit();

    private DecodeUnit()
    {
        super();
        isPipelined = false;
        cyclesRequired = 1;
        pipeSize = 1;
        stage = StageType.ID;
        createPipelineQueue(pipeSize);
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return cyclesRequired;
    }

    @Override
    public void execute() throws Exception
    {
        Instruction inst = iQueue.peekFirst();

        if (inst instanceof NULL)
            return;

        boolean hazards = processRAW(inst) || processWAR(inst) || processWAW(inst) || processStruct(inst);

        if (!hazards)
            executeDecode(inst);

    }

    private void executeDecode(Instruction inst) throws Exception
    {

    	inst.exitTime[this.stage.getId()] = Processor.CLOCK;
        
        ResultsUtility.instance.addInstruction(inst);
        
        List<SourceObject> sources = inst.getSourceRegister();
        if (sources != null)
        {
            for (SourceObject register : sources)
            {
                register.setSource(RegisterUtility.instance
                        .getRegisterValue(register.getSourceLabel()));
            }
        }

        // lock destination
        WriteBackObject destReg = inst.getDestinationRegister();
        if (destReg != null)
            RegisterUtility.instance.setRegisterBusy(destReg
                    .getDestinationLabel());

        if (inst instanceof J)
        {            
            Processor.PC = ProgramUtility.instance
                    .getInstructionAddreessForLabel(((J) inst)
                            .getDestinationLabel());

            IFStage.fetchObj.flushStage();

        }
        
        else if (inst instanceof ConditionalBranchInstruction)
        {
            if (inst instanceof BEQ)
            {
                if (((ConditionalBranchInstruction) inst).compareRegisters())
                {
                    // update PC
                    Processor.PC = ProgramUtility.instance
                            .getInstructionAddreessForLabel(((BEQ) inst)
                                    .getDestinationLabel());
                    // Flush fetch stage
                    IFStage.fetchObj.flushStage();
                }
            }
            else if (inst instanceof BNE)
            {
                if (!((ConditionalBranchInstruction) inst).compareRegisters())
                {
                    // update PC
                    Processor.PC = ProgramUtility.instance
                            .getInstructionAddreessForLabel(((BNE) inst)
                                    .getDestinationLabel());
                    // Flush fetch stage
                    IFStage.fetchObj.flushStage();
                }
            }
        }
        
        else if (inst instanceof HLT)
        {
            ResultsUtility.instance.setHALT(true);
        }
        else
        {

            if (!EXStage.excuteObj.isStageFree(inst))
                throw new Exception(
                        "DecodeUnit: failed in exstage.checkIfFree after resolving struct hazard "
                                + inst.toString());

            EXStage.excuteObj.acceptInstruction(inst);

        }

        rotateQueue();
    }

    private boolean processStruct(Instruction inst) throws Exception
    {        
        FunctionalUnitType type = inst.fu;
        if (!type.equals(FunctionalUnitType.UNKNOWN))
        {
            if (!(EXStage.excuteObj.isStageFree(inst)))
            {
                inst.STRUCT = true;
                return true;
            }
        }

        return false;
    }

    private boolean processRAW(Instruction inst) throws Exception
    {
        // Check for possible RAW hazards

        List<SourceObject> sources = inst.getSourceRegister();
        if (sources != null)
        {
            for (SourceObject register : sources)
            {
                if (!RegisterUtility.instance.isRegisterFree(register
                        .getSourceLabel()))
                {
                    inst.RAW = true;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean processWAW(Instruction inst) throws Exception
    {
        WriteBackObject dest = inst.getDestinationRegister();
        if (dest != null)
        {

            if (!RegisterUtility.instance.isRegisterFree(dest
                    .getDestinationLabel()))
            {
                inst.WAW = true;
                return true;
            }
        }
        return false;
    }

    private boolean processWAR(Instruction inst)
    {
        return false;
    }

    private boolean processHazards(Instruction inst) throws Exception
    {
        return (processRAW(inst) || processWAR(inst) || processWAW(inst) || processStruct(inst));
    }
}
