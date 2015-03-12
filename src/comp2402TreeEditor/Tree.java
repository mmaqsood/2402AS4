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


public class Tree implements TreeADT{
	//       =====
	//This class represents an arbitrary tree in which any number of children are
	//allowed per node
	//This tree has no restriction on the order of nodes and provides no height
	//balancing
	
	private TreeNode root = null; //root of the tree
	
	private TreeEditor owner = null; //editor who is editing this tree
	
	
    Random rand = new Random(); //used by algorithms that need a random number
    
    
	
	private static int nextAvailableIntKey = 1; //used when assigning keys to nodes automatically	
	public static String getNextKey(){return ""+ nextAvailableIntKey++;}
	public static void resetNextAvailableKey(){nextAvailableIntKey = 1;}
	
    public static double distanceBetween(Point a, Point b)
    //                   ===============
    //helper method to determine distance between two points
	{
		return Math.abs(Math.sqrt(Math.pow((double)(a.getX() - b.getX()),2) + 
        Math.pow((a.getY() - b.getY()),2)));
	}

	//CONSTRUCTORS==================================================================
	public Tree() {
		resetNextAvailableKey();
    }


    // TreeADT methods=============================================================
    public boolean isEmpty() {
    	//O(1)
    	return root == null;
    }
    public int size(){
    	//This is in theory an O(1) operation,but currently is implmemented
    	//as an O(n) for convenience n=number of nodes.
    	//That is, it current counts the nodes each time. This creates maximum flexibility
    	//for the editor to prune the tree any way it likes
    	
    	if(isEmpty()) return 0;
    	else return getNodes().size();
    }
    
    public TreeNodeADT root() { 
        //O(1)
        return getRoot(); 
    }
    
    public ArrayList<TreeNodeADT> nodes(){
    	
    	//O(n)
    	
    	ArrayList<TreeNodeADT> nodes = new ArrayList<TreeNodeADT>();
    	if(!isEmpty())
    	   nodes.addAll(getNodes());
    	return nodes;
    	
    }
  
     public ArrayList<DataADT> entries(){
    	
    	//O(n)
    	
    	if(isEmpty()) return new ArrayList<DataADT>();
    	
    	return root.entries();
    }
   
    public void insert(String dataString){
    	
    	/*For a general tree insert the new nodes as children of the root
    	 *unless there is a selected node in the tree, in that case
    	 *add the new node as a child of the selected node
    	 */
    	 
    	//O(n)
    	
    	Data data = new Data(dataString);
    	TreeNode newChildNode = new TreeNode(data);
    	
    	if(isEmpty()){
    		setRoot(newChildNode);
    		newChildNode.setLocation(owner.getDefaultRootLocation());
        }
    	else if(hasAnySelectedNodes()){
    		TreeNode selectedNode = selectedNodes().get(0);
    		newChildNode.setLocation(selectedNode.getLocation());
    		selectedNode.insertNode(newChildNode);
    	}
    	else{
    	
    	   TreeNode parent = getRoot();
    	   newChildNode.setLocation(parent.getLocation());
    	   parent.insertNode(newChildNode);
    	}

    }
    
    public void remove(String aKeyString){
    //remove a node whose Data object's key matches aKeyString
    
    //O(n) 
    
      if(isEmpty()) return;
      if(root.key() != null && root.key().equals(aKeyString)) {
      	root=null;
      }
      else root.remove(aKeyString);
    }
    
 	// Return the node with the given key, null if there is none
	public DataADT find(String aKeyString) {
    //          ====
    /*This find method makes no assumptions about the tree and
     *so does a complete search of the tree'
     *
     *O(n)
     */
        
        System.out.println("Tree::find(String)");
        
        clearSelections(); //clear any existing selections
       
        if (root == null) return null;
        
        if(root.key().equals(aKeyString))  {
        	root.setSelection(true);
        	return root.getData();
        	
        }
        
        return root.find(aKeyString);
        
    }   
    
    public int height(){
    	if(isEmpty() ) return 0;
    	else return root.height();
    }
   
    //=============================================================================
    
    public TreeEditor getOwner(){return owner;}
    public void setOwner(TreeEditor editor) {
    	//set the editor that is editing this tree
    	owner = editor;
    };
    
    
    //Internal Non-ADT methods for creating and manipulating the tree
    //These methods are used by the ADT methods to do internal tree manipulation
    //These methods themselves do not have any regard for balancing properties of the
    //tree, they simply preform the requested manipulation of the tree
    //These methods should not be called directly by ADT clients and are not
    //part of the ADT interfaces
    
    public ArrayList<TreeNode> getNodes(){
    	
    	if(isEmpty()) return new ArrayList<TreeNode>();
    	
    	return root.getNodes();
    }
    
    public TreeNode getRoot(){return root; }

    public void createNewRoot(Point aLocation){
    	//create a new root for this tree graphically positioned at aLocation	
    	setRoot(new TreeNode(aLocation));
    }
    
    public void setRoot(TreeNode aNode) {
    	
    	root = aNode;
    	if(root == null) return;
        if(root.getLocation() == null)
           root.setLocation(owner.getDefaultRootLocation());
    }
   
    public void createChildForNode(TreeNode parentNode, Point aLocation){
    	//create a child for aNode in this tree with drawing location aLocation
    	
        TreeNode newChildNode = new TreeNode(aLocation);
        parentNode.insertNode(newChildNode);
    	
    }

