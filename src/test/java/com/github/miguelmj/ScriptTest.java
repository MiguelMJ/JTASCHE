package com.github.miguelmj;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.python.util.PythonInterpreter;
import org.python.core.PyCode;

public class ScriptTest {
	@Before 
	public void initializeInterpreter(){
		PythonInterpreter.initialize(System.getProperties(), null, new String[0]);
		Script.pyMachine = new PythonInterpreter();
	}
	@After
	public void closeInterpreter() {
		Script.pyMachine.close();
	}
	@Test
	public void scopesInInterpretersTest() {
		PythonInterpreter m1 = new PythonInterpreter();
		PythonInterpreter m2 = new PythonInterpreter();
		m1.exec("a='machine 1'; b=3");
		m2.exec("a='machine 2'; b=3");
		// PyCode code = m1.compile("print(a)");
		// m1.exec(code);
		// m2.exec(code);
		m1.close();
		m2.close();
		assertTrue(!m1.get("a").equals(m2.get("a")) && m1.get("b").equals(m2.get("b")));
	}
	@Test
	public void evaluateTest() {
		Script.setup();
		Script[] scripts = new Script[10];
		Script.pyMachine.exec("a = 5; b=3; c='hola'");
		
		// this should eval true
		scripts[0] = new Script("a == 5");
		scripts[1] = new Script("b ==3");
		scripts[2] = new Script("c[b] == 'a'");
		scripts[3] = new Script("'0'");
		scripts[4] = new Script("1");

		// this should eval false
		scripts[5] = new Script("a==b");
		scripts[6] = new Script("h=4;False");
		scripts[7] = new Script("c[0]=='o'");
		scripts[8] = new Script("0");
		scripts[9] = new Script("''");
		
		boolean ok = true; int i=0;
		for(Script s: scripts) {
			boolean ev = s.evaluate();
			ok &= i++ < 5 == ev;
		}
		
		assertTrue(ok);
	}

}
