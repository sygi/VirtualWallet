package com.example.virtualwallet;

import java.util.HashMap;

public class Currency {
	public String name, cut;
	private HashMap<String, Double> other;
	public Currency(String name, String cut, HashMap<String, Double>other){
		this.name = name;
		this.cut = cut;
		this.other = other;
	}
	public Double getOther(String otherCut){
		return other.get(otherCut);
	}
}