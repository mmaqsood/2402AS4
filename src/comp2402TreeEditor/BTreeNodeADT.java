package comp2402TreeEditor;

import java.util.*;

//DISCLAIMER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==========
//This code is designed for classroom illustration.
//It may have intentional omissions or defects that are
//for illustration or assignment purposes.
//
//That being said: Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public interface BTreeNodeADT extends TreeNodeADT{
    
    public TreeNodeADT rightChild(); //provide a reference to the right child
	public TreeNodeADT leftChild();  //provide a reference to the left child
	
}