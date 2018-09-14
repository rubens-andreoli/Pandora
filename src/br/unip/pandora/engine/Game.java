package br.unip.pandora.engine;

import java.awt.Graphics2D;

public abstract class Game {
    
    protected String title;
    protected int width, height;

    public Game(String title, int width, int height) {
	this.title = title;
	this.width = width;
	this.height = height;
    }

    public Game(String title) {
	this(title, Display.DEFAULT_WIDTH, Display.DEFAULT_HEIGHT);
    }
 
    public abstract void tick(KeyHandler key, MouseHandler mouse);
    public abstract void draw(Graphics2D g);
    
}
