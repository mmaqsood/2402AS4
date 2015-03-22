package comp2402TreeEditor;
import java.util.*;
import java.awt.*;
import java.io.*;

public class AVLTree extends BSTree implements BTreeADT{

	AVLTreeNode lastInsertedNode;
	AVLTreeNode lastDeletedNodeParent;
	boolean deletedNodeWasLeftChild;
	
	/*
	 * Find
	 * 
	 * Return the node with the given key, null if there is none
	 */
	public DataADT find(String aKeyString) {
		// Standard BST Search.
		return super.find(aKeyString);
    }   
	
	
	/*
	 * 
	 * Remove 
	 * 
	 */
	public void remove(String aKeyString){
	    /*
	     * TA NOTE:
	     * 
	     * With the Professor's code, the node that was being deleted was not being returned.
	     * 
	     * I need the node that was deleted's parent so I can begin looking up the tree starting that point.
	     * 
	     * Since this is an issue with the Prof's code, I find the node first and then delete it so I can keep track
	     * of the parent for rebalancing. 
	     * 
	     * Please understand I have to do this in order to get the parent node, this is due to the way the Prof programmed it.
	     * 
	     * I attempted to change his recursive call but when I was setting a variable in the recursive call,
	     * it was not being persisted so there was no way.
	     */
		boolean found = false;
		AVLTreeNode iterateeNode = (AVLTreeNode) this.root();
		while (!found){
			Data temp = new Data(aKeyString); //turn the string into a data object for comparisons

		   //compare this node's key using a temporary data object 
		   int comparision = temp.compare(iterateeNode.getData());
		   
		   if(comparision < 0 && iterateeNode.leftChild() != null) {
		   	   //search the left subtree
			   iterateeNode = (AVLTreeNode) iterateeNode.leftChild();
		   }
		   
		   else if(comparision > 0 && iterateeNode.rightChild() != null) {
		   	  //search the right subtree
			   iterateeNode = (AVLTreeNode) iterateeNode.rightChild();
		   }
		   else if(comparision == 0){
		      //found the node so track the parent
		      found = true;
		      lastDeletedNodeParent = (AVLTreeNode) iterateeNode.parent();
		      deletedNodeWasLeftChild = iterateeNode.isLeftChild();
		   }
		}
		// Perform the delete.
		super.remove(aKeyString);
		// Rebalance if needed
		checkAndRebalanceForDelete();
    }
	
	private void checkAndRebalanceForDelete(){
		// Decide what node we are going to be iterating starting with
		AVLTreeNode iterateeNode = (AVLTreeNode) ((lastDeletedNodeParent != null) ? lastDeletedNodeParent : this.root());
		
		// In the case that this is not the root, we want to start at the new child
		if (deletedNodeWasLeftChild && lastDeletedNodeParent.leftChild() != null){
			iterateeNode = (AVLTreeNode) lastDeletedNodeParent.leftChild();
		}
		else if (!deletedNodeWasLeftChild && lastDeletedNodeParent.rightChild() != null){
			iterateeNode = (AVLTreeNode) lastDeletedNodeParent.rightChild();
		}
		
		// Tell us if we should stop balancing the tree
		boolean completedBalancing = false;
		
		// While we have not completed a balance
		while (!completedBalancing){
			// We attempt to balance the tree if we need to, and this function will return true if it performed 
			// a balance.
			this.balanceIfNeededForDelete(iterateeNode /*z*/);
			
			// We do not need to do the following (moving up) if we have reached the top, the root.
			if (!iterateeNode.isRoot()) {
				// Move up the tree.
				iterateeNode = (AVLTreeNode) iterateeNode.parent();
			}
			else {
				// Right away, for the next iteration, we flip the flag
				// if we have reached the root, meaning we will no longer
				// further process
				completedBalancing = iterateeNode.isRoot();
			}
			
		}
	}
	
	private boolean balanceIfNeededForDelete(AVLTreeNode nodeToBeRebalanced){
		// Get the balance of z, the tree that we fear might be imbalanced.
		int balance = nodeToBeRebalanced.getBalance();
		
		// Three values determine an imbalance, 1, -1, 0.
		boolean isBalanced = balance == 1 || balance == -1 || balance == 0;
		
		// Balance the tree based on the case.
		if (!isBalanced) { 
			int leftHeight = (nodeToBeRebalanced.leftChild() == null) ? 0 : ((AVLTreeNode) nodeToBeRebalanced.leftChild()).getHeight();
			int rightHeight = (nodeToBeRebalanced.rightChild() == null) ? 0 : ((AVLTreeNode) nodeToBeRebalanced.rightChild()).getHeight();
			
			AVLTreeNode tallestChild = (AVLTreeNode) ((leftHeight > rightHeight) ? nodeToBeRebalanced.leftChild() : nodeToBeRebalanced.rightChild());
			
			leftHeight = (tallestChild.leftChild() == null) ? 0 : ((AVLTreeNode) tallestChild.leftChild()).getHeight();
			rightHeight = (tallestChild.rightChild() == null) ? 0 : ((AVLTreeNode) tallestChild.rightChild()).getHeight();
			
			AVLTreeNode tallestSubChild = (AVLTreeNode) ((leftHeight > rightHeight) ? tallestChild.leftChild() : tallestChild.rightChild());
			
			return determineCaseAndRebalanceForDelete(nodeToBeRebalanced, tallestChild, tallestSubChild); 
		}
		
		// Return false, indicating no balance has been performed.
		return false;
	}
	
