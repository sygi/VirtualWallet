package com.example.virtualwallet;

import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

public class Wallet {
	//TODO zmienic wszystko na private i zrobic jakies sensowne zarzadzanie tym :P
	public ArrayList<Person> people;
	public ArrayList<Transaction> trans;
	private String name;
	public Date creationTime;
	public String log;
	public int currencyNum = 0;
	public Currency baseCur;
	public int activePeople = 0;

	Wallet(){
		name = "";
		creationTime = new Date();
		people = new ArrayList<Person>();
		people.add(new Person("wallet")); //zawsze pierwsza osoba jest wallet
		trans = new ArrayList<Transaction>();
		activePeople = 0;
	}
	
	Wallet(String name){
		this.name = name;
		creationTime = new Date();
		people = new ArrayList<Person>();
		trans = new ArrayList<Transaction>();
		activePeople = 0;
	}
	
	public String[] getNames(){
		Log.d("sygi", "activePeople = " + activePeople);
		String[] tab = new String[activePeople];
		int iter = 0;
		for(int i = 1; i < people.size(); i++){
			if (people.get(i).active){
				Log.d("sygi", "person: " + people.get(i).name);
				tab[iter++] = people.get(i).name;
			}
		}
		return tab;
		//na wypadek kompatybilnosci wstecz, gdybym usunal 'wallet'
		/*String tab[] = new String[people.size()];
		for(int i = 0; i < people.size(); i++){
			tab[i] = people.get(i).name;
		}
		return tab;*/
	}
	
	public void addPerson(Person a){
		people.add(a);
		activePeople++;
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
	
	public boolean removePerson(String name){
		for(Person p: people){
			if(p.name.equals(name)){
				activePeople--;
				p.active = false;
				//people.remove(p);
				return true;
			}
		}
		return false;
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
					log += f.toString();
				}
			}
			log += "\n";
			log += "users:\n";
			for(Fee f : t.charge){
				if (f.paid < 0.0){
					log += f.who.name + " (" + -f.paid +")\n";
				}
			}
			
			log += "================\n";
		}
		
		return log;
	}
}
