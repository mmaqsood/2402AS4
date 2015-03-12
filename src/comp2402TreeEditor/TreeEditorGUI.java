package comp2402TreeEditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//DISCLAIMER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//==========
//This code is designed for classroom illustration
//It may have intentional omissions that are
//for illustration or assignment purposes
//
//That being said: Please report any bugs to me so I can fix them
//...Lou Nel (ldnel@scs.carleton.ca)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


// This class represents the view of the tree editor
// It contains the menus and dispatching of menu events

public class TreeEditorGUI extends JFrame implements ActionListener, KeyListener {

	private TreeEditor		editor;
    private JScrollPane     scrollpane;
    
    private JButton         findButton = new JButton("Find");
    private JButton         insertButton = new JButton("Insert");
    private JButton         removeButton = new JButton("Remove");
    private JTextField      itemsTextField = new JTextField();
    
	private	JMenuBar		aMenuBar = new JMenuBar();
	private	JMenu			fileMenu = new JMenu("File");
    private JMenu           searchMenu = new JMenu("Search");
    private	JMenu			editMenu = new JMenu("Edit");
    private	JMenu			displayMenu = new JMenu("Display");
    private JMenu           traverseMenu = new JMenu("Traverse");

    //FILE MENU ITEMS
	private JMenuItem		newTreeItem = new JMenuItem("New Tree");    
	private JMenuItem		newBinaryTreeItem = new JMenuItem("...New Binary Tree");    
	private JMenuItem		newBinarySearchTreeItem = new JMenuItem("......New Binary Search Tree");    
	private JMenuItem		newBinaryHeapItem = new JMenuItem("......New Binary Heap");    
	private JMenuItem		newTrieItem = new JMenuItem("...New Trie");    
	private JMenuItem		newAVLTreeItem = new JMenuItem(".........New AVL Tree");    
	private JMenuItem		newRedBlackTreeItem = new JMenuItem(".........New Red-Black Tree");    
	
    
    //SEARCH MENU ITEMS

    //EDIT MENU ITEMS
    private JMenuItem       clearSelectionsItem = new JMenuItem("Clear Selections");
    private JMenuItem       deleteSelectedNodesItem = new JMenuItem("Delete Selected Nodes");
    private JMenuItem       repositionNodeLocationsItem = new JMenuItem("Reposition Node Locations");
    private JMenuItem       randomNodeLocationsItem = new JMenuItem("Random Node Locations");

    //DISPLAY MENU ITEMS
    private JRadioButtonMenuItem    displayNodeLabelsItem = new JRadioButtonMenuItem("node labels");
    private JRadioButtonMenuItem    displayAnimationItem = new JRadioButtonMenuItem("show animation");
    private JRadioButtonMenuItem    displayDataValuesItem = new JRadioButtonMenuItem("data values");
    
    //TRAVERSE MENU ITEMS
    private JMenuItem       inOrderItem = new JMenuItem("in-order");
    private JMenuItem       preOrderItem = new JMenuItem("pre-order");
    private JMenuItem       postOrderItem = new JMenuItem("post-order");


    public static boolean shiftPressed = false; //true when the shift key is being pressed
    public static boolean controlPressed = false; //true when the control key is being pressed
    public static boolean altPressed = false; //true when the alt key is being pressed

    //CONSTRUCTOR========================================================================
	
	public TreeEditorGUI (String title) {
		
		super(title);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);
		
		editor = new TreeEditor(this);
        scrollpane = new JScrollPane(editor);
		add(scrollpane);
		
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 5;
        constraints.gridheight = 1;
        constraints.weightx = 100; 
        constraints.weighty = 100; 
        constraints.fill = GridBagConstraints.BOTH;
        layout.setConstraints(scrollpane, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0; 
        constraints.weighty = 0; 
        layout.setConstraints(findButton, constraints);
        add(findButton);
        findButton.addActionListener(this);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0; 
        constraints.weighty = 0; 
        layout.setConstraints(removeButton, constraints);
        add(removeButton);
        removeButton.addActionListener(this);

        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0; 
        constraints.weighty = 0; 
        layout.setConstraints(insertButton, constraints);
        add(insertButton);
        insertButton.addActionListener(this);
		
        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 100; 
        constraints.weighty = 0;
        layout.setConstraints(itemsTextField, constraints);
        add(itemsTextField);
		
		initialize();
	    editor.displayGraphOnly();
	}

