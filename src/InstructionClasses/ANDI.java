package InstructionClasses;

import processor.Parser;
import processor.Processor;

public class ANDI extends Instruction {


	public ANDI(String opcode, String[] operands) {
		super(opcode, operands);
		getRegisters();
	}

	@Override
	public void executeInstruction() {
		result = sValue1&immediateValue;
	}

	@Override
	public void getRegisters() {
		destinationRegister = Integer.parseInt(this.operands[0].substring(1));
		sourceRegister1 = Integer.parseInt(this.operands[1].substring(1));
		//Assuming there is no #
		immediateValue = Integer.parseInt(this.operands[2].substring(1));
		destinationRegisterType = 0;
	}

	@Override
	public void lockDestination() {
		Processor.registerAccessStatus[0][destinationRegister] = false;
	}

	@Override
	public void unlockDestination() {
		Processor.registerAccessStatus[0][destinationRegister] = true;

	}

	@Override
	public void storeResult() {
		Parser.RegisterData[destinationRegister] = result;
	}

	@Override
	public void getDataFromRegisters() {
		sValue1 = Parser.RegisterData[sourceRegister1];
		sValue2=-1;//for decode to check raw hazard
	}

}
