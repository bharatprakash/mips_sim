package InstructionClasses;

public class SD extends Instruction {


	public SD(String opcode, String[] operands) {
		super(opcode, operands);
		getRegisters();
	}

	@Override
	public void executeInstruction() {
		// TODO Auto-generated method stub

	}



	@Override
	public void getRegisters() {
		destinationRegister = -1;//avoid waw
		sourceRegister1 = Integer.parseInt(this.operands[0].substring(1));
		sourceRegister2 = Integer.parseInt(this.operands[1].substring(this.operands[1].lastIndexOf('(')+2,this.operands[1].lastIndexOf(')')));
		destinationRegisterType = 1;//For F type instrucitons
	}

	@Override
	public void lockDestination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unlockDestination() {
		// TODO Auto-generated method stub

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
