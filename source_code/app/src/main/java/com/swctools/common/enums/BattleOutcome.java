package com.swctools.common.enums;

public enum BattleOutcome {

	DEFEAT{

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "YOU LOST!";
		}
		
	},
	VICTORY{

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "YOU WON!";
		}
		
	}
	;
	public abstract String toString();
}
