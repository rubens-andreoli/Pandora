package br.unip.pandora.box;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Clock {
    
    //ui
    private int sunSize = 15;
    private Color sunColor = Color.YELLOW;
    private int moonSize = 10;
    private Color moonColor = Color.GRAY;
    private String textMask = "Year: %04d Day: %03d Hour: %02d";
    private Font textFont = new Font(Font.MONOSPACED, Font.PLAIN, 9);
    private Color textColor = Color.GRAY;
    private int width, height;
    
    //orbit
    private int dayHours, yearDays;
    private int orbitX, orbitY, orbitRadius;
    private double orbitSpeed;
    
    private BufferedImage image;

    public Clock(int width, int dayHours, int yearDays) {
	this.width = width;
	this.dayHours = dayHours;
	this.yearDays = yearDays;
	orbitRadius = (width-40-sunSize)/2;
	orbitX = width/2;
	orbitY = orbitRadius+25;
	orbitSpeed = Math.PI / (dayHours/2);
	height = orbitRadius+40;
	image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    
    public BufferedImage drawImage(int hour){
	Graphics g = image.getGraphics();

	//clear
	g.clearRect(0, 0, width, height);
	
	//sun-moon
	double radian = orbitSpeed * (hour + dayHours/2);
	double starX = orbitX + (orbitRadius*Math.cos(radian));
	double starY = orbitY + (orbitRadius*Math.sin(radian));
	g.setColor(sunColor);
	g.fillOval((int)(starX - sunSize/2), (int)(starY - sunSize/2), sunSize, sunSize);
	radian = orbitSpeed * hour;
	starX = orbitX + (orbitRadius*Math.cos(radian));
	starY = orbitY + (orbitRadius*Math.sin(radian));
	g.setColor(moonColor);
	g.fillOval((int)(starX - moonSize/2), (int)(starY - moonSize/2), moonSize, moonSize);
	g.clearRect(0, orbitRadius+26, width, orbitRadius);
	
	//time
	g.setFont(textFont);
	g.setColor(textColor);
	g.drawLine(10, orbitRadius+25, width-10, orbitRadius+25);
	String time = String.format(textMask, 
		(hour/dayHours)/yearDays, 
		(hour/dayHours)%yearDays, 
		hour%dayHours
	);
	g.drawString(time, (width/2 - g.getFontMetrics(textFont).stringWidth(time)/2)+1, orbitRadius+35);
	
	return image;
    }

    public int getHeight() {
	return height;
    }
}
