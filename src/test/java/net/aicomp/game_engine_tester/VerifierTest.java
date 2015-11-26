package net.aicomp.game_engine_tester;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VerifierTest {

	private ByteArrayOutputStream _baos;
	private PrintStream _out;

	@Before
	public void setUp() {
		_baos = new ByteArrayOutputStream();
		_out = System.out;
		System.setOut(new PrintStream(new BufferedOutputStream(_baos)));
	}

	@After
	public void tearDown() {
		System.setOut(_out);
	}

	@Test
	public void testVerifier() {
		Verifier verifier = new Verifier();
		StringBuilder builder = new StringBuilder();
		int aiCount = 3;
		builder.append("Starting\n");
		builder.append("Started\n");
		for (int i = 0; i < aiCount; i++) {
			builder.append("Sending 'READY'\n");
			builder.append("Sent 'READY'\n");
		}
		builder.append("Received 'EOD'\n");
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < aiCount - 1; i++) {
				builder.append("Received 'EOD'\n");
				builder.append("Sending 'DUMMY'\n");
				builder.append("Sent 'DUMMY'\n");
			}
		}

		try (Scanner sc = new Scanner(new StringReader(builder.toString()))) {
			assertThat(verifier.verify(sc, aiCount), is(true));
		}
	}
}