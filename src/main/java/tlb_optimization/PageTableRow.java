package tlb_optimization;
/**
* This class serves the purpose of storing data for a page table row.
*/
public class PageTableRow {
	
	// storing values for index,valid,location (physical page number).
	private int index;

	private int valid;

	private String location;
	
	/**
	 * Constructor of PageTableRow
	 * @param index index of the page table row entry.
	 * @param valid if the index is valid or not.
	 * @param location location of the relevant index.
	 */
	public PageTableRow(int index, int valid, String location) {
		super();
		this.index = index;
		this.valid = valid;
		this.location = location;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
