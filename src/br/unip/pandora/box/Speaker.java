package br.unip.pandora.box;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Speaker { //TODO: implement size

    private Color color;
    private Color noSoundColor = Color.RED;
    private int duration; //ticks
    private long timer;
    private BufferedImage image;

    public Speaker(Color color, int duration) {
	this.color = color;
	this.duration = duration;
	image = new BufferedImage(35, 25, BufferedImage.TYPE_INT_RGB);
    }
    
    public void tick(){
	if(timer>0) timer--;
    }

    public BufferedImage drawImage(int volume){
	Graphics g = image.getGraphics();

	g.clearRect(0, 0, 35, 25);
	if(timer>0){
	    g.setColor(color);
	    g.fillPolygon(new int[]{0, 2, 15, 15, 3, 0}, new int[]{8, 8, 1, 21, 16, 16}, 6);
	    if(volume == 0){
		g.setColor(noSoundColor);
		g.drawLine(18, 4, 28, 17);
		g.drawLine(18, 17, 28, 4);
	    }
	    if(volume >= 25) g.drawArc(-20, -9, 40, 40, -15, 30);
	    if(volume >= 50) g.drawArc(-15, -9, 40, 40, -25, 50);
	    if(volume >= 75) g.drawArc(-10, -9, 40, 40, -35, 70);
	}

	return image;
    }
    
    public void startTimer(){timer = duration;}
    
}