	private void initialize() {
		setJMenuBar(aMenuBar);
		
		//FILE MENU
		aMenuBar.add(fileMenu);
		fileMenu.add(newTreeItem);
		fileMenu.add(newTrieItem);
		fileMenu.add(newBinaryTreeItem);
		fileMenu.add(newBinaryHeapItem);
		fileMenu.add(newBinarySearchTreeItem);
		fileMenu.add(newAVLTreeItem);
		fileMenu.add(newRedBlackTreeItem);
		newTreeItem.addActionListener(this);
		newBinaryTreeItem.addActionListener(this);
		newBinarySearchTreeItem.addActionListener(this);
		newBinaryHeapItem.addActionListener(this);
		newTrieItem.addActionListener(this);
		newAVLTreeItem.addActionListener(this);
		newRedBlackTreeItem.addActionListener(this);

        
        //EDIT MENU
        editMenu.add(clearSelectionsItem);
        editMenu.add(new JSeparator());		
        editMenu.add(deleteSelectedNodesItem);
        editMenu.add(new JSeparator());		
        editMenu.add(repositionNodeLocationsItem);
        editMenu.add(randomNodeLocationsItem);
        
        deleteSelectedNodesItem.addActionListener(this);
        clearSelectionsItem.addActionListener(this);
        repositionNodeLocationsItem.addActionListener(this);
        randomNodeLocationsItem.addActionListener(this);
       
        aMenuBar.add(editMenu);
 
       
        //DISPLAY MENU
        
        ButtonGroup group = new ButtonGroup();
        group.add(displayNodeLabelsItem);
        group.add(displayDataValuesItem);
        
        displayMenu.add(displayNodeLabelsItem);
		displayNodeLabelsItem.addActionListener(this);
        displayMenu.add(displayDataValuesItem);
		displayDataValuesItem.addActionListener(this);
        displayMenu.add(new JSeparator());		
        displayMenu.add(displayAnimationItem);
		displayAnimationItem.addActionListener(this);
        aMenuBar.add(displayMenu);
        
         //TRAVERSE MENU
        traverseMenu.add(inOrderItem);
        traverseMenu.add(preOrderItem);
        traverseMenu.add(postOrderItem);
        
        inOrderItem.addActionListener(this);
        preOrderItem.addActionListener(this);
        postOrderItem.addActionListener(this);
       
        aMenuBar.add(traverseMenu);
 


 
        //needed for the scroll pane
		editor.setPreferredSize(new Dimension(editor.getWidth(), editor.getHeight() + 40 ));
        //setSize(editor.getWidth(), editor.getHeight() + 40 );
        pack();
                
        addKeyListener(this);

	}
	
	//KEYBOARD EVENT HANDLERS
	public void keyPressed(KeyEvent event) {        
        int keyCode = event.getKeyCode();
        
        if (keyCode == KeyEvent.VK_SHIFT)  shiftPressed = true;
        if (keyCode == KeyEvent.VK_CONTROL)controlPressed = true;
        if (keyCode == KeyEvent.VK_ALT)altPressed = true;		
	
	}
	public void keyReleased(KeyEvent event) {
    	int keyCode = event.getKeyCode();
    	 
        if (keyCode == KeyEvent.VK_SHIFT) shiftPressed = false;
        if (keyCode == KeyEvent.VK_CONTROL)controlPressed = false;
        if (keyCode == KeyEvent.VK_ALT) altPressed = false;
       		
	}
    // Key typed event handler
	public void keyTyped(KeyEvent event) {
	//	System.out.println("Key typed");
		int keyChar = event.getKeyChar();
        if (keyChar == KeyEvent.VK_DELETE) {
        	//System.out.println("Delete typed");
        	editor.deleteSelectedNodes();
        }
    }

	// MENU ITEM EVENT HANDLER
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == insertButton){
		    editor.insertItems(itemsTextField.getText().trim());
		    itemsTextField.setText("");
		}
		
		else if(e.getSource() == removeButton){
		    editor.removeItems(itemsTextField.getText().trim());
		    itemsTextField.setText("");
		}
		   
		else if(e.getSource() == findButton){
		    editor.findItems(itemsTextField.getText().trim());
		    itemsTextField.setText("");
		}
		   
		//FILE MENU ITEMS
		else if (e.getSource() == newTreeItem)
			editor.newTree();
		else if (e.getSource() == newBinaryTreeItem)
			editor.newBinaryTree();
		else if (e.getSource() == newBinarySearchTreeItem)
			editor.newBinarySearchTree();
		else if (e.getSource() == newBinaryHeapItem)
			editor.newBinaryHeap();
		else if (e.getSource() == newTrieItem)
			editor.newTrie();
		else if (e.getSource() == newAVLTreeItem)
			editor.newAVLTree();
		else if (e.getSource() == newRedBlackTreeItem)
			editor.newRedBlackTree();
		
		//DISPLAY MENU ITEMS
 	    else if (e.getSource() == displayNodeLabelsItem)
			editor.displayNodeLabels(displayNodeLabelsItem.isSelected());
	    else if (e.getSource() == displayAnimationItem)
			editor.displayAnimation(displayAnimationItem.isSelected());
	    else if (e.getSource() == displayDataValuesItem)
			{editor.displayDataValues(displayDataValuesItem.isSelected());}

		//TRAVERSE MENU ITEMS
 	    else if (e.getSource() == inOrderItem){
 	        StringBuffer nodesVisited = new StringBuffer();
			editor.inOrderTraversal(nodesVisited);
			itemsTextField.setText(nodesVisited.toString());
		}
	    else if (e.getSource() == preOrderItem){
 	        StringBuffer nodesVisited = new StringBuffer();	   
			editor.preOrderTraversal(nodesVisited);
			itemsTextField.setText(nodesVisited.toString());
	    }
	    else if (e.getSource() == postOrderItem){
 	        StringBuffer nodesVisited = new StringBuffer();	   
			editor.postOrderTraversal(nodesVisited);
			itemsTextField.setText(nodesVisited.toString());
		} 
            
        //EDIT MENU ITEMS         
        else if (e.getSource() == clearSelectionsItem)
            editor.clearSelections();
        else if (e.getSource() == deleteSelectedNodesItem)
            editor.deleteSelectedNodes();
        else if (e.getSource() == randomNodeLocationsItem)
            editor.randomNodeLocations();
        else if (e.getSource() == repositionNodeLocationsItem)
            editor.repositionNodeLocations();
            
        editor.update();

	}
	
	public void forceUpdate(){
		update(getGraphics()); //to force GUI update 
	}

	public void runGUI() {
	//                 =====
                      
		// Add the usual window listener (for closing ability)
		this.addWindowListener(
			new WindowAdapter() {
 				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);

        //show the frame
		this.setVisible(true); //this will return and leave running
	}   
}