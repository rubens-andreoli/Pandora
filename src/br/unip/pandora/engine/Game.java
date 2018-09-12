package br.unip.pandora.engine;

import java.awt.Graphics2D;

public abstract class Game {
    
    protected String title;
    protected int width, height;
    protected int tickRate;

    public Game(String title, int width, int height, int tickRate) {
	this.title = title;
	this.width = width;
	this.height = height;
	this.tickRate = tickRate;
    }
    
    public Game(String title, int width, int height){
	this(title, width, height, Engine.DEFAULT_TICK_RATE);
    }
    
    public Game(String title) {
	this(title, Display.DEFAULT_WIDTH, Display.DEFAULT_HEIGHT, Engine.DEFAULT_TICK_RATE);
    }
 
    public abstract void tick(KeyHandler key, MouseHandler mouse);
    public abstract void draw(Graphics2D g);
    
}
