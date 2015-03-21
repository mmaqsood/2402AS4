package comp2402TreeEditor;

import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.JOptionPane;

//DISCLAIMER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==========
//This code is designed for classroom illustration
//It may have intentional ommisions or defects that are
//for illustration or assignment purposes
//
//That being said: Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public class BSTree extends BTree implements BTreeADT{
	//       =====
    //This class represents a binary search tree
    
	private TreeNode lastInsertedNode;
	
	public TreeNode getLastInsertedNode() { return lastInsertedNode ;}
	
	//CONSTRUCTORS==================================================================
	public BSTree() {
    }



    //BTreeADT inteface methods =========================================================
     public void insert(String dataString){
    	
    	/*A binary search tree inserts nodes based on the key value of their Data item.
    	 *The tree maintains nodes in a total ordering. Balance of the tree depends on
    	 *the randomness of the inserts. Inserting an already sorted sequence will produce
    	 *the least efficient (unbalanced) tree
    	
    	O(height)
   	    */
   	    
   	    //System.out.println("BSTree::insert(String)");
    	Data data = new Data(dataString);
    	
    	TreeNode newChildNode = new BSTreeNode(data);
    	
    	if(isEmpty()) {
    		setRoot(newChildNode);
    		newChildNode.setLocation(getOwner().getDefaultRootLocation());
    	}
        else getRoot().insertNode(newChildNode);
    	
    	lastInsertedNode = newChildNode;
    	
    }
    
    public void remove(String aKeyString){
    //remove a node whose Data object's key matches aKeyString
    //for a binary search tree this requires moving nodes around so that
    //the total ordering of nodes is maintained
    
    //O(height)
    
      //System.out.println("BSTree::remove(String) ");
      
      if(isEmpty()) return;
      
      if(size()==1){
      	Data temp = new Data(aKeyString);
      	if(root().getData().compare(temp) == 0)
      	  setRoot(null);
      	return;
      }
      
      getRoot().remove(aKeyString);
      
    }
    
 	// Return the node with the given key, null if there is none
	public DataADT find(String aKeyString) {
    //          ====
    /*This find method makes uses the fact that binary search trees
     *maintain a total order among their nodes so only the correct portion
     *of the tree is searched
     *so does NOT do a complete search of the tree'
     *O(height)
     */
        
        System.out.println("BSTree::find(String)");
        
        clearSelections(); //clear any existing selections
       
        if (root() == null) return null;
        
        if(getRoot().key().equals(aKeyString))  {
        	getRoot().setSelection(true);
        	return getRoot().getData();
        	
        }
        
        return getRoot().find(aKeyString);
        
    }   
    
    public int height(){
    	if(isEmpty() ) return 0;
    	else return root().height();
    }       
    //===================================================================================
    // The get & set methods=============================================================
    
    //These are non-ADT methods for manipulating the Tree.
    //These methods should not be called directly by ADT clients
    
    public void createNewRoot(Point aLocation){
    	//create a new root for the tree    	
    	setRoot(new BSTreeNode(aLocation));
    }    
    public void createChildForNode(TreeNode aNode, Point aLocation){
    	/*Graphical creating of nodes is not allowed for a binary search tree
    	 *since the binary search
    	 *tree wants to control where Nodes are placed.
    	*/
         
        JOptionPane.showMessageDialog(getOwner(), 
        "Please use ADT Insert to add nodes to Binary Search Tree", 
        "Not allowed for Binary Search Tree", 
        JOptionPane.ERROR_MESSAGE);
  
    	
    }

    public boolean allowsGraphicalDeletion(){ 
    //Binary search trees do not allow the deletion of arbitrary nodes since the tree
    //must get a chance to rebalance itself. The TreeADT remove method should be use
    //to delete nodes
      return false;
    }

}
