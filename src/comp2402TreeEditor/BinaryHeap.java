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

//This code is based on hierarchy that still requires lots of casting
//
//That being said: Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public class BinaryHeap extends BTree implements BTreeADT{
	//       =====
    //This class represents a binary heap
	
	private BinaryHeapNode lastNode = null;
    
	//CONSTRUCTORS==================================================================
	public BinaryHeap() {
    }
	
	
	public DataADT top(){
		//Answer the top (root) data item in the heap which should be the smallest
		if(this.isEmpty()) return null;
		else return root().getData();
	}
	
	public DataADT removeTop(){
		//Remove the top (root) node from heap and return its data item
		//which should be the one with the smallest key value
		
	   if(this.isEmpty()) return null;
	   
	   DataADT rootData = root().getData();
	   removeNode((BinaryHeapNode) root());
	   return rootData;
	}



    //BTreeADT inteface methods =========================================================
     public void insert(String dataString){
    	
    	/*A binary heap tree inserts nodes based on the key value such that for any internal
    	 * node it's key value is smaller, or a at least no bigger, then those of its two children
    	 * This heap condition is recursively true for any interal node.
    	
    	O(log(n)) //heap is at most O(log(n)) high for a heap of n nodes
   	    */
   	    
   	    //System.out.println("BSTree::insert(String)");
    	Data data = new Data(dataString);
    	
    	
    	BinaryHeapNode newChildNode = new BinaryHeapNode(data);
    	
    	if(isEmpty()) {
    		setRoot(newChildNode);
    		newChildNode.setLocation(getOwner().getDefaultRootLocation());
    		lastNode = newChildNode;
    	}
        else {
        	//add the child node to the next available node location
        	//that next to the current last node in the complete binary tree order
        	BinaryHeapNode lastParent = (BinaryHeapNode) lastNode.parent();
        	if(lastParent == null){
        		//last node must be the root
        		((BinaryHeapNode)this.root()).leftChild = newChildNode;
        		newChildNode.setParent(this.root());
        		newChildNode.setLocation(((BinaryHeapNode)this.root()).getLocation()); //starting location for animation
       		
        		lastNode = newChildNode;
        	}
        	else if(lastNode == this.getRightMostChild()){
        		//the next location would be the right child of lastNode's parent
        		BinaryHeapNode leftMostLeaf = this.getLeftMostChild();
        		leftMostLeaf.leftChild = newChildNode;
        		lastNode = newChildNode;
        		newChildNode.setParent(leftMostLeaf);
        		newChildNode.setLocation(leftMostLeaf.getLocation()); //starting location for animation
        		
        	}
        	else if(lastNode.isLeftChild()){
        		//the next location would be the right child of lastNode's parent
        		lastParent.rightChild = newChildNode;
        		lastNode = newChildNode;
        		newChildNode.setParent(lastParent);
        		newChildNode.setLocation(lastParent.getLocation()); //starting location for animation
        		
        	}
        	else if(lastNode.isRightChild()){
        		//the next location would be the right child of lastNode's parent
        		BinaryHeapNode parent = lastParent;
        		BinaryHeapNode grandParent = (BinaryHeapNode) parent.parent();
        		while(!parent.isLeftChild()){
        			parent = grandParent;
        			grandParent = (BinaryHeapNode)parent.parent();
        		}
        		//parent is a left child of grand parent
        		//proceed down right child of grand parent and then
        		//stay left till a leaf is found.
        		BinaryHeapNode newParent = (BinaryHeapNode)grandParent.rightChild;
        		
        		while(newParent.leftChild != null) 
        			  newParent = (BinaryHeapNode) newParent.leftChild;
        		
        		newParent.leftChild = newChildNode;
        		lastNode = newChildNode;
        		newChildNode.setParent(newParent);
        		newChildNode.setLocation(newParent.getLocation()); //starting location for animation
        		
        	}
        	
        	lastNode.bubbleUp(); //bubble smallest data key value towards the top


         }
    	
    }
    
    public void remove(String aKeyString){
    	
    //Remove a node whose key value is aKeyString
    	//It will take O(n) to find the node to remove but from that point
    	//the removal  of the node will take O(log(n)) time
    
    BinaryHeapNode nodeToRemove = null;	
    
    //O(N) to find an arbitrary node to remove
    for(TreeNodeADT t : this.nodes()){
    	if(t.getData().key().equals(aKeyString)) {
    		nodeToRemove = (BinaryHeapNode) t;
    		break;
    	}
    	
    }
    if(nodeToRemove == null) return;
    
    removeNode(nodeToRemove);
    }
    
    public void removeNode(BinaryHeapNode nodeToRemove){
    	//remove the nodeToRemove from heap and restore heap key
    	//properties by bubbling bigger key values down.
    	//The removal takes O(log(n)) time
    	
     if(isEmpty()) return;
     if(nodeToRemove == root() && size() == 1) {
    	 setRoot(null);
    	 lastNode = null;
    	 return;
     }
     
     BinaryHeapNode previousLastNode = null;
     
     if(lastNode.isRightChild()) 
    	 previousLastNode = (BinaryHeapNode)((BinaryHeapNode)lastNode.parent()).leftChild;
     
     else if(this.getLeftMostChild()==lastNode) 
    	 previousLastNode = this.getRightMostChild();
     
 	else if(lastNode.isLeftChild()){
		//the next location would be the right child of lastNode's parent
		BinaryHeapNode parent = (BinaryHeapNode) lastNode.parent();
		BinaryHeapNode grandParent = (BinaryHeapNode) parent.parent();
		while(!parent.isRightChild()){
			parent = grandParent;
			grandParent = (BinaryHeapNode)parent.parent();
		}
		//parent is a right child of grand parent
		//proceed down left child of grand parent and then
		//stay right till a leaf is found.
		previousLastNode = (BinaryHeapNode)grandParent.leftChild;
		
		while(previousLastNode.rightChild != null) 
			previousLastNode = (BinaryHeapNode) previousLastNode.rightChild;
		
		
		
	}
     
       nodeToRemove.setData(lastNode.getData()); //swap last node data with node to removes data  
       
       //disconnect last node
       BinaryHeapNode parentOfLast = (BinaryHeapNode) lastNode.parent();
       if(lastNode.isLeftChild()) parentOfLast.setLeftChild(null);
       else if(lastNode.isRightChild()) parentOfLast.setRightChild(null);
       lastNode.setParent(null);
       
       //assign new last node
	   lastNode = previousLastNode;
	   
	   nodeToRemove.bubbleDown();

      
      
    }
    
    //find method is inherited and runs in time O(n) for a heap of n nodes

    

    
    public int height(){
    	if(isEmpty() ) return 0;
    	else return root().height();
    }       
    //===================================================================================
    // The get & set methods=============================================================
    
    //These are non-ADT methods for manipulating the Tree.
    //These methods should not be called directly by ADT clients
    
    private BinaryHeapNode getRightMostChild(){
    	//answer the child node found by traversing down the right side of tree
    	if(isEmpty()) return null;
    	if(((BinaryHeapNode)root()).rightChild == null) return (BinaryHeapNode) root();
    	
    	BinaryHeapNode right = (BinaryHeapNode) ((BinaryHeapNode) root()).rightChild;
    	
    	while(right.rightChild != null) right = (BinaryHeapNode) right.rightChild;
    	
    	return right;
    }
    
    private BinaryHeapNode getLeftMostChild(){
    	//answer the child node found traversing down the left side of tree
    	if(isEmpty()) return null;
    	if(((BinaryHeapNode)root()).leftChild == null) return (BinaryHeapNode) root();
    	
    	BinaryHeapNode left = (BinaryHeapNode) ((BinaryHeapNode) root()).leftChild;
    	
    	while(left.leftChild != null) left = (BinaryHeapNode) left.leftChild;
    	
    	return left;
    }
    
    public void createNewRoot(Point aLocation){
    	//create a new root for the tree    	
    	setRoot(new BinaryHeapNode(aLocation));
    }    
    public void createChildForNode(TreeNode aNode, Point aLocation){
    	/*Graphical creating of nodes is not allowed for a binary heap
    	 *since the heap
    	 *wants to control where Nodes are placed.
    	*/
         
        JOptionPane.showMessageDialog(getOwner(), 
        "Please use ADT Insert to add nodes to Binary Search Tree", 
        "Not allowed for Binary Search Tree", 
        JOptionPane.ERROR_MESSAGE);
  
    	
    }

    public boolean allowsGraphicalDeletion(){ 
    //Binary search trees do not allow the deletion of arbitrary nodes since the heap
    //must get a chance to restore itself. The TreeADT remove method should be used
    //to delete nodes
      return false;
    }

}
