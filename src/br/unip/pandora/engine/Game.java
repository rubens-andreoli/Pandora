package br.unip.pandora.engine;

import static br.unip.pandora.engine.Display.DEFAULT_HEIGHT;
import static br.unip.pandora.engine.Display.DEFAULT_SCALE;
import static br.unip.pandora.engine.Display.DEFAULT_WIDTH;
import static br.unip.pandora.engine.Engine.DEFAULT_TICK_RATE;
import java.awt.Graphics2D;

public abstract class Game {
    
    protected String title;
    protected int width, height, scale;
    protected int tickRate;
    protected KeyHandler key;
    protected MouseHandler mouse;

    public Game(String title, int width, int height, int scale, int tickRate) {
	this.title = title;
	this.width = width;
	this.height = height;
	this.scale = scale;
	this.tickRate = tickRate;
    }
    
    public Game(String title, int width, int height){
	this(title, width, height, DEFAULT_SCALE, DEFAULT_TICK_RATE);
    }
    
    public Game(String title) {
	this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SCALE, DEFAULT_TICK_RATE);
    }
 
    public abstract void tick();
    public abstract void draw(Graphics2D g);
   
    public void setKeyHandler(KeyHandler k){
	key = k;
    }
    
    public void setMouseHandler(MouseHandler m){
	mouse = m;
    }
    
}
