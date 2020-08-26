package com.github.miguelmj;

import java.util.Optional;

/**
 * Base class for responses.
 */
public abstract class Response{
	public Pattern input;
	public Script condition;
	public boolean valid(String question) {
		return (input == null     || input.matches(question)) &&
			   (condition == null || condition.evaluate());
	}
	public abstract Optional<String> answer(String question);
	public abstract SerializableResponse serializable();
}
