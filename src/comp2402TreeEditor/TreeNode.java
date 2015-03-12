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


public class TreeNode implements TreeNodeADT{
	//This class represents a node in a general tree.
    //This class also maintains many values and methods related to drawing the tree
    //in the tree editor GUI window. 
    
	// Here is a class variable to keep the radius of all nodes for drawing
	final public static int	RADIUS = 15; //drawing radius of the tree nodes
    
    final public static Color SELECTED_COLOR = Color.red; //color to show selected node in
    final public static Color NORMAL_COLOR = new Color(220,220,220); //light gray
    final public static Color ROOT_COLOR = new Color(220,240,240); //to display root of the tree
    final public static Color NODE_LABEL_COLOR = Color.green;
    
    final private static int labelPointSize = RADIUS * 3/2;
    final public static Font labelFont = new Font("Serif", Font.PLAIN, labelPointSize);

	// These are the important model variables
	private DataADT element; //model object this node is meant to represent
	
	private TreeNodeADT parent;
    private ArrayList<TreeNode>  children; 
	
    //Variables used for drawing and editing the graph
	private boolean   selected; //selection state for editing or display
	private Point     location; //drawing location of the node (center of node)
    
    //variables used to animate node movement
    private double deltaX = 0; //used when animating node moves
    private double deltaY = 0; //used when animating node moves
    private int numberOfSteps = 0; //number of steps to take in delta directions for animation
    private Point targetLocation = null; //used as eventual target location during an animation
	
	//Variables used by algorithms
	//These variables can be used by algorithms that need this sort of
	//information. 
	//Can be re-initialized and reused by algorithms that need them
    private boolean visited; //used by algorithms to mark node as visited
    private String label; //used by algorithms that want to label the nodes
                          //such as traversals that want to label the node with
                          //their traversal order
                          
    public static int traversalCount = 0;

	// CONSTRUCTORS ========================================================
	public TreeNode() {
		element = new Data(Tree.getNextKey(), "");
		initialize();
	}
	public TreeNode(Data aDataItem) {
		element = aDataItem;
		initialize();
	}


	public TreeNode(Point aPoint) {
		element = new Data(Tree.getNextKey(), "");
		initialize();
		location = aPoint;
	}


	private void initialize() {
		location = new Point(0,0);
		children = new ArrayList<TreeNode>();
		selected = false;
		deltaX = 0;
		deltaY = 0;
		numberOfSteps = 0;
	}

	// TreeNodeADT methods============================================================
	public DataADT getData() {return element;}
	public void setData(DataADT aData) {element = aData;}
	public String key() {return element.key();}
	public String value() {return element.value();}
	public TreeNodeADT parent() {return getParent();}
	public ArrayList<TreeNodeADT> children() {
		ArrayList<TreeNodeADT> theChildren = new ArrayList<TreeNodeADT>(); 
		theChildren.addAll(getChildren());
		return theChildren;
	}
    
    public boolean isLeaf(){
    	    	
    	return (children ==null || children.size() == 0);
    }
    
    public boolean isRoot(){
    	//answer whether this is a root node.
    	//it is considered a root node if it has no parent
    	return parent == null;
    }

    public int height(){
    	//return the height of this node in its subtree
    	//O(n) n=number of nodes in sub-tree
    	
    	if(isLeaf()) return 0;
        int h=0;
        Iterator<TreeNode> theChildren = getChildren().iterator();
        while(theChildren.hasNext()){
        	TreeNode node = theChildren.next();
        	h=Math.max(h, node.height());
        }
        return h+1;
   }
   
   public int depth(){
    	//return the depth of this node in the tree it participates in
    	//O(depth)
    	
    	if(isRoot()) return 0;
    	else return getParent().depth() + 1;
    }

    
	
	// end TreeNodeADT methods============================================================
	
