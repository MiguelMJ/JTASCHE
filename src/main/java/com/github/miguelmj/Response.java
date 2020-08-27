package com.github.miguelmj;

import java.util.Optional;

/**
 * Base class for responses.
 */
public abstract class Response{
	public Pattern input;
	public Script condition;
	/**
	 * Checks if the Response is able to give an answer.
	 * @param question User input
	 * @return True if the input question matches and the condition is evaluated as truth.
	 */
	public boolean valid(String question) {
		return (input == null     || input.matches(question)) &&
			   (condition == null || condition.evaluate());
	}
	/**
	 * Returns the output associated to the question, if the Response is able to.
	 * @param question User input
	 * @return An answer if possible, empty optional if not.
	 */
	public abstract Optional<String> answer(String question);
	/**
	 * Return a serializable version of this Response object.
	 * @return A serializable version of this Response object.
	 */
	public abstract SerializableResponse serializable();
}
