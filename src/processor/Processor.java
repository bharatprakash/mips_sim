package processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
import pipelineStages.Decode;
import pipelineStages.Execute;
import pipelineStages.Fetch;
import pipelineStages.Memory;
import pipelineStages.WriteBack;
*/
import Exception.InCorrectInstructionFormat;

//import InstructionClasses.Instruction;


public class Processor {

	// System data file paths
	private static final String INSTRUCTION_FILE = "inst.txt";
	private static final String CONFIGURATION_FILE = "config.txt";
	private static final String DATA_FILE = "data.txt";
	private final static String REGISTER_FILE = "reg.txt";

	// Global clock
	private static int systemClock = 1;

	// Maintain access status.
	// 0 for R
	// 1 for F
	// Busy = false
	// Free = true
	public static boolean registerAccessStatus[][] = new boolean[2][32];

	// Pipeline stages    
	/*
	public static Fetch fetch = new Fetch(false);
	public static Decode decode = new Decode(false);
	public static Execute execute = new Execute(false);
	public static Memory memory = new Memory(false);
	public static WriteBack writeback = new WriteBack(false);
	
	public static Instruction iCache[][] = new Instruction[4][4];

	*/
	
	
	public static Parser parser;
	public static ArrayList<String> instructionFunctionalUnitMap;
	public static HashMap<String, ArrayList<String>> instructionFunctionalUnit = new HashMap<String, ArrayList<String>>();

	public static void startUp() {

		// 1.We set all the registers as free
		setRegisterAccessStatus();

		// 2. Set the parser
		parser = new Parser();

		// 3. We need to maintain a mapping of instructions to their respective
		// functional units and get data from the files and set the functional
		// unit parameters
		setList();

		// 4. We now start processing
		//startProcessorExecution();
	}

	private static void setList() {
		try {
			instructionFunctionalUnitMap = new ArrayList<String>();

			// Instructions for Addition Unit
			instructionFunctionalUnitMap.add("ADD.D");
			instructionFunctionalUnitMap.add("SUB.D");
			instructionFunctionalUnit.put("AdditionUnit",
					instructionFunctionalUnitMap);
			instructionFunctionalUnitMap
					.removeAll(instructionFunctionalUnitMap);

			// Instructions for Integer Unit
			instructionFunctionalUnitMap.add("DADD");
			instructionFunctionalUnitMap.add("DADDI");
			instructionFunctionalUnitMap.add("DSUB");
			instructionFunctionalUnitMap.add("DSUBI");
			instructionFunctionalUnitMap.add("AND");
			instructionFunctionalUnitMap.add("ANDI");
			instructionFunctionalUnitMap.add("OR");
			instructionFunctionalUnitMap.add("ORI");
			instructionFunctionalUnitMap.add("LW");
			instructionFunctionalUnitMap.add("L.D");
			instructionFunctionalUnitMap.add("SW");
			instructionFunctionalUnitMap.add("S.D");
			instructionFunctionalUnit.put("IntegerUnit",
					instructionFunctionalUnitMap);
			instructionFunctionalUnitMap
					.removeAll(instructionFunctionalUnitMap);

			// Instructions for Multiplication Unit
			instructionFunctionalUnitMap.add("MUL.D");
			instructionFunctionalUnit.put("MultiplicationUnit",
					instructionFunctionalUnitMap);
			instructionFunctionalUnitMap
					.removeAll(instructionFunctionalUnitMap);

			// Instructions for Division Unit
			instructionFunctionalUnitMap.add("DIV.D");
			instructionFunctionalUnit.put("DivionUnit",
					instructionFunctionalUnitMap);
			instructionFunctionalUnitMap
					.removeAll(instructionFunctionalUnitMap);

			parser.parse(CONFIGURATION_FILE, false, 2);
			parser.parse(REGISTER_FILE, false, 3);
			parser.parse(DATA_FILE, true, 4);

			parser.parse(INSTRUCTION_FILE, false, 1);
			parser.setFunctionalUnitParameters();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.getMessage();
		} catch (InCorrectInstructionFormat e) {
			System.out.println(e.getmMessage());
		}
	}

	private static void setRegisterAccessStatus() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 32; j++) {
				registerAccessStatus[i][j] = true;
			}
		}
	}

	/*
	private static void startProcessorExecution() {

		int instPtr = 4;
		int i = 0, j = 0;
		Boolean HIT = false;

		while (systemClock < 68) {

			// Process writeback stage
			writeback.processInstruction(systemClock);

			// Process memory stage
			memory.processInstruction(systemClock);

			// Process execute stage
			execute.processInstruction(systemClock);

			// Process decode stage
			decode.processInstruction(systemClock);

			// Process fetch stage
			fetch.fetchInstruction();

			/*for (j = 0; j < 4; j++) {
				if (iCache[i][j].equals(fetch.instruction)) {
					HIT = false;
				} else {
					HIT = true;
				}
			}

			if (HIT == false) {
				for (int k = instPtr - 4; k < instPtr; k++) {
					iCache[i][k] = Parser.instructionList.get(k);
				}
				i++;
				instPtr += 4;
				fetch.processInstruction(systemClock);

				// Increment global clock
				systemClock++;
			} else if (HIT == true) {
				fetch.processInstruction(systemClock);
				systemClock += 2 * Parser.memoryClockCycle
						+ Parser.iCacheClockCycle;
			}

			fetch.processInstruction(systemClock);
			systemClock++;
			
			// Logic for iteration
			if (decode.jumpPointer >= 0) {
				printOutputCycles();
				decode.jumpPointer = -1;
				System.out.println();
			}
		}
	}
	
	*/ 
	
	
	/*
	public static void printOutputCycles() {
		System.out.println("Inst\tF\tD\tE\tWB\tRAW\tWAW\tSTRUCT");
		for (int j = 0; j < Parser.instructionList.size(); j++) {
			System.out.print(Parser.instructionList.get(j).opcode);
			for (int i = 0; i < 4; i++) {
				System.out.print("\t"
						+ Parser.instructionList.get(j).clockCycleStages[i]);
			}
			System.out.print("\t" + decode.raw[j] + "\t" + decode.waw[j] + "\t"
					+ decode.structural[j]);
			System.out.println();
		}
	}
	*/

	static boolean isIcache = true;
	static boolean isIcacheBusy = false;
	static int iCNo = 0;
}
