package processor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Exception.InCorrectInstructionFormat;
import FunctionalUnit.AdditionUnit;
import FunctionalUnit.DivisionUnit;
import FunctionalUnit.IntegerUnit;
import FunctionalUnit.MultiplicationUnit;
import InstructionClasses.ADDD;
import InstructionClasses.AND;
import InstructionClasses.ANDI;
import InstructionClasses.BEQ;
import InstructionClasses.BNE;
import InstructionClasses.DADD;
import InstructionClasses.DADDI;
import InstructionClasses.DIVD;
import InstructionClasses.DSUB;
import InstructionClasses.DSUBI;
import InstructionClasses.HLT;
import InstructionClasses.Instruction;
import InstructionClasses.J;
import InstructionClasses.LD;
import InstructionClasses.LW;
import InstructionClasses.MULD;
import InstructionClasses.OR;
import InstructionClasses.ORI;
import InstructionClasses.SD;
import InstructionClasses.SUBD;
import InstructionClasses.SW;

public class Parser {

	// Information about the data in memory from data.txt
	public static int DataOfData[] = new int[32];
	public int dataDataSize = 0;

	// Information about the data in registers from reg.txt
	public static int RegisterData[] = new int[32];
	public int registerDataSize = 0;

	// Configuration File parameters
	public static int adderClockCycle = 0;
	public static int multiplierClockCycle = 0;
	public static int dividerCockCycle = 0;
	public static int memoryClockCycle = 0;
	public static int iCacheClockCycle = 0;
	public static int dCacheClockCycle = 0;
	public static boolean isAdderPipelined = false;
	public static boolean isMultiplierPipelined = false;
	public static boolean isDividerPipelined = false;

	// Instruction file parameters
	public static ArrayList<Instruction> instructionList = new ArrayList<Instruction>();
	public static HashMap<String, Integer> loopMap = new HashMap<>();
	public int instructionCount = 0;

	// Functional Units
	public static IntegerUnit integerUnit;
	public static AdditionUnit additionUnit;
	public static MultiplicationUnit multiplicationUnit;
	public static DivisionUnit divisionUnit;

	public void parse(String fileName, boolean isDataFile, int filePointer)
			throws FileNotFoundException, IOException,
			InCorrectInstructionFormat {

		FileReader file = null;
		String line;
		Scanner scanner = null;
		try {
			scanner = new Scanner(getClass().getResourceAsStream(fileName));
			while (scanner.hasNext()) {
				line = scanner.nextLine();
				switch (filePointer) {
				case 1:
					parseInstructionFile(line);
					break;
				case 2:
					parseConfigurationFile(line);
					break;
				case 3:
					convertBinaryToDecimal(line, isDataFile);
					break;
				case 4:
					convertBinaryToDecimal(line, isDataFile);
					break;
				}

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

		/* CHECK IF IT HAS A LOOP */
		if (line.contains(":")) {
			int index = line.lastIndexOf(':');
			String loopName = line.substring(0, index);
			line = line.substring(index + 1);
			line = line.trim();
			loopMap.put(loopName.trim(), instructionCount);
		}

		/* SPLIT FOR RECOGNISING INSTRUCTION */
		tokens = line.split("[\\s]", 2);
		String opcode = tokens[0].trim().toUpperCase();

		switch (opcode) {
		case "LW":
			inst = new LW("LW", getOperands(tokens));
			break;
		case "L.D":
			inst = new LD("L.D", getOperands(tokens));
			break;
		case "SW":
			inst = new SW("SW", getOperands(tokens));
			break;
		case "S.D":
			inst = new SD("S.D", getOperands(tokens));
			break;
		case "ADD.D":
			inst = new ADDD("ADD.D", getOperands(tokens));
			break;
		case "SUB.D":
			inst = new SUBD("SUB.D", getOperands(tokens));
			break;
		case "MUL.D":
			inst = new MULD("MUL.D", getOperands(tokens));
			break;
		case "DIV.D":
			inst = new DIVD("DIV.D", getOperands(tokens));
			break;
		case "DADD":
			inst = new DADD("DADD", getOperands(tokens));
			break;
		case "DADDI":
			inst = new DADDI("DADDI", getOperands(tokens));
			break;
		case "DSUB":
			inst = new DSUB("DSUB", getOperands(tokens));
			break;
		case "DSUBI":
			inst = new DSUBI("DSUBI", getOperands(tokens));
			break;
		case "AND":
			inst = new AND("AND", getOperands(tokens));
			break;
		case "ANDI":
			inst = new ANDI("ANDI", getOperands(tokens));
			break;
		case "OR":
			inst = new OR("OR", getOperands(tokens));
			break;
		case "ORI":
			inst = new ORI("ORI", getOperands(tokens));
			break;
		case "BEQ":
			inst = new BEQ("BEQ", getOperands(tokens));
			break;
		case "BNE":
			inst = new BNE("BNE", getOperands(tokens));
			break;
		case "HLT":
			inst = new HLT("HLT", getOperands(tokens));
			break;
		case "J":
			inst = new J("J", getOperands(tokens));
			break;
		default:// TODO throw exception
			break;

		}
		instructionList.add(inst);
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
						if (!loopMap.containsKey(arg))
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

	public void convertBinaryToDecimal(String line, boolean isMemorydata) {
		if (isMemorydata) {
			DataOfData[dataDataSize] = Integer.parseInt(line, 2);
			dataDataSize++;
		} else {
			RegisterData[registerDataSize] = Integer.parseInt(line, 2);
			registerDataSize++;
		}
	}

	public void parseConfigurationFile(String line) {
		String s[];
		line = line.replace(',', ':');
		s = line.split(":");

		for (int i = 0; i < s.length; i++) {
			String key = s[i].trim().toUpperCase();

			switch (key) {
			case "FP ADDER":
				i++;
				adderClockCycle = Integer.parseInt(s[i].trim());
				i++;
				s[i] = s[i].trim();
				isAdderPipelined = s[i].equalsIgnoreCase("yes");

				break;

			case "FP MULTIPLIER":
				i++;
				multiplierClockCycle = Integer.parseInt(s[i].trim());
				i++;
				s[i] = s[i].trim();
				isMultiplierPipelined = s[i].equalsIgnoreCase("yes");
				break;

			case "FP DIVIDER":
				i++;
				dividerCockCycle = Integer.parseInt(s[i].trim());
				i++;
				s[i] = s[i].trim();
				isDividerPipelined = s[i].equalsIgnoreCase("yes");
				break;

			case "MAIN MEMORY":
				i++;
				memoryClockCycle = Integer.parseInt(s[i].trim());
				break;

			case "I-CACHE":
				i++;
				iCacheClockCycle = Integer.parseInt(s[i].trim());
				break;

			case "D-CACHE":
				i++;
				dCacheClockCycle = Integer.parseInt(s[i].trim());
				break;
			default:
				break;
			}
		}
	}

	public void setFunctionalUnitParameters() {
		integerUnit = new IntegerUnit(false, 2, false);
		multiplicationUnit = new MultiplicationUnit(isMultiplierPipelined,
				multiplierClockCycle, false);
		divisionUnit = new DivisionUnit(isDividerPipelined, dividerCockCycle, false);
		additionUnit = new AdditionUnit(isAdderPipelined, adderClockCycle, false);
	}
}
