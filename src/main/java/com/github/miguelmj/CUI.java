package com.github.miguelmj;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;

import org.python.util.PythonInterpreter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * 
 */
public class CUI {
	private PythonInterpreter innerState;
	private List<Module> modules;
	public CUI() {
		innerState = new PythonInterpreter();
		Script.pyMachine = innerState;
		modules = new ArrayList<Module>();
	}
	public boolean load(Reader reader) {
		try {
			Module mod = new Module();
			mod.load(reader);
			modules.add(mod);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean load(String filename) {
		try {
			return load(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	public void initialize() {
		for(Module module: modules) {
			module.initialize();
		}
	}
	public String answer(String question) {
		StringBuilder ans = new StringBuilder();
		for(Module module : modules) {
			ans.append(module.answer(question).orElse(""));
		}
		return ans.toString();
	}
	public String toJson(boolean pretty, boolean escape) {
		SerializableModule mod = new SerializableModule();
		mod.response.responses = new ArrayList<SerializableResponse>();
		for(Module module: modules) {
			if(module.init != null) mod.init += module.init.toString() + '\n';
			if(module.response != null) mod.response.responses.add(module.response.serializable());
		}
		
		GsonBuilder builder = new GsonBuilder();
		if(pretty) builder.setPrettyPrinting();
		if(!escape) builder.disableHtmlEscaping();
		Gson gson = builder.create();
		return gson.toJson(mod);
	}
}
