package com.github.miguelmj;

import java.io.Reader;

import org.python.core.PyCode;
import org.python.util.PythonInterpreter;
/**
 * Python script that can be executed or evaluated as a condition.
 */
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
	/**
	 * Constructs the Script with an empty string.
	 */
	public Script(){
		setCode("");
	}
	/**
	 * Constructs the Script with the code contained in str.
	 * @param str
	 */
	public Script(String str){
		setCode(str);
	}
	/**
	 * Constructs the Script with the code read by reader.
	 * @param reader
	 */
	public Script(Reader reader){
		setCode(reader);
	}
	/**
	 * Sets the code to execute as str.
	 * @param str
	 */
	public void setCode(String str) {
		code = pyMachine.compile(str);
		codeStr = str;
	}
	/**
	 * Sets the code to execute as the read by reader.
	 * @param reader
	 */
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
	/**
	 * Evaluates the code as a condition.
	 * @return True if the scripts evaluates to True in Python.
	 */
	public boolean evaluate() {
		return pyMachine.eval(code).__nonzero__();
	}
	/**
	 * Executes the code of the script.
	 */
	public void execute() {
		pyMachine.exec(code);
	}
	/**
	 * Returns the code of the script.
	 */
	public String toString() {
		return codeStr;
	}
	/*public static void main(String[] args) {
		pyMachine.exec("import sys");
		pyMachine.exec("print(sys.version_info)");
	}*/

}
