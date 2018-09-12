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
    public static final int PAUSE = KeyEvent.VK_SPACE;
    public static final int SPEED_UP = KeyEvent.VK_PAGE_UP;
    public static final int SPEED_DOWN = KeyEvent.VK_PAGE_DOWN;
    public static final int VOLUME_UP = KeyEvent.VK_ADD;
    public static final int VOLUME_DOWN = KeyEvent.VK_SUBTRACT;
    
    //ui
    private String hourSecMask = "Hour/Sec: %.4f";
    private Color hourSecColor = Color.GRAY;
    private String numSoulsTxt = "Nº Souls: ";
    private String pauseMsg = "PAUSED";
    private Font pauseFont = new Font(Font.MONOSPACED, Font.BOLD, 10);
    private Color pauseColor = Color.RED;
    private Font nameFont = new Font(Font.MONOSPACED, Font.BOLD, 18);
    private Color nameColor = Color.YELLOW;
    private Font infoFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);
    private Color infoColor = Color.GRAY;
    private boolean borderDrawn;
    private int borderSize = 8; //8
    private Color borderColor = Color.GRAY;
    private int volume, speakerTimer, speakerDuration;
    private int infoWidth = 160; //160
    private float minimapScale;
    private int clockHeight, infoY;
    
    //world camera
    private float xOffset, yOffset, clickX, clickY;
//    private float zoom = 1;
    private Rectangle worldBounds;
    private Rectangle minimapBounds;
    private Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private Cursor moveCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    private Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    
    //components
    private World world;
    private Clock clock;
    private Speaker speaker;
    private BufferedImage minimap;
    
    //logic
    private boolean paused;
    private int tick, hour;
    private float hourRate;

    public Pandora() {
	super("PANDORA", 640, 480, 60); //width 800

	hourRate = 1*tickRate;
	speakerDuration = 2*tickRate;
	
	clock = new Clock(infoWidth, 24, 365);
	clockHeight = clock.getHeight();
	worldBounds = new Rectangle(
		infoWidth+borderSize+1, 
		borderSize+1, 
		width-infoWidth-(borderSize*2)-2, 
		height-(borderSize*2)-2
	);
	minimapBounds = new Rectangle(
		10+borderSize,
		clockHeight+13+borderSize,
		infoWidth-(borderSize*2)-18,
		0
	);
	world = new World(worldBounds.width, worldBounds.height, minimapBounds.width);
	minimap = world.getMinimap();
	minimapBounds.height = minimap.getHeight();
	minimapScale = world.getMinimapScale();
	infoY = minimapBounds.y+minimapBounds.height+borderSize;
	speaker = new Speaker();
    }

    @Override
    public void tick(KeyHandler key, MouseHandler mouse) {
	if(speakerTimer>0) speakerTimer--;
	
	if(!paused){ 
	    tick++;
	    if(tick >= hourRate){
		hour++;
		tick = 0;
	        world.update(); //TODO: update each hour? half hour?
	    }
	}
	
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
	    if(mouse.isClicked(3)){
		clickX = mouse.getX()+xOffset;
		clickY = mouse.getY()+yOffset;
	    }
	    if(mouse.isPressed(3)){
		Display.setCursor(moveCursor);
		xOffset = -(mouse.getX()-(clickX));
		yOffset = -(mouse.getY()-(clickY));
		if(xOffset<0) xOffset = 0;
		else if(xOffset+worldBounds.width > world.getTerrainWidth()) 
		    xOffset = world.getTerrainWidth()-worldBounds.width; //TODO: getTerrainInfo only once, or pass on creation from here
		if(yOffset<0) yOffset = 0;
		else if(yOffset+worldBounds.height > world.getTerrainHeight()) 
		    yOffset = world.getTerrainHeight()-worldBounds.height;
	    }else{
		Display.setCursor(handCursor);
	    }
//	    if(zoom >= 0.5) zoom += mouse.getWheel()/20f;
	}else if(mouse.isOver(minimapBounds)){
	    Display.setCursor(handCursor);
	    if(mouse.isClicked(1)){
		xOffset = ((mouse.getX()-minimapBounds.x)/minimapScale)-(worldBounds.width/2);
		yOffset = ((mouse.getY()-minimapBounds.y)/minimapScale)-(worldBounds.height/2);
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
	//borders (just once)
	if(!borderDrawn){
	    g.setColor(borderColor);
	    for(int x=infoWidth+1; x<width; x+=borderSize){
		for(int y=1; y<height; y+=borderSize){
		    if(x>infoWidth+1 && x<width-borderSize-1 && y>1 && y<height-borderSize-1) continue;
		    g.fillRect(x, y, borderSize-2, borderSize-2);
		}
	    }
	    for(int x=10; x<infoWidth-10; x+=borderSize){
		for(int y=clockHeight+13; y<infoY; y+=borderSize){
		    if(x>11 && x<infoWidth-borderSize-11 && y>clockHeight+14 && y<infoY-borderSize-1) continue;
		    g.fillRect(x, y, borderSize-2, borderSize-2);
		}
	    }
	   borderDrawn = true; 
	}

	if(!paused){
	    //clock
	    g.drawImage(clock.drawImage(hour), 0, 0, null);
	    
	    //title
	    g.setFont(nameFont);
	    g.setColor(nameColor);
	    g.drawString(title, (infoWidth/2-g.getFontMetrics(nameFont).stringWidth(title)/2), 15);
	    
	    //clear
	    g.clearRect(0, infoY+4, infoWidth, height-infoY);

	    //info
	    g.setFont(infoFont);
	    g.setColor(infoColor);
	    g.drawRect(10, infoY+4, infoWidth-20, height-infoY-20);
//	    world.getEntity(clickX, clickY);
	    //TODO: show entity info
	    g.setColor(nameColor);
	    g.drawString(numSoulsTxt+world.getNumSouls(), 10, height-6);	

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
	g.setColor(nameColor);
	g.clearRect(0, clock.getHeight(), 108, 10);
	g.drawString(String.format(hourSecMask, 60.0/hourRate), 10, clock.getHeight()+10);
	
	//world
	g.drawImage(world.drawImage(xOffset, yOffset/*, zoom*/), infoWidth+borderSize+1, borderSize+1, null);
	
	//minimap
	g.drawImage(minimap, minimapBounds.x, minimapBounds.y, null);
	g.setColor(nameColor);
	g.drawRect(
		(int)(10+borderSize+(xOffset*minimapScale)), 
		(int)(clockHeight+borderSize+13+(yOffset*minimapScale)), 
		(int)(worldBounds.width*minimapScale), 
		(int)(worldBounds.height*minimapScale)
	);
    }

}
