package br.unip.pandora.box;

import br.unip.pandora.box.entity.Creature;
import br.unip.pandora.engine.Display;
import br.unip.pandora.engine.Game;
import br.unip.pandora.engine.KeyHandler;
import br.unip.pandora.engine.MouseHandler;
import br.unip.pandora.engine.SoundManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Properties;

public class Pandora extends Game {

    //keys
    private int pauseKey;
    private int speedUpKey;
    private int speedDownKey;
    private int volumeUpKey;
    private int volumeDownKey;
    private int saveQValueKey;
    
    //text
    private Font titleFont = new Font(Font.MONOSPACED, Font.BOLD, 18);
    private Color featureColor = Color.YELLOW;
    private String hourSecMask = "Hour/Sec: %.4f";
    private Font infoFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);
    private Color infoColor = Color.GRAY;
    private Font pauseFont = new Font(Font.MONOSPACED, Font.BOLD, 10);
    private Color pauseColor = Color.RED;
    private String pauseMsg = "PAUSED";
    private BasicStroke defaultStroke = new BasicStroke(1);
    private BasicStroke boldStroke = new BasicStroke(2);
    private String infoMsg = "---INFORMATIONS---";
    
    //borders
    private boolean borderDrawn;
    private final int borderSize = 8;
    private Color borderColor = Color.GRAY;
    
    //map and minimap
    private World world;
    private Rectangle worldBounds;
    private BufferedImage minimap;
    private Rectangle minimapBounds;
    private float minimapXScale, minimapYScale;
    private float xOffset, yOffset, clickX, clickY;
    private Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private Cursor moveCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    private Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private int modelSize = 5;
    
    //ui others
    private Clock clock;
    private Speaker speaker;
    private final int infoWidth = 160;
    private float volume = 0.7F;
    private float volumeRate = 0.1F;
    private int clockHeight, infoY, terrainWidth, terrainHeight;
    private Creature creature;
    private SoundManager sound;
    
    //logic
    private boolean paused;
    private int tick, hour;
    private float hourRate = 30; //1 hours per 30 ticks -> 60 ticks per 1 seg -> 1 hour per 1/2 sec

    public Pandora(Properties p) {
	super("PANDORA", Integer.parseInt((String)p.get("width")), Integer.parseInt((String)p.get("height")));
	pauseKey = Integer.parseInt((String)p.get("pauseKey"));
	speedUpKey = Integer.parseInt((String)p.get("speedUpKey"));
	speedDownKey = Integer.parseInt((String)p.get("speedDownKey"));
	volumeUpKey = Integer.parseInt((String)p.get("volumeUpKey"));
	volumeDownKey = Integer.parseInt((String)p.get("volumeDownKey"));
	saveQValueKey = Integer.parseInt((String)p.get("saveQValueKey"));
	clock = new Clock(
		infoWidth, 
		Integer.parseInt((String)p.get("dayHours")), 
		Integer.parseInt((String)p.get("yearDays"))
	);
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
		infoWidth-18-(borderSize*2),
		infoWidth-18-(borderSize*2)
	);
	sound = new SoundManager(volume);
	world = new World(
		p, 
		worldBounds.width, 
		worldBounds.height, 
		minimapBounds.width, 
		minimapBounds.height, 
		sound
	);
	minimap = world.getMinimap();
	minimapXScale = world.getMinimapXScale();
	minimapYScale = world.getMinimapYScale();
	infoY = minimapBounds.y+minimapBounds.height+borderSize;
	terrainWidth = world.getTerrainWidth();
	terrainHeight = world.getTerrainHeight();
	speaker = new Speaker(featureColor, 120);
	creature = world.getCreature();
    }

    @Override
    public void tick(KeyHandler key, MouseHandler mouse) {
	if(!paused){ 
	    tick++;
	    if(tick >= hourRate){
		hour++;
		world.update();
		tick = 0;
	    }
	}
	
	speaker.tick();
	
	xOffset = creature.getX()*world.getGridSize()-(worldBounds.width/2);
	yOffset = creature.getY()*world.getGridSize()-(worldBounds.height/2);
	applyOffsetLimit();
	
	//<editor-fold defaultstate="collapsed" desc="KEYBOARD INPUT"> 
	if(key.isReleased(pauseKey)) paused = !paused;
	if(key.isPressed(speedUpKey)) if(hourRate>2)hourRate-=0.2;
	if(key.isPressed(speedDownKey)) if(hourRate<60)hourRate+=0.2;
	if(key.isTyped(volumeUpKey)){
	    volume+=volumeRate;
	    if(volume > 1) volume = 1F;
	    sound.setVolume(volume);
	    speaker.startTimer();
	}
	if(key.isReleased(volumeDownKey)){
	    volume-=volumeRate;
	    if(volume < 0) volume = 0;
	    sound.setVolume(volume);
	    speaker.startTimer();
	}
	if(key.isReleased(saveQValueKey)) world.saveQValues();
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="MOUSE INPUT">
	if(mouse.isOver(worldBounds)){
	    if(mouse.isClicked(3) || mouse.isClicked(1)){
		clickX = mouse.getX()+xOffset;
		clickY = mouse.getY()+yOffset;
	    }
	    if(mouse.isPressed(3)){
		Display.setCursor(moveCursor);
		xOffset = -(mouse.getX()-(clickX));
		yOffset = -(mouse.getY()-(clickY));
		applyOffsetLimit();
	    }else if(mouse.isClicked(1)){
//		world.getEntity(clickX, clickY);
	    }else{
		Display.setCursor(handCursor);
	    }
//	}else if(mouse.isOver(minimapBounds)){
//	    Display.setCursor(handCursor);
//	    if(mouse.isClicked(1)){
//		xOffset = ((mouse.getX()-minimapBounds.x)/minimapXScale)-(worldBounds.width/2);
//		yOffset = ((mouse.getY()-minimapBounds.y)/minimapYScale)-(worldBounds.height/2);
//		applyOffsetLimit();
//	    }
	}else{
	    Display.setCursor(defaultCursor);
	}
	//</editor-fold>
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
	    g.setFont(titleFont);
	    g.setColor(featureColor);
	    g.drawString(title, (infoWidth/2-g.getFontMetrics(titleFont).stringWidth(title)/2), 15);
	    
	    //info
	    g.setFont(infoFont);
	    g.setColor(infoColor);
	    g.clearRect(0, infoY+4, infoWidth, height-infoY);
	    g.drawRect(10, infoY+4, infoWidth-20, height-infoY-6);
	    g.drawString(infoMsg, 25, infoY+15);
	    g.drawString("Action:"+creature.getCurrentAction(), 13, infoY+30);
	    g.drawString("State:"+creature.getCurrentState(), 13, infoY+44);
	    g.drawString("Life:"+creature.getLife(), 13, infoY+59);
	    drawInfo(g, 13, infoY+64, (int)creature.getLife(), (int)creature.getLifeMax(), featureColor);
	    int thirst = (int)creature.getThirstMax();
	    g.drawString("Hydration:"+creature.getThirst(), 13, infoY+119);
	    drawInfo(g, 13, infoY+124, (int)(thirst-creature.getThirst()), thirst, Color.BLUE);
	    int hunger = (int)creature.getHungerMax();
	    g.drawString("Satiation:"+creature.getHunger(), 13, infoY+179);
	    drawInfo(g, 13, infoY+184, (int)(hunger-creature.getHunger()), hunger, Color.RED);	
	}else{
	    //paused
	    g.setFont(pauseFont);
	    g.setColor(pauseColor);
	    g.drawString(pauseMsg, infoWidth/2-g.getFontMetrics(pauseFont).stringWidth(pauseMsg)/2, 64);
	    g.setStroke(boldStroke);
	    g.drawLine(infoWidth/2-4, 45, infoWidth/2-4, 55);
	    g.drawLine(infoWidth/2+4, 45, infoWidth/2+4, 55);
	    g.setStroke(defaultStroke);
	}
	
	//speaker
	g.drawImage(speaker.drawImage(volume), 5, 5, null);
	
	//pandora hour per real second
	g.setFont(infoFont);
	g.setColor(featureColor);
	g.clearRect(0, clockHeight, infoWidth, 11); //11 -> font height
	g.drawString(String.format(hourSecMask, 60.0/hourRate), 10, clockHeight+10); //60.0 -> tick rate
	
	//world
	g.drawImage(
		world.drawImage(xOffset, yOffset), 
		infoWidth+borderSize+1, 
		borderSize+1, 
		null
	);
	
	//minimap
	g.setColor(featureColor);
	g.drawImage(minimap, minimapBounds.x, minimapBounds.y, null);
	g.drawRect((int)(minimapBounds.x+(xOffset*minimapXScale)), 
		(int)(minimapBounds.y+(yOffset*minimapYScale)), 
		(int)(worldBounds.width*minimapXScale), 
		(int)(worldBounds.height*minimapYScale)
	);
	g.fillRect(
		Math.min(
		    (int)(minimapBounds.x+creature.getX()*(world.getGridSize()*minimapXScale)),
		    minimapBounds.x+minimap.getWidth()-modelSize
		),
		Math.min(
		    (int)(minimapBounds.y+creature.getY()*(world.getGridSize()*minimapYScale)),
		    minimapBounds.y+minimap.getHeight()-modelSize
		), 
		modelSize, 
		modelSize
	);
    }

    private void applyOffsetLimit(){
	if(xOffset<0) xOffset = 0;
	else if(xOffset+worldBounds.width > terrainWidth) 
	    xOffset = terrainWidth-worldBounds.width;
	
	if(yOffset<0) yOffset = 0;
	else if(yOffset+worldBounds.height > terrainHeight) 
	    yOffset = terrainHeight-worldBounds.height;
    }
    
    private void drawInfo(Graphics g, int x, int y, int value, int max, Color c){
	int size = 20;
	g.setColor(c);
	int i = 0;
	while(i<max/2){
	    if(i < value)g.fillRect(x+(size*i)+3*i, y, size, size);
	    else g.drawRect(x+(size*i)+3*i, y, size, size);
	    i++;
	}
	while(i<max){
	    if(i < value)g.fillRect(x+(size*(i-max/2))+3*(i-max/2), y+size+3, size, size);
	    else g.drawRect(x+(size*(i-max/2))+3*(i-max/2), y+size+3, size, size);
	    i++;
	}
	g.setColor(infoColor);
    }
    
}
