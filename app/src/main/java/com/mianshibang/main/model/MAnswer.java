package com.mianshibang.main.model;

public class MAnswer extends MData {
	public String answer;
	public Integer best;
	
	public boolean isBest() {
		return best != null && best == 0;
	}

}
