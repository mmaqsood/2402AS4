package comp2402TreeEditor;

import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.JOptionPane;

public class Trie extends Tree{
	
	// Constructor
	public Trie() {
    }
    
    
    //insert a word given by textString into the Trie
    //This runs in O(dm) time where d is the size of the alphabet (max number of children per node)
    //and m is the size of the string we are removing (or the height it travels since this is a Trie)
    //We run through the length of textString (m) and for each character we check the children (max of d)
    //Therefore the running time is dxm... thus it runs in O(dm)
    public void insert(String textString){
     	
    	super.clearSelections(); //clear any existing selections
    	Data data = new Data(textString);
    	
    	TreeNode newChildNode = new TrieNode(data);
    	TreeNode blankRoot = new TrieNode(" ");
    	
    	if(isEmpty()) {
    		setRoot(blankRoot);
    		blankRoot.setLocation(getOwner().getDefaultRootLocation());
    	}
    	
    	TreeNode parent = getRoot();

    	// Loop for input string
        for (int i = 0; i < textString.length(); i++) {
        	boolean matched = false; // to detect whether or not character existed in tree
        	
    		data = new Data("" + textString.charAt(i));
    		newChildNode = new TrieNode(data);
			
			
			// Loop to check if current character at this position in string already exists in tree
    		for (int j = 0; j < parent.getChildren().size(); j++) {
        		if (parent.getChildren().get(j).key().toLowerCase().equals((""+textString.charAt(i)).toLowerCase())) {
        			parent = parent.getChildren().get(j);
        			matched = true;
        			((TrieNode)parent).setCount(((TrieNode)parent).getCount() + 1);
        			break;
        		}
        	}
			
			// Add new node for an unmatched character
			if (!matched) {
				newChildNode.setLocation(parent.getLocation());
				parent.insertNode(newChildNode);
				parent = newChildNode;
			}
			
			// Set the last letter
			if (i == (textString.length() - 1)) {
				((TrieNode)parent).setLastLetter(true);
			}
    	}
    	
    }
    
    
    //remove a node whose Data object's key matches aKeyString
    //This runs in O(dm) time where d is the size of the alphabet (max number of children per node)
    //and m is the size of the string we are removing (or the height it travels since this is a Trie)
    public void remove(String aKeyString){
    	
    	super.clearSelections(); //clear any existing selections
    	
    	TreeNode parent = getRoot();
    
    	for (int i=0; i < aKeyString.length(); i++) {
    		boolean matched = false;
    		
	    	for (int j=0; j < parent.getChildren().size(); j++) {
	    		if (parent.getChildren().get(j).key().toLowerCase().equals((""+aKeyString.charAt(i)).toLowerCase())) {
	    			parent = parent.getChildren().get(j);
	    			matched = true;
	    			break;
	    		}
	    	}
	    
	    	if (!matched) return;
	    	
	    	// Set the last letter
	    	if (i == (aKeyString.length() -1)) {
	    		((TrieNode)parent).setLastLetter(false);
	    	}
	    }
	    // select the last character near where the word is printed
	    // if we got to the bottom that means we found the word
	    if (parent.isLeaf()) {
	    	parent.setSelection(true);
	    
	    	TreeNode newParent = (TreeNode) parent.getParent();
	    
		    for (int i=0; i < aKeyString.length(); i++) {
		    	parent.remove("" + aKeyString.charAt(aKeyString.length()-i-1));
		    	parent = newParent;
		    	newParent = (TreeNode) newParent.getParent();
		    }
		}
    
    }
    
 	//This method starts from the root and tries to match character for character down the tree
 	//This runs in O(dm) time where d is the size of the alphabet (max number of children per node)
    //and m is the size of the string we are removing (or the height it travels since this is a Trie)
	public DataADT find(String aKeyString) {
        
        super.clearSelections(); //clear any existing selections
       
        if (getRoot() == null) return null;
        
        TreeNode parent = getRoot();
    
    	for (int i=0; i < aKeyString.length(); i++) {
    		boolean matched = false;
    	
    		for (int j=0; j < parent.getChildren().size(); j++) {
    			if (parent.getChildren().get(j).key().toLowerCase().equals((""+aKeyString.charAt(i)).toLowerCase())) {
    				parent = parent.getChildren().get(j);
    				matched = true;
    				break;
    			}
    		}
    
    		if (!matched) return null;
    	}
    	if (parent.isLeaf() || ((TrieNode)parent).isLastLetter()) {
    		// Select the word
    		selectWord((TrieNode) parent);
    	}
        return parent.getData();
    }
    
    // Helper method for selecting an entire path of nodes
    // To be used with find
    public void selectWord(TrieNode aNode) {
    	if (!aNode.isLastLetter())	return;
    	
    	while (!aNode.isRoot()) {
    		aNode.setSelection(true);
    		aNode = (TrieNode) aNode.getParent();
    	}
    }
    
    public void createChildForNode(TreeNode aNode, Point aLocation){
    	/*Graphical creating of nodes is not allowed for a binary search tree
    	 *since the binary search
    	 *tree wants to control where Nodes are placed.
    	*/
         
        JOptionPane.showMessageDialog(getOwner(), 
        "Please use ADT Insert to add nodes to Trie", 
        "Not allowed for Trie", 
        JOptionPane.ERROR_MESSAGE);
  
    	
    }
    
    public void createNewRoot(Point aLocation){
    	//disallow double-clicking for root creation	
    	JOptionPane.showMessageDialog(getOwner(), 
        "Please use ADT Insert to add nodes to Trie", 
        "Not allowed for Trie", 
        JOptionPane.ERROR_MESSAGE);
    }

    public boolean allowsGraphicalDeletion(){ 
    //Tries do not allow the deletion of arbitrary nodes since the tree
    //must get a chance to re-balance itself.
    	return false;
    }
    
}

