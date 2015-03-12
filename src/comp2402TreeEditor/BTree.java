package comp2402TreeEditor;

import java.util.*;
import java.awt.*;
import java.io.*;

//DISCLAIMER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==========
//This code is designed for classroom illustration
//It may have intentional omissions or defects that are
//for illustration or assignment purposes
//
//That being said: Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public class BTree extends Tree implements BTreeADT{
	//       =====
    //This class represents a binary tree
    
	//CONSTRUCTORS==================================================================
	public BTree() {
    }



    //BTreeADT interface methods =========================================================
     public void insert(String dataString){
    	
    	/*For a general binary tree: insert the new data items represented by data string
    	 *based on the current height of the left and right subtrees. Add the new data item
    	 *to the shortest of the subtrees to maintain a balancing of the binary tree.
    	 *Note that the nodes are not in any particular useful order, the tree is just kept
    	 *balanced to illustrate a balanced insert.
   	    */
   	    
   	    //System.out.println("BTree::insert(String)");
    	Data data = new Data(dataString);
    	
    	TreeNode newChildNode = new BTreeNode(data);
    	
    	if(isEmpty()) {
    		setRoot(newChildNode);
    		newChildNode.setLocation(getOwner().getDefaultRootLocation());
    	}
        else getRoot().insertNode(newChildNode);
    	
    }
    
    public void remove(String aKeyString){
    //remove any node whose Data object's key matches aKeyString
    //to remove it, find some leaf node and swap data items with the leaf node, and
    //then remove the leaf node.
    //Again for a general binary tree the nodes are not deemed to be in any meaningful order.
    
      Iterator<TreeNode> theNodes = getNodes().iterator();
      
      TreeNode nodeToDelete = null;
      TreeNode someLeafNode = null;
      
      while(theNodes.hasNext()){
      	TreeNode node = theNodes.next();
      	if(node.key().equals(aKeyString)) nodeToDelete = node;
      	if(node.isLeaf()) someLeafNode = node;
      }
      
      if(nodeToDelete == null) 
        return;
        
      if(nodeToDelete.isRoot() && nodeToDelete.isLeaf()) {
      	setRoot(null);
      	return;
      }
      if(nodeToDelete.isLeaf()){
      	((TreeNode) nodeToDelete.getParent()).removeChildNode(nodeToDelete);
      	return;
      }
      nodeToDelete.setData(someLeafNode.getData());
      ((TreeNode) someLeafNode.getParent()).removeChildNode(someLeafNode);

    }   
    //===================================================================================
    // The get & set methods=============================================================
    
    //These are Non-ADT methods for manipulating the tree. These methods should not
    //be called directly by ADT clients
    
    public void createNewRoot(Point aLocation){
    	//create a new root for the tree    	
    	setRoot(new BTreeNode(aLocation));
    } 
       
    public void createChildForNode(TreeNode aNode, Point aLocation){
    	
    	if(!(aNode instanceof BTreeNode)) return; //do nothing
    	//create a child for aNode in this tree if permitted by this type of tree
    
    	
    	
    	BTreeNode node = (BTreeNode) aNode; //cast as BTreeNode
    	BTreeNode newNode = new BTreeNode(aLocation);
    	
    	if(aLocation.x < node.getLocation().x){
    		//creating a left child
    		
    		if(node.leftChild() == null){
    			newNode.setParent(node);
    			node.setLeftChild(newNode);
    			
    		}
    	}
    	else{
    		//creating a right child
    		if(node.rightChild() == null){
    			newNode.setParent(node);
    			node.setRightChild(newNode);
    			
    		}
    	}
    	
    }

}
