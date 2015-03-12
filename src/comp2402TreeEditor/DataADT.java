package comp2402TreeEditor;

import java.util.*;

//DISCLAIMER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==========
//This code is designed for classroom illustration
//It may have intentional omissions or defects that are
//for illustration or assignment purposes.
//
//That being said, Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public interface DataADT {
	
	//This interface represents the methods that can be invoked on a 
	//Data object that is stored in the Tree data types
	
	public String key();
	public String value();
	
	public int compare(DataADT aData);
		
		//If both the this object and aData have String keys that could represent
		//integers, then compare them based on the integer values of their keys.
		//The number returned is:
		// < 0 if the this object should precede the aData object
		// ==0 if they are equal
		// > 0 if the this object should after the aData object
		//based on comparison of their keys
	
}