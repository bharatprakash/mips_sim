package Exception;

@SuppressWarnings("serial")
public class InCorrectInstructionFormat extends Exception{
	private String message ="EXCEPTION: Incorrect Instruction format";
	private int lineNumber;
	
	public InCorrectInstructionFormat(int instructionCount) {
		lineNumber = instructionCount;
	}

	public String getmMessage() {
		return message+ " at line number "+lineNumber;
	}	
}
