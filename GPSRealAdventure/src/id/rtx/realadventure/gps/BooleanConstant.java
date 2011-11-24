package id.rtx.realadventure.gps;

public class BooleanConstant {
	public static boolean ParserBoolean(String code){
		boolean True = true;
		boolean False = false;
		
		if(code=="1")
			return True;
		else
			return False;		
	}

}
