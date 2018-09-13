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
import java.awt.image.BufferedImage;

public class Pandora extends Game {

    //keys
    private static final int PAUSE = KeyEvent.VK_SPACE;
    private static final int SPEED_UP = KeyEvent.VK_PAGE_UP;
    private static final int SPEED_DOWN = KeyEvent.VK_PAGE_DOWN;
    private static final int VOLUME_UP = KeyEvent.VK_ADD;
    private static final int VOLUME_DOWN = KeyEvent.VK_SUBTRACT;
    
    //text
    private static final Font TITLE_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);
    private static final Color FEATURE_COLOR = Color.YELLOW;
    private static final String HOUR_SEC_MASK = "Hour/Sec: %.4f";
    private static final Font INFO_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 10);
    private static final Color INFO_COLOR = Color.GRAY;
    private static final String NUM_SOULS_MASK = "Nº Souls: %d";
    private static final Font PAUSE_FONT = new Font(Font.MONOSPACED, Font.BOLD, 10);
    private static final Color PAUSE_COLOR = Color.RED;
    private static final String PAUSE_MSG = "PAUSED";
    
    //borders
    private boolean borderDrawn;
    private int borderSize = 8; //FIX: don't change properly
    private static final Color BORDER_COLOR = Color.GRAY;
    
    //map and minimap
    private World world;
    private BufferedImage minimap;
    private float xOffset, yOffset, clickX, clickY;
    private Rectangle worldBounds;
    private Rectangle minimapBounds;
    private float minimapXScale, minimapYScale;
    private static final Cursor HAND_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private static final Cursor MOVE_CURSOR = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    private static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    
    //ui others
    private Clock clock;
    private Speaker speaker;
    private int infoWidth = 160;  //FIX: border don't change properly
    private int volume;
    private int infoY;
    
    //logic
    private boolean paused;
    private int tick, hour;
    private float hourRate;

    public Pandora() {
	super("PANDORA", 640, 480, 60); //width 800
	hourRate = 1*tickRate;
	clock = new Clock(infoWidth, 24, 365);
	worldBounds = new Rectangle(
		infoWidth+borderSize+1, 
		borderSize+1, 
		width-infoWidth-(borderSize*2)-2, 
		height-(borderSize*2)-2
	);
	minimapBounds = new Rectangle(
		10+borderSize,
		clock.getHeight()+13+borderSize,
		infoWidth-18-(borderSize*2),
		infoWidth-18-(borderSize*2)
	);
	world = new World(200, minimapBounds.width);
	minimap = world.getMinimap();
	minimapXScale = world.getMinimapXScale();
	minimapYScale = world.getMinimapYScale();
	infoY = minimapBounds.y+minimapBounds.height+borderSize;
	speaker = new Speaker(FEATURE_COLOR, 2*tickRate);
    }

    @Override
    public void tick(KeyHandler key, MouseHandler mouse) {
	if(!paused){ 
	    tick++;
	    if(tick >= hourRate){
		hour++;
		tick = 0;
	        world.update(); //TODO: update each hour? half hour?
	    }
	}
	
	speaker.tick();
	
	//<editor-fold defaultstate="collapsed" desc="KEYBOARD INPUT">
	if(key.isReleased(PAUSE)) paused = !paused;
	if(key.isPressed(SPEED_UP)) if(hourRate>2)hourRate-=0.2; //TODO: float hourRate messes with update?
	if(key.isPressed(SPEED_DOWN)) hourRate+=0.2;
	if(key.isTyped(VOLUME_UP)){
	    if(volume<=90) volume+=10;
	    speaker.startTimer();
	}
	if(key.isReleased(VOLUME_DOWN)){
	    if(volume>=10) volume-=10;
	    speaker.startTimer();
	}
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="MOUSE INPUT">
	if(mouse.isOver(worldBounds)){
	    if(mouse.isClicked(3) || mouse.isClicked(1)){
		clickX = mouse.getX()+xOffset;
		clickY = mouse.getY()+yOffset;
	    }
	    if(mouse.isPressed(3)){
		Display.setCursor(MOVE_CURSOR);
		xOffset = -(mouse.getX()-(clickX));
		yOffset = -(mouse.getY()-(clickY));
		if(xOffset<0) xOffset = 0;
		else if(xOffset+worldBounds.width > world.getTerrainWidth()) 
		    xOffset = world.getTerrainWidth()-worldBounds.width; //TODO: getTerrainInfo only once, or pass on creation from here
		if(yOffset<0) yOffset = 0;
		else if(yOffset+worldBounds.height > world.getTerrainHeight()) 
		    yOffset = world.getTerrainHeight()-worldBounds.height;
	    }else if(mouse.isClicked(1)){
//		world.getEntity(clickX, clickY);
	    }else{
		Display.setCursor(HAND_CURSOR);
	    }
	}else if(mouse.isOver(minimapBounds)){
	    Display.setCursor(HAND_CURSOR);
	    if(mouse.isClicked(1)){
		xOffset = ((mouse.getX()-minimapBounds.x)/minimapXScale)-(worldBounds.width/2);
		yOffset = ((mouse.getY()-minimapBounds.y)/minimapYScale)-(worldBounds.height/2);
		if(xOffset<0) xOffset = 0;
		else if(xOffset+worldBounds.width > world.getTerrainWidth()) 
		    xOffset = world.getTerrainWidth()-worldBounds.width; //TODO: getTerrainInfo only once, or pass on creation from here
		if(yOffset<0) yOffset = 0;
		else if(yOffset+worldBounds.height > world.getTerrainHeight()) 
		    yOffset = world.getTerrainHeight()-worldBounds.height;
	    }
	}else{
	    Display.setCursor(DEFAULT_CURSOR);
	}
	//</editor-fold>
    }

    @Override
    public void draw(Graphics2D g) {
	//borders (just once)
	if(!borderDrawn){
	    g.setColor(BORDER_COLOR);
	    for(int x=infoWidth+1; x<width; x+=borderSize){
		for(int y=1; y<height; y+=borderSize){
		    if(x>infoWidth+1 && x<width-borderSize-1 && y>1 && y<height-borderSize-1) continue;
		    g.fillRect(x, y, borderSize-2, borderSize-2);
		}
	    }
	    for(int x=10; x<infoWidth-10; x+=borderSize){
		for(int y=minimapBounds.y-borderSize; y<infoY; y+=borderSize){
		    if(x>11 && x<infoWidth-borderSize-11 && y>minimapBounds.y-borderSize && y<infoY-borderSize-1) continue;
		    g.fillRect(x, y, borderSize-2, borderSize-2);
		}
	    }
	   borderDrawn = true; 
	}

	if(!paused){
	    //clock
	    g.drawImage(clock.drawImage(hour), 0, 0, null);
	    
	    //title
	    g.setFont(TITLE_FONT);
	    g.setColor(FEATURE_COLOR);
	    g.drawString(title, (infoWidth/2-g.getFontMetrics(TITLE_FONT).stringWidth(title)/2), 15);
	    
	    //info
	    g.setFont(INFO_FONT);
	    g.setColor(INFO_COLOR);
	    g.clearRect(0, infoY+4, infoWidth, height-infoY);
	    g.drawRect(10, infoY+4, infoWidth-20, height-infoY-20);
	    //TODO: show entity info
	    g.drawString(String.format(NUM_SOULS_MASK, world.getNumSouls()), 10, height-6);	

	}else{
	    //paused
	    g.setFont(PAUSE_FONT);
	    g.setColor(PAUSE_COLOR);
	    g.drawString(PAUSE_MSG, infoWidth/2-g.getFontMetrics(PAUSE_FONT).stringWidth(PAUSE_MSG)/2, 64);
	    g.setStroke(new BasicStroke(2));
	    g.drawLine(infoWidth/2-4, 45, infoWidth/2-4, 55);
	    g.drawLine(infoWidth/2+4, 45, infoWidth/2+4, 55);
	    g.setStroke(new BasicStroke(1));
	}
	
	//speaker
	g.drawImage(speaker.drawImage(volume), 5, 5, null);
	
	//pandora hour per real second
	g.setFont(INFO_FONT);
	g.setColor(FEATURE_COLOR);
	g.clearRect(0, clock.getHeight(), 108, 10);
	g.drawString(String.format(HOUR_SEC_MASK, 60.0/hourRate), 10, clock.getHeight()+10);
	
	//world
	g.drawImage(
		world.drawImage(xOffset, yOffset, worldBounds.width, worldBounds.height), 
		infoWidth+borderSize+1, 
		borderSize+1, 
		null
	);
	
	//minimap
	g.drawImage(minimap, minimapBounds.x, minimapBounds.y, null);
	g.drawRect((int)(minimapBounds.x+(xOffset*minimapXScale)), 
		(int)(minimapBounds.y+(yOffset*minimapYScale)), 
		(int)(worldBounds.width*minimapXScale), 
		(int)(worldBounds.height*minimapYScale)
	);
    }

}
