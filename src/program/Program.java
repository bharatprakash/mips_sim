package program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import Exception.InCorrectInstructionFormat;
import instructions.ADDD;
import instructions.AND;
import instructions.ANDI;
import instructions.BEQ;
import instructions.BNE;
import instructions.DADD;
import instructions.DADDI;
import instructions.DIVD;
import instructions.DSUB;
import instructions.DSUBI;
import instructions.HLT;
import instructions.Instruction;
import instructions.J;
import instructions.LD;
import instructions.LW;
import instructions.MULD;
import instructions.OR;
import instructions.ORI;
import instructions.SD;
import instructions.SUBD;
import instructions.SW;


public class Program {
	
	public static int instructionCount;
	public static HashMap<Integer, Instruction> InstructionList = new HashMap<Integer, Instruction>();
	public static HashMap<String, Integer> LabelMap = new HashMap<String, Integer>();
	
	public void parse(String filePath) throws FileNotFoundException, IOException,
			InCorrectInstructionFormat {
		
		FileReader file = null;
		String line;
		Scanner scanner = null;
		try {
			scanner = new Scanner(getClass().getResourceAsStream(filePath));
			while (scanner.hasNext()) {
				line = scanner.nextLine();
				
				parseInstructionFile(line);
				
			}
			
		} finally {
			if (file != null) {
				try {
					file.close();
					scanner.close();
				} catch (IOException e) {
					throw new RuntimeException("IO error");
				}
			}
		}
	}	
	
	private void parseInstructionFile(String line)
			throws InCorrectInstructionFormat {
		Instruction inst = null;
		String tokens[] = new String[5];
		line = line.trim();
		String sourceRegister1, sourceRegister2, destinationRegister;
		String[] operands;
		int offset;
		int immediate;
		
		
		/* CHECK IF IT HAS A LOOP */
		if (line.contains(":")) {
			int index = line.lastIndexOf(':');
			String loopName = line.substring(0, index);
			line = line.substring(index + 1);
			line = line.trim();
			LabelMap.put(loopName.trim(), instructionCount);
		}

		/* SPLIT FOR RECOGNISING INSTRUCTION */
		tokens = line.split("[\\s]", 2);
		String opcode = tokens[0].trim().toUpperCase();

		switch (opcode) {
		case "LW":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			offset = Integer.parseInt(operands[1].substring(0, operands[1].lastIndexOf('(')));
			sourceRegister1 = operands[1].substring(operands[1].lastIndexOf('(')+1,operands[1].lastIndexOf(')'));
			inst = new LW(sourceRegister1,destinationRegister,offset);			
			break;
		case "L.D":			
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			offset = Integer.parseInt(operands[1].substring(0, operands[1].lastIndexOf('(')));
			sourceRegister1 = operands[1].substring(operands[1].lastIndexOf('(')+1,operands[1].lastIndexOf(')'));
			inst = new LD(sourceRegister1,destinationRegister,offset);
			break;
		case "SW":			
			operands =  getOperands(tokens);
			sourceRegister1 = operands[0];
			offset = Integer.parseInt(operands[1].substring(0, operands[1].lastIndexOf('(')));
			destinationRegister = operands[1].substring(operands[1].lastIndexOf('(')+1,operands[1].lastIndexOf(')'));
			inst = new SW(sourceRegister1,destinationRegister,offset);
			break;
		case "S.D":			
			operands =  getOperands(tokens);
			sourceRegister1 = operands[0];
			offset = Integer.parseInt(operands[1].substring(0, operands[1].lastIndexOf('(')));
			destinationRegister = operands[1].substring(operands[1].lastIndexOf('(')+1,operands[1].lastIndexOf(')'));
			inst = new SD(sourceRegister1,destinationRegister,offset);
			break;
		case "ADD.D":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			sourceRegister2 = operands[2];
			inst = new ADDD(sourceRegister1,sourceRegister2, destinationRegister);
			break;
		case "SUB.D":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			sourceRegister2 = operands[2];
			inst = new SUBD(sourceRegister1,sourceRegister2, destinationRegister);		
			break;
		case "MUL.D":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			sourceRegister2 = operands[2];
			inst = new MULD(sourceRegister1,sourceRegister2, destinationRegister);
			break;
		case "DIV.D":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			sourceRegister2 = operands[2];
			inst = new DIVD(sourceRegister1,sourceRegister2, destinationRegister);
			break;
		case "DADD":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			sourceRegister2 = operands[2];
			inst = new DADD(sourceRegister1,sourceRegister2, destinationRegister);
			break;
		case "DADDI":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			immediate = Integer.parseInt(operands[2]);
			inst = new DADDI(sourceRegister1,destinationRegister, immediate);
			break;
		case "DSUB":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			sourceRegister2 = operands[2];
			inst = new DSUB(sourceRegister1,sourceRegister2, destinationRegister);
			break;
		case "DSUBI":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			immediate = Integer.parseInt(operands[2]);
			inst = new DSUBI(sourceRegister1,destinationRegister, immediate);
			break;
		case "AND":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			sourceRegister2 = operands[2];
			inst = new AND(sourceRegister1,sourceRegister2, destinationRegister);
			break;
		case "ANDI":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			immediate = Integer.parseInt(operands[2]);
			inst = new ANDI(sourceRegister1,destinationRegister, immediate);
			break;
		case "OR":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			sourceRegister2 = operands[2];
			inst = new OR(sourceRegister1,sourceRegister2, destinationRegister);
			break;
		case "ORI":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			immediate = Integer.parseInt(operands[2]);
			inst = new ORI(sourceRegister1,destinationRegister, immediate);
			break;
		case "BEQ":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			sourceRegister2 = operands[2];
			inst = new BEQ(sourceRegister1,sourceRegister2, destinationRegister);
			break;
		case "BNE":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			sourceRegister1 = operands[1];
			sourceRegister2 = operands[2];
			inst = new BNE(sourceRegister1,sourceRegister2, destinationRegister);
			break;
		case "HLT":
			inst = new HLT();
			break;
		case "J":
			operands =  getOperands(tokens);
			destinationRegister = operands[0];
			inst = new J(destinationRegister);
			break;
		default:// TODO throw exception
			break;

		}
		
		InstructionList.put(instructionCount,inst);
		instructionCount++;
	}
	
	private String[] getOperands(String[] tokens)
			throws InCorrectInstructionFormat {

		String argListArray[] = new String[3];
		String arg1[] = new String[3];
		if (!tokens[0].equalsIgnoreCase("HLT")) {
			String argList = tokens[1];
			argListArray = argList.trim().split(",");
			for (int i = 0; i < argListArray.length; i++) {
				String arg = argListArray[i] = argListArray[i].trim();
				/* VALIDATE ARG */
				if (arg.charAt(0) != 'R' && arg.charAt(0) != 'F') {
					if (arg.charAt(0) < '0' || arg.charAt(0) > '9')
						if (!LabelMap.containsKey(arg))
							if (!tokens[0].equalsIgnoreCase("BEQ")
									&& !tokens[0].equalsIgnoreCase("BNE")
									&& !tokens[0].equalsIgnoreCase("J"))
								throw new InCorrectInstructionFormat(
										instructionCount);
				}
				arg1[i] = argListArray[i];
			}
		}
		return arg1;
	}

	

}
