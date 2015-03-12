package comp2402TreeEditor;

import java.util.*;
import java.awt.*;
import java.io.*;

public class TrieNode extends TreeNode{
	
	// Constructors
	public TrieNode() {
	}
	
	public TrieNode(String text) {
		super(new Data(text));
	}
	
	public TrieNode(Point aPoint) {
		super(aPoint);
	}
	
	public TrieNode(Data data) {
		super(data);
	}
	
	// TrieNode counting for nodes that are used for multiple words
	private int 		count = 1;
	private boolean		isLastLetter = false;
	
	// Get and Set Methods
	public int getCount() { return count; }
	public boolean isLastLetter() { return isLastLetter; }
	public void setCount(int aCount) { count = aCount; }
	public void setLastLetter(boolean isLast) { isLastLetter = isLast; }
	
	
	
	public void insertNode(TreeNode aNode){
		
		if(!(aNode instanceof TrieNode)) return;
		
		aNode.setParent(this);
		
		if(getChildren().size() ==0) {
    		getChildren().add(aNode);
    		return;
    	}
    	else if(aNode.getLocation()==null){
    		getChildren().add(aNode);
    		return;
    	}
    			
		for (int i=0; i < getChildren().size(); i++) {
			// node should go on left of another node
			if (aNode.getData().compare(getChildren().get(i).getData()) < 0) {
				getChildren().add(i,aNode);
				return;
			}
		}
		// node should go on the end
		getChildren().add(aNode);
		return;
		
	}
	
	
	public void remove(String aKeyString){
      // if the keyString is the same as this node's key, remove it from its parent 
      if(key() != null && key().equals(aKeyString) && getParent() != null) {
      	((TrieNode)getParent()).removeChildNode(this);
      }
    }
        
	public void removeChildNode(TrieNode aChildNode){
    	// If this node is not shared remove it
    	if (aChildNode.getCount() == 1){
    		getChildren().remove(aChildNode);
    		aChildNode.setParent(null);
    	} else {
    		// otherwise decrease its count
    		aChildNode.setCount(aChildNode.getCount() - 1);
    	}
    }

	// Method for getting a word string for words entered into the Trie
	public String getWord(TreeNode aNode) {
		String word = "";
		
		if (aNode.isRoot()) return word;
		word += getWord((TreeNode) aNode.getParent()) + aNode.key();
		
		return word;
	}

	// We overwrite the drawNodeLabel for some special cases
	public void drawNodeLabel(Graphics aPen) {
		
		// If the node is a leaf draw it differently	 
		if (isLeaf()) {
			aPen.setColor(super.getDrawingColor());
			aPen.fillRect(getLocation().x - TreeNode.RADIUS, getLocation().y - TreeNode.RADIUS, TreeNode.RADIUS * 2, TreeNode.RADIUS * 2);
			
			aPen.setColor(Color.black);
			aPen.drawRect(getLocation().x - TreeNode.RADIUS, getLocation().y - TreeNode.RADIUS, TreeNode.RADIUS * 2, TreeNode.RADIUS * 2);
			
			aPen.drawString(getWord(this), getLocation().x - TreeNode.RADIUS, getLocation().y + TreeNode.RADIUS * 2);
		// If the node is a last letter of a word
		} else if (isLastLetter()) {
			aPen.setColor(Color.black);
			aPen.drawString(getWord(this), getLocation().x + TreeNode.RADIUS + 2, getLocation().y + 5);
		}
		super.drawNodeLabel(aPen);
	}

}
	
