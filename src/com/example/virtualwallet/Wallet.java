package com.example.virtualwallet;

import java.util.ArrayList;
import java.util.Date;

public class Wallet {
	private ArrayList<Person> people;
	private ArrayList<Transaction> trans;
	public String name;
	private Date creationTime;
	
	
	Wallet(){
		name = "";
		creationTime = new Date();
		people = new ArrayList<Person>();
		trans = new ArrayList<Transaction>();
	}
	
	Wallet(String name){
		this.name = name;
		creationTime = new Date();
		people = new ArrayList<Person>();
		trans = new ArrayList<Transaction>();
	}
	
	void addPerson(Person a){
		people.add(a);
	}
	
	void addTransaction(Transaction a){
		trans.add(a);
	}
	
	String getLog(){
		return null;
	}
}
