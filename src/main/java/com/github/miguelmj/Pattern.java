/**
 * @file
 * @brief This file contains the Pattern class.
 * @author Miguel Mejía J.
 * @copyright MIT License
 */
package com.github.miguelmj;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


import com.mifmif.common.regex.Generex;

/**
 * Input matcher and output generator.
 */
public class Pattern {
	private java.util.regex.Pattern pattern;
	private Generex generator;
	private List<String> variables;
	private List<String> placeholders;
	private String outputTemplate;
	private String inputTemplate;
	/**
	 * Constructs the Pattern with an empty string.
	 */
	public Pattern() {
		pattern = java.util.regex.Pattern.compile("");
		generator = new Generex("");
		variables = new ArrayList<String>();
		placeholders = new ArrayList<String>();
	}
	/**
	 * Constructs the Pattern with the regex parsed in str.
	 * @param str Regular expression to parse.
	 */
	public Pattern(String str) {
		setString(str, false);
	}
	/**
	 * Constructs the Pattern with the regex parsed in str.
	 * @param str Regular expression to parse.
	 * @param adapt Tells to adapt the regex from custom format
	 */
	public Pattern(String str, boolean adapt) {
		setString(str, adapt);
	}
	/**
	 * Chanves the Pattern to use the regex parsed in str.
	 * @param str Regular expression to parse.
	 */
	public void setString(String str) {
		setString(str,false);
	}
	/**
	 * Changes the Pattern to use the regex parsed in str.
	 * @param str Regular expression to parse.
	 * @param adapt Tells to adapt the regex from custom format
	 */
	public void setString(String str, boolean adapt) {
		if (adapt) str = RegexAdapter.adapt(str);
		inputTemplate = str;
		variables = new ArrayList<String>();
		placeholders = new ArrayList<String>();
		
		// First we look for placeholders in the pattern
		String phPattStr = "\\(\\?<(?<id>[^>]*)>\\([^)]*\\)[^)]*\\)";
		java.util.regex.Pattern phPatt = java.util.regex.Pattern.compile(phPattStr);
		
		Matcher phFinder = phPatt.matcher(str);
		while(phFinder.find()) {
			placeholders.add(phFinder.group("id"));
		}
		// Then we look for variables in the pattern
		Matcher varFinder = RegexAdapter.variable.matcher(str);
		while(varFinder.find()) {
			variables.add(varFinder.group("id"));
		}
		// And now we set the outputTemplate (the inputTemplate is the same pattern)
		outputTemplate = phFinder.replaceAll("\\\\\\$${id}");
		
		// Now we make the recognizer and the generator if they
		// don't depend on any variable
		if(variables.isEmpty()) {
			pattern = java.util.regex.Pattern.compile(inputTemplate);
			if(placeholders.isEmpty()) {
				generator = new Generex(outputTemplate);
			}
		}
	}
	/**
	 * Checks if str matches the pattern.
	 * @param str String to compare against the pattern.
	 * @return True if the string matches the pattern.
	 */
	public boolean matches(String str) {
		java.util.regex.Pattern localPatt;
		if(pattern != null){
			localPatt = pattern;
		}else {
			String recognizerStr = RegexAdapter.remplaceVars(inputTemplate);
			localPatt = java.util.regex.Pattern.compile(recognizerStr);
		}
		
		java.util.regex.Matcher matcher = localPatt.matcher(str);
		boolean match = matcher.matches();
		if(match) 
			for(String ph: placeholders) {
				String value = matcher.group(ph);
				if(!ph.isEmpty())
					Script.pyMachine.set(ph, value);
			}
		return match;
	}
	/**
	 * Generate a string that matches the pattern.
	 * @return A random string that matches the pattern.
	 */
	public String generate() {
		Generex localGen;
		if(generator != null) {
			localGen = generator;
		}else{
			String generatorStr = RegexAdapter.remplaceVars(outputTemplate);
			localGen = new Generex(generatorStr);
		} 
		return localGen.random();
	}
	/**
	 * Return the regex parsed to construct the pattern.
	 */
	public String toString() {
		return pattern.toString();
	}
}
