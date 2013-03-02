package com.example.virtualwallet;

import java.util.ArrayList;
import java.util.Date;

public class Wallet {
	ArrayList<Person> people;
	ArrayList<Transaction> trans;
	String name;
	Date creationTime;
	Wallet(String name){
		this.name = name;
		creationTime = new Date();
	}
	String getLog(){
		return null;
	}
}
