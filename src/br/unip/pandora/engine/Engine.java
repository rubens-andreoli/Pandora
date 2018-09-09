package br.unip.pandora.engine;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Engine {
    
    public static final int DEFAULT_UPS = 60;
    
    //loop
    private double nanoTick;
    private boolean running;
    
    //parts
    private Thread thread;
    private Display display;
    private Game game;
    private InputHandler input;
    
    public Engine(Game game, Display display, InputHandler input) {
	this.game = game;
	this.display = display;
	this.input = input;
	nanoTick = 1000000000D/game.ups;
    }
      
    public Engine(Game game){
	this(game, new Display(game.title, game.width, game.height, game.scale), new InputHandler(game.scale));
    }
    
    public void start(){
	if(running) return;
	running = true;
	
	game.setInputHandler(input);
	display.addWindowListener(new WindowAdapter(){
	    @Override
	    public void windowClosing(WindowEvent e) {
		stop();
	    }    
	});
	display.addInputHandler(input); //TODO: disable or enable listeners
	display.setVisible(true);
	
	thread = new Thread(() -> {
	    loop();
	});
	thread.setName(game.title);
	thread.start();
    }
    
    public void stop(){
	if(!running) return;
	running = false;
	try {
	    thread.join(); 
	} catch (InterruptedException ex) {
//	    Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    display.close();
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
	    deltaUps += (now-lastTime)/nanoTick;
	    lastTime = now;

//	    shouldRender = false;

	    while(deltaUps >= 1){
		ticks++;
		game.tick();
		input.tick();
		deltaUps -= 1;
//		shouldRender = true;
	    }
	    
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
	    
	    try {
		Thread.sleep(2);
	    } catch (InterruptedException ex) {
		ex.printStackTrace();
	    }
	}
    }
}
