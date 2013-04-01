package com.example.virtualwallet;

import java.util.HashMap;

import android.util.Log;

public class Currency {
	public String name, cut;
	private HashMap<String, Double> other;
	public Currency(String name, String cut, HashMap<String, Double>other){
		this.name = name;
		this.cut = cut;
		this.other = other;
	}
	public Double getOther(String otherCut){
		if (otherCut.equals(cut))
			return 1.0;
		Log.d("sygi", "question " + cut + "to " + otherCut);
	//	if (other.get(otherCut) == null)
	//		return -1.0;
		return other.get(otherCut);
	}
}