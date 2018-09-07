package br.unip.pandora;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public abstract class Game implements MouseListener, KeyListener{
    
    public final int ups;
    public final String title;
    public final int width, height, scale;

    public Game(String title, int width, int height, int scale, int ups) {
	this.title = title;
	this.width = width;
	this.height = height;
	this.scale = scale;
	this.ups = ups;
    }
    
    public Game(String title, int width, int height) {
	this(title, width, height, 1, 60);
    }
    
    public Game(String title) {
	this(title, 320, 240, 1, 60);
    }
    
    public abstract void tick();
    public abstract void draw(Graphics2D g);

}
