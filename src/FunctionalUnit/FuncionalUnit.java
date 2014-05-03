package FunctionalUnit;

import instructions.Instruction;


public abstract class FuncionalUnit {
	
	boolean isPipelined;
	boolean isAvailable;
	int clockCyclesRequired;
	
	
	
	public abstract void processFunctionalUnit();
	
	
}
