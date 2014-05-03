package InstructionClasses;

import processor.Processor;

public class LD extends Instruction {

	//public int offset;
	public LD(String opcode, String[] operands) {
		super(opcode, operands);
		//offset = 0;
		getRegisters();
	}

	@Override
	public void executeInstruction() {
		// TODO Auto-generated method stub

	}



	@Override
	public void getRegisters() {
		destinationRegister = Integer.parseInt(this.operands[0].substring(1));
		sourceRegister1 = Integer.parseInt(this.operands[1].substring(this.operands[1].lastIndexOf('(')+2,this.operands[1].lastIndexOf(')')));
		destinationRegisterType = 1;//For F type instrucitons
		sourceRegister2=-1;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void getDataFromRegisters() {
		// TODO Auto-generated method stub

	}

}
