package comp2402TreeEditor;

import java.util.Iterator;
import java.util.Random;
import java.math.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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

// This class represents a panel on which a graph is edited and displayed
// It handles mouse events for editing


public class TreeEditor extends JPanel implements MouseListener, ActionListener, MouseMotionListener{

	// Keep the model (i.e. the graph)
    private Tree tree;
    public static boolean displayNodeLabels =false; 
    public static boolean displayDataValues =false; 
    
    public static boolean useEdgeWeights =false; //use edge weights instead of graphica length with true
    
    
    final public static BasicStroke stroke = new BasicStroke(1.0f);
    
    //use this stroke for drawing wider selected edges
    final static BasicStroke wideStroke = new BasicStroke(4.0f);
    
    final static float dash1[] = {10.0f};
    final static BasicStroke dashed = new BasicStroke(1.0f,
                                                      BasicStroke.CAP_BUTT,
                                                      BasicStroke.JOIN_MITER,
                                                      10.0f, dash1, 0.0f);
    
    //timer and step size for animation.
    public static int LEVEL_HEIGHT = 40; //height in pixels between levels of trees
    
    public static boolean displayAnimation =false; 
    private Timer aTimer = new Timer(100, this); //can used for animation 
    public static int AnimationSteps =20; //number of steps to animate a relocation of a node.
    
    
    Random rand = new Random(); //used when random numbers are required

	// Keep the frame for use with key events
    private TreeEditorGUI	view;

	// Instance variables for node movement mouse actions
	private Point				dragStartLocation;
	private Rectangle           dragBox;
	private TreeNode		    dragNode;
        


	
	public TreeEditor(TreeEditorGUI aView) {
        view = aView;
		BufferedReader aFile;
		tree = new Tree();
	    tree.setOwner(this);


		setSize(600, 500);
		setBackground(Color.white);
		addEventHandlers();
		dragStartLocation = null;
		dragNode = null;
        aTimer.stop();
                
	}
        public Tree getTree() {return tree;}

