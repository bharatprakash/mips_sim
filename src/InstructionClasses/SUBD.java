package InstructionClasses;

import processor.Processor;

public class SUBD extends Instruction {

	

	public SUBD(String opcode, String[] operands) {
		super(opcode, operands);
		getRegisters();
	}

	@Override
	public void executeInstruction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRegisters() {
		destinationRegister = Integer.parseInt(this.operands[0].substring(1));
		sourceRegister1 = Integer.parseInt(this.operands[1].substring(1));
		sourceRegister2 = Integer.parseInt(this.operands[2].substring(1));
		
		destinationRegisterType = 1;//For F type instrucitons
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
