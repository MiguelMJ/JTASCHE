package com.github.miguelmj;

public class AppTest {
    public static void main(String[] args){
    	CUI cui = new CUI();
    	if(cui.load("examples/example1.json")) {
    		System.out.println("LOADED:");
    		System.out.println(cui.toJson(true,false));
    	}
    	try {
    		cui.initialize();
			System.out.println(cui.answer("mi nombre es Miguel Mejia"));
			System.out.println(cui.answer("Me llamo MiguelMJ Mejía"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
