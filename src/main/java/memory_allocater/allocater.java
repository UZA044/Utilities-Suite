package memory_allocater;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * Main function for memory management.
 */
public class allocater {

    // size of memory
    public static final int TOTAL_BYTES = 1024;

    /*
     * The first number is the reference id of job. The second number is a request
     * to allocate or deallocate. (1 - Allocate, 2 - Deallocate) The third number if
     * allocate will try and allocate those amount of bytes into a memory stack of
     * 1024 bytes. If it is deallocate, the third argument will be the reference id
     * to deallocate from memory.
     * 
     *

    //};
    /**
     * This method reads through the csv file and initializes a 
     * array to insert list of processes and data into so that it can
     * be ready for a memory allocation simulation.
     */
    private static int[][] process_allocation(){
    	String filePath = "memory_data.csv";
    	File content= new File(filePath);
    	String file_content="";
    	int rowcount =0;
    	
    	try {
    		Scanner reader = new Scanner(content);
    		while (reader.hasNextLine()) {
    			String line = reader.nextLine();
    			if (line != null) {
    				file_content+=line+"\n";
    				rowcount++;
    			}
    		}
    		if (file_content != null && file_content.length() > 0) {
                file_content=file_content.substring(0, file_content.length() - 1);
                
            }
    		reader.close();
    		
    	}catch (FileNotFoundException e) {
    		System.out.println("Not enough");
    		e.printStackTrace();
    	}
    	int j=0;
    	String[] rows=file_content.split("\n");
    	int[][] data = new int[rowcount][3];
    	for (String i: rows) {
    		String[] columns=i.split(",");
    		for (int k=0;k<3;k++ ) {
    			data[j][k]=Integer.parseInt(columns[k].trim());
    			
    			
    		}
    		j++;
    	}
    	return data;
    	
    	
    }

    // Keep track of all processes created
    private static ArrayList<Process> listof_processes;

    public static void createProcesses() {
	Process proc;
	int[][] alloc=process_allocation();
	listof_processes = new ArrayList<>();
	for (int i = 0; i < alloc.length; i++) {
	    proc = new Process(alloc[i][0], alloc[i][1], alloc[i][2]);
	    listof_processes.add(proc);
	}
    }

    /**
     * This method runs the First Fit Memory Allocation simulation using a linked
     * list. Loops through the Processes in the Process list and allocates
     * appropriately. If it cannot allocate, it will fail and print why accordingly.
     * If it succeeds it will print 'Success'.
     */
    private static void firstFit() {

	MainMemory manager = new MainMemory();
	manager.insertAtStart(new Block());

	for (Process proc : listof_processes) {

	    if (proc.isAllocating()) {
		boolean placed = manager.firstFitInsert(proc);
		// externalFragmentation has not been implemented
		/*
		 * if(!placed){ System.out.println("Request " + proc.getReference_number() +
		 * " failed at allocating " + proc.getArgument() + " bytes." );
		 * System.out.println("External Fragmentation is " +
		 * manager.externalFragmentation() + " bytes."); //memory print
		 * manager.printBlocks(); return; }
		 */
	    } else if (proc.isDeallocating()) {
		manager.deallocateBlock(proc.getArgument());
	    }
	}

	System.out.println("Success");
	// memory print
	manager.printBlocks();
    }

    /**
     * This method runs the Best Fit Memory Allocation simulation using a linked
     * list. Loops through the Processes in the Process list and allocates
     * appropriately. If it cannot allocate, it will fail and print why accordingly.
     * If it succeeds it will print 'Success'.
     */
    private static void bestFit() {

	MainMemory manager = new MainMemory();
	manager.insertAtStart(new Block());

	for (Process proc : listof_processes) {

	    if (proc.isAllocating()) {
		boolean placed = manager.bestFitInsert(proc);
		// you should calculate the total bytes of the external fragmentation
		if (!placed) {
		    System.out.println("Request " + proc.getReference_number() + " failed at allocating "
			    + proc.getArgument() + " bytes.");
		    System.out.println("External Fragmentation is " + manager.externalFragmentation() + " bytes.");
		    // memory print
		    manager.printBlocks();
		    manager.compaction();
		    System.out.println("\n"+"-------After Compaction ------");
		    boolean placed2 = manager.bestFitInsert(proc);
		    manager.printBlocks();
		    if (!placed2) {
		    	System.out.println("Process "+proc.getReference_number()+": allocation failed for "+proc.getArgument()+" bytes.");
		    }
		    
		    return;
		}
	    } else if (proc.isDeallocating()) {
		manager.deallocateBlock(proc.getArgument());
	    }
	}
	System.out.println("Success");
	// memory print
	manager.printBlocks();
    }

    public static void main(String[] args) {

	createProcesses();

	System.out.println("----------Best Fit---------");
	bestFit();

    }

}
