package com.example.virtualwallet;

import java.util.ArrayList;
import java.util.Date;

public class Transaction {
	Date time;
	String desc;
	ArrayList<Fee> charge; 
	//powinno sumować się do zera. 
	//W szczególności, jeśli ktoś coś wpłaci, a też dostaie, to powinien pojawić się dwa razy w tablicy
	Transaction(String desc){
		time = new Date();
		this.desc = desc;
		charge = new ArrayList<Fee>();
	}
	Transaction(String desc, Date time){
		this.time = time;
		this.desc = desc;
		charge = new ArrayList<Fee>();
	}
	Transaction(String desc, ArrayList<Fee> charge){
		this.desc = desc;
		this.charge = charge;
	}
	
	Transaction(){
		time = new Date();
		this.desc = "";
		charge = new ArrayList<Fee>();
	}
	
	public String getDesc(){
		//zmienic na SimpleDateFormat.format
		return time.toLocaleString() + desc;
		//to trzeba bedzie pewnie zmienic w zaleznosci od UI
	}
}
