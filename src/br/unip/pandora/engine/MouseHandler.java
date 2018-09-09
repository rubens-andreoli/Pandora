package br.unip.pandora.engine;

import static br.unip.pandora.engine.Display.DEFAULT_SCALE;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler {
    
    private int scale;
    private MouseListener ml;
    private MouseMotionListener mml;
    
    private boolean newState;
    private boolean isDragging;
    private boolean isRealeased;
    private int button, modifier;
    private int mouseX, mouseY;
    private long time;
   
    //<editor-fold defaultstate="collapsed" desc="CLICK">
    public class Click	{
	public final int button;
	public final int modifier;
	public final boolean isRelease;
	public final int x, y;
	public final long time;

	private Click(int button, int modifier, boolean isRelease, int x, int y, long time) {
	    this.button = button;
	    this.modifier = modifier;
	    this.isRelease = isRelease;
	    this.x = x/scale;
	    this.y = y/scale;
	    this.time = time;
	}

	public boolean isOver(Rectangle r){
	    return r.contains(x, y);
	}
    } 
    //</editor-fold>
    
    public MouseHandler(int scale){
	this.scale = Math.max(1, scale);
	
	ml = new MouseListener(){
	    @Override
	    public void mousePressed(MouseEvent e) {
		if(!newState)setState(e, false);
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		isDragging = false;
		if(!newState)setState(e, true);
	    }

	    public @Override void mouseClicked(MouseEvent e) {}
	    public @Override void mouseEntered(MouseEvent e) {}
	    public @Override void mouseExited(MouseEvent e) {}
	};
	
	mml = new MouseMotionListener(){
	    @Override
	    public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	    }

	    @Override
	    public void mouseDragged(MouseEvent e) {
		isDragging = true;
		mouseX = e.getX();
		mouseY = e.getY();
	    }
	};
	
    }
    
    public MouseHandler(){
	this(DEFAULT_SCALE);
    }
    
    private void setState(MouseEvent e, boolean isReleased){
	newState = true;
	button = e.getButton();
	modifier = e.getModifiersEx();
	isRealeased = isReleased;
	time = e.getWhen();
    }
    
    public boolean hasClick(){
	return newState;
    }
    
    public Click getClick(){
	newState = false;
	return new Click(button, modifier, isRealeased, mouseX, mouseY, time);
    }
    
    public boolean isOver(Rectangle r){
	return r.contains(mouseX/scale, mouseY/scale);
    }

    public int getX() {return mouseX/scale;}
    public int getY() {return mouseY/scale;}
    public boolean isDragging() {return isDragging;}
    public MouseListener getMouseListener() {return ml;}
    public MouseMotionListener getMouseMotionListener() {return mml;}
}
