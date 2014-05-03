package InstructionClasses;

import processor.Parser;
import processor.Processor;



public class LW extends Instruction{
	
	public int offset;
	public LW(String opcode, String[] operands) {
		super(opcode, operands);
		offset = 0;
		getRegisters();
	}


	@Override
	public void executeInstruction() {
		
		result = sValue1;
	}

	@Override
	public void getRegisters() {
		destinationRegister = Integer.parseInt(this.operands[0].substring(1));
		offset = Integer.parseInt(this.operands[1].substring(0, this.operands[1].lastIndexOf('(')));
		sourceRegister1 = Integer.parseInt(this.operands[1].substring(this.operands[1].lastIndexOf('(')+2,this.operands[1].lastIndexOf(')')));
		destinationRegisterType = 0;//For R type instrucitons
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
		Parser.RegisterData[destinationRegister] = result;
	}


	@Override
	public void getDataFromRegisters() {
		sValue1 = Parser.DataOfData[Parser.RegisterData[sourceRegister1]
				+offset-255];
		sValue2= -1;
	}
}
