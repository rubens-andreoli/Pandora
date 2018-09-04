package br.unip.pandora.world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

//TODO: test diferent sizes

public class Clock {
    
    private int sunSize = 15;
    private Color sunColor = Color.YELLOW;
    private int moonSize = 10;
    private Color moonColor = Color.GRAY;
    private Font textFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);
    private Color textColor = Color.WHITE;
    
    private int x, y, size;
    private int dayHours;
    private int orbitX, orbitY, orbitRadius;
    private double orbitSpeed;

    public Clock(int x, int y, int size, int dayHours) {
	this.x = x;
	this.y = y;
	this.size = size;
	this.dayHours = dayHours;
	orbitRadius = (size-40-sunSize)/2;
	orbitX = (size/2)+x;
	orbitY = orbitRadius+15+y;
	orbitSpeed = Math.PI / (dayHours/2);
    }
    
    public void draw(Graphics2D g, int hour, Color backColor){
	//sun-moon
	double radian = orbitSpeed * (hour + dayHours/2);
	double starX = orbitX + orbitRadius * Math.cos(radian);
	double starY = orbitY + orbitRadius * Math.sin(radian);
	g.setColor(sunColor);
	g.fillOval((int)(starX - sunSize/2), (int)(starY - sunSize/2), sunSize, sunSize);
	radian = orbitSpeed * hour;
	starX = orbitX + orbitRadius * Math.cos(radian);
	starY = orbitY + orbitRadius * Math.sin(radian);
	g.setColor(moonColor);
	g.fillOval((int)(starX - moonSize/2), (int)(starY - moonSize/2), moonSize, moonSize);
	g.setColor(backColor);
	g.fillRect(x, y+orbitRadius+16, size, size);
	g.setColor(textColor);
	g.drawLine(x+10, y+orbitRadius+15, x+size-10, y+orbitRadius+15);

	//time
	g.setFont(textFont);
	String time = "Day: " + hour / dayHours + " Hour: " + hour % dayHours;
	g.drawString(time, (size/2-g.getFontMetrics(textFont).stringWidth(time)/2)+x, y+orbitRadius+30);
    }
    
}
