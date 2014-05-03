package InstructionClasses;

import processor.Parser;
import Exception.InvalidJumpException;


public class J extends Instruction {
	

	public J(String opcode, String[] operands) {
		super(opcode, operands);
		getRegisters();
	}

	@Override
	public void executeInstruction() {
		if(!Parser.loopMap.containsKey(label)){
			try {
				throw new InvalidJumpException();
			} catch (InvalidJumpException e) {

			}
		}
		else{
			int programCounter = Parser.loopMap.get(label);
		}
	}

	
	@Override
	public void getRegisters() {
		label = this.operands[0];
		
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
