package lcom.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class Logger {
	public static String logFile = null;

	public static void log(String str) {
		System.out.println(str);
		writeToLogFile(str);
	}

	private static void writeToLogFile(String str) {
		if (logFile == null) {
			return;
		}
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(logFile, true), StandardCharsets.UTF_8))) {
			writer.write(str + "\n");
		} catch (IOException ex) {
			System.out.println("Exception during logging. " + ex.getMessage());
		}
	}
}
