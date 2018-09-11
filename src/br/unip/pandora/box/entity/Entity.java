package br.unip.pandora.box.entity;

import java.awt.Color;

public abstract class Entity {
       
    protected int id, metadata;
    protected int x, y;
    
    public Entity(int id, int x, int y){
	this.id = id;
	this.x = x;
	this.y = y;
    }
    
    public int getId(){return id;}
    public synchronized int getMetadata(){return metadata;}
    public synchronized void setMetadata(int metadata){this.metadata = metadata;}
    
    public abstract void update(Entity[][] map); 
    public abstract Color getColor();
    public abstract void use(Creature c);
  
}
