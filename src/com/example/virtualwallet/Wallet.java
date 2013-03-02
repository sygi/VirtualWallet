package com.example.virtualwallet;

import java.util.ArrayList;
import java.util.Date;

public class Wallet {
	public ArrayList<Person> people;
	private ArrayList<Transaction> trans;
	private String name;
	private Date creationTime;
	
	
	Wallet(){
		name = "";
		creationTime = new Date();
		people = new ArrayList<Person>();
		people.add(new Person("wallet")); //zawsze pierwsza osoba jest wallet
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
	
	public String getName(){
		return name;
	}
	
	String getLog(){
		return null;
	}
}
