package comp2402TreeEditor;
import java.util.*;
import java.awt.*;
import java.io.*;
public class AVLTreeNode extends BSTreeNode implements BTreeNodeADT{
	
	private int height;
	// This is the last node that was encountered
	private AVLTreeNode lastChild;
	// This is the last node that was deleted
	private AVLTreeNode lastDeletedChild;
	public AVLTreeNode(Data data)
	{ super(data); }
	 
	public AVLTreeNode removeNodeFromTree(String aDataString){
		AVLTreeNode removedNode = removeNode(aDataString);
		return removedNode;
	}
	public AVLTreeNode removeNode(String aDataString){
		//recursively remove a node whose key matches the key contained in aDataString
		//This method must maintain the total ordering of the nodes in the binary search tree
		//O(tree height)
		
	   Data temp = new Data(aDataString); //turn the string into a data object for comparisons
	   
	   AVLTreeNode lastNodeDeletedParent = null;
	   //compare this node's key using a temporary data object 
	   int comparision = temp.compare(this.getData());
	   
	   if(comparision < 0 && leftChild != null) {
	   	  //search the left subtree
	   	  ((AVLTreeNode) this.leftChild()).removeNode(aDataString);
	   }
	   
	   else if(comparision > 0 && rightChild != null) {
	   	  //search the right subtree
	   	  ((AVLTreeNode) this.rightChild()).removeNode(aDataString);
	   }
	   else if(comparision == 0){
	      //found a node to remove so remove this node
		   lastNodeDeletedParent = (AVLTreeNode) getParent();
		   
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
	   return lastNodeDeletedParent;
	}

	public void remove(String aKeyString){
	    //remove any nodes whose Data object's key matches aKeyString
	    //when a node is removed from a general tree, the entire subtree
	    //rooted at that node is removed
	      
      if(key() != null && key().equals(aKeyString) && this.parent() != null) {
      	((TreeNode) this.parent()).removeChildNode(this);
      }
      Iterator<TreeNode> theChildren = getChildren().iterator();
      while(theChildren.hasNext()){
      	TreeNode child = theChildren.next();
      	if(child.key() != null && child.key().equals(aKeyString)){
      		removeChildNode(child);
      		return;
      	}
      	else
      	   child.remove(aKeyString);
      }

    }
	
	/*
	 * Getter/Setter for  last child that we use to know which was the 
	 * last child we hit on the path from the new inserted node
	 * to the imbalanced root
	 */
	public AVLTreeNode getLastChild(){
		return lastChild;
	}
	
	
	/*
	 * Sets last child that we use to know which was the 
	 * last child we hit on the path from the new inserted node
	 * to the imbalanced root
	 */
	public void setLastChid(AVLTreeNode lastChild){
		this.lastChild = lastChild;
	}
	
	/*
	 * Determins if node is left child of parent
	 */
	public boolean isLeftChild(){
		AVLTreeNode parent = (AVLTreeNode) this.getParent();
		
		return parent != null && (AVLTreeNode) parent.leftChild() == this;
	}
	
	/*
	 * Determines if node is right child of parent
	 */
	public boolean isRightChild(){
		AVLTreeNode parent = (AVLTreeNode) this.getParent();
		
		return parent != null && (AVLTreeNode) parent.rightChild() == this;
	}
	
	
	public void setHeight()
    {
       height = Math.max(leftHeight(), righttHeight()) + 1;
    }
	
	private int leftHeight()
    {
       return (this.leftChild() == null) ? 0 : this.leftChild().height();
    }
	
	private int righttHeight()
    {
       return (this.rightChild() == null) ? 0 : this.rightChild().height();
    }
	
	/*
	 * Get the balance i.e rightHeight - leftHeight
	 * 
	 * Also known as the balance factor.
	 */
	public int getBalance() {
		AVLTreeNode leftChild = (AVLTreeNode) this.leftChild();
		AVLTreeNode rightChild = (AVLTreeNode) this.rightChild();
		
		// Return the difference
		int leftHeight = (leftChild == null) ? 0 : leftChild.getHeight();
		int rightHeight = (rightChild == null) ? 0 : rightChild.getHeight();
		return leftHeight - rightHeight;
	}
	
	/*
	 * Look at right and left child, return the highest.
	 */
	public int getHeight() {
		AVLTreeNode leftChild = (AVLTreeNode) this.leftChild();
		AVLTreeNode rightChild = (AVLTreeNode) this.rightChild();
		
		// Return the max
		int leftHeight = (leftChild == null) ? 0 : leftChild.getHeight();
		int rightHeight = (rightChild == null) ? 0 : rightChild.getHeight();
		return 1 + Math.max(leftHeight, rightHeight);
	}

}