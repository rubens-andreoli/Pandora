package br.unip.pandora.box;

import br.unip.pandora.engine.Game;
import br.unip.pandora.engine.KeyHandler;
import br.unip.pandora.engine.MouseHandler;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Box extends Game {

    //keys
    public final static int PAUSE = KeyEvent.VK_SPACE;
    public final static int SPEED_UP = KeyEvent.VK_PAGE_UP;
    public final static int SPEED_DOWN = KeyEvent.VK_PAGE_DOWN;
    public final static int VOLUME_UP = KeyEvent.VK_ADD;
    public final static int VOLUME_DOWN = KeyEvent.VK_SUBTRACT;
    
    //objects
    private World world;
    private Clock clock;
    private Speaker speaker;
    
    //time
    private boolean isPaused;
    private int hour;
    private int hourRate;
    private int tick;
    
    //ui
    private String hourSecMask = "Hour/Sec: %.4f";
    private Color hourSecColor = Color.GRAY;
    private String numSoulsTxt = "Nº Souls: ";
    private String pauseMsg = "PAUSED";
    private Font pauseFont = new Font(Font.MONOSPACED, Font.BOLD, 10);
    private Color pauseColor = Color.RED;
    private Color backColor = Color.BLACK;
    private Font nameFont = new Font(Font.MONOSPACED, Font.BOLD, 15);
    private Color nameColor = Color.YELLOW;
    private Font infoFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);
    private Color infoColor = Color.WHITE;
    private int infoWidth = 160;
    private int volume;
    private int speakerTimer;
    private int speakerDuration;

    
    public Box() {
	super("PANDORA", 640, 480, 2, 60);

	hourRate = 1*tickRate;
	speakerDuration = 2*tickRate;
	
	world = new World(width-infoWidth, height);
	clock = new Clock(infoWidth, 24, 365);
	speaker = new Speaker();
    }

    @Override
    public void tick() {
	if(speakerTimer>0) speakerTimer--;
	
	if(!isPaused){ 
	    tick++;
	    if(tick >= hourRate){
		hour++;
		tick = 0;
	        world.update(); //TODO: update each hour? half hour?
	    }
	}
    }
    
    @Override
    public void input(KeyHandler key, MouseHandler mouse) {
	//TODO: ajust input speed
	if(key.isReleased(PAUSE)) isPaused = !isPaused;
	if(key.isPressed(SPEED_UP)) if(hourRate>2)hourRate-=2;
	if(key.isPressed(SPEED_DOWN)) hourRate+=2;
	if(key.isTyped(VOLUME_UP)){
	    if(volume<=90) volume+=10;
	    speakerTimer=speakerDuration;
	}
	if(key.isReleased(VOLUME_DOWN)){
	    if(volume>=10) volume-=10;
	    speakerTimer=speakerDuration;
	}
    }

    @Override
    public void draw(Graphics2D g) {
	if(!isPaused){
	    //clear
	    g.clearRect(0, clock.getHeight(), infoWidth+5, height-clock.getHeight());

	    //clock
	    g.drawImage(clock.drawImage(hour), 0, 0, null);
	    
	    //name
	    g.setFont(nameFont);
	    g.setColor(nameColor);
	    g.drawString(title, (infoWidth/2-g.getFontMetrics(nameFont).stringWidth(title)/2), 12);
	    
	    //info
	    g.setFont(infoFont);
	    g.setColor(hourSecColor);
	    g.drawString(String.format(hourSecMask, 60.0/hourRate), 10, 105); //TODO: hour sec on pause
	    g.setColor(infoColor);
	    g.drawRect(10, 110, infoWidth-20, height-130);
	    //TODO: show soul info
	    g.drawString(numSoulsTxt+world.getNumSouls(), 10, height-6);	
	    
	    //world
	    g.drawImage(world.drawImage(), infoWidth, 0, null);
	}else{
	    //pause
	    g.setFont(pauseFont);
	    g.setColor(pauseColor);
	    g.drawString(pauseMsg, infoWidth/2-g.getFontMetrics(pauseFont).stringWidth(pauseMsg)/2, 64);
	    g.setStroke(new BasicStroke(2));
	    g.drawLine(infoWidth/2-4, 45, infoWidth/2-4, 55);
	    g.drawLine(infoWidth/2+4, 45, infoWidth/2+4, 55);
	    g.setStroke(new BasicStroke(1));
	}
	
	if(speakerTimer != 0) g.drawImage(speaker.drawImage(volume), 5, 5, null); //5 5
	else{ //TODO: better solution?
	    g.clearRect(5, 0, 35, 30);
	}
	
    }

}
