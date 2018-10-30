package br.unip.pandora.box.entity;

import java.awt.Color;

public class Water extends Entity {
  
    public static final byte ID = 1;
    public static final Color COLOR = Color.BLUE; //needed to draw with terrain
    
    public Water(){
	super(ID, 0, 0, COLOR);
    }
   
}