	private boolean determineCaseAndRebalanceForDelete(AVLTreeNode nodeToBeRebalanced, AVLTreeNode tallestChild, AVLTreeNode tallestSubChild){
		
		// We have two of the nodes we need, z and y, but we need to define x.

		// Case 1: Left Left
		if (tallestChild.isLeftChild() && tallestSubChild.isLeftChild()){
			// Rotate right on z
			performRightRotation(nodeToBeRebalanced);
		}
		// Case 2: Left Right
		if (tallestChild.isLeftChild() & tallestSubChild.isRightChild()){
			// Rotate left on y
			// Rotate right on z
			doubleRight(nodeToBeRebalanced);
		}
		// Case 3: Right Right
		if (tallestChild.isRightChild() & tallestSubChild.isRightChild()){
			// Rotate left on z
			performLeftRotation(nodeToBeRebalanced);
		}
		// Case 4: Right Left
		if (tallestChild.isRightChild() & tallestSubChild.isLeftChild()){
			// Rotate right on y
			// Rotate left on z
			doubleLeft(nodeToBeRebalanced);
		}
		
		return true;
	}
	
	/*
	 *
	 * Insert
	 *  
	 */
	public void insert(String dataString){
    	
    	/*A binary search tree inserts nodes based on the key value of their Data item.
    	 *The tree maintains nodes in a total ordering. Balance of the tree depends on
    	 *the randomness of the inserts. Inserting an already sorted sequence will produce
    	 *the least efficient (unbalanced) tree
    	
    	O(height)
   	    */
   	    
   	    //System.out.println("BSTree::insert(String)");
    	Data data = new Data(dataString);
    	
    	TreeNode newChildNode = new AVLTreeNode(data);
    	
    	if(isEmpty()) {
    		setRoot(newChildNode);
    		newChildNode.setLocation(getOwner().getDefaultRootLocation());
    	}
        else getRoot().insertNode(newChildNode);
    	
    	lastInsertedNode = (AVLTreeNode) newChildNode;
    	checkAndRebalanceForInsert();
    	
    }
	
	/*
	 * 
	 */
	private void checkAndRebalanceForInsert(){
		// Decide what node we are going to be iterating starting with
		AVLTreeNode iterateeNode = lastInsertedNode;
		
		// Tell us if we should stop balancing the tree
		boolean completedBalancing = false;
		
		// No need to check for balance if this is the first node added.
		if (!iterateeNode.isRoot()){
			// While we have not completed a balance
			while (!completedBalancing){
				
				//completedBalancing = this.balanceIfNeededForDelete(iterateeNode)
				
				// Check the balance only if there is a last child,
				// indicating we have performed a traversal
				if (iterateeNode.getLastChild() != null){
					// NOTE: For now, based on the knowledge we have on rotations, we will stop
					//		 attempting to balance once we rotate ONCE as that is what we think will
					//		 fix ALL balance problems.
					
					// We attempt to balance the tree if we need to, and this function will return true if it performed 
					// a balance.
					completedBalancing = this.balanceIfNeededForInsert(iterateeNode /*z*/, iterateeNode.getLastChild()/*y*/);
				}
				
				// We do not need to do the following (moving up) if we have reached the top, the root.
				if (!iterateeNode.isRoot()) {
					// Finally, in order to track nodes we have hit from the path to the last node added to the 
					// node that is imbalanced, we attach it to each node.
					((AVLTreeNode) iterateeNode.parent()).setLastChid(iterateeNode);
					// Move up the tree.
					iterateeNode = (AVLTreeNode) iterateeNode.parent();
				}
				else {
					// Right away, for the next iteration, we flip the flag
					// if we have reached the root, meaning we will no longer
					// further process
					completedBalancing = iterateeNode.isRoot();
				}
				
			}
		}
	}
	