	public void addEventHandlers() {
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public void removeEventHandlers() {
		removeMouseListener(this);
		removeMouseMotionListener(this);
	}
	
	public Point getDefaultRootLocation(){
		return new Point(getWidth()/2, LEVEL_HEIGHT);
	}
	
	
	public void forceUpdate(){
		if(displayAnimation ==true )
		   view.forceUpdate();
	}

	// Unused event handlers
	public void mouseEntered(MouseEvent event) {}
	public void mouseExited(MouseEvent event) {}
	public void mouseMoved(MouseEvent event) {}

	 // Mouse click event handler
	public void mouseClicked(MouseEvent event) {
	
	
	    // If this was a double-click, then add a node at the mouse location 
	    // or toggle the selection state of targeted node or edge
        if ((event.getClickCount() == 2)&& (!event.isPopupTrigger())) {
            TreeNode aNode = tree.nodeAt(event.getPoint());
            if (aNode == null) {
                 if(tree.isEmpty()) tree.createNewRoot(event.getPoint());
			}
            else
                aNode.toggleSelected();

            // We have changed the model, so now we update
            update();
        }
	}

	// Mouse press event handler
	public void mousePressed(MouseEvent event) {
		// First check to see if we are about to drag a node
        TreeNode     aNode = tree.nodeAt(event.getPoint());
        dragStartLocation = event.getPoint();
        
        if (aNode != null) dragNode = aNode; 
	    
	}

	// Mouse drag event handler
    public void mouseDragged(MouseEvent event) {
        Graphics2D pen = (Graphics2D) getGraphics();
        if (dragNode != null) {
            if (dragNode.isSelected()) 

            {
                Point nodeLocation = dragNode.getLocation();
                Point mouseLocation = event.getPoint();
                int dx = (int) mouseLocation.getX() - (int) nodeLocation.getX();
                int dy = (int) mouseLocation.getY() - (int ) nodeLocation.getY();
                Iterator<TreeNode> theNodes = tree.getNodes().iterator();
                
                //erase the graph drawing
                pen.setColor(getBackground());
                tree.draw(pen);
                
                TreeNode currentNode;
                  while (theNodes.hasNext()) {
                     currentNode = theNodes.next();
                     if(currentNode.isSelected()) {
                          currentNode.getLocation().translate(dx,dy);
                        }
                    }
                
               }
            else {
                 pen.setColor(getBackground());
                 pen.drawLine(dragNode.getLocation().x, dragNode.getLocation().y,
                          dragStartLocation.x, dragStartLocation.y);

                 dragStartLocation = event.getPoint();
                 pen.setColor(Color.black);
                 pen.drawLine(dragNode.getLocation().x, dragNode.getLocation().y,
                 dragStartLocation.x, dragStartLocation.y);

                 }
        }
        else{
        	if(dragBox != null){
               //erase old drag box
        	   pen.setColor(getBackground());
          	   pen.drawRect(dragBox.getLocation().x, dragBox.getLocation().y, (int) dragBox.getWidth(), (int) dragBox.getHeight());
        	}
        	int boxWidth = Math.abs(dragStartLocation.x - event.getPoint().x);
        	int boxHeight = Math.abs(dragStartLocation.y - event.getPoint().y);
        	int boxX = Math.min(dragStartLocation.x, event.getPoint().x);
        	int boxY = Math.min(dragStartLocation.y, event.getPoint().y);
        	
        	dragBox = new Rectangle(boxX, boxY, boxWidth, boxHeight);
        }
        
        // We have changed the model, so now update
        update();
    }

	// Mouse release event handler (i.e. stop dragging process)
    public void mouseReleased(MouseEvent event) {
    	
         if (event.isPopupTrigger()){
         	//we right clicked to run the right click message dialog
         	
         	TreeNode aNode = tree.nodeAt(event.getPoint() );

            if(aNode != null && tree.allowsGraphicalDeletion()){
            
                //We right clicked on a node
                
                String inputString = JOptionPane.showInputDialog(this, "Please enter node key [,value]"); 

		        if(inputString != null || inputString.length()>0) {
		        
		           String key = aNode.key();
		           String value = aNode.value();
		           
		           String items[] = inputString.split(",");
		           if(items.length == 1){
		             key = items[0];
		             value = null;
	               }
	               if(items.length == 2){
		              key = items[0];
		              value = items[1];
	               }
                
                
                aNode.setData(new Data(key.trim(), value.trim()));
                //System.out.println("User input is " + inputString); 
                }
            }
            else{
            	//We right clicked in empty space so ignore this
            
            }
         }
         
         else{
       
            // Check to see if we have let go on a node
           TreeNode     aNode = tree.nodeAt(event.getPoint());
           
           if ((aNode == null) && (dragNode != null) ){
               //we dragged from a node to empty space so make a child for the  source node
               tree.createChildForNode(dragNode, event.getPoint());
               
           }               
           else if(aNode == null && dragBox == null){
           
               tree.clearSelections();
           }
           else if(dragBox != null){
           	  if(TreeEditorGUI.shiftPressed)
           	     tree.addNotesInAreaToSelection(dragBox);
           	  else
                 tree.selectNodesInArea(dragBox);
           	
           }

           // Refresh the canvas either way
           dragNode = null;
        
        }
        
        dragBox = null;
        update();
	}
	
	public void insertItems(String itemsList){
		//Insert items according to the rules of the particular kind of tree
		//System.out.println("TreeEditor::insertItems: " + itemsList);
		
		//Cast to illustrate that only ADT methods are being used
		TreeADT treeADT = (TreeADT) tree;
		
		String items[] = itemsList.split(" ");
		for(int i=0; i<items.length; i++){
			String itemString = items[i].trim();
			if(itemString.length() > 0)
			    treeADT.insert(itemString);
		}
       
        tree.repositionNodeLocations(getWidth(),getHeight());
		
		update();
	}

	public void removeItems(String itemsList){
		//remove items according to the rules of the particular kind of tree
		//System.out.println("TreeEditor::removeItems: " + itemsList);

		//Cast to illustrate that only ADT methods are being used
		TreeADT treeADT = (TreeADT) tree;

		String items[] = itemsList.split(" ");
		for(int i=0; i<items.length; i++){
			String itemString = items[i].trim();
			if(itemString.length() > 0)
			    treeADT.remove(itemString);
		}

       tree.repositionNodeLocations(getWidth(),getHeight());
		
	   update();
	}
	
	public void findItems(String itemsList){
		//remove items according to the rules of the particular kind of tree
		//System.out.println("TreeEditor::removeItems: " + itemsList);
		
		//Cast to illustrate that only ADT methods are being used
		TreeADT treeADT = (TreeADT) tree;

		String items[] = itemsList.split(" ");
		for(int i=0; i<items.length; i++){
			String itemString = items[i].trim();
			if(itemString.length() > 0)
			    treeADT.find(itemString);
		}
		
		update();
	}

    
    public void inOrderTraversal(StringBuffer nodesVisited){
    	//perform an in-order traversal of the tree
    	tree.inOrderTraversal(nodesVisited);
    }

    public void postOrderTraversal(StringBuffer nodesVisited){
    	//perform an post-order traversal of the tree
    	tree.postOrderTraversal(nodesVisited);
    }

    public void preOrderTraversal(StringBuffer nodesVisited){
    	//perform an pre-order traversal of the tree
   	    tree.preOrderTraversal(nodesVisited);
    }
	
	public void deleteSelectedNodes() {

       if(!tree.allowsGraphicalDeletion()) {
            	
            JOptionPane.showMessageDialog(this, 
            "Please use ADT remove to delete nodes from " + tree.getClass().getName(), 
            "Not allowed for this kind of tree", 
            JOptionPane.ERROR_MESSAGE);
            
            return;
       }
            
       //remove the selected nodes
       Iterator<TreeNode> highlightedNodes = tree.selectedNodes().iterator();
         while (highlightedNodes.hasNext())
             tree.deleteNode(highlightedNodes.next());
       update();
        
    }
    

    public void displayNodeLabels(boolean displayIfTrue) {
       displayNodeLabels = displayIfTrue;
       update();             
    }
    
    public void displayDataValues(boolean displayIfTrue){
       displayDataValues = displayIfTrue;
       update();
    }

    public boolean requiresAnimation(){
    	//answer whether animation is required to reposition the nodes.
    	
    	if(!displayAnimation) return false;
        
        Iterator<TreeNode> theNodes = tree.getNodes().iterator();
            while (theNodes.hasNext()){
           
                if(theNodes.next().requiresAnimation()) return true;
             } 
                 
        return false;
   }
    
    public void displayAnimation(boolean displayIfTrue) {
    	
       
       displayAnimation = displayIfTrue; //display forced updates
       //if(displayAnimation) startAnimation();
       //else stopAnimation();
       update();             
    }


	// The update method
	public void update() {
		
		removeEventHandlers();
		repaint();
		addEventHandlers();
		view.requestFocus();
 	}

	// This is the method that is responsible for displaying the graph
	
	
    public void paintComponent(Graphics aPen) {
		super.paintComponent(aPen);
		
		//switch to Graphics2D pen so we can control stroke widths better etc.
	    Graphics2D aPen2D = (Graphics2D) aPen;
        aPen2D.setStroke(stroke); 
        
        if(requiresAnimation() ){
        	if(!aTimer.isRunning()) startAnimation();
        }
        else stopAnimation();
        
       
        tree.draw(aPen2D);
		if (dragNode != null){
		 
		     if (!dragNode.isSelected())
                     aPen.drawLine(dragNode.getLocation().x, dragNode.getLocation().y,
                          dragStartLocation.x, dragStartLocation.y);
        }
          
        if (dragBox != null){
          	 aPen.drawRect(dragBox.getLocation().x, dragBox.getLocation().y, (int) dragBox.getWidth(), (int) dragBox.getHeight());
        }
                          
      


    }

    public void clearSelections() {
       tree.clearSelections();
       update();             
    }
    
    public void randomNodeLocations() {
       
       tree.randomNodeLocations(getWidth(),getHeight());
                  
    }

    public void repositionNodeLocations() {
       
       tree.repositionNodeLocations(getWidth(),getHeight());
                    
    }


   


    public void displayGraphOnly() {
       aTimer.stop();
       update();             
    }
    
    
   
    public void startAnimation() {
       //System.out.println("TreeEditor::startAnimation()");
       if(!displayAnimation) return;	
       if(!aTimer.isRunning()) aTimer.start();
    }
  
    public void stopAnimation() {
       //System.out.println("TreeEditor::stopAnimation()");
       aTimer.stop();
    } 
    
    private double distanceBetween(Point p1, Point p2) {
            return Math.sqrt ((double) ((p2.x - p1.x) *  (p2.x - p1.x) +
                           (p2.y - p1.y) * 
                           (p2.y - p1.y)));
           
    }



   // This is the Timer event handler 
   public void actionPerformed(ActionEvent e) {
    
       //used to do animations if desired
         //System.out.println("TreeEditor::actionPerformed");
         tree.doAnimationStep();
         update(); 
        } 


	// create a new empty general tree
	public void newTree() {
	    tree = new Tree();
	    tree.setOwner(this);
		update(); 
	}
	
    // create a new empty binary tree
	public void newBinaryTree() {
	    tree = new BTree();
	    tree.setOwner(this);
		update(); 
	}

   // create a new empty binary search tree
	public void newBinarySearchTree() {
	    tree = new BSTree();
	    tree.setOwner(this);
		update(); 
	}
	
	// create a new empty binary heap
		public void newBinaryHeap() {
		    tree = new BinaryHeap();
		    tree.setOwner(this);
			update(); 
		}


   // create a new empty Trie
	public void newTrie() {
	    tree = new Trie();
	    tree.setOwner(this);
		update(); 
	}

   // create a new empty AVL Tree
	public void newAVLTree() {
	    tree = new AVLTree();
	    tree.setOwner(this);
		update(); 
	}
	   // create a new empty Red-Black Tree
		public void newRedBlackTree() {
		    tree = new RedBlackTree();
		    tree.setOwner(this);
			update(); 
		}

}

