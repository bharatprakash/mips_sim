package InstructionClasses;

import processor.Parser;

public class SW extends Instruction {
	public int offset;
	public SW(String opcode, String[] operands) {
		super(opcode, operands);
		getRegisters();
		offset = 0;

	}

	@Override
	public void executeInstruction() {
		result = Parser.RegisterData[sValue1];
	}



	@Override
	public void getRegisters() {
		destinationRegister = -1;//avoid waw
		sourceRegister1 = Integer.parseInt(this.operands[0].substring(1));
		offset = Integer.parseInt(this.operands[1].substring(0, this.operands[1].lastIndexOf('(')));
		sourceRegister2 = Integer.parseInt(this.operands[1].substring(this.operands[1].lastIndexOf('(')+2,this.operands[1].lastIndexOf(')')));
		destinationRegisterType = 0;
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
		Parser.DataOfData[sValue2+offset-255] = result;
	}

	@Override
	public void getDataFromRegisters() {
		sValue1=Parser.RegisterData[sourceRegister1];
		sValue2=Parser.RegisterData[sourceRegister2];
	}

}
