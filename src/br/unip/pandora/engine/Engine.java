package br.unip.pandora.engine;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Engine {
    
    public static final int DEFAULT_TICK_RATE = 60;

    //loop
    private double nanoTick;
    private boolean running;

    //parts
    private Thread thread;
    private Display display;
    private Game game;
    private KeyHandler key;
    private MouseHandler mouse;

    public Engine(Game game, Display display, KeyHandler key, MouseHandler mouse, int tickRate){
	this.game = game;
	this.display = display;
	this.key = key;
	this.mouse = mouse;
	nanoTick = 1000000000D/tickRate;
    }

    public Engine(Game game, String frameTitle, int scale){
	this(game, 
		new Display(frameTitle, game.width, game.height, scale), 
		new KeyHandler(),
		new MouseHandler(scale),
		DEFAULT_TICK_RATE
	);
    }
 
    public Engine(Game game, int tickRate){
	this(game, 
		new Display(game.title, game.width, game.height), 
		new KeyHandler(),
		new MouseHandler(),
		tickRate
	);
    }

    public Engine(Game game){
	this(game, DEFAULT_TICK_RATE);
    }

    public void start(){
	if(running) return;
	running = true;
	
	display.addWindowListener(new WindowAdapter(){
	    @Override
	    public void windowClosing(WindowEvent e) {
		stop();
	    }    
	});
	display.addKeyHandler(key);
	display.addMouseHandler(mouse);
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
	} catch (InterruptedException ex){
	} finally {
	    display.close();
	}
    }

    private void loop(){
	long lastTime = System.nanoTime();
	long now;
	
	int ticks = 0;
	int frames = 0;
	
	long lastTimer = System.currentTimeMillis();
	double delta = 0;
	
	boolean shouldRender = false;  //limit to 1 render for update
	
	while(running){
	    now = System.nanoTime();
	    delta += (now-lastTime)/nanoTick;
	    lastTime = now;

	    shouldRender = false;

	    while(delta >= 1){
		ticks++;
		game.tick(key, mouse);
		key.tick();
		mouse.tick();
		delta -= 1;
		shouldRender = true;
	    }

	    if(shouldRender){
		frames++;
		game.draw(display.getGraphics());
		display.show();
	    }
	    
	    if(System.currentTimeMillis() - lastTimer>1000){
		lastTimer += 1000;
		display.appendTitle(String.format("ticks/sec: %d, frames/sec: %d", ticks, frames));
		frames = 0;
		ticks = 0;
	    }
	    
	    try {
		Thread.sleep(1);
	    } catch (InterruptedException ex){}
	}
    }
    
}
