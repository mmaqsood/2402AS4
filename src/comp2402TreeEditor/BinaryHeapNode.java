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
//This code is based on a hierarchy that still requires lots of casting

//That being said: Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public class BinaryHeapNode extends BTreeNode implements BTreeNodeADT{
	//This class represents a node in a binary tree
	

	// CONSTRUCTORS ========================================================
	public BinaryHeapNode() {
	}


	public BinaryHeapNode(Point aPoint) {
		super(aPoint);
	}
	
	public BinaryHeapNode(Data data) {
		super(data);
	}


	//The following methods should be promoted to BTreeNodeADT but have been left here so
	//legacy code is not affected for assignment purposes.
	boolean isLeftChild(){
		//answer whether this node is a left child of its parent
		if(this.parent() == null) return false;
		BTreeNodeADT parent = (BTreeNodeADT) this.parent();
		BTreeNodeADT leftChild = (BTreeNodeADT) parent.leftChild();
		if(leftChild == this)return true;
		
		return false;
	}
	boolean isRightChild(){
		//answer whether this node is a left child of its parent
		if(this.parent() == null) return false;
		BTreeNodeADT parent = (BTreeNodeADT) this.parent();
		BTreeNodeADT rightChild = (BTreeNodeADT) parent.rightChild();
		if(rightChild == this)return true;
		
		return false;
	}
	
	public void bubbleUp(){
		//bubble the smallest data key up from this node towards the root
		//O(log(n))
		
		if(this.parent()==null) return;
		BinaryHeapNode parent = (BinaryHeapNode) this.parent();
		if(this.getData().compare(parent.getData()) < 0){
			DataADT temp = this.getData();
			this.setData(parent.getData());
			parent.setData(temp);
		}
		
		parent.bubbleUp(); //recursively bubble the smallest data key element up
					
	}
	
	public void bubbleDown(){
		//bubble the larger data key down from this node.
		//swap it downward with the small of the leftchild or rightchild keys
		//O(log(n))
		
		if(leftChild == null && rightChild == null){
			return;			
		}
		else if(leftChild != null && rightChild == null){
			if(this.getData().compare(leftChild.getData()) > 0){
				DataADT temp = this.getData();
				this.setData(leftChild.getData());
				leftChild.setData(temp);
				((BinaryHeapNode)leftChild).bubbleDown();				
			}
			return;
		}
		else if(leftChild == null && rightChild != null){
			if(this.getData().compare(rightChild.getData()) > 0){
				DataADT temp = this.getData();
				this.setData(rightChild.getData());
				rightChild.setData(temp);
				((BinaryHeapNode)rightChild).bubbleDown();
			}
			return;
		}
		else if(leftChild != null && rightChild != null ){
			BinaryHeapNode nodeToSwapWith = null;
			if(leftChild.getData().compare(rightChild.getData()) < 0) nodeToSwapWith = (BinaryHeapNode) leftChild;
			else nodeToSwapWith = (BinaryHeapNode) rightChild;
				
			if(this.getData().compare(nodeToSwapWith.getData()) > 0){
				DataADT temp = this.getData();
				this.setData(nodeToSwapWith.getData());
				nodeToSwapWith.setData(temp);
				((BinaryHeapNode)nodeToSwapWith).bubbleDown();				
			}
			return;
		}


					
	}

    //BTreeNodeADT interface methods=====================================================
    
    
    //===================================================================================
    


}