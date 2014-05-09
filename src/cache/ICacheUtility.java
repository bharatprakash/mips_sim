package cache;

import stages.Processor;
import inputUtilities.ConfigUtility;
import inputUtilities.ProgramUtility;
import instructions.Instruction;


public class ICacheUtility {

	int lastRequestInstruction;
	int lastRequestCycle;
	int clockCyclesToBlock;
	int delayToBus;
	boolean cacheHit;
	
	 private int                       iCacheAccessRequests;
	 private int                       iCacheAccessHits;
	

	public static volatile ICacheUtility instance;

	public static ICacheUtility getInstance() {
		if (null == instance)
			synchronized (ICacheUtility.class) {
				if (null == instance)
					instance = new ICacheUtility();
			}

		return instance;
	}

	private ICacheUtility() {
		
		iCacheAccessRequests= 0;
		iCacheAccessHits = 0;

		resetValues();

	}

	private void resetValues() {

		lastRequestInstruction = -1;
		lastRequestCycle = -1;
		clockCyclesToBlock = -1;
		delayToBus = -1;
		cacheHit = false;

	}
	
	 public String getICacheStatistics()
	    {
	        String format = "%-60s %4s";
	        StringBuilder sb = new StringBuilder();
	        sb.append(String.format(format,
	                "Total number of requests to instruction cache",
	                iCacheAccessRequests));
	        sb.append('\n');sb.append('\n');
	        sb.append(String.format(format,
	                "Total number of instruction cache hit", iCacheAccessHits));
	        return sb.toString();
	    }

	public Instruction getInstructionFromCache(int pc) throws Exception {

		
		if (lastRequestInstruction == -1) {

			iCacheAccessRequests++;
			
			if (ICache.getInstance().isInstructionInCache(pc)) {

				iCacheAccessHits++;
				
				lastRequestInstruction = pc;
				lastRequestCycle = Processor.CLOCK;
				clockCyclesToBlock = ConfigUtility.instance.ICache - 1;
				cacheHit = true;

				if (clockCyclesToBlock == 0) {

					resetValues();
					return ProgramUtility.instance.getInstructionAtAddress(pc);

				} else
					return null;

			} else {

				lastRequestInstruction = pc;
				lastRequestCycle = Processor.CLOCK;
				delayToBus = MemoryBusManager.getInstance().getDelayFromBCM();
				clockCyclesToBlock = 2
						* (ConfigUtility.instance.ICache + ConfigUtility.instance.Memory)
						+ delayToBus - 1;

				return null;

			}

		} else {

			if (Processor.CLOCK - lastRequestCycle == clockCyclesToBlock) {
				if (!cacheHit)
					ICache.getInstance().populateICache(pc);
				resetValues();
				return ProgramUtility.instance.getInstructionAtAddress(pc);

			} else
				return null;

		}

	}
}
