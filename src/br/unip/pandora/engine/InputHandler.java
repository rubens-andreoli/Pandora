package br.unip.pandora.engine;

import static br.unip.pandora.engine.Display.DEFAULT_SCALE;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class InputHandler{
    
    private KeyListener kl = new KeyListener(){
	//<editor-fold defaultstate="collapsed" desc="KEY-LISTENER">
	@Override
	public void keyPressed(KeyEvent e) {
	    if(e.getKeyCode() < 0 || e.getKeyCode() >= NUM_KEYS) return;
	    keyState[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	    if(e.getKeyCode() < 0 || e.getKeyCode() >= NUM_KEYS) return;
	    keyState[e.getKeyCode()] = false;
	}

	public @Override void keyTyped(KeyEvent e) {}
	//</editor-fold>
    };
    private MouseListener ml = new MouseListener(){
	//<editor-fold defaultstate="collapsed" desc="MOUSE-LISTENER">
	@Override
	public void mousePressed(MouseEvent e) {
	    try {
		clicks.offer(new Click(e.getButton(), e.getModifiersEx(), false, e.getX(), e.getY()), 500, TimeUnit.MILLISECONDS);
	    } catch (InterruptedException ex) {} //if interrupted just don't add click
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    isDragging = false;
	    try {
		clicks.offer(new Click(e.getButton(), e.getModifiersEx(), true, e.getX(), e.getY()), 500, TimeUnit.MILLISECONDS);
	    } catch (InterruptedException ex) {} //if interrupted just don't add click
	}
	
	public @Override void mouseClicked(MouseEvent e) {}
	public @Override void mouseEntered(MouseEvent e) {}
	public @Override void mouseExited(MouseEvent e) {}
	//</editor-fold>
    };
    private MouseMotionListener mml = new MouseMotionListener(){
	//<editor-fold defaultstate="collapsed" desc="MOUSE-MOTION-LISTENER">
	@Override
	public void mouseMoved(MouseEvent e) {
	    mouseX = e.getX()/scale;
	    mouseY = e.getY()/scale;
	}

	@Override
	public void mouseDragged(MouseEvent e) { //mouseMoved don't register if dragged
	    isDragging = true;
	    mouseX = e.getX()/scale;
	    mouseY = e.getY()/scale;
	}
	//</editor-fold>
    };
    
    private int scale;
    
    //keyboard
    public static final int NUM_KEYS = 256;
    private boolean[] keyState;
    private boolean[] lastKeyState;
    
    //mouse
    public static final int NUM_BUTTONS = 5;
    public static final int CLICK_QUEUE_SIZE = 4;
    private int mouseX;
    private int mouseY;
    private boolean isDragging;
    private BlockingQueue<Click> clicks; 
    
    //<editor-fold defaultstate="collapsed" desc="CLICK">
    public class Click	{
	public final int button;
	public final int modifier;
	public final boolean isRelease;
	public final int x, y;
	public final long time;

	private Click(int button, int modifier, boolean isRelease, int x, int y) {
	    this.button = button;
	    this.modifier = modifier;
	    this.isRelease = isRelease;
	    this.x = x/scale;
	    this.y = y/scale;
	    time = System.currentTimeMillis();
	}
	
	public boolean isOver(Rectangle r){
	    return r.contains(x, y);
	}
    }
    //</editor-fold>
    
    public InputHandler(int scale) {
	this.scale = Math.max(1, scale);
	keyState = new boolean[NUM_KEYS];
	lastKeyState = new boolean[NUM_KEYS];
	
	clicks = new ArrayBlockingQueue(CLICK_QUEUE_SIZE);
    }
    
    public InputHandler(){
	this(DEFAULT_SCALE);
    }
    
    public void tick(){
	System.arraycopy(keyState, 0, lastKeyState, 0, NUM_KEYS);
	
	if(!clicks.isEmpty()) 
	    if(System.currentTimeMillis()-clicks.peek().time > 1000) clicks.remove(); //TODO: if click too old remove?
    }
    
    public boolean isTyped(int keyCode) {
	return keyState[keyCode] && !lastKeyState[keyCode];
    }

    public boolean isPressed(int keyCode) {
	return keyState[keyCode];
    }
    
    public boolean hasClick(){
	return !clicks.isEmpty();
    }
    
    public Click getClick(){
	return clicks.poll();
    }

    public int getMouseX() {return mouseX;}
    public int getMouseY() {return mouseY;}
    public boolean isDragging() {return isDragging;}
    public KeyListener getKeyListener() {return kl;}
    public MouseListener getMouseListener() {return ml;}
    public MouseMotionListener getMouseMotionListener() {return mml;}

}
