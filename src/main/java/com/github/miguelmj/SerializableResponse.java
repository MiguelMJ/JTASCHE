package com.github.miguelmj;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Intermediate class for serializating and deserializating responses.
 */
public class SerializableResponse{
	public String input;
	public String output;
	public String condition;
	public String execute;
	public String new_question;
	List<SerializableResponse> responses;
	/**
	 * Depending on the fields that are not null, this function returns the corresponding Response object.
	 * @return Response object associated.
	 */
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
