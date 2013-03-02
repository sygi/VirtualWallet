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
	
	public void addPerson(Person a){
		people.add(a);
	}
	
	public void addTransaction(Transaction a){
		trans.add(a);
	}
	
	public int personCount(){
		return people.size();
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	String getLog(){
		return null;
	}
}
