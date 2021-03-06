package cache;

public class ICache {

	public ICacheBlock[] iCache =  new ICacheBlock[4];
	private static volatile ICache instance;

	public static ICache getInstance() {
		if (null == instance)
			synchronized (ICache.class) {
				if (null == instance)
					instance = new ICache();
			}

		return instance;
	}

	private ICache() {
		
		ICacheBlock[] iCache =  new ICacheBlock[4];
		
	}

	public boolean isInstructionInCache(int pc) throws Exception {
		
		int blockIndex;
		int pcounter = pc;
		int tag;
		
		pcounter = pcounter >> 2;
		blockIndex = pcounter & 0b11;
		tag = pcounter >> 2;
		
		ICacheBlock iBlock = ICache.getInstance().iCache[blockIndex];  // this.iCache[blockIndex];
		
		if(iBlock != null)
			if(iBlock.tag == tag)
				return true;		

		return false;

	}

	// Fetch instruction from Memory to store in the cache

	public void populateICache(int pc) {

		int startPointer;
		int blockIndex;

		startPointer = pc % 4 == 0 ? pc : pc - (pc % 4);

		ICacheBlock iblock = new ICacheBlock();

		for (int i = 0; i < 4; i++) {
			iblock.words[i] = startPointer++;
		}

		pc = pc >> 2;
		blockIndex = pc & 0b11;
		
		int tag = pc >> 2;
		iblock.tag = tag;
		
		this.iCache[blockIndex] = iblock;

	}

}
