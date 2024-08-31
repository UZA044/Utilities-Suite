package tlb_optimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
* This class serves the purpose of carrying out optimization.
*/
public class optimizer {
	
	// arrayLists for TLB page table and all the virtual address who need to be translated.
	private static ArrayList<TLBRow> tLB = new ArrayList<>();
	private static ArrayList<String> virtualAddresses = new ArrayList<>();
	private static ArrayList<PageTableRow> pageTable = new ArrayList<>();
	
	/**
	 * This method writes to the relevant file 
	 * whatever has been passed as parameter.
	 * @param text the text to be written.
	 */
	public static void writeToFile(String text) {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("tlb_output.txt", true))) {
			writer.write(text);
		} catch (IOException e) {
			System.err.println("An error occurred while trying writing to the file: " + e.getMessage());
		}
	}
	
	/**
	 * This method checks if there will be a TLB hit for the relevant
	 * tag retrieved from virtual address.
	 * @param tag the page number retrieved from the virtual address.
	 * @return boolean value depending if there will be a TLB hit or not.
	 */
	private static boolean checkTagInTLB(int tag) {
		boolean tagCheck = false;
		for (TLBRow i : tLB) {
			if ((i.getTag() == tag) && (i.getValid() == 1)) {
				tagCheck = true;

			}

		}
		return tagCheck;
	}
	
	/**
	 * This method checks if there is a page fault.
	 * @param tag the page number retrieved from the virtual address.
	 * @return boolean value depending on if there is a page fault or not.
	 */
	private static boolean checkForPageFault(int tag) {
		boolean check = false;
		for (PageTableRow i : pageTable) {
			if ((i.getIndex() == tag) && (i.getValid() == 0)) {
				check = true;

			}

		}
		return check;

	}
	/**
	 * This method completely empties the file / deletes all contents.
	 */
	private static void emptyFile() {
		try (FileWriter writer = new FileWriter("tlb_output.txt")) {
		} catch (IOException e) {
		}
	}
	
	/**
	 * This method handles the correct format for the text to be written to file.
	 * @param address the address it is referring to when writing to file.
	 * @param Result result as in pagefault/tlbmiss/tlbhit.
	 */
	private static void printBlock(String address, String Result) {
		writeToFile("# After the memory access " + address + "\n" + "Address, Result (Hit, Miss, PageFault)" + "\n"
				+ address + "," + Result + "\n" + "#updated TLB" + "\n" + "#Valid, Tag, Physical Page #, LRU\n");

		for (TLBRow i : tLB) {
			writeToFile(i.getValid() + "," + i.getTag() + "," + i.getPage() + "," + i.getLru() + "\n");
		}
		writeToFile("#updated Page table\n" + "#Index,Valid,Physical Page or On Disk	\n");

		for (PageTableRow k : pageTable) {
			writeToFile(k.getIndex() + "," + k.getValid() + "," + k.getLocation() + "\n");
		}
	}
	
	/**
	 * This method is for updating the LRU for all 
	 * TLB table entries when a hit/miss/page fault occurs.
	 * @param tag the tag which has just been accessed most recently.
	 */
	private static void updateLRU(int tag) {
		int accessedLRU = 0;
		TLBRow recent = null;
		for (TLBRow i : tLB) {
			if (i.getTag() == tag) {

				recent = i;
				accessedLRU = i.getLru();

				i.setLru(4);

			}
		}
		for (TLBRow row : tLB) {
			if (row.getTag() != tag && row.getValid() == 1) {
				if (row.getLru() > accessedLRU) {
					row.setLru(row.getLru() - 1);
				}
			}

		}

	}
	
	/**
	 * This method checks if there will be a TLB miss.
	 * @param tag the page number retrieved from the virtual address.
	 */
	private static void setTLBMiss(int tag) {
		for (TLBRow i : tLB) {
			if (i.getLru() == 1) {
				for (PageTableRow k : pageTable) {
					if ((k.getIndex() == tag) && (k.getValid() == 1)) {
						i.setPage(Integer.parseInt(k.getLocation().trim()));
						i.setValid(1);
						i.setTag(tag);
						updateLRU(tag);
						break;

					}
				}
			}
		}
	}
	
	/**
	 * This method handles page fault if it occurs.
	 * @param tag the page number retrieved from the virtual address.
	 */
	private static void handlePageFault(int tag) {
		ArrayList<Integer> pageValues = new ArrayList<>();
		int maxNum = 0;
		String availablePageNum = "";
		for (PageTableRow k : pageTable) {
			if (k.getLocation().trim().matches("\\d+")) {
				pageValues.add(Integer.parseInt(k.getLocation().trim()));

			}

		}
		for (Integer i : pageValues) {
			if (i > maxNum) {
				maxNum = i;
			}
		}

		availablePageNum = Integer.toString(maxNum + 1);

		for (PageTableRow k : pageTable) {
			if (k.getIndex() == tag) {
				k.setLocation(availablePageNum);
				k.setValid(1);
			}

		}
		TLBRow tLBRow = null;

		for (TLBRow i : tLB) {
			if (i.getLru() == 1) {
				tLBRow = i;

			}
		}

		for (PageTableRow k : pageTable) {
			if ((k.getIndex() == tag) && (k.getValid() == 1)) {
				tLBRow.setPage(Integer.parseInt(k.getLocation().trim()));
				tLBRow.setValid(1);
				tLBRow.setTag(tag);
				updateLRU(tag);
				break;
			}
		}

	}
	
	/**
	 * This method is the run procedure to updating page table and TLB table.
	 */
	private static void run() {
		String[] tagNumber = new String[100];
		int index = 0;
		for (String i : virtualAddresses) {

			tagNumber[index] = i;
			index++;

		}
		for (String k : tagNumber) {
			if (k != null) {

				String tag = Character.toString((k.substring(2)).charAt(0));
				int tagValue = Integer.parseInt(tag, 16);

				if (checkTagInTLB(tagValue)) {
					updateLRU(tagValue);

					printBlock(k, "Hit");

				} else {
					if (checkForPageFault(tagValue)) {
						handlePageFault(tagValue);
						printBlock(k, "PageFault");

					} else {
						setTLBMiss(tagValue);
						printBlock(k, "Miss");

					}

				}

			}
		}
	}

	/**
	 * This method initializes the arrays used for page table TLB table
	 * and virtual addresses.
	 * Also starts the procedure (by running run() ) to updating the tables
	 * as virtual addresses are read.
	 */
	private static void initialise() {
		String filePath = "tlb_data.txt";
		File content = new File(filePath);
		String file_content = "";
		boolean readTLBRows = false;
		boolean readPageTableRows = false;

		try {
			Scanner reader = new Scanner(content);
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if ((line != null) && (line.contains("0x"))) {
					virtualAddresses.add(line.trim());

				}
				if (line.startsWith("#Valid, Tag, Physical Page #, LRU")) {
					readTLBRows = true;
					continue;
				}

				if (line.startsWith("#Initial Page table")) {
					readTLBRows = false;
				}

				if (line.startsWith("#Index,Valid,Physical Page or On Disk")) {
					readPageTableRows = true;
					continue;
				}

				if (readPageTableRows && line.trim().isEmpty()) {
					readPageTableRows = false;
				}

				if (readTLBRows) {
					String[] tlb_row = line.split(",");
					tLB.add(new TLBRow(Integer.parseInt(tlb_row[0]), Integer.parseInt(tlb_row[1]),
							Integer.parseInt(tlb_row[2]), Integer.parseInt(tlb_row[3])));
				} else if (readPageTableRows) {
					String[] page_table = line.split(",");
					pageTable.add(new PageTableRow(Integer.parseInt(page_table[0]), Integer.parseInt(page_table[1]),
							page_table[2]));
				}

			}
			if (file_content != null && file_content.length() > 0) {
				file_content = file_content.substring(0, file_content.length() - 1);
			}
			reader.close();

		} catch (FileNotFoundException e) {
			System.out.println("Not enough");
			e.printStackTrace();
		}

		run();

	}
	/**
	 * Just starts the program.
	 * @param args
	 */
	public static void main(String[] args) {
		emptyFile();
		initialise();

	}

}
