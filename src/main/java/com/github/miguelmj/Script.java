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
		pyMachine.close();
	}
	
	private PyCode code;
	private String codeStr;
	
	public Script(){
		setCode("");
	}
	public Script(String str){
		setCode(str);
	}
	public Script(Reader reader){
		setCode(reader);
	}
	public void setCode(String str) {
		code = pyMachine.compile(str);
		codeStr = str;
	}
	public void setCode(Reader reader){
		try {
			code = pyMachine.compile(reader);
			// https://www.baeldung.com/java-convert-reader-to-string
			int intValueOfChar;
			codeStr = "";
			while((intValueOfChar = reader.read()) != -1) {
				codeStr += (char)intValueOfChar;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public boolean evaluate() {
		return pyMachine.eval(code).__nonzero__();
	}
	public void execute() {
		pyMachine.exec(code);
	}
	public String toString() {
		return codeStr;
	}
	public static void main(String[] args) {
		pyMachine.exec("import sys");
		pyMachine.exec("print(sys.version_info)");
	}

}
