package cache;

public class ICacheBlock {

	int[] words;
	int tag;

	public ICacheBlock() {
		words = new int[4];
		tag = 1;
	}
}