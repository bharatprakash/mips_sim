package InstructionClasses;

public abstract class Instruction {
		
	public String opcode;
	public String operands[];
	public int clockCycleStages[];
	public int enterClckCyclePosition[];
	public String inWhichStage;
	public boolean isBusy;
	public int result;
	public int destinationRegister, sourceRegister1, sourceRegister2;
	public char destinationRegisterType;
	public int dValue, sValue1, sValue2;
	public int immediateValue;
	public String label;
	public int jumpTo;
	public int delay;

	public Instruction(String opcode, String[] operands) {
		this.opcode = opcode;
		this.operands = operands;
		clockCycleStages = new int[6];
		enterClckCyclePosition = new int[2];
		isBusy = false;
		result = dValue = sValue1 = sValue2 = immediateValue = delay = 0;
		label = "";
		jumpTo = -1;
	}

	public abstract void executeInstruction();

	public abstract void lockDestination();

	public abstract void unlockDestination();

	public abstract void getRegisters();

	public abstract void storeResult();

	public abstract void getDataFromRegisters();
}
