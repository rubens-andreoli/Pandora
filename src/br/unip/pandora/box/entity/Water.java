package br.unip.pandora.box.entity;

import java.awt.Color;

public class Water extends Entity {
  
    public static final int ID = 1;
    public static final Color COLOR = Color.BLUE; //because water is rendered with terrain
    
    public Water(){
	super(ID, 0, 0);
    }
    
    @Override
    public void update(Entity[][] map) {}
    
    @Override
    public void use(Creature c) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Color getColor() {
	return COLOR;
    }
   
}
