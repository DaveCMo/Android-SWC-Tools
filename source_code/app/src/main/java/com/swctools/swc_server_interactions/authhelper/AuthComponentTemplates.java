package com.swctools.swc_server_interactions.authhelper;

public enum AuthComponentTemplates {
	USERTIMESTAMP{

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "{%1$suserId%1$s:%1$s%2$s%1$s,%1$sexpires%1$s:%3$s}";
		}
		
	},
	TOKENSTR{

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "%1$s.%2$s";
		}
		
	},
	REQUESTLOGIN{
		
		@Override
		public String toString(){
			String formatStr = "{%1$sauthKey%1$s:%1$s%1$s,%1$scommands%1$s:[{%1$saction%1$s:%1$sauth.getAuthToken%1$s,%1$sargs%1$s:{%1$splayerId%1$s:%1$s%2$s%1$s,%1$srequestToken%1$s:%1$s%3$s%1$s},%1$srequestId%1$s:1,%1$stime%1$s:0,%1$stoken%1$s:null}],%1$slastLoginTime%1$s:0,%1$spickupMessages%1$s:true}";
			return formatStr;
		}
	}
	;


	public abstract String toString();
	
}
//{"authKey":"","commands":[{"action":"auth.getAuthToken","args":{"playerId":"bcf4fc83-291a-11e7-9def-06948e004f29",
//"requestToken":"OTQyMDk1NTI2Mzc3NDYyRjk1RTYzQzUzNUVCNzg3MDQ1NjlBNDQ5OEM1MTM1N0I2QTQ3REY1OTA5M0Q3MjMzQS57InVzZXJJZCI6ImJjZjRmYzgzLTI5MWEtMTFlNy05ZGVmLTA2OTQ4ZTAwNGYyOSIsImV4cGlyZXMiOjE1MTYwMTUyMDMwOTB9"},"requestId":1,"time":0,"token":null}],"lastLoginTime":0,"pickupMessages":true}