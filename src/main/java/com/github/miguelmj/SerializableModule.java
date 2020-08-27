package com.github.miguelmj;

import java.util.ArrayList;
import java.util.List;

/**
 * Intermediate class to serialize and deserialize modules.
 */
public class SerializableModule{
	public String init = new String();
	public List<SerializableResponse> responses = new ArrayList<SerializableResponse>();
}