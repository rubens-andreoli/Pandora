package br.unip.pandora.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler {
     
    public static final int NUM_KEYS = 256;
    
    private KeyListener kl;
    private boolean[] keyState;
    private boolean[] keyLast;
    
    public KeyHandler() {
	keyState = new boolean[NUM_KEYS];
	keyLast = new boolean[NUM_KEYS];
	
	kl = new KeyListener(){
	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() < NUM_KEYS) keyState[e.getKeyCode()] = true;
	    }
	    @Override
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < NUM_KEYS) keyState[e.getKeyCode()] = false;	
	    }
	    public @Override void keyTyped(KeyEvent e) {}
	};
    }
    
    public void tick(){
	System.arraycopy(keyState, 0, keyLast, 0, NUM_KEYS);
    }

    public boolean isReleased(int keyCode) {
	return !keyState[keyCode] && keyLast[keyCode];
    }

    public boolean isPressed(int keyCode) {
	return keyState[keyCode];
    }
 
    public boolean isTyped(int keyCode) {
	return keyState[keyCode] && !keyLast[keyCode];
    }

    public KeyListener getKeyListener() {return kl;}
        
}
