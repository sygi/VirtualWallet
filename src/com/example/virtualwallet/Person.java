package com.example.virtualwallet;


public class Person {
	//wlasciwie to juz z tego nie korzystam
	static final String RED = "#FF00OO";
	String name; //specjalna osoba o name = wallet - oznacza wirtualny portfel
	Double paid;
	String mail;
	boolean active = true;
	
	Person(String nm){
		name = nm;
		paid = 0.0;
		mail = "";
		active = true;
		if (nm.equals("wallet"))
			active = false;
	}
	
	Person(String nm, String ml){
		name = nm;
		mail = ml;
		paid = 0.0;
		active = true;
		if (nm.equals("wallet"))
			active = false;
	}
	
	Person(Person other){
		this.name = other.name;
		this.paid = other.paid;
		this.mail = other.mail;
		this.active = other.active;
	}
	
	@Override
	public String toString(){
		//TODO - skracanie z dokladnoscia do 2 miejsc po przecinku
		//String paidDesc = (Double.valueOf(Math.round(paid * 100)/100.0)).toString();
		
		if (name == "wallet"){
			return ""; //nieladnie, moznaby wymyslec cos lepszego
		}
		if (paid == 0.0){ //TODO zmienic na < eps
			return name + " <font color=\"blue\">" + String.format("%.2f", paid) + "</font><br/>";			
		}
		if (paid > 0.0){
			return name + " <font color=\"green\">" + String.format("%.2f", paid) + "</font><br/>";
		}
		return name + " <font color=\"red\">" + String.format("%.2f", paid) + "</font><br/>";
	}
}
