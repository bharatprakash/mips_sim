package InstructionClasses;

import processor.Processor;

public class ADDD extends Instruction {


	public ADDD(String opcode, String[] operands) {
		super(opcode, operands);
		getRegisters();
	}

	@Override
	public void executeInstruction() {
		//you do nothing
	}


	@Override
	public void getRegisters() {
		destinationRegister = Integer.parseInt(this.operands[0].substring(1));
		sourceRegister1 = Integer.parseInt(this.operands[1].substring(1));
		sourceRegister2 = Integer.parseInt(this.operands[2].substring(1));
		
		destinationRegisterType = 1;//For F type instructions
		
	}

	@Override
	public void lockDestination() {
		Processor.registerAccessStatus[1][destinationRegister] = false;
	}

	@Override
	public void unlockDestination() {
		Processor.registerAccessStatus[1][destinationRegister] = true;
	}

	@Override
	public void storeResult() {
		//you do nothing
	}

	@Override
	public void getDataFromRegisters() {
		//you do nothing
	}

}
