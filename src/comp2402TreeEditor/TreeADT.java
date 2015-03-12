package comp2402TreeEditor;

import java.util.*;

public interface TreeADT {
	//           =======
	/*This is the interface for a general tree Abstract Data Type
	 *A general tree can have any number of children, unlike a binary
	 *tree that can have at most two children per node.
	 *
	 *The TreeADT also provides the basic methods of a Map or Dictionary ADT
	 */
	
    public boolean isEmpty();  //O(1)
    public int size(); //O(1)
    
    public TreeNodeADT root(); //O(1) 
    public ArrayList<TreeNodeADT> nodes(); //O(n) //a destruct-able collection of the tree nodes
    
    //these method are used to insert key-value data items into
    //the tree or remove nodes based on their key value
    
    public void insert(String aKeyValueString); //O(height)
    public void remove(String aKeyString); //O(height)
    public DataADT find(String aKeyString); //O(height)...O(n) depends on specific tree
    public ArrayList<DataADT> entries(); //O(n)
    
    int height(); //O(n) answer the height of the tree
    
    /*Note: for simplicity we have omitted the findAll(String key) that would also
     *typically be part of a Dictionary. So in that sense this data type behaves
     *more like a Map
     */
    	
}