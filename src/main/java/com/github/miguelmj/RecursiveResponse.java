package com.github.miguelmj;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Response that can evaluate multiple conditions
 */
public class RecursiveResponse extends Response{
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
