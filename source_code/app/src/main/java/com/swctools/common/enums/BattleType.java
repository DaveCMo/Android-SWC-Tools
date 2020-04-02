package com.swctools.common.enums;

public enum BattleType {

	ATTACK{

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "Attack";
		}

		@Override
		public String pastTense() {
			// TODO Auto-generated method stub
			return "attacked";
		}
		
	},
	DEFENCE{

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "Defence";
		}

		@Override
		public String pastTense() {
			// TODO Auto-generated method stub
			return "defended";
		}
		
	};
	public abstract String toString();
	public abstract String pastTense(); 
	
}
