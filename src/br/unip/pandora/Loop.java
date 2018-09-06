package br.unip.pandora;

import br.unip.pandora.Display.InitializedDisplay;

public class Loop {

    //loop
    private boolean running;
    public static final int UPS = 60;
//    private int FPS = 120;
    private double nsPerTick = 1000000000D/UPS;
    
    //parts
    private Thread thread;
    private Display display;
    private InitializedDisplay initDisplay;
    private Game game;
    
    public Loop(Game game, Display display) {
	this.display = display;
	this.game = game;
    }
     
    public void start(){
	if(running) return;
	
	display.init(game.width, game.height);
	initDisplay = display.getInitDisplay();
	initDisplay.addMouseListener(game);
	initDisplay.addKeyListener(game);
	
	running = true;
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
	    initDisplay.close();
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
	
//	boolean shouldRender = false;
	
	while(running){
	    now = System.nanoTime();
	    deltaUps += (now-lastTime)/nsPerTick;
	    lastTime = now;

//	    shouldRender = true; //limit to 1 render for update

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
	    
//	    if(shouldRender/* || frames < FPS-UPS*/){
		frames++;
		game.draw(initDisplay.getGraphics());
		initDisplay.show();
//	    }
	    
	    if(System.currentTimeMillis() - lastTimer>1000){
		lastTimer += 1000;
		initDisplay.appendTitle(String.format("ticks/sec: %d, frames/sec: %d", ticks, frames));
		frames = 0;
		ticks = 0;
	    }
	}
    }
    
}
