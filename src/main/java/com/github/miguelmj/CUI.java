package com.github.miguelmj;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.python.util.PythonInterpreter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class SerializableResponse{
	public String input;
	public String output;
	public String condition;
	public String execute;
	public String new_question;
	List<SerializableResponse> responses;
	
	public Response getResponse() {
		Response ret;
		if(output != null && responses == null) {
			// SimpleRespons
			SimpleResponse sr = new SimpleResponse();
			sr.output = new Pattern(output);
			if(input != null) sr.input = new Pattern(input);
			if(condition != null) sr.condition = new Script(condition);
			if(execute != null) sr.execute = new Script(execute);
			ret = sr;
		}else if(output == null && responses != null) {
			// RecursiveResponse
			RecursiveResponse rr = new RecursiveResponse();
			rr.responses = new ArrayList<Response>(responses.size());
			for(int i = 0; i < responses.size(); i++) {
				rr.responses.add(responses.get(i).getResponse());
			}
			if(input != null) rr.input = new Pattern(input);
			if(condition != null) rr.condition = new Script(condition);
			if(new_question != null) rr.new_question = new_question;
			ret = rr;
		}else {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			responses = null;
			throw new RuntimeException("Incompatible response attributes in "+gson.toJson(this));
		}	
		return ret;
	}
}

abstract class Response{
	public Pattern input;
	public Script condition;
	public boolean valid(String question) {
		return (input == null     || input.matches(question)) &&
			   (condition == null || condition.evaluate());
	}
	public abstract Optional<String> answer(String question);
	public abstract SerializableResponse serializable();
}
class SimpleResponse extends Response{
	public Pattern output;
	public Script execute;
	@Override
	public Optional<String> answer(String question) {
		Optional<String> ans = Optional.empty();
		if(valid(question)){
			ans = Optional.of(output.generate());
			if(execute != null) execute.execute();
		}
		return ans;
	}
	@Override
	public SerializableResponse serializable() {
		SerializableResponse sr = new SerializableResponse();
		if(input != null) sr.input = input.toString();
		if(condition != null) sr.condition = condition.toString();
		if(output != null) sr.output = output.toString();
		if(execute != null) sr.execute = execute.toString();
		return sr;
	}
}
class RecursiveResponse extends Response{
	public List<Response> responses;
	public String new_question;
	@Override
	public SerializableResponse serializable() {
		SerializableResponse sr = new SerializableResponse();
		if(input != null) sr.input = input.toString();
		if(condition != null) sr.condition = condition.toString();
		if(responses != null) {
			sr.responses = new ArrayList<SerializableResponse>();
			for(Response response: responses) {
				sr.responses.add(response.serializable());
			}
		}
		if(new_question != null) sr.new_question = new_question;
		return sr;
	}
	@Override
	public Optional<String> answer(String question) {
		Optional <String> ans = Optional.empty();
		if(valid(question)) {
			if(new_question != null) {
				question = new_question;
			}
			for(Response response : responses) {
				ans = response.answer(question);
				if(ans.isPresent()) break;
			}
		}
		return ans;
	}
}

public class CUI {
	private PythonInterpreter innerState;
	public Response response;
	public CUI() {
		innerState = new PythonInterpreter();
		Script.pyMachine = innerState;
	}
	public boolean load(Reader reader) {
		try {
			Gson gson = new Gson();
			SerializableResponse res = gson.fromJson(reader, SerializableResponse.class);
			response = res.getResponse();
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
	public String answer(String question) {
		return response.answer(question).orElse("");
	}
	public String toJson(boolean pretty, boolean escape) {
		GsonBuilder builder = new GsonBuilder();
		if(pretty) builder.setPrettyPrinting();
		if(!escape) builder.disableHtmlEscaping();
		Gson gson = builder.create();
		return gson.toJson(response.serializable());
	}
}