	//===================================================================================
	//These are NON-ADT methods use to manipulate the nodes. These methods
	//should not be called directly by ADT clients
	public String getNodeLabel(){return label;}
	public void setNodeLabel(String aNodeLabelString){label = aNodeLabelString;}
	public void setNodeLabel(int aNodeLabelInt){label = ""+ aNodeLabelInt;}
	
    public void setParent(TreeNodeADT treeNodeADT) {parent = treeNodeADT;}
    public TreeNodeADT getParent() {return parent;}
    public ArrayList<TreeNode> getChildren(){return children;}

    public DataADT find(String aKeyString){
    	

        System.out.println("TreeNode::find(String)");

    	DataADT data = getData();
    	if(data.key().equals(aKeyString)) {
    		setSelection(true); //select the found node
    		return data;
    	}
    	
    	if(isLeaf()) return null;
    	
    	Iterator<TreeNode> theChildren = getChildren().iterator();
    	while(theChildren.hasNext()){
    		TreeNode child = theChildren.next();
    		data = child.find(aKeyString);
    		if(data != null) return data;
    	}
    	
    	return null;
    }
   
    public void remove(String aKeyString){
    //remove any nodes whose Data object's key matches aKeyString
    //when a node is removed from a general tree, the entire subtree
    //rooted at that node is removed
      
      if(key() != null && key().equals(aKeyString) && parent != null) {
      	((TreeNode) parent).removeChildNode(this);
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
    
	public void insertNode(TreeNode aNode){
		//Add aNode as a child of this node
    	addChildNode(aNode);
    }
    public void addChildNode(TreeNode aNode){
    	//Add aNode as a child of this node.
    	//If aNode's graphical location has been set then try and add it based on
    	//its graphical location relative to the location of the other children
    	//This is to maintain the correct relative order in which they were 
    	//graphically added with the tree editor.
    	
    	aNode.setParent(this);
    	
    	if(children.size() ==0) {
    		children.add(aNode);
    		return;
    	}
    	else if(aNode.getLocation()==null){
    		children.add(aNode);
    		return;
    	}
    	else if(aNode.getLocation().x < children.get(0).getLocation().x){
    		//new node is to the left of all existing children
    		children.add(0, aNode);
    		return;
    	}
    	else if(aNode.getLocation().x > children.get(children.size()-1).getLocation().x){
    		//new node is to the right of all existing children
    		children.add(aNode);
    		return;
    	}
    	else{
    	  //new node is somewhere between existing children
     	  boolean added = false;
   	      for(int i=0; i<children.size(); i++){
    		TreeNode child = children.get(i);
    		if(child.getLocation().x >= aNode.getLocation().x && !added ){
    			children.add(i, aNode);
    			return;
    		}
    	  }
    	}
    }
    
   public void removeChildNode(TreeNode aChildNode){
    	
    	System.out.println("TreeNode::removeChild()");
    	children.remove(aChildNode);
    	aChildNode.setParent(null);
    }
  
    
	public ArrayList<TreeNode> getNodes(){
		//return a list of all the nodes in the tree
		
		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
		ArrayList<TreeNode> theChildren = getChildren();
		if(!theChildren.isEmpty()) {
		   for(int i=0; i<theChildren.size(); i++){
		   	  TreeNode child = theChildren.get(i);
			  nodes.addAll(child.getNodes());
		   }
			
		}
		
		nodes.add(this);
		
		return nodes;
		
	}
    
	public ArrayList<DataADT> entries(){
		//return a list of all the nodes in the tree
		
		ArrayList<DataADT> entries = new ArrayList<DataADT>();
		ArrayList<TreeNode> theChildren = getChildren();
		if(!theChildren.isEmpty()) {
		   for(int i=0; i<theChildren.size(); i++){
		   	  TreeNode child = theChildren.get(i);
			  entries.addAll(child.entries());
		   }
			
		}
		
		entries.add(this.getData());
		
		return entries;
		
	}
	
	public void preOrder(StringBuffer nodesVisited){
    	//perform a pre-order traversal an label the nodes with their traversal order
    	//O(n) n=number of nodes in sub-tree
    	
    	//perform the visit action
    	setNodeLabel(traversalCount++);
    	nodesVisited.append(key() + " ");
    	
        Iterator<TreeNode> theChildren = getChildren().iterator();
        while(theChildren.hasNext()){
        	TreeNode node = theChildren.next();
        	node.preOrder(nodesVisited);
        }
   }

	public void postOrder(StringBuffer nodesVisited){
    	//perform a post-order traversal an label the nodes with their traversal order
    	//O(n) n=number of nodes in sub-tree
    	
    	
        Iterator<TreeNode> theChildren = getChildren().iterator();
        while(theChildren.hasNext()){
        	TreeNode node = theChildren.next();
        	node.postOrder(nodesVisited);
        }

    	//perform the visit action
    	setNodeLabel(traversalCount++);
    	nodesVisited.append(key() + " ");
   }

	public void inOrder(StringBuffer nodesVisited){
    	//perform a in-order traversal an label the nodes with their traversal order
    	//For an arbitrary tree consider an in-order traversal to process half the children, then
    	//the root, then the other half of the children
    	//This operation really only makes sense for a binary tree
    	
    	//Accumulate in the nodesVisited StringBuffer the order in which the nodes
    	//were visited
    	
    	ArrayList<TreeNode> theChildren = getChildren();
    	int numberOfChildren = theChildren.size();
    	boolean rootProcessed = false;
    	
    	for(int i=0; i<numberOfChildren; i++){
    		TreeNode node = theChildren.get(i);
    		if(i> numberOfChildren/2-1 && rootProcessed == false){
    			rootProcessed = true;
    			//perform visit action
    			setNodeLabel(traversalCount++);
    			nodesVisited.append(key() + " ");
    		}
    		node.inOrder(nodesVisited);
    	}
    	if(!rootProcessed) {
    		setNodeLabel(traversalCount++);
    		nodesVisited.append(key() + " ");
    	}
    }	
  
    //Graphical Manipulation and Editing methods ====================================
    public String toString(){
    	return "TreeNode:" + element.key();
    }

    public boolean isSelected() { return selected; }
    public void toggleSelected() { selected = !selected; }    
    public void setSelection(boolean selectIfTrue){selected = selectIfTrue;}
   
    public boolean getVisited() {return visited;}
    public void setVisited(boolean state) {visited = state;}
    
    
	public Point getLocation() { return location; }	
	public void setLocation(Point aPoint) { location = aPoint; }
	public void setLocation(int x, int y) { setLocation(new Point(x, y)); }

	public void setDeltaForLocation(Point newLocation, int animationSteps) { 
	     //used to relocation nodes so that they will animate their relocation
	     
	     targetLocation = newLocation;
	     deltaX = ((double)(newLocation.x - location.x))/animationSteps;
	     deltaY = ((double) (newLocation.y - location.y))/animationSteps;
	     numberOfSteps = animationSteps;
	}

    
    public boolean requiresAnimation(){
    	//answer true if the node has not reached its final destination
    	return numberOfSteps > 0;
    }
 
		
	public void moveNodeOneStep() {
		 
		 //move node one step deltaX and deltaY towards its target location
	     if(numberOfSteps > 0) {
	     	
	        int x = (int) (((double) location.x) + deltaX);
	        int y = (int) (((double) location.y) + deltaY);
	        
	        numberOfSteps--;
	        
	        if(numberOfSteps == 0) setLocation(targetLocation);
	        else setLocation(x,y);
	     }
	}
	



    //helper methods to do calculations
    private double distanceBetween(TreeNode n1, TreeNode n2) {
            return Math.sqrt ((double) ((n2.getLocation().x - n1.getLocation().x) *  (n2.getLocation().x - n1.getLocation().x) +
                           (n2.getLocation().y - n1.getLocation().y) * 
                           (n2.getLocation().y - n1.getLocation().y)));
    }

    private double distanceBetween(Point p1, Point p2) {
            return Math.sqrt ((double) ((p2.x - p1.x) *  (p2.x - p1.x) +
                           (p2.y - p1.y) * 
                           (p2.y - p1.y)));
           
    }



     // reposition nodes
    public void repositionNodeLocations(int originX, int originY, int width, int height) {
    //            ==============
    
    //reposition the nodes in the area provided by the bounding box
    
        final int levelHeight = TreeEditor.LEVEL_HEIGHT; //pixel height between levels in the tree

        Point newLocation = new Point(originX + width/2, originY + levelHeight);
	    if(TreeEditor.displayAnimation){
        	  setDeltaForLocation(newLocation, TreeEditor.AnimationSteps);
        }
        else{ setLocation(newLocation);}


        if(!isLeaf()){
        	
           ArrayList<TreeNode> theChildren = getChildren();
           int numberOfChildren = theChildren.size();
           
           int widthPerChild = width/numberOfChildren;
           
           for(int i=0; i<numberOfChildren; i++){
           	  theChildren.get(i).repositionNodeLocations(originX+i*widthPerChild, originY + levelHeight*2, widthPerChild, height-levelHeight*2);
           }       	
        }
        
        
 	}



	// Drawing Methods ===================================================================
	
	public Color getDrawingColor(){
		
		 if (isSelected()) return SELECTED_COLOR;
		 else if(isRoot()) return ROOT_COLOR;
         else return NORMAL_COLOR;
		
	}
	
	public void draw(Graphics2D aPen) {
         
         
         //draw connection to parent
         if(parent != null)
             aPen.drawLine(((TreeNode) parent).getLocation().x, ((TreeNode) parent).getLocation().y,
                      getLocation().x, getLocation().y);

         // Draw a filled circle around the center of the node
		 aPen.setColor(getDrawingColor());
		 	 
         aPen.fillOval(location.x - TreeNode.RADIUS, location.y - TreeNode.RADIUS, 
					   TreeNode.RADIUS * 2, TreeNode.RADIUS * 2);
         // Draw a black border around the circle
         aPen.setColor(Color.black);
         
         aPen.drawOval(location.x - TreeNode.RADIUS, location.y - TreeNode.RADIUS, 
					   TreeNode.RADIUS * 2, TreeNode.RADIUS * 2);
		 
		 //draw the label on the node
		 drawNodeLabel(aPen);
	}

	
     public void drawNodeLabel(Graphics aPen) {
         // Draw the node label
         Font oldFont = aPen.getFont(); //cache any font currently in use
         Color oldColor = aPen.getColor();
         
         aPen.setFont(labelFont);
         FontMetrics metrics = aPen.getFontMetrics();
         String label = key();
         int labelWidth = metrics.stringWidth(label);
         int stringHeightOffset = labelPointSize/4; //rough estimate
         
         aPen.setColor(Color.black);
	     aPen.drawString(label, location.x - labelWidth/2, location.y + stringHeightOffset);
	     
	     if(TreeEditor.displayDataValues && value() != null && value().length() > 0){
	     	aPen.drawString(value(), location.x+ RADIUS, location.y - RADIUS);
	     	
	     }

	     if(TreeEditor.displayNodeLabels && getNodeLabel() != null && getNodeLabel().length() > 0){
	     	Color currentPenColor = aPen.getColor();
	     	aPen.setColor(NODE_LABEL_COLOR);
	     	aPen.drawString(getNodeLabel(), location.x- RADIUS, location.y - RADIUS);
	     	aPen.setColor(currentPenColor);
	     	
	     }
	     
	     aPen.setFont(oldFont);
	     aPen.setColor(oldColor);
	     
	}

}