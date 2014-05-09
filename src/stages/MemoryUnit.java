package stages;

import inputUtilities.ConfigUtility;
import inputUtilities.DataMemoryUtility;
import instructions.Instruction;
import instructions.InstructionType;
import instructions.NULL;
import instructions.StoreInstruction;
import stages.Processor;
import stages.StageType;
import stages.WBStage;

public class MemoryUnit extends FunctionalUnit {

	static volatile MemoryUnit instance = new MemoryUnit();

	private MemoryUnit() {
		super();
		isPipelined = false;
		cyclesRequired = ConfigUtility.instance.Memory;
		pipeSize = 1;
		stage = StageType.EX;
		createPipelineQueue(pipeSize);
	}

	@Override
	public void execute() throws Exception {
		checkQueue();

		Instruction inst = iQueue.peekFirst();
		if (!(inst instanceof NULL)) {
			if (!((Processor.CLOCK - inst.enterTime[stage.getId()]) >= this
					.getClockCyclesRequiredForNonPipeLinedUnit()))
				return;

			if (Instruction.isLoadStore(inst)) {
				if (inst instanceof StoreInstruction)
					DataMemoryUtility.instance.setValueToAddress(
							(int) inst.address, (int) ((StoreInstruction) inst)
									.getValueToWrite().getSource());
				else
					inst.getDestinationRegister().setDestination(
							DataMemoryUtility.instance
									.getValueFromAddress((int) inst.address));
			}

			if (!WBStage.writeBackObj.isStageFree(inst))
				throw new Exception(
						"Memory: EB not ready");

			WBStage.writeBackObj.acceptInstruction(inst);
			inst.exitTime[this.stage.getId()] = Processor.CLOCK;
		}
		rotateQueue();

	}

	@Override
	public int getClockCyclesRequiredForNonPipeLinedUnit() throws Exception {
		// TODO Auto-generated method stub
		Instruction inst = iQueue.peekFirst();
		if (inst.iType.equals(InstructionType.MEMORY_FPREG)
				|| inst.iType.equals(InstructionType.MEMORY_REG))
			return cyclesRequired;
		else if (inst.iType.equals(InstructionType.ARITHMETIC_REG)
				|| inst.iType.equals(InstructionType.ARITHMETIC_IMM))
			return 1;

		throw new Exception("Memory - wrong inst"+ inst.toString());
	}
	
}
