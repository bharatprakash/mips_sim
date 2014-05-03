package InstructionClasses;

import processor.Parser;
import Exception.InvalidJumpException;

public class BEQ extends Instruction {
	
	public BEQ(String opcode, String[] operands) {
		super(opcode, operands);
		getRegisters();
	}

	@Override
	public void executeInstruction() {

		if(!Parser.loopMap.containsKey(label)){
			try {
				throw new InvalidJumpException();
			} catch (InvalidJumpException e) {
				System.out.println("Loop does not exist");
			}
		}
		else{
			if(sValue1==sValue2){
				jumpTo = Parser.loopMap.get(label);
				//CALL some function
			}
			else{
				//Continue execution
				return;
			}
		}

	}

	@Override
	public void getRegisters() {
		//HAVE CONSIDERED BOTH AS SOURCE
		//what about d?? -> have setDest = -1
		destinationRegister = -1;
		sourceRegister1 = Integer.parseInt(this.operands[0].substring(1));
		sourceRegister2 = Integer.parseInt(this.operands[1].substring(1));
		label = this.operands[2];
		destinationRegisterType = 0;
	}

	@Override
	public void lockDestination() {
		//not reuired
	}

	@Override
	public void unlockDestination() {
		//not reuired
	}

	@Override
	public void storeResult() {
		//not reuired
	}

	@Override
	public void getDataFromRegisters() {
		sValue1 = Parser.RegisterData[sourceRegister1];
		sValue2 = Parser.RegisterData[sourceRegister2];
	}

}
