package com.github.miguelmj;

import java.io.Reader;
import java.util.Optional;

import com.google.gson.Gson;

/**
 * List of responses with an initialization script for the innter state.
 */
public class Module {
	protected Script init;
	protected Response response;
	Module(){
		init = new Script();
	}
	/**
	 * Loads the module in a JSON format from the reader.
	 * @param reader Reader to load from.
	 * @return True if the load was successful.
	 */
	public boolean load(Reader reader) {
		try {
			Gson gson = new Gson();
			SerializableModule mod = gson.fromJson(reader, SerializableModule.class);
			response = mod.response.getResponse();
			init.setCode(mod.init);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Execute the initialization script. Should be called before the first answer.
	 */
	public void initialize() {
		init.execute();
	}
	/**
	 * Return the answer of the module's loaded response, if possible.
	 * @param question User input.
	 * @return An answer if possible, empty optional if not.
	 */
	public Optional<String> answer(String question){
		return response.answer(question);
	}
}
