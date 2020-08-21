package com.github.miguelmj;

import static org.junit.Assert.*;

import org.junit.Test;

public class PatternTest {
	private Pattern pattern;
	public PatternTest() {
		pattern = new Pattern();
	}
	@Test
	public void recognizeGeneratedTest() {
		boolean ok = true;
		String str = "(Hi|Hello), how are you( today)?\\?";
		pattern.setString(str);
		for(int i=0; i<10; i++) {
			String gen = pattern.generate();
			boolean match = pattern.matches(gen);
			ok &= match;
			System.out.print("Generated: "+gen);
			if(match) {
				System.out.println("[OK]");
			}else {
				System.out.println("[UNRECOGNIZED]");
			}
		}
		assertTrue("All patterns generated are recognized",ok);
	}

}
