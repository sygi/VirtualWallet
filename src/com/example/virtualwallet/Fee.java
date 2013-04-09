package com.example.virtualwallet;

public class Fee {
	Person who;
	Double paid; //dodatnie - zapłacił - ujemne - dostał
	
	Fee(Person p, Double q){
		who = p;
		paid = q;
	}
	
	public String toString(){
		return who.name + " " + String.format("%.2f", paid) + "\n";
	}
}
