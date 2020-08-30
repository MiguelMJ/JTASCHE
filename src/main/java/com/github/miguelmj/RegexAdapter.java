package com.github.miguelmj;

import java.util.regex.Matcher;

import org.python.core.PyObject;

// import com.mifmif.common.regex.Generex;

public class RegexAdapter {
	final static private char[] reserved = {'?','^','.','$','[',']'};
	final static private String id = "[a-zA-Z][a-zA-Z0-9]*";
	final static private String num = "[0-9]+";
	final static private String print = "[^\\\\t\\\\n ]+";
	final static private String quant = "\\{\\d+,\\d*\\}|\\+|\\*";
	
	final static private java.util.regex.Pattern optional 
					     = java.util.regex.Pattern.compile("\\\\\\[(?<opt>[^\\]]+)\\\\\\]");
	final static private java.util.regex.Pattern alphaplaceholder 
					     = java.util.regex.Pattern.compile("\\@>(?<id>"+id+")(?<quant>"+quant+")?");
	final static private java.util.regex.Pattern numplaceholder 
					     = java.util.regex.Pattern.compile("\\#>(?<id>"+id+")(?<quant>"+quant+")?");
	final static public java.util.regex.Pattern variable 
	                     = java.util.regex.Pattern.compile("\\\\\\$(?<id>"+id+")");
	
	private static String quoteRegex(String regex) {
		for(char ch: reserved) {
			regex = regex.replaceAll("\\"+ch, Matcher.quoteReplacement("\\"+ch));
		}
		return regex;
	}
	public static String adapt(String expr) {
		expr = quoteRegex(expr);
		Matcher matcher;
		matcher = optional.matcher(expr);
		expr = matcher.replaceAll("(${opt})?");
		matcher = alphaplaceholder.matcher(expr); 
		expr = matcher.replaceAll("(?<${id}>("+print+" ?)${quant})");
		matcher = numplaceholder.matcher(expr); 
		expr = matcher.replaceAll("(?<${id}>("+num+" ?)${quant})");
		return expr;
	}
	public static String remplaceVars(String expr) {
		Matcher matcher = variable.matcher(expr);
		while(matcher.find()) {
			PyObject pyValue = Script.pyMachine.get(matcher.group(1));
			String value = pyValue == null? "" : pyValue.asString();
			expr = matcher.replaceFirst(value);
			matcher.reset(expr);
		}
		
		return expr;
	}
	public static void main(String[] args) {
		Script.setup();
		
		Script.pyMachine.exec("nombre = 'Miguel'; cumplido = 'guapo'");
		
		String str1 = "Hola[, amigo[ mío]] $nombre, como estas[ hoy]? $cumplido";
		String str2 = "(Me llamo|Mi nombre es) @>nombre{1,3} y tengo #>age+ agnos";
		String str3 = "* + ^ $ .";
		String str4 = "Mis hermanos se llaman @>nombre+";
		System.out.println(adapt(str1));
		System.out.println(adapt(str2));
        //System.out.println(adapt(str3));
		//System.out.println(adapt(str4));
		
		//System.out.println(remplaceVars(adapt(str1)));
		String t = "Me llamo miguel mejía jiménez y tengo 2 3 agnos";
		System.out.println(t);
		System.out.println(java.util.regex.Pattern.matches(adapt(str2),t));
		
//		Generex gen;
//		gen= new Generex(optional.toString());
//		System.out.println("OPTIONAL");
//		System.out.println("========");
//		for(int i=0; i<5; i++) System.out.println(gen.random(8));
//
//		gen = new Generex(alphaplaceholder.toString());
//		System.out.println("ALPHA: "+alphaplaceholder.toString());
//		System.out.println("========");
//		for(int i=0; i<5; i++) System.out.println(gen.random(8));
//
//
//		gen = new Generex(numplaceholder.toString());
//		System.out.println("NUM");
//		System.out.println("========");
//		for(int i=0; i<5; i++) System.out.println(gen.random(8));
//
//		gen = new Generex(variable.toString());
//		System.out.println("VAR");
//		System.out.println("========");
//		for(int i=0; i<5; i++) System.out.println(gen.random(8));

	}
}
