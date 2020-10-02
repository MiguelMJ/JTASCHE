package com.github.miguelmj;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

/**
 * List of responses with an initialization script for the inner state.
 */
public class Module {
	protected Script init;
	protected List<Response> responses;
	Module(){
		init = new Script();
		responses = new ArrayList<Response>();
	}
	/**
	 * Loads the module in a JSON format from the reader.
	 * @param reader Reader to load from.
	 * @return True if the load was successful.
	 */
	public boolean load(Reader reader) {
		Gson gson = new Gson();
		SerializableModule mod = gson.fromJson(reader, SerializableModule.class);
		for(SerializableResponse sr : mod.responses) {
			responses.add(sr.getResponse());
		}
		init.setCode(mod.init);
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
		for(Response res: responses){
			Optional<String> ans = res.answer(question);
			if(ans.isPresent()) return ans;
		}
		return Optional.empty();
	}
}
