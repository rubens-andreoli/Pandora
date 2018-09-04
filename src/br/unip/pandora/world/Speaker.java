package br.unip.pandora.world;

import java.awt.Color;
import java.awt.Graphics2D;

//TODO: implement size
//TODO: implement fade (speaker must be one polygon; stay high alpha more time before reduce)

public class Speaker {

    private int x, y;
    private Color color = Color.WHITE;
    private Color noSoundColor = Color.RED;
    
    public Speaker(int x, int y) { 
	this.x = x;
	this.y = y;
    }

    public void draw(Graphics2D g, double volume, Color backColor) {
	g.setColor(color);
	g.fillRect(x+7, y+11, 5, 8);
	g.fillPolygon(new int[]{x, x+20, x+20}, new int[]{y+15, y+6, y+23}, 3);
	if(volume == 0){
	    g.setColor(noSoundColor);
	    g.drawLine(x+23, y+8, x+33, y+21);
	    g.drawLine(x+23, y+21, x+33, y+8);
	}
	if(volume >= 25) g.drawArc(x-15, y-6, 40, 40, -15, 30);
	if(volume >= 50) g.drawArc(x-10, y-6, 40, 40, -25, 50);
	if(volume >= 75) g.drawArc(x-5, y-6, 40, 40, -35, 70);
	g.setColor(backColor);
	g.fillRect(x, y+11, 5, 8);
    }
    
}
