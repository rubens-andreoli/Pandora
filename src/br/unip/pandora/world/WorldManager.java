package br.unip.pandora.world;

import br.unip.pandora.Loop;
import br.unip.pandora.Main;
import br.unip.pandora.Renderer;
import br.unip.pandora.life.Food;
import br.unip.pandora.life.Soul;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class WorldManager implements MouseListener, KeyListener{

    //objects
    private World world;
    private Clock clock;
    private Speaker speaker;
    private Food[] foodArray = new Food[20];
    private ArrayList<Soul> soulList = new ArrayList<>();
    
    //time
    private boolean isPaused;
    private int hour;
    private int hourRate = 1*Loop.UPS;
    private int tick;
    
    //ui
    private String pauseMsg = "PAUSED";
    private Font pauseFont = new Font(Font.MONOSPACED, Font.BOLD, 100);
    private Color pauseColor = Color.YELLOW;
    private Color backColor = Color.BLACK;
    private Font nameFont = new Font(Font.MONOSPACED, Font.BOLD, 15);
    private Color nameColor = Color.YELLOW;
    private Font infoFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);
    private Color infoColor = Color.WHITE;
    private int infoWidth = Renderer.WIDTH/4;
    private double volume;
    private int speakerTimer;
    private int speakerDuration = 2*Loop.UPS;

    
    public WorldManager() {
	world = new World(Renderer.WIDTH-infoWidth, Renderer.HEIGHT, 6);
	clock = new Clock(0, 5, infoWidth, 24);
	speaker = new Speaker(0, 0);
	//TODO: add food
	//TODO: add souls
    }

    public void update() {
	if(speakerTimer>0) speakerTimer--;
	if(!isPaused){ 
	    tick++;
	    if(tick >= hourRate){
		hour++;
		tick = 0;
	    }
	    //TODO: update food
	    //TODO: update souls
	}
    }

    public void draw(Graphics2D g) {
	if(!isPaused){
	    //backgound
	    g.setColor(backColor);
	    g.fillRect(0, 0, Renderer.WIDTH, Renderer.HEIGHT);

	    //name
	    g.setFont(nameFont);
	    g.setColor(nameColor);
	    g.drawString(Main.NAME, (infoWidth/2-g.getFontMetrics(nameFont).stringWidth(Main.NAME)/2), 12);
	    
	    //info
	    clock.draw(g, hour, backColor);
	    g.setFont(infoFont);
	    g.setColor(Color.GRAY);
	    g.drawString(String.format("Hour/Sec: %.4f", 60.0/hourRate), 10, 105);
	    g.setColor(infoColor);
	    g.drawRect(10, 110, infoWidth-20, Renderer.HEIGHT-130);
	    //TODO: show soul info
	    
	    g.drawString("Nº Souls: "+soulList.size(), 10, Renderer.HEIGHT-6);	
	    
	    g.drawImage(world.getImage(), infoWidth, 0, null);
	    //TODO: draw food
	    //TODO: draw souls
	}else{
	    g.setFont(pauseFont);
	    g.setColor(pauseColor);
	    g.drawString(pauseMsg, Renderer.WIDTH/2-g.getFontMetrics(pauseFont).stringWidth(pauseMsg)/2, Renderer.HEIGHT/2);
	}
	
	g.setColor(backColor);
	g.fillRect(0, 0, 40, 26); //FIX: temp solution for timer not disapearing
	if(speakerTimer != 0) speaker.draw(g, volume, backColor);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	//TODO: select soul to display info or clear if out
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
	switch(e.getKeyCode()){
	    case KeyEvent.VK_SPACE:
		isPaused = !isPaused;
		break;
	    case KeyEvent.VK_ADD:
		if(volume<=90) volume+=10;
		speakerTimer=speakerDuration;
		break;
	    case KeyEvent.VK_SUBTRACT:
		if(volume>=10) volume-=10;
		speakerTimer=speakerDuration;
		break;
	    case KeyEvent.VK_PAGE_UP:
		if(hourRate>2)hourRate-=2;
		break;
	    case KeyEvent.VK_PAGE_DOWN:
		hourRate+=2;
		break;
	    default:
		break;
	}

    }

    public @Override void mouseReleased(MouseEvent e) {}
    public @Override void mousePressed(MouseEvent e) {}   
    public @Override void mouseEntered(MouseEvent e) {}
    public @Override void mouseExited(MouseEvent e) {}
    public @Override void keyTyped(KeyEvent e) {}
    public @Override void keyReleased(KeyEvent e) {}
    
}
