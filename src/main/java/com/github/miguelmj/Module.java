package com.github.miguelmj;

import java.io.Reader;
import java.util.Optional;

import com.google.gson.Gson;

public class Module {
	protected Script init;
	protected Response response;
	Module(){
		init = new Script();
	}
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
	public void initialize() {
		init.execute();
	}
	public Optional<String> answer(String question){
		return response.answer(question);
	}
}
