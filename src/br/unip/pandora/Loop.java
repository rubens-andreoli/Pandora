package br.unip.pandora;

import br.unip.pandora.world.Box;
import javax.swing.JFrame;

public class Loop implements Runnable {

    //thread
    private boolean running;
    public static final int UPS = 60;
//    private int FPS = 120;
    private double nsPerTick = 1000000000D/UPS;
    
    //parts
    private JFrame frame; //TODO: move frame to Renderer and rename to Display
    private Renderer renderer;
    private Box box;
    
    public Loop(Renderer renderer) {
	this.frame = new JFrame(Main.NAME);
	this.renderer = renderer;
	this.frame.setContentPane(renderer);
	this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.frame.setResizable(false);
	this.frame.pack();
	this.frame.setVisible(true);
	this.frame.requestFocus();
    }
    
    private void init(){
	box = new Box();
	frame.addMouseListener(box);
	frame.addKeyListener(box);
	running = true;
    }

    @Override
    public void run() {
	this.init();
	
	long lastTime = System.nanoTime();
	long now;
	
	int ticks = 0;
	int frames = 0;
	
	long lastTimer = System.currentTimeMillis();
	double delta = 0;
	
	boolean shouldRender = false;
	
	while(running){
	    now = System.nanoTime();
	    delta += (now-lastTime)/nsPerTick;
	    lastTime = now;

	    shouldRender = true; //change to false to limit to 1 render for update

	    while(delta >= 1){
		ticks++;
		box.tick();
		delta -= 1;
		shouldRender = true;
	    }
	    
//	    try {
//		Thread.sleep(2);
//	    } catch (InterruptedException ex) {
//		ex.printStackTrace();
//	    }
	    
	    if(shouldRender){
		frames++;
		box.draw(renderer.getG2D());
		renderer.drawToScreen();
	    }
	    
	    if(System.currentTimeMillis() - lastTimer>1000){
		lastTimer += 1000;
		frame.setTitle(Main.NAME+" [ticks/sec: "+ticks+", frames/sec: "+frames+"]");
		frames = 0;
		ticks = 0;
	    }
	}
    }

}
