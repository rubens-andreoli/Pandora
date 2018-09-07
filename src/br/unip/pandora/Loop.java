package br.unip.pandora;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Loop {

    //loop
    private final int ups;
    private final double nsPerTick;
    private boolean running;
    
    //parts
    private Thread thread;
    private final Display display;
    private final Game game;
    
    public Loop(final Game game, final Display display) {
	this.display = display;
	this.game = game;
	ups = game.ups;
	nsPerTick = 1000000000D/ups;
    }
     
    public void start(){
	if(running) return;
	
	display.addWindowListener(new WindowAdapter(){
	    @Override
	    public void windowClosing(WindowEvent e) {
		stop();
	    }    
	});
	display.addMouseListener(game);
	display.addKeyListener(game);
	
	running = true;
	thread = new Thread(() -> {
	    loop();
	});
	thread.setName(game.title);
	thread.start();
	
	display.setVisible(true);
    }
    
    public void stop(){
	if(!running) return;
	running = false;
	try {
	    thread.join();
	    display.close();
	} catch (InterruptedException ex) {
//	    Logger.getLogger(Loop.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    private void loop() {
	long lastTime = System.nanoTime();
	long now;
	
	int ticks = 0;
	int frames = 0;
	
	long lastTimer = System.currentTimeMillis();
	double deltaUps = 0;
	
//	boolean shouldRender = false;  //limit to 1 render for update
	
	while(running){
	    now = System.nanoTime();
	    deltaUps += (now-lastTime)/nsPerTick;
	    lastTime = now;

//	    shouldRender = false;

	    while(deltaUps >= 1){
		ticks++;
		game.tick();
		deltaUps -= 1;
//		shouldRender = true;
	    }
	    
//	    try {
//		Thread.sleep(2);
//	    } catch (InterruptedException ex) {
//		ex.printStackTrace();
//	    }
	    
//	    if(shouldRender){
		frames++;
		game.draw(display.getGraphics());
		display.show();
//	    }
	    
	    if(System.currentTimeMillis() - lastTimer>1000){
		lastTimer += 1000;
		display.appendTitle(String.format("ticks/sec: %d, frames/sec: %d", ticks, frames));
		frames = 0;
		ticks = 0;
	    }
	}
    }
    
}
