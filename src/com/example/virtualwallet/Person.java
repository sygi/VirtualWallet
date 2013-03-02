package com.example.virtualwallet;

public class Person {
	String name; //specjalna osoba o name = wallet - oznacza wirtualny portfel
	Double paid;
	String mail;
	Person(String nm){
		name = nm;
		paid = 0.0;
		mail = "";
	}
	
	Person(String nm, String ml){
		name = nm;
		mail = ml;
		paid = 0.0;
	}
	
}
