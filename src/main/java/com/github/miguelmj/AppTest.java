package com.github.miguelmj;

public class AppTest {
    public static void main(String[] args){
    	CUI cui = new CUI();
    	if(cui.load("examples/example0.json")) {
    		System.out.println("LOADED:");
    		System.out.println(cui.toJson(true,false));
    	}
    	try {
    		cui.initialize();
			System.out.println(cui.answer("hola"));
			System.out.println(cui.answer("hola"));
			System.out.println(cui.answer("como estas"));
			System.out.println(cui.answer("me alegro de verte"));
			System.out.println(cui.answer("adios"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
