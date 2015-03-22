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


public class BSTreeNode extends BTreeNode implements BTreeNodeADT{
	//This class represents a node in a binary tree
	

	// CONSTRUCTORS ========================================================
	public BSTreeNode() {
	}


	public BSTreeNode(Point aPoint) {
		super(aPoint);
	}
	
	public BSTreeNode(Data data) {
		super(data);
	}



    //BTreeNodeADT interface methods=====================================================
    
    
    //===================================================================================
    
	public void insertNode(TreeNode aNode){
		//This insert method of a Binary Search Tree inserts aNode
		//in search tree order.
		//O(tree height)
		
		if(!(aNode instanceof BSTreeNode)) return;
		
		if(aNode.getData().compare(getData()) < 0){
	    //aNode should go in left subtree
	      if(leftChild == null) {
	      	//left child is free
	      	leftChild = (BSTreeNode) aNode;
	      	aNode.setParent(this);
	      	aNode.setLocation(this.getLocation()); //starting location for animation
	      }
	      else{
	      	leftChild.insertNode(aNode);
	      }
		}
		else{
	    //aNode should go in right subtree
	       if(rightChild == null) {
	       	  //right child is free
	       	  rightChild = (BSTreeNode) aNode;
	       	  aNode.setParent(this);
	       	  aNode.setLocation(this.getLocation()); //starting location for animation
	       }
	       else {
	       	 rightChild.insertNode(aNode);
	       }
	    
		}
	}
	
	
	
	public void remove(String aDataString){
	//recursively remove a node whose key matches the key contained in aDataString
	//This method must maintain the total ordering of the nodes in the binary search tree
	//O(tree height)
	
	   Data temp = new Data(aDataString); //turn the string into a data object for comparisons

	   //compare this node's key using a temporary data object 
	   int comparision = temp.compare(this.getData());
	   
	   if(comparision < 0 && leftChild != null) {
	   	  //search the left subtree
	   	  leftChild.remove(aDataString);
	   }
	   
	   else if(comparision > 0 && rightChild != null) {
	   	  //search the right subtree
	   	  rightChild.remove(aDataString);
	   }
	   else if(comparision == 0){
	      //found a node to remove so remove this node
	      
	      if(isLeaf()) ((BSTreeNode) getParent()).removeChildNode(this);
	      
	      else if(isRoot() && rightChild == null) {
	      	  //hijack the leftChild as the new root of the tree
	      	  setData(leftChild.getData());
	      	  leftChild.setParent(null);
	      	  rightChild = (BSTreeNode) leftChild.rightChild();
	      	  if(rightChild != null) rightChild.setParent(this);
	      	  leftChild = (BSTreeNode) leftChild.leftChild();
	      	  if(leftChild != null) leftChild.setParent(this);
	      }
	      else if(isRoot() && leftChild == null) {
	      	  //hijack the right as the new root of the tree
	      	  setData(rightChild.getData());
	      	  rightChild.setParent(null);
	      	  leftChild = (BSTreeNode) rightChild.leftChild();
	      	  if(leftChild != null) leftChild.setParent(this);
	      	  rightChild = (BSTreeNode) rightChild.rightChild();
	      	  if(rightChild != null) rightChild.setParent(this);
	      	}
	      else if(leftChild == null && rightChild != null) {
	      	 //promote rightchild as new child of parent
	      	 ((BSTreeNode) getParent()).replaceChildNode(this, rightChild);
	      }
	      else if(rightChild == null && leftChild != null) {
	      	 //promote left child as new child of parent
	      	((BSTreeNode) getParent()).replaceChildNode(this, leftChild);
	      }
	      else{
	      	//we need to remove an internal node with two current children
	      	//find the smallest next node via the right subtree and use it as a
	      	//replacement for this node
	      	
	      	TreeNode smallest = ((BSTreeNode) rightChild).findSmallestNode();
	      	this.setData(smallest.getData()); //steel the data object of the smallest node
	      	smallest.remove(smallest.key());
	      	//System.out.println("BSTreeNode::remove: smallest to remove= " + smallest.key());
	      }
	      
		
	   }
	}
	
	
	    public DataADT find(String aKeyString){
    	
        System.out.println("BSTreeNode::find(String)");

	    Data temp = new Data(aKeyString); //turn the string into a data object for comparisons

	   //compare this node's key using a temporary data object 
	   int comparision = temp.compare(this.getData());
    	
	   if(comparision < 0 && leftChild != null) {
	   	  //search the left subtree
	   	  return leftChild.find(aKeyString);
	   }
	   
	   else if(comparision > 0 && rightChild != null) {
	   	  //search the right subtree
	   	  return rightChild.find(aKeyString);
	   }
	   else if(comparision == 0){
    		setSelection(true); //select the found node
    		return getData();
	   
	   }
    	
      return null;
    }
	
	protected TreeNode findSmallestNode(){
		//find the smallest node searching from this node along the left subtrees
		//in a binary search tree the node with the smallest key will be found by
		//going left as far as possible.
		
		if(leftChild == null) return this;
		else return ((BSTreeNode) leftChild).findSmallestNode();
		
	}
	
	protected void replaceChildNode(TreeNode currentChild, TreeNode newChild){
		//replace the currentChild of this node with the newChild node
		if(leftChild == currentChild){
			removeChildNode(currentChild);
			leftChild = (BSTreeNode) newChild;
			newChild.setParent(this);
		}
		else if(rightChild == currentChild){
			removeChildNode(currentChild);
			rightChild = (BSTreeNode) newChild;
			newChild.setParent(this);
		}
	}
	public void removeChildNode(TreeNode aChildNode){
		//remove the child aChildNode from this node 
    	if(leftChild == aChildNode) leftChild = null;
    	else if(rightChild == aChildNode) rightChild = null;
    	
    	aChildNode.setParent(null);
    }
		
    //===================================================================================
	

}