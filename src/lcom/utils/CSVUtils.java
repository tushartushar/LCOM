package lcom.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtils {
	static final String TYPE_METRICS_HEADER = "Project Name"
			+ ",Package Name"
			+ ",Type Name"
			+ ",LCOM1"
			+ ",LCOM2"
			+ ",LCOM3"
			+ ",LCOM4"
			+ ",LCOM5"
			+ ",YALCOM"
			+ "\n";
	public static void initializeCSVDirectory(String projectName, String dirPath) {
		File dir = new File(dirPath);
		createDirIfNotExists(dir);
		cleanup(dir);
		initializeNeededFiles(dir);
	}
	
	private static void createDirIfNotExists(File dir) {
		if (!dir.exists()) {
			try {
				//The program is failing here. It couldn't create the directory.
				//I see we are providing relative path; that could be the reason
				//We may prepare the absolute path (by combining it with the output path)
				//and try again.
				if(dir.mkdirs()==false)
					System.out.print("oops, couldn't create the directory " + dir);
			} catch (Exception e) {
				e.printStackTrace();
				Logger.log(e.getMessage());
			}
		}
	}
	
	private static void cleanup(File dir) {
		if (dir.listFiles() != null) {
			for (File file : dir.listFiles()) {
				file.delete();
			}
		}
	}
	
	private static void initializeNeededFiles(File dir) {
		createCSVFile(dir.getPath() + File.separator + "TypeMetrics.csv", TYPE_METRICS_HEADER);
	}
	
	private static void createCSVFile(String path, String header) {
		try {
			File file = new File(path);
	        file.createNewFile(); 
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.append(header);
			bufferedWriter.close();
		} catch(IOException e) {
			e.printStackTrace();
			Logger.log(e.getMessage());
		}
	}
	
	public static void addToCSVFile(String path, String row) {
		try {
			File file = new File(path);
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.append(row);
			bufferedWriter.close();
		} catch(IOException e) {
			e.printStackTrace();
			Logger.log(e.getMessage());
		}
	}
	
	public static void addAllToCSVFile(String path, List collection) {
		try {
			File file = new File(path);
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (Object obj : collection) {
				String row = (obj instanceof String) ? (String) obj : obj.toString();
				bufferedWriter.append(row);
			}
			bufferedWriter.close();
		} catch(IOException e) {
			e.printStackTrace();
			Logger.log(e.getMessage());
		}
	}

}
