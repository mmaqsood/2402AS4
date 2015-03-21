package comp2402TreeEditor;
import java.util.*;
import java.awt.*;
import java.io.*;
public class AVLTreeNode extends BSTreeNode implements BTreeNodeADT{
	
	private int height;
	// This is the last node that was encountered
	private AVLTreeNode lastChild;
	
	public AVLTreeNode(Data data)
	{ super(data); }
	
	/*
	 * From the BSTreeNode, we have to copy it over because of instanceOf.
	 */
	
	
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
	private int getHeight() {
		AVLTreeNode leftChild = (AVLTreeNode) this.leftChild();
		AVLTreeNode rightChild = (AVLTreeNode) this.rightChild();
		
		// Return the max
		int leftHeight = (leftChild == null) ? 0 : leftChild.getHeight();
		int rightHeight = (rightChild == null) ? 0 : rightChild.getHeight();
		return 1 + Math.max(leftHeight, rightHeight);
	}

}