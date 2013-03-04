package com.example.virtualwallet;

import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

public class Wallet {
	public ArrayList<Person> people;
	private ArrayList<Transaction> trans;
	private String name;
	private Date creationTime;
	public String log;
	
	
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
	
	public String[] getNames(){
		String[] tab = new String[people.size()];
		for(int i = 0; i < people.size(); i++){
			tab[i] = people.get(i).name;
		}
		return tab;
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
	
	public Person findPerson(String name) throws Exception{
		Log.d("sygi", "szukam " + name);
		for(Person p : people){
			Log.d("sygi", "osoba" + p.name);
			if (p.name.equals(name))
				return p;
		}
		throw new Exception("No such person");
	}
	
	String getLog(){
		//TODO - zamienic hard coded strings na XMLe
		//TODO zmienic troche model i umozliwic zapamietywanie, kiedy jaka osoba przyszla
		String log = "";
		log = name + " created " + creationTime.toString() + "\n================\n";
		for(Transaction t : trans){
			log += "transaction \"" + t.desc + "\" on " + t.time.toString() + "\n";
			log += "payants:\n";
			for(Fee f : t.charge){
				if (f.paid > 0.0){
					log += f.toString() + "\n";
				}
			}
			
			log += "users:\n";
			for(Fee f : t.charge){
				if (f.paid < 0.0){
					log += f.who.name + "\n";
				}
			}
			
			log += "================\n";
		}
		
		return log;
	}
}