    public void deleteNode(TreeNode aNode){
    	//force aNode to be deleted from this tree
    	
    	if(aNode.isRoot()) {
    		setRoot(null);
    	    return;
    	}
    	
    	TreeNode parent = (TreeNode) aNode.getParent();
    	if(parent != null){
    		parent.removeChildNode(aNode);
    		
    	}
    }
    
    //Methods for traversing the nodes
    public void inOrderTraversal(StringBuffer nodesVisited){
    	if(isEmpty()) return;
    	TreeNode.traversalCount = 1;
    	root.inOrder(nodesVisited);
    }
    public void preOrderTraversal(StringBuffer nodesVisited){
     	if(isEmpty()) return;
    	TreeNode.traversalCount = 1;
    	root.preOrder(nodesVisited);
   	
    }
    public void postOrderTraversal(StringBuffer nodesVisited){
      	if(isEmpty()) return;
     	TreeNode.traversalCount = 1;
     	root.postOrder(nodesVisited);
  }
    

    //Methods for Graphical Manipulation and Editing of Tree==========================================	

    public boolean allowsGraphicalDeletion(){ 
    //answer whether this tree allows graphical deletion of arbiratry nodes
    //this method should be over-ridden by trees that don't want this to be possible
    return true;
    }
    
    // Return the node at the given location (if one exists)
    public TreeNode nodeAt(Point p) {
    //              ======
        
        ArrayList<TreeNode> nodes = getNodes();
        
        for (int i=0; i<nodes.size(); i++) {
            TreeNode aNode = nodes.get(i);
            int distance = (p.x - aNode.getLocation().x) * (p.x - aNode.getLocation().x) +
                           (p.y - aNode.getLocation().y) * (p.y - aNode.getLocation().y);
            if (distance <= (TreeNode.RADIUS * TreeNode.RADIUS))
                return aNode;
        }
        return null;
    }


 	public boolean hasAnySelectedNodes(){
 	//             ===================
 		//answer whether tree has any selected nodes
 		
		return selectedNodes().size() > 0;
	}

     public void clearSelections() {
    //           =================
        Iterator<TreeNode>    selectedNodes = selectedNodes().iterator();
        TreeNode           aNode;

        while(selectedNodes.hasNext()) {
            aNode = selectedNodes.next();
            aNode.toggleSelected();
        }


    }  

    public ArrayList<TreeNode> selectedNodes() {
    //                         ==============
    // Gets all the nodes that are selected
	    ArrayList<TreeNode>         selected = new ArrayList<TreeNode>();
        Iterator<TreeNode>    allNodes = getNodes().iterator();
        TreeNode           aNode;

        while(allNodes.hasNext()) {
			aNode = allNodes.next();
			if (aNode.isSelected())
				selected.add(aNode);
		}
        return selected;
	}
	
    public void selectNodesInArea(Rectangle boundingBox){
    //          =================
    	//Mark those nodes that fall within the boundingBox as selected
    	//otherwise mark as unselected
    	
    	
    	Iterator<TreeNode> nodes = getNodes().iterator();
    	
    	while(nodes.hasNext()){
    		TreeNode node = nodes.next();
    		node.setSelection(boundingBox.contains(node.getLocation()));
    	}
    }


    public void addNotesInAreaToSelection(Rectangle boundingBox){
    //          =========================
    	//Mark any nodes that fall within the boundingBox as selected
    	
    	Iterator<TreeNode> nodes = getNodes().iterator();
    	
    	while(nodes.hasNext()){
    		TreeNode node = nodes.next();
    		if(boundingBox.contains(node.getLocation())){
    		    node.setSelection(true);	
    		}
    	}
    }
    
    
     // Relocate nodes to random positions
    public void randomNodeLocations(int width, int height) {
    //            ==============
        Iterator<TreeNode>    allNodes1 = getNodes().iterator();
        TreeNode           aNode1;

        while(allNodes1.hasNext()) {
        	int randomX = rand.nextInt(width);
        	int randomY = rand.nextInt(height);
        	Point newLocation = new Point(randomX,randomY);
			aNode1 = allNodes1.next();
		    
			if(TreeEditor.displayAnimation){
        	    aNode1.setDeltaForLocation(newLocation, TreeEditor.AnimationSteps);
        	}
            else{
            
               aNode1.setLocation(newLocation);
            }
		}

 	}
    
     // reposition nodes
    public void repositionNodeLocations(int width, int height) {
    //          =======================
        //Reposition the nodes so that they spread out across the screen and are in
        //"mathematically correct" positions. This method can be called on a tree
        //to do automatic layout of the tree. 
        
        if(!isEmpty()) root.repositionNodeLocations(0,0, width, height);


 	}
   

    //Drawing methods========================================================
    public void doAnimationStep(){
    //          ==============
    	
        Iterator<TreeNode>    allNodes1 = getNodes().iterator();
        TreeNode           aNode1;
        
        //System.out.println("Graph::doAnimationStep");
        
        while(allNodes1.hasNext()) {
			aNode1 = allNodes1.next();
        	aNode1.moveNodeOneStep();
		}
    	
    	
    }
 
    public void draw(Graphics2D aPen){
    //          ====
    	
        if(isEmpty()) return;
        
        //Draw tree statistics
        aPen.drawString("type= " + getClass().getName(), 30, 30);
        aPen.drawString("size= " + size(),30,45);
        aPen.drawString("height= " + height(),30,60);
        
        //Draw the actual tree
        ArrayList<TreeNode> theNodes = getNodes();

    	Iterator<TreeNode> nodes = theNodes.iterator();
    	while(nodes.hasNext()){
    		nodes.next().draw(aPen);
    	}
    }
          
    //==========================================================================================

    
}
