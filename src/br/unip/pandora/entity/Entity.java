package br.unip.pandora.entity;

import java.awt.Graphics;

public abstract class Entity {
       
    protected int id, metadata;
    protected int x, y;
    
    public Entity(int id, int x, int y){
	this.id = id;
	this.x = x;
	this.y = y;
    }
    
    public int getId(){return id;}
    public int getMetadata(){return metadata;}
    
    public abstract void update(); 
    public abstract void draw(Graphics g);
}
