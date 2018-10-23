package br.unip.pandora.box.entity;

import java.awt.Color;

public abstract class Entity {
    
    public final byte id;
    protected int x, y;
    protected Color color;
    
    public Entity(byte id, int x, int y, Color color){
	this.id = id;
	this.x = x;
	this.y = y;
	this.color = color;
    }
    
    public int getX(){return x;}
    public int getY(){return y;}
    public Color getColor(){return color;}
  
}
