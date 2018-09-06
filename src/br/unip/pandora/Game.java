package br.unip.pandora;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public abstract class Game implements MouseListener, KeyListener{
    
    protected final String title;
    protected final int width, height;

    public Game(String title, int width, int height) {
	this.title = title;
	this.width = width;
	this.height = height;
    }
    
    public abstract void tick();
    public abstract void draw(Graphics2D g);

}
