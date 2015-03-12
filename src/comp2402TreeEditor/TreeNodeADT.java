package comp2402TreeEditor;

import java.util.*;

//DISCLAIMER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==========
//This code is designed for classroom illustration
//It may have intentional omissions or defects that are
//for illustration or assignment purposes
//
//That being said: Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public interface TreeNodeADT {
	/*
	This interface represents our ADT for a general tree node.
	This abstract data type allows clients to obtain the data
	represented by the node and obtain the node's parent or children.
	
	The abstract data type does not allow clients to directly insert
	or remove nodes. That must be done via the tree itself so that the
	tree can control its own structure, perhaps keeping it balanced etc.
	
	Notice the TreeNodeADT does not itself provide any means to modify the
	structure of the tree. This is intentional. The concrete TreeNode classes
	will provide such methods, but TreeNodeADT clients can only navigate the
	tree.
	*/
	
    public DataADT getData(); //O(1)
	public void setData(DataADT aData); //O(1)
	public String key(); //O(1)
	public String value(); //O(1)
    public TreeNodeADT parent(); //O(1)
    public ArrayList<TreeNodeADT> children(); //O(number of children)
    public boolean isLeaf(); //O(1)
    public boolean isRoot(); //O(1)
    
    //utility methods
    public int height(); //O(n) answer the height of this node in its subtree
    public int depth(); //O(n) answer the depth from the tree root of this node

}