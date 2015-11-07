package net.aicomp.game_engine_tester;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class Main {
	private static final String GAME_COMMAND = "g";
	private static final String NUM_AI = "n";
	private static final String NORMAL_AI = "a";
	private static final String TIME_OUT_AI = "t";
	private static final String PAUSE_COMMAND = "p";
	private static final String UNPAUSE_COMMAND = "u";
	private static final String LOG_FILE_NAME = "log.txt";
	private static final String JAR_FILE_NAME = "GameEngineTester.jar";

	public static void main(String[] args) throws ParseException, IOException {
		Options options = new Options().addOption(GAME_COMMAND, true, "the command to execute a game engine")
				.addOption(NUM_AI, true, "the number of AI programs")
				.addOption(NORMAL_AI, false, "work as a normal AI program")
				.addOption(TIME_OUT_AI, false, "work as a slow AI program which time out")
				.addOption(PAUSE_COMMAND, false, "work as a pause command")
				.addOption(UNPAUSE_COMMAND, false, "work as a unpause command");
		CommandLineParser parser = new DefaultParser();
		if (!start(parser.parse(options, args))) {
			HelpFormatter help = new HelpFormatter();
			help.printHelp("java -jar GameEngineTester.jar [OPTIONS]\n" + "[OPTIONS]: ", "", options, "", true);
		}
	}

	private static boolean start(CommandLine cl) throws IOException {
		File logFile = new File(LOG_FILE_NAME);
		if (cl.hasOption(GAME_COMMAND)) {
			if (!cl.hasOption(NUM_AI)) {
				return false;
			}
			int aiCount = Integer.parseInt(cl.getOptionValue(NUM_AI));
			ArrayList<String> commandAndArgs = Lists.newArrayList(cl.getOptionValue(GAME_COMMAND).split(" "));
			commandAndArgs.add("-a");
			commandAndArgs.add("java -jar " + JAR_FILE_NAME + " -t");
			commandAndArgs.add("-p");
			commandAndArgs.add("java -jar " + JAR_FILE_NAME + " -p");
			commandAndArgs.add("-u");
			commandAndArgs.add("java -jar " + JAR_FILE_NAME + " -u");
			for (int i = 1; i < aiCount; i++) {
				commandAndArgs.add("-a");
				commandAndArgs.add("java -jar " + JAR_FILE_NAME + " -n");
				commandAndArgs.add("-p");
				commandAndArgs.add("java -jar " + JAR_FILE_NAME + " -p");
				commandAndArgs.add("-u");
				commandAndArgs.add("java -jar " + JAR_FILE_NAME + " -u");
			}
			if (logFile.exists()) {
				logFile.delete();
			}
			Files.append("start\n", logFile, Charset.defaultCharset());
			ProcessBuilder pb = new ProcessBuilder(commandAndArgs);
			pb.start();
		} else if (cl.hasOption(NORMAL_AI)) {
			try (Scanner sc = new Scanner(System.in);) {
				while (true) {
					while (!sc.nextLine().startsWith("EOD")) {
					}
					System.out.println("DUMMY");
				}
			}
		} else if (cl.hasOption(TIME_OUT_AI)) {
			try (Scanner sc = new Scanner(System.in);) {
				while (true) {
					while (!sc.nextLine().startsWith("EOD")) {
					}
					System.out.println("DUMMY");
				}
			}
		} else if (cl.hasOption(PAUSE_COMMAND)) {
			Files.append("pause\n", logFile, Charset.defaultCharset());
		} else if (cl.hasOption(UNPAUSE_COMMAND)) {
			Files.append("unpause\n", logFile, Charset.defaultCharset());
		} else {
			return false;
		}
		return true;
	}
}
