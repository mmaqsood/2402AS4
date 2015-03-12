package comp2402TreeEditor;

import java.util.*;

//DISCLAIMER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==========
//This code is designed for classroom illustration
//It may have intentional omissions or defects that are
//for illustration or assignment purposes
//
//That being said, Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public class Data implements DataADT{
	
	//This class represents the data model object stored in a tree node
	//The key and value are both Strings, but if two keys are compared
	//that happen to be Strings that could parse to Integers, they are
	//compared based on their Integer values.
	
	String key;
	String value; 
	
	// CONSTRUCTORS ========================================================
	
	public Data(String itemString){
		/*itemString is either a single string that represents the key or
		 *a String with two items separated by a comma. The first represents the key
		 *and the other of the pair a value.
		*/
		
		if(itemString == null || itemString.length()==0) return;
		
		String items[] = itemString.split(",");
		if(items.length == 1){
		  key = items[0];
		  value = null;
	    }
	    if(items.length == 2){
		  key = items[0];
		  value = items[1];
	    }

	}
	
	public Data(String aKey, String aValue) {
		key = aKey;
		value = aValue;
	}
	
	//get and set methods
	public String key() {return key;}
	public String value() {return value;}	

	
	public int compare(DataADT aData){
		
		//If both the this object and aData have String keys that could represent
		//integers, then compare them based on the integer values of their keys.
		//The number returned is:
		// < 0 if the this object should precede the aData object
		// ==0 if they are equal
		// > 0 if the this object should after the aData object
		//based on comparison of their keys
	
	    boolean areIntegerKeys = true;
	    Integer thisKey = null;
	    Integer aDataKey = null;
	    
	    try{
	       thisKey = Integer.parseInt(key);
	    }
	    catch(NumberFormatException e){
	    	areIntegerKeys = false;
	    }
	    try{
	      aDataKey = Integer.parseInt(aData.key());
	    }
	    catch(NumberFormatException e){
	    	areIntegerKeys = false;
	    }
	    
	    if(areIntegerKeys){
	       	return thisKey.compareTo(aDataKey);
	    }
	    else return key.compareTo(aData.key());
	
	}
	
}