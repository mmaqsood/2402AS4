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


public class BTreeNode extends TreeNode implements BTreeNodeADT{
	//This class represents a node in a binary tree
	
	BTreeNode rightChild = null;
	BTreeNode leftChild = null;

	// CONSTRUCTORS ========================================================
	public BTreeNode() {
	}


	public BTreeNode(Point aPoint) {
		super(aPoint);
	}
	
	public BTreeNode(Data data) {
		super(data);
	}


	private void initialize() {
	}

    //BTreeNodeADT interface methods=====================================================
	public TreeNode rightChild(){return rightChild;}
	public TreeNode leftChild(){return leftChild;}
	
	public boolean isLeaf(){ return (rightChild == null && leftChild == null); }
	
	public ArrayList<TreeNodeADT> children() {
		ArrayList<TreeNodeADT> theChildren = new ArrayList<TreeNodeADT>();
		theChildren.addAll(getChildren());
		return theChildren;
	
	}
    

		
    //===================================================================================
	
	// The get & set methods ============================================================
	public void setRightChild(BTreeNode aNode) {rightChild = aNode;}
	public void setLeftChild(BTreeNode aNode) {leftChild = aNode;}
	
	public ArrayList<TreeNode> getChildren(){
		
		ArrayList<TreeNode> children = new ArrayList<TreeNode>();
		if(leftChild != null) children.add(leftChild);
		if(rightChild != null) children.add(rightChild);
		
		return children;
	}

	public void insertNode(TreeNode aNode){
	//          ==========
		//This insert method of a Binary Tree attempts to keep the
		//tree balanced, although the nodes are not put in any meaningful
		//order
		
		if(!(aNode instanceof BTreeNode)) return;
		if(leftChild == null){
			leftChild = (BTreeNode) aNode;
			aNode.setParent(this);
			aNode.setLocation(this.getLocation());
		}
		else if(rightChild == null){
			rightChild = (BTreeNode) aNode;
			aNode.setParent(this);
			aNode.setLocation(this.getLocation());
		}
		else{
			int leftSubtreeHeight = leftChild.height();
			int rightSubtreeHeight = rightChild.height();
			if(leftSubtreeHeight < rightSubtreeHeight) leftChild.insertNode(aNode);
			else rightChild.insertNode(aNode);
		}
	}
	
	public void removeChildNode(TreeNode aNode){
	//          ===============
		System.out.println("BTreeNode::removeChild()");
    	if(leftChild == aNode) leftChild = null;
    	else if(rightChild == aNode) rightChild = null;
    	
    	aNode.setParent(null);
    }
    
    public void inOrder(StringBuffer nodesVisited){
    	//perform a in-order traversal an label the nodes with their traversal order
    	
    	if(leftChild != null) leftChild.inOrder(nodesVisited);
    	setNodeLabel(traversalCount++);
    	nodesVisited.append(key() + " ");
    	if(rightChild != null) rightChild.inOrder(nodesVisited);
    	

   }

    	
	// reposition nodes for a binary tree
    public void repositionNodeLocations(int originX, int originY, int width, int height) {
    //          =======================
    
    //reposition the nodes in the area provided by the bounding box
    
        final int levelHeight = TreeEditor.LEVEL_HEIGHT; //pixel height between levels in the tree

        Point newLocation = new Point(originX + width/2, originY + levelHeight);
	    if(TreeEditor.displayAnimation){
        	  setDeltaForLocation(newLocation, TreeEditor.AnimationSteps);
        }
        else{ setLocation(newLocation);}

        int widthPerChild = width/2; //for binary tree

        if(!isLeaf()){
        	
           if(leftChild != null)
             leftChild.repositionNodeLocations(originX, originY + levelHeight*2, widthPerChild, height-levelHeight*2);
           
           if(rightChild != null)
           	  rightChild.repositionNodeLocations(originX+widthPerChild, originY + levelHeight*2, widthPerChild, height-levelHeight*2);
                 	
        }
        
        
 	}


}