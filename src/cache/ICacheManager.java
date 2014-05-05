package cache;

import config.ConfigManager;
import program.ProgramManager;
import stages.CPU;
import instructions.Instruction;

/**
 * Called by main while fetching instructions if instructions not present in
 * cache keep track of when to populate the instruction in the cache
 * 
 * 
 * 
 * Instruction isInstructionInCache(int pc){ if instruction is not found in the
 * cache track of when to populate the instruction in the cache by marking it in
 * the tracker return null; else return instruction from cache
 * 
 * }
 */

public class ICacheManager {

	int lastRequestInstruction;
	int lastRequestCycle;
	int clockCyclesToBlock;
	int delayToBus;

	private static volatile ICacheManager instance;

	public static ICacheManager getInstance() {
		if (null == instance)
			synchronized (ICacheManager.class) {
				if (null == instance)
					instance = new ICacheManager();
			}

		return instance;
	}

	private ICacheManager() {

		lastRequestInstruction = -1;
		lastRequestCycle = -1;
		clockCyclesToBlock = -1;
		delayToBus = -1;
	}

	public Instruction getInstructionFromCache(int pc) throws Exception {

		if (lastRequestInstruction == -1) {

			if (ICache.getInstance().isInstructionInCache(pc)) {

				lastRequestInstruction = pc;
				lastRequestCycle = 0; // edit this shit

				/*
				 * if() return null else return instruction;
				 */

			} else {

				lastRequestInstruction = pc;
				lastRequestCycle = CPU.CLOCK;
				delayToBus = MemoryBusManager.getInstance().getDelayFromBCM();
				clockCyclesToBlock = 2
						* (ConfigManager.instance.ICacheLatency + ConfigManager.instance.MemoryLatency)
						+ delayToBus - 1; 

				return null;

			}

		} else {

			if (CPU.CLOCK - lastRequestCycle == clockCyclesToBlock) {																
				ICache.getInstance().populateICache(pc);
				return ProgramManager.instance.getInstructionAtAddress(pc);

			} else
				return null;

		}

		return null;
	}
}
