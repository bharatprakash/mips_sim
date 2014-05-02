package Exception;

@SuppressWarnings("serial")
public class InvalidJumpException extends Exception{
	private String message ="EXCEPTION: Invalid Jump found";
	private int lineNumber;
	//EDIT THIS IN J.JAVA
	public InvalidJumpException(int instructionCount) {
		lineNumber = instructionCount;
	}

	public InvalidJumpException() {
		System.out.println(message);
	}

	public String getmMessage() {
		return message+ " at line number "+lineNumber;
	}
}
