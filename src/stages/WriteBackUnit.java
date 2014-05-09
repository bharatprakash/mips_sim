package stages;

import outputUtilities.ResultsUtility;
import inputUtilities.RegisterUtility;
import instructions.Instruction;
import instructions.NULL;
import instructions.WriteBackObject;
import stages.Processor;
import stages.StageType;

public class WriteBackUnit extends FunctionalUnit {

	static volatile WriteBackUnit instance = new WriteBackUnit();

	private WriteBackUnit() {
		super();
		isPipelined = false;
		cyclesRequired = 1;
		pipeSize = 1;
		stage = StageType.WB;
		createPipelineQueue(pipeSize);
	}

	@Override
	public void execute() throws Exception {
		Instruction inst = iQueue.peekFirst();

		if (inst instanceof NULL)
			return;

		WriteBackObject writeBackObject = inst.getDestinationRegister();

		if (writeBackObject != null) {

			RegisterUtility.instance.setRegisterValue(
					writeBackObject.getDestinationLabel(),
					writeBackObject.getDestination());
			RegisterUtility.instance.setRegisterFree(writeBackObject
					.getDestinationLabel());
		}

		inst.exitTime[this.stage.getId()] = Processor.CLOCK;
		ResultsUtility.instance.addInstruction(inst);

		rotateQueue();
	}

	@Override
	public int getClockCyclesRequiredForNonPipeLinedUnit() {
		// TODO Auto-generated method stub
		return cyclesRequired;
	}
}
