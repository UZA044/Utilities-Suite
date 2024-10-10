# Java System Utilities Suite

## Overview

This repository contains a Java-based implementation of three key system utilities:

1. **File Operations**: Reimplementation of basic file operations with support for piping commands, similar to Unix-based tools.
2. **Memory Allocator**: Simulation of a contiguous memory allocation system, focusing on memory management algorithms.
3. **TLB Optimization**: A program that simulates the management of a Translation Lookaside Buffer (TLB) with Least Recently Used (LRU) replacement policy.




## Project Structure

The project consists of the following main files and folders:

- `src/`: Contains all Java source files.
- `sample_data.csv`: Sample input file for File Operations.
- `memory_data.csv`: Sample input file for Memory Allocator.
- `tlb_data.txt`: Sample input file for TLB Optimization.
- `README.md`: This document.
- `tlb_output.txt`: Output file for TLB Optimization.

## File Operations

This component implements a set of basic file operations commonly found in Unix/Linux environments. The commands implemented are:

- **`cat`**: Reads a file and outputs its contents.
- **`cut`**: Extracts specified fields from a file.
- **`sort`**: Sorts the contents of a file.
- **`uniq`**: Filters out repeated lines from a file.
- **`wc`**: Counts the number of lines, words, and bytes in a file.

### Features

- Supports command chaining using pipes (`|`), allowing for complex operations.
- Commands mimic their Unix counterparts.

### Usage Example

```bash
>> cat taskA.csv | cut -f 2 -d ',' | sort | uniq | wc -l
>> cat taskA.csv | cut -f 1-3 | wc -l
```

## Memory Allocator

This component simulates contiguous memory allocation using the Best Fit algorithm. It handles memory requests, releases, and displays memory state.

### Key Functionalities

- **Request Memory**: Allocates memory using the Best Fit algorithm.
- **Release Memory**: Frees up specified memory blocks.
- **Memory Compaction**: Combines free memory holes into one contiguous block.
- **Displays Memory State**: Shows allocated and free regions of memory.

### Sample Console Output

```
----------Best Fit----------
Process 21 request failed at allocating 125 bytes.
External Fragmentation is 83 bytes.
Current memory display
[0-124): OS
[124-150): Process 1
...
```

## TLB Optimization

The TLB Optimization task simulates a Translation Lookaside Buffer (TLB) using a four-entry fully associative cache with LRU replacement.

### Functionalities

- **TLB and Page Table Updates**: Updates entries based on address accesses.
- **TLB Hits, Misses, and Page Faults**: Reports on the status of each memory access.
- **Output File**: Saves updated tables to `tlb_output.txt`.

### Usage Example

The program reads input addresses from `tlb_data.txt`, updates the TLB and page table, and logs results.
