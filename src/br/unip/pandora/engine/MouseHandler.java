package br.unip.pandora.engine;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseHandler {
    
    private MouseListener ml;
    private MouseMotionListener mml;
    private MouseWheelListener mwl;
    
    private boolean click, released;
    private int button, modifier;
    private int mouseX, mouseY;
    private int wheel;

    public MouseHandler(int scale){
	ml = new MouseListener(){
	    @Override
	    public void mousePressed(MouseEvent e) {
		click = true;
		released = false;
		button = e.getButton();
		modifier = e.getModifiers();
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		released = true;
		button = e.getButton();
		modifier = e.getModifiers();
	    }

	    public @Override void mouseClicked(MouseEvent e) {}
	    public @Override void mouseEntered(MouseEvent e) {}
	    public @Override void mouseExited(MouseEvent e) {}
	};
	
	mml = new MouseMotionListener(){
	    @Override
	    public void mouseMoved(MouseEvent e) {
		mouseX = e.getX()/scale;
		mouseY = e.getY()/scale;
	    }

	    @Override
	    public void mouseDragged(MouseEvent e) {
		mouseX = e.getX()/scale;
		mouseY = e.getY()/scale;
	    }
	};
	
	mwl = new MouseWheelListener(){
	    @Override
	    public void mouseWheelMoved(MouseWheelEvent e) {
		wheel += e.getWheelRotation();
	    }
	};
	
    }
    
    public MouseHandler(){
	this(Display.DEFAULT_SCALE);
    }
    
    public void tick(){
	if(released) button = -1;
	click = false;
	wheel = 0;
    }

    public boolean isPressed(int button){
	return (this.button == button) && !released;
    }
    
    public boolean isClicked(int button){
	return (this.button == button) && click;
    }
    
    public boolean isReleased(int button){
	return (this.button == button) && released;
    }
    
    public boolean isOver(Rectangle r){
	return r.contains(mouseX, mouseY);
    }

    public int getX() {return mouseX;}
    public int getY() {return mouseY;}
    public int getModifier() {return modifier;}
    public int getWheel(){return wheel;}

    public MouseListener getMouseListener() {return ml;}
    public MouseMotionListener getMouseMotionListener() {return mml;}
    public MouseWheelListener getMouseWheelListener() {return mwl;}
}