	/*
	 * Determines if the node needs to be balanced, if so, balances it.
	 */
	private boolean balanceIfNeededForInsert(AVLTreeNode nodeToBeRebalanced, AVLTreeNode childOnThePathFromInsertedNode){
		// Get the balance of z, the tree that we fear might be imbalanced.
		int balance = nodeToBeRebalanced.getBalance();
		
		// Three values determine an imbalance, 1, -1, 0.
		boolean isBalanced = balance == 1 || balance == -1 || balance == 0;
		
		// Balance the tree based on the case.
		if (!isBalanced) { return determineCaseAndRebalanceForInsert(nodeToBeRebalanced, childOnThePathFromInsertedNode); }
		
		// Return false, indicating no balance has been performed.
		return false;
	}
	
	/*
	 * Judges what the case is to appropriately balance the tree
	 */
	private boolean determineCaseAndRebalanceForInsert(AVLTreeNode nodeToBeRebalanced, AVLTreeNode childOnThePathFromInsertedNode){
		
		// We have two of the nodes we need, z and y, but we need to define x.

		// x is the child of y, that was on the path from w to z.
		AVLTreeNode subChildOnThePathFromInsertedNode = childOnThePathFromInsertedNode.getLastChild();
		
		// Case 1: Left Left
		if (childOnThePathFromInsertedNode.isLeftChild() && subChildOnThePathFromInsertedNode.isLeftChild()){
			// Rotate right on z
			performRightRotation(nodeToBeRebalanced);
		}
		// Case 2: Left Right
		if (childOnThePathFromInsertedNode.isLeftChild() & subChildOnThePathFromInsertedNode.isRightChild()){
			// Rotate left on y
			// Rotate right on z
			doubleRight(nodeToBeRebalanced);
		}
		// Case 3: Right Right
		if (childOnThePathFromInsertedNode.isRightChild() & subChildOnThePathFromInsertedNode.isRightChild()){
			// Rotate left on z
			performLeftRotation(nodeToBeRebalanced);
		}
		// Case 4: Right Left
		if (childOnThePathFromInsertedNode.isRightChild() & subChildOnThePathFromInsertedNode.isLeftChild()){
			// Rotate right on y
			// Rotate left on z
			doubleLeft(nodeToBeRebalanced);
		}
		
		return true;
	}
	
	/*
	 * Generic
	 * 
	 */
	
	/*
	 * Performs a right rotation on a node.
	 */
	private void performRightRotation(AVLTreeNode nodeToRotate){
		// We will right rotate this root
		
		// Grab the children
		AVLTreeNode leftChild = (AVLTreeNode) nodeToRotate.leftChild();
		AVLTreeNode rightChild = (AVLTreeNode) nodeToRotate.rightChild();
		
		
		// First, if the node we are rotating is the root, we are going 
		// to update the root
		if (nodeToRotate.isRoot()) { this.setRoot(leftChild);}
		
		// Deal with the simple cases first.
		
		//1. If we have nothing on the left, we can simply attach ourselves 
		//	 as our child's left child.
		if (rightChild == null){
			// Update the leftChild's parent
			if (nodeToRotate.parent() != null){
				AVLTreeNode grandParent = (AVLTreeNode) leftChild.parent().parent();
				AVLTreeNode parent = nodeToRotate;
				
				leftChild.setParent(grandParent);
				// When linking to the grand parent, we need to check 
				// what type of child we are and then link appropriately 
				if (parent.isLeftChild()) {
					grandParent.setLeftChild(leftChild);
				}
				else {
					grandParent.setRightChild(leftChild);
				}
			}
			else {
				leftChild.setParent(null);
			}
			// Make the nodeToRotate a rightChild of leftChild
			nodeToRotate.setParent(leftChild);
			nodeToRotate.setLeftChild(null);
			leftChild.setRightChild(nodeToRotate);
		}
		else if (leftChild != null) {
			// Now we enter the more complex cases.
			AVLTreeNode grandParent = (AVLTreeNode) nodeToRotate.parent();
			
			// 1. Link the grandParent with the new parent, which is the leftChild
			// If z is not a root and is a right child
			if (!nodeToRotate.isRoot() && nodeToRotate.isRightChild()){
				grandParent.setRightChild(leftChild);
			}
			// If z is not a root and is a left child
			else if (!nodeToRotate.isRoot() && nodeToRotate.isLeftChild()){
				grandParent.setLeftChild(leftChild);
			}
			
			// 2. Link the new parent (leftChild) to the grandParent.
			if (!nodeToRotate.isRoot()){
				leftChild.setParent(grandParent);
			}
			else {
				// Since this is the new root, it has no parent so null it.
				leftChild.setParent(null);
			}
			
			// 3. Link the old parent (nodeToRotate) to the new parent (leftChild)
			nodeToRotate.setParent(leftChild);
			
			// 4. In the case the new parent (leftChild) has a right child, switch it to
			//	  be the old parent (nodeToRtate)'s parent instead
			if (leftChild.rightChild() != null){
				AVLTreeNode _child = (AVLTreeNode) leftChild.rightChild();
				// Link the right child to the old parent (noteToRotate)
				_child.setParent(nodeToRotate);
				// Link the old parent (nodeToRotate) to it's new child (_child, rightChild of the leftChild)
				nodeToRotate.setLeftChild(_child);
			}
			else {
				// Unhook the old parent from the new parent, as they had a parent-child relationship before
				nodeToRotate.setLeftChild(null);
			}
			// 5. Finally, link the new parent (leftChild) to the old parent (rightChild)
			leftChild.setRightChild(nodeToRotate);
		}
	}
	
