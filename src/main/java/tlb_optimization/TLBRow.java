package tlb_optimization;
/**
* This class serves the purpose of storing data for a TLB table row.
*/
public class TLBRow {
	
	// storing data for a row : valid,tag,page,LRU.
	private int valid;

	private int tag;

	private int page;

	private int lru;
	
	/**
	 * Constructor of TLBRow
	 * @param valid data about validity of TLB entry.
	 * @param tag tag is the page number retrieved from the virtual address.
	 * @param page the page of the TLB entry
	 * @param lru LRU of the entry  is the priority if to be replaced.
	 */
	public TLBRow(int valid, int tag, int page, int lru) {
		super();
		this.valid = valid;
		this.tag = tag;
		this.page = page;
		this.lru = lru;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLru() {
		return lru;
	}

	public void setLru(int lru) {
		this.lru = lru;
	}

}
