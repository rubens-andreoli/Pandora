package br.unip.pandora.world;

import br.unip.pandora.Loop;
import br.unip.pandora.Main;
import br.unip.pandora.Renderer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Box implements MouseListener, KeyListener{

    //objects
    private World world;
    private Clock clock;
    private Speaker speaker;
    
    //time
    private boolean isPaused;
    private int hour;
    private int hourRate = 1*Loop.UPS;
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
    private int speakerDuration = 2*Loop.UPS;

    
    public Box() {
	world = new World(Renderer.WIDTH-infoWidth, Renderer.HEIGHT);
	clock = new Clock(infoWidth, 24, 365);
	speaker = new Speaker();
    }

    public void tick() {
	if(speakerTimer>0) speakerTimer--;
	if(!isPaused){ 
	    tick++;
	    if(tick >= hourRate){
		hour++;
		tick = 0;
	        world.update(); //update each hour? half hour?
	    }
	}
    }

    public void draw(Graphics2D g) {
	
	if(!isPaused){
	    //backgound
	    g.setColor(backColor);
	    g.fillRect(0, 0, Renderer.WIDTH, Renderer.HEIGHT);

	    g.drawImage(clock.drawImage(hour), 0, 0, null);
	    //name
	    g.setFont(nameFont);
	    g.setColor(nameColor);
	    g.drawString(Main.NAME, (infoWidth/2-g.getFontMetrics(nameFont).stringWidth(Main.NAME)/2), 12);
	    
	    //info
	    g.setFont(infoFont);
	    g.setColor(hourSecColor);
	    g.drawString(String.format(hourSecMask, 60.0/hourRate), 10, 105);
	    g.setColor(infoColor);
	    g.drawRect(10, 110, infoWidth-20, Renderer.HEIGHT-130);
	    //TODO: show soul info
	    	
	    g.drawString(numSoulsTxt+world.getNumSouls(), 10, Renderer.HEIGHT-6);	
	    
	    g.drawImage(world.drawImage(), infoWidth, 0, null);
	}else{
	    g.setFont(pauseFont);
	    g.setColor(pauseColor);
	    g.drawString(pauseMsg, infoWidth/2-g.getFontMetrics(pauseFont).stringWidth(pauseMsg)/2, 64);
	    
	    g.setStroke(new BasicStroke(2));
//	    g.drawString(pauseMsg, Renderer.WIDTH/2-g.getFontMetrics(pauseFont).stringWidth(pauseMsg)/2, Renderer.HEIGHT/2);
	    g.drawLine(infoWidth/2-4, 45, infoWidth/2-4, 55);
	    g.drawLine(infoWidth/2+4, 45, infoWidth/2+4, 55);
	    g.setStroke(new BasicStroke(1));
	}
	
	if(speakerTimer != 0) g.drawImage(speaker.drawImage(volume), 5, 5, null);
	else{ //better solution?
	    g.setColor(backColor);
	    g.fillRect(0, 0, 40, 30);
	}
	
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