	/*
	 * Performs a left rotation on a node.
	 */
	private void performLeftRotation(AVLTreeNode nodeToRotate){
		// We will right rotate this root
		
		// Grab the children
		AVLTreeNode leftChild = (AVLTreeNode) nodeToRotate.leftChild();
		AVLTreeNode rightChild = (AVLTreeNode) nodeToRotate.rightChild();
		
		
		// First, if the node we are rotating is the root, we are going 
		// to update the root
		if (nodeToRotate.isRoot()) { this.setRoot(rightChild);}
		
		// Deal with the simple cases first.
		
		//1. If we have nothing on the left, we can simply attach ourselves 
		//	 as our child's left child.
		if (leftChild == null){
			// Update the rightChild's parent
			if (nodeToRotate.parent() != null){
				AVLTreeNode grandParent = (AVLTreeNode) rightChild.parent().parent();
				AVLTreeNode parent = nodeToRotate;
				
				rightChild.setParent(grandParent);
				// When linking to the grand parent, we need to check 
				// what type of child we are and then link appropriately 
				if (parent.isLeftChild()) {
					grandParent.setLeftChild(rightChild);
				}
				else {
					grandParent.setRightChild(rightChild);
				}
			}
			else {
				rightChild.setParent(null);
			}
			// Make the nodeToRotate a rightChild of leftChild
			nodeToRotate.setParent(rightChild);
			nodeToRotate.setRightChild(null);
			rightChild.setLeftChild(nodeToRotate);
		}
		else if (rightChild != null) {
			// Now we enter the more complex cases.
			AVLTreeNode grandParent = (AVLTreeNode) nodeToRotate.parent();
			
			// 1. Link the grandParent with the new parent, which is the rightChild
			// If z is not a root and is a right child
			if (!nodeToRotate.isRoot() && nodeToRotate.isRightChild()){
				grandParent.setRightChild(rightChild);
			}
			// If z is not a root and is a left child
			else if (!nodeToRotate.isRoot() && nodeToRotate.isLeftChild()){
				grandParent.setLeftChild(rightChild);
			}
			
			// 2. Link the new parent (rightChild) to the grandParent.
			if (!nodeToRotate.isRoot()){
				rightChild.setParent(grandParent);
			}
			else {
				// Since this is the new root, it has no parent so null it.
				rightChild.setParent(null);
			}
			
			// 3. Link the old parent (nodeToRotate) to the new parent (rightChild)
			nodeToRotate.setParent(rightChild);
			
			// 4. In the case the new parent (rightChild) has a left child, switch it to
			//	  be the old parent (nodeToRtate)'s parent instead
			if (rightChild.leftChild() != null){
				AVLTreeNode _child = (AVLTreeNode) rightChild.leftChild();
				// Link the right child to the old parent (noteToRotate)
				_child.setParent(nodeToRotate);
				// Link the old parent (nodeToRotate) to it's new child (_child, rightChild of the leftChild)
				nodeToRotate.setRightChild(_child);
			}
			else {
				// Unhook the old parent from the new parent, as they had a parent-child relationship before
				nodeToRotate.setRightChild(null);
			}
			// 5. Finally, link the new parent (leftChild) to the old parent (rightChild)
			rightChild.setLeftChild(nodeToRotate);
			
		}
	}

	/*
	 * Performs a right rotation on a node's right child and then
	 * performs a left rotation on the node itself.
	 */
    private void doubleLeft(AVLTreeNode nodeToBeRebalanced) {
    	performRightRotation((AVLTreeNode) nodeToBeRebalanced.rightChild());
        performLeftRotation(nodeToBeRebalanced);
    }
    
    /*
     * Performs a left rotation on a node's left child and then
     * performs a right rotation on the node itself.
     */
    private void doubleRight(AVLTreeNode nodeToBeRebalanced) {
    	performLeftRotation((AVLTreeNode) nodeToBeRebalanced.leftChild());
    	performRightRotation(nodeToBeRebalanced);
    }
}