package com.github.miguelmj;

import java.util.Optional;

/**
 * Response of single evaluation
 */
public class SimpleResponse extends Response{
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
		if(execute != null) sr.execute = execute.toString();
		if(output != null) sr.output = output.toString();
		return sr;
	}
}
