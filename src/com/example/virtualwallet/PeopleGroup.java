package com.example.virtualwallet;

import java.util.ArrayList;

/**
 * Opisuje grupe znajomych
 * @author sygi
 *
 */
public class PeopleGroup {
	public String name;
	public ArrayList<Person> people; //zalozenie - korzystam tylko z pol name, mail, nie zmieniam paid
	public PeopleGroup(String nm){
		name = nm;
		people = new ArrayList<Person>();
	}
	
	public boolean removePerson(String name){
		for(Person p : people){
			if (p.name.equals(name)){
				people.remove(p);
				return true;
			}
		}
		return false;
	}
}
