package net.aicomp.game_engine_tester;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

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

	public static void main(String[] args) throws ParseException, IOException {
		Options options = new Options().addOption(GAME_COMMAND, true, "the command to execute a game engine")
				.addOption(NUM_AI, true, "the number of AI programs")
				.addOption(NORMAL_AI, true, "work as a normal AI program")
				.addOption(TIME_OUT_AI, true, "work as a slow AI program which time out")
				.addOption(PAUSE_COMMAND, true, "work as a pause command")
				.addOption(UNPAUSE_COMMAND, true, "work as a unpause command");
		CommandLineParser parser = new DefaultParser();
		if (!start(parser.parse(options, args))) {
			HelpFormatter help = new HelpFormatter();
			help.printHelp("java -jar GameEngineTester.jar [OPTIONS]\n" + "[OPTIONS]: ", "", options, "", true);
		}
	}

	private static boolean start(CommandLine cl) throws IOException {
		if (!cl.hasOption(NUM_AI)) {
			return false;
		}
		int aiCount = Integer.parseInt(cl.getOptionValue(NUM_AI));
		File logFile = new File(LOG_FILE_NAME);
		if (cl.hasOption(GAME_COMMAND)) {
			ArrayList<String> commandAndArgs = Lists.newArrayList(cl.getOptionValue(GAME_COMMAND).split(" "));
			commandAndArgs.add("-a");
			commandAndArgs.add("java -jar GameEngineTest.jar -t");
			commandAndArgs.add("-p");
			commandAndArgs.add("java -jar GameEngineTest.jar -p");
			commandAndArgs.add("-u");
			commandAndArgs.add("java -jar GameEngineTest.jar -u");
			for (int i = 1; i < aiCount; i++) {
				commandAndArgs.add("-a");
				commandAndArgs.add("java -jar GameEngineTest.jar -n");
				commandAndArgs.add("-p");
				commandAndArgs.add("java -jar GameEngineTest.jar -p");
				commandAndArgs.add("-u");
				commandAndArgs.add("java -jar GameEngineTest.jar -u");
			}
			if (logFile.exists()) {
				logFile.delete();
			}
			Files.append("start\n", logFile, Charset.defaultCharset());
			ProcessBuilder pb = new ProcessBuilder(commandAndArgs);
			pb.start();
		} else if (cl.hasOption(NORMAL_AI)) {
		} else if (cl.hasOption(TIME_OUT_AI)) {
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
