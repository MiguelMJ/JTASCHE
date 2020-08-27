package com.github.miguelmj;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.python.util.PythonInterpreter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Conversational User Interface, loads conversational modules.
 */
public class CUI {
	private PythonInterpreter innerState;
	private List<Module> modules;
	public CUI() {
		innerState = new PythonInterpreter();
		Script.pyMachine = innerState;
		modules = new ArrayList<Module>();
	}
	/**
	 * Load new module from a reader.
	 * @param reader Reader to read from.
	 * @return True if the operation was successful.
	 */
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
	/**
	 * Load a module from a file.
	 * @param filename Path to the file.
	 * @return True if the operation was successful.
	 */
	public boolean load(String filename) {
		try {
			return load(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Initializes all the modules, running their init scripts.
	 */
	public void initialize() {
		for(Module module: modules) {
			module.initialize();
		}
	}
	/**
	 * Returns the concatenated answer of all modules.
	 * @param question User input
	 * @return
	 */
	public String answer(String question) {
		List<String> list = new ArrayList<String>();
		for(Module module : modules) {
			Optional<String> ans = module.answer(question);
			if(ans.isPresent()) list.add(module.answer(question).get());
		}
		return String.join(" ",list);
	}
	/**
	 * Returns a JSON string with all the information of the CUI.
	 * @param pretty True to indent the output.
	 * @param escape True to enable Html escaping.
	 * @return The JSON representation of the CUI.
	 */
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
