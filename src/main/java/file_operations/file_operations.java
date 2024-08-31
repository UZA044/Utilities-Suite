package file_operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class file_operations {

	public static void main(String[] args) {

		System.out.println("File operations");
		System.out.println("Name:Uzair"); // display your name in here
		System.out.println("Please enter your commands - cat, cut, sort, uniq, wc or |");

		// read the command from the terminal
		String filePath = "sample_data.csv";
		File content = new File(filePath);
		String file_content = "";

		try {
			Scanner reader = new Scanner(content);
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if (line != null) {
					file_content += line + "\n";
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
		/**
		 * Reads command entered by the user and handles it appropriately depending on 
		 * piping used or not.
		 */
		Scanner scanner = new Scanner(System.in);
		boolean loop = true;
		while (loop) {
			System.out.print(">> ");
			String input = scanner.nextLine();
			if (input.equals("exit")) {
				loop = false;

			} else if (input.indexOf('|') == -1) {
				System.out.println(Commands(input, file_content, filePath));

			} else if (input.indexOf('|') != -1) {
				String[] inputs = input.split("\\|");
				String data = file_content;
				String final_result = "";
				for (String i : inputs) {
					final_result = Commands(i, data, filePath);
					data = final_result;

				}
				System.out.println(final_result);

			} else {
				loop = false;
			}

		}
		scanner.close();

	}
	
	/**
	 * This method handles which command method to be called relevant to user input.
	 * @return output of command.
	 */
	
	public static String Commands(String input, String file_content, String filePath) {
		if (input.contains("cat")) {
			return cat(file_content);

		} else if (input.contains("cut")) {
			return cut(input, file_content);
		} else if (input.contains("sort")) {
			return sort(file_content);
		} else if (input.contains("uniq")) {
			return uniq(file_content);
		} else if (input.contains("wc")) {
			return wc(file_content, input, filePath);
		} else {
			return null;
		}

	}

	/**
	 * This method handles cat command.
	 * @return file content as a string.
	 */
	public static String cat(String content) {
		return (content);

	}
	/**
	 * This method handles the cut command relevant to
	 * user's desired field ranges and delimiter.
	 * @return result fo cut command on file content.
	 */
	// returns result of cut command according to field ranges and delimiter
	// entered.
	public static String cut(String input, String data_content) {
		int start_i = input.indexOf("-f");
		int end_i = 0;
		String delimiter = ",";
		ArrayList<Integer> range = new ArrayList<Integer>();
		if (input.contains("-d") && (input.contains("-f"))) {
			end_i = input.indexOf("-d");

			String field_range = input.substring(start_i + 2, end_i).trim();
			range = extract_fields(field_range);

			delimiter = getDelimiter(input);

		} else if (input.contains("-f")) {
			end_i = input.indexOf(" ", start_i + 3);
			String field_range = input.substring(start_i + 2, end_i).trim();
			range = extract_fields(field_range);

		} else {
			System.out.println("Incorrect comamand syntax entered");
		}

		String file_content = "";

		try {
			Scanner reader = new Scanner(data_content);
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if (line != null) {
					String[] line_split = line.split(delimiter);
					for (int i = 0; i < line_split.length; i++) {
						if (range.contains(i)) {
							file_content += line_split[i] + ",";
						}
					}
					if (file_content != null && file_content.length() > 0) {
						file_content = file_content.substring(0, file_content.length() - 1);
					}
					file_content += "\n";

				}
			}
			reader.close();

		} catch (NullPointerException e) {
			System.out.println("Not enough");
			e.printStackTrace();
		}
		if (file_content != null && file_content.length() > 0) {
			file_content = file_content.substring(0, file_content.length() - 1);
		}
		return file_content;

	}

	/**
	 * This method extracts field ranges relevant to cut command.
	 * @return field ranges.
	 */
	public static ArrayList<Integer> extract_fields(String field_range) {
		ArrayList<Integer> range = new ArrayList<Integer>();
		if (field_range.contains("-")) {
			int start_val = Integer.parseInt(field_range.split("-")[0]) - 1;
			int end_val = Integer.parseInt(field_range.split("-")[1]) - 1;
			for (int i = 0; i <= end_val; i++) {
				if (i >= start_val) {
					range.add(i);
				}
			}

		} else if (field_range.contains(",")) {
			String[] selected_indexes = field_range.split(",");
			for (String k : selected_indexes) {
				int num = Integer.parseInt(k.trim());
				range.add(num - 1);
			}

		} else {
			range.add(Integer.parseInt(field_range) - 1);
		}
		return range;

	}

	/**
	 * This method extracts delimiter from use input.
	 * @return delimiter.
	 */
	public static String getDelimiter(String input) {
		String[] delimiterSplit = input.split("-d\\s+");
		if (delimiterSplit.length > 1) {
			String delimiterPart = delimiterSplit[1].trim();
			if (delimiterPart.startsWith("'") && delimiterPart.endsWith("'")) {
				return delimiterPart.substring(1, delimiterPart.length() - 1);
			} else if (delimiterPart.startsWith("\"") && delimiterPart.endsWith("\"")) {
				return delimiterPart.substring(1, delimiterPart.length() - 1);
			} else if (delimiterPart.equals("','")) {
				return ",";
			} else if (delimiterPart.equals("\",\"")) {
				return ",";
			}
		}
		return ",";
	}

	/**
	 * This method sorts the content read from the file.
	 * @return sorted content.
	 */
	public static String sort(String content) {
		String sorted_content = "";
		String[] lines = content.split("\n");
		Arrays.sort(lines);
		for (String i : lines) {
			sorted_content += i + "\n";
		}

		if (!sorted_content.isEmpty()) {
			sorted_content = sorted_content.substring(0, sorted_content.length() - 1);
		}

		return sorted_content;

	}

	/**
	 * This method carries out uniq operation on content.
	 * @return content which has had uniq operation done on it.
	 */
	public static String uniq(String content) {
		String unique_content = "";
		String[] lines = content.split("\n");
		ArrayList<String> unique_lines = new ArrayList<String>();
		unique_lines.add(lines[0]);

		for (int i = 1; i < lines.length; i++) {
			if (!lines[i].equals(lines[i - 1])) {
				unique_lines.add(lines[i]);
			}
		}

		for (String i : unique_lines) {
			unique_content += i + "\n";
		}

		if (!unique_content.isEmpty()) {
			unique_content = unique_content.substring(0, unique_content.length() - 1);
		}

		return unique_content;
	}
	/**
	 * This method handles the wc command.
	 * @return number of lines , words, characters in file content.
	 */
	public static String wc(String content, String input, String file) {
		int wordcount = 0;
		int charactercount = 0;
		int linecount = 0;

		try {
			Scanner reader = new Scanner(content);
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if (line != null) {
					charactercount += line.length();
					String[] words = line.split("\\s+");
					wordcount += words.length;
					linecount += 1;

				}
			}
			reader.close();
			String[] lines = content.split("\n");
			linecount = lines.length;

		} catch (NullPointerException e) {
			System.out.println("Not enough");
			e.printStackTrace();
		}
		if (input.contains("-l")) {
			return ("  " + (linecount));
		} else {

		}
		return ("  " + linecount + "  " + wordcount + " " + content.length() + " " + file);

	}
}