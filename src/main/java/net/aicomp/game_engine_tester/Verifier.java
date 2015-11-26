package net.aicomp.game_engine_tester;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Verifier {

	private String lastActualLine;
	private String lastExpectedLine;
	private int lineNumber;

	public Verifier() {
	}

	public boolean verify(File file, int aiCount) {
		try (Scanner sc = new Scanner(file)) {
			return verify(sc, aiCount);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean verify(Scanner sc, int aiCount) {
		try {
			if (!checkStartsWith(sc, "Starting"))
				return false;
			if (!checkStartsWith(sc, "Started"))
				return false;
			for (int i = 1; i < aiCount; i++) {
				if (!checkStartsWith(sc, "Sending 'READY'"))
					return false;
			}
			for (int i = 1; i < aiCount; i++) {
				if (!checkStartsWith(sc, "Paused"))
					return false;
			}
			if (!checkStartsWith(sc, "Unpaused"))
				return false;
			if (!checkStartsWith(sc, "Received 'EOD'"))
				return false;
			while (sc.hasNextLine()) {
				for (int i = 2; i < aiCount; i++) {
					if (!checkStartsWith(sc, "Unpaused"))
						return false;
					if (!checkStartsWith(sc, "Received 'EOD'"))
						return false;
					if (!checkStartsWith(sc, "Sending 'DUMMY'"))
						return false;
					if (!checkStartsWith(sc, "Paused"))
						return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkStartsWith(Scanner sc, String expectedLine) {
		lastActualLine = sc.nextLine();
		lastExpectedLine = expectedLine;
		lineNumber++;
		return lastActualLine != null && lastActualLine.startsWith(expectedLine);
	}

	public String getLastActualLine() {
		return lastActualLine;
	}

	public String getLastExpectedLine() {
		return lastExpectedLine;
	}

	public int getLineNumber() {
		return lineNumber;
	}
}
