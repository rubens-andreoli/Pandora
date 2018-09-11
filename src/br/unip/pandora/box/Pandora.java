package br.unip.pandora.box;

import br.unip.pandora.engine.Display;
import br.unip.pandora.engine.Game;
import br.unip.pandora.engine.KeyHandler;
import br.unip.pandora.engine.MouseHandler;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Pandora extends Game {

    //keys
    public final static int PAUSE = KeyEvent.VK_SPACE;
    public final static int SPEED_UP = KeyEvent.VK_PAGE_UP;
    public final static int SPEED_DOWN = KeyEvent.VK_PAGE_DOWN;
    public final static int VOLUME_UP = KeyEvent.VK_ADD;
    public final static int VOLUME_DOWN = KeyEvent.VK_SUBTRACT;
    
    //ui
    private String hourSecMask = "Hour/Sec: %.4f";
    private Color hourSecColor = Color.GRAY;
    private String numSoulsTxt = "Nº Souls: ";
    private String pauseMsg = "PAUSED";
    private Font pauseFont = new Font(Font.MONOSPACED, Font.BOLD, 10);
    private Color pauseColor = Color.RED;
    private Font nameFont = new Font(Font.MONOSPACED, Font.BOLD, 15);
    private Color nameColor = Color.YELLOW;
    private Font infoFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);
    private Color infoColor = Color.WHITE;
    private boolean borderDrawn;
    private int borderSize = 8; //FIX: only even numbers
    private Color borderColor = Color.GRAY;
    private int infoWidth = 160;
    private int volume, speakerTimer, speakerDuration;
    
    //world camera
    private float xOffset, yOffset, clickX, clickY;
    private Rectangle worldBounds;
    private Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private Cursor moveCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    private Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    
    //components
    private World world;
    private Clock clock;
    private Speaker speaker;
    
    //logic
    private boolean paused;
    private int tick, hour;
    private float hourRate;

    public Pandora() {
	super("PANDORA", 640, 480, 2, 60); //width 800

	hourRate = 1*tickRate;
	speakerDuration = 2*tickRate;
	
	worldBounds = new Rectangle(
		infoWidth+borderSize+1, 
		borderSize+1, 
		width-infoWidth-(borderSize*2)-2, 
		height-(borderSize*2)-2
	);
	world = new World(worldBounds.width, worldBounds.height);
	 
	clock = new Clock(infoWidth, 24, 365);
	speaker = new Speaker();
    }

    @Override
    public void tick() {
	if(speakerTimer>0) speakerTimer--;
	
	if(!paused){ 
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
	if(key.isReleased(PAUSE)) paused = !paused;
	if(key.isPressed(SPEED_UP)) if(hourRate>2)hourRate-=0.2; //TODO: float hourRate messes with update?
	if(key.isPressed(SPEED_DOWN)) hourRate+=0.2;
	if(key.isTyped(VOLUME_UP)){
	    if(volume<=90) volume+=10;
	    speakerTimer=speakerDuration;
	}
	if(key.isReleased(VOLUME_DOWN)){
	    if(volume>=10) volume-=10;
	    speakerTimer=speakerDuration;
	}
	if(mouse.isOver(worldBounds)){
	    Display.setCursor(handCursor);
	    if(mouse.isClicked(1)){
		clickX = mouse.getX()+xOffset;
		clickY = mouse.getY()+yOffset;
	    }
	    if(mouse.isPressed(1)){
		Display.setCursor(moveCursor);
		xOffset = -(mouse.getX()-(clickX));
		yOffset = -(mouse.getY()-(clickY));
		if(xOffset<0) xOffset = 0;
		else if(xOffset+worldBounds.width > world.getTerrainWidth()) 
		    xOffset = world.getTerrainWidth()-worldBounds.width; //TODO: getTerrainInfo only once, or pass on creation from here
		if(yOffset<0) yOffset = 0;
		else if(yOffset+worldBounds.height > world.getTerrainHeight()) 
		    yOffset = world.getTerrainHeight()-worldBounds.height;

	    }
	}else{
	    Display.setCursor(defaultCursor);
	}
    }

    @Override
    public void draw(Graphics2D g) {
	//border (just once)
	if(!borderDrawn){
	    g.setColor(borderColor);
	    for(int x=infoWidth+1; x<width; x+=borderSize){
		for(int y=1; y<height; y+=borderSize){
		    if(x>infoWidth+1 && x<width-borderSize-1 && y>1 && y<height-borderSize-1) continue;
		    g.fillRect(x, y, borderSize-2, borderSize-2);
		}
	    }
	   borderDrawn = true; 
	}

	if(!paused){
	    //clear
	    g.clearRect(0, clock.getHeight(), infoWidth, height-clock.getHeight());

	    //clock
	    g.drawImage(clock.drawImage(hour), 0, 0, null);
	    
	    //title
	    g.setFont(nameFont);
	    g.setColor(nameColor);
	    g.drawString(title, (infoWidth/2-g.getFontMetrics(nameFont).stringWidth(title)/2), 12);
	    
	    //info
	    g.setFont(infoFont);
	    g.setColor(infoColor);
	    g.drawRect(10, clock.getHeight()+13, infoWidth-20, height-clock.getHeight()-30);
	    //TODO: show soul info
//	    world.getEntity(clickX, clickY);
	    g.drawString(numSoulsTxt+world.getNumSouls(), 10, height-6);	
	    
	    //world
	    g.drawImage(world.drawImage(xOffset, yOffset), infoWidth+borderSize+1, borderSize+1, null);
	}else{
	    //paused
	    g.setFont(pauseFont);
	    g.setColor(pauseColor);
	    g.drawString(pauseMsg, infoWidth/2-g.getFontMetrics(pauseFont).stringWidth(pauseMsg)/2, 64);
	    g.setStroke(new BasicStroke(2));
	    g.drawLine(infoWidth/2-4, 45, infoWidth/2-4, 55);
	    g.drawLine(infoWidth/2+4, 45, infoWidth/2+4, 55);
	    g.setStroke(new BasicStroke(1));
	}
	
	//speaker
	if(speakerTimer != 0) g.drawImage(speaker.drawImage(volume), 5, 5, null);
	else g.clearRect(5, 0, 35, 30); //TODO: better solution?
	
	//pandora hour per real second
	g.setFont(infoFont);
	g.setColor(hourSecColor);
	if(paused) g.clearRect(0, clock.getHeight(), 108, 10); //TODO: better solution?
	g.drawString(String.format(hourSecMask, 60.0/hourRate), 10, clock.getHeight()+10);
    }

}
