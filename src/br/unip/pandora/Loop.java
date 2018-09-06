package br.unip.pandora;

import br.unip.pandora.world.Box;

public class Loop implements Runnable {

    //thread
    private boolean running;
    public static final int UPS = 60;
    private double nsPerTick = 1000000000D/UPS;
    
    //parts
    private Display display;
    private Box box;
    
    public Loop(Display display) {
	this.display = display;
    }
    
    private void init(){
	box = new Box(display.getWidth(), display.getHeight());
	display.addMouseListener(box);
	display.addKeyListener(box);
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
	double deltaUps = 0;
	
	boolean shouldRender = false;
	
	while(running){
	    now = System.nanoTime();
	    deltaUps += (now-lastTime)/nsPerTick;
	    lastTime = now;

	    shouldRender = true; //change to false to limit to 1 render for update

	    while(deltaUps >= 1){
		ticks++;
		box.tick();
		deltaUps -= 1;
		shouldRender = true;
	    }
	    
//	    try {
//		Thread.sleep(2);
//	    } catch (InterruptedException ex) {
//		ex.printStackTrace();
//	    }
	    
	    if(shouldRender){
		frames++;
		box.draw(display.getGraphics());
		display.show();
	    }
	    
	    if(System.currentTimeMillis() - lastTimer>1000){
		lastTimer += 1000;
		display.setTitle(Laucher.NAME+" [ticks/sec: "+ticks+", frames/sec: "+frames+"]");
		frames = 0;
		ticks = 0;
	    }
	}
    }

}
