package com.github.miguelmj;

import java.io.Reader;

import org.python.core.PyCode;
import org.python.util.PythonInterpreter;

public class Script {
	public static PythonInterpreter pyMachine;
	public static void setup() {
		PythonInterpreter.initialize(System.getProperties(), null, new String[0]);
		pyMachine = new PythonInterpreter();
	}
	public static void cleanup() {
		pyMachine.cleanup();
		pyMachine.close();
	}
	private PyCode code;
	
	public Script(){
		code = pyMachine.compile("");
	}
	public Script(String str){
		code = pyMachine.compile(str);
	}
	public Script(Reader reader){
		code = pyMachine.compile(reader);
	}
	public void setCode(String str) {
		code = pyMachine.compile(str);
	}
	public void setCode(Reader reader){
		code = pyMachine.compile(reader);
	}
	public boolean evaluate() {
		return pyMachine.eval(code).__nonzero__();
	}
	public void execute() {
		pyMachine.exec(code);
	}
	public static void main(String[] args) {
		pyMachine.exec("import sys");
		pyMachine.exec("print(sys.version_info)");
	}

}
