package br.unip.pandora.entity;

import java.awt.Graphics;

public abstract class Entity {
    
    public static final int WATER = 1;
    public static final int FOOD = 2;
    public static final int SOUL = 3;
    
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
