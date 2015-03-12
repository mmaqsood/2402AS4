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


// This class is the launch point
// It contains the menus and dispatching of menu events

public class TreeEditorMain{
	public static void main(String args[]) {
	//                 =====
		
		//Create the GUI and run it
		TreeEditorGUI frame =  new TreeEditorGUI("Tree Editor");
		
		System.out.println("Starting Tree Editor GUI");
		frame.runGUI(); //this method will not block; it will return and keep GUI running.
		System.out.println("Returning from GUI Launch");
                      

	}   
}