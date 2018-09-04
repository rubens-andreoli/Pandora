package br.unip.pandora.world;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class World {

    private Color soilColor = new Color(87, 142, 96);
    private Color gridColor = new Color(123, 174, 131);

    private BufferedImage image;
    
    World(int width, int height, int gridSize) {
	int padding = gridSize; //duplicated for readability
	image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	Graphics2D g = (Graphics2D) image.getGraphics();
	g.setColor(soilColor);
	g.fillRect(padding, padding, height-(2*padding), height-(2*padding));
	g.setColor(gridColor);
	for(int x=gridSize+padding; x<width-padding; x+=gridSize){
	    g.drawLine(x, padding, x, height-padding-1);
	}
	for(int y=padding+gridSize; y<height-padding; y+=gridSize){
	    g.drawLine(padding, y, width-padding-1, y);
	}
	g.setColor(Color.GRAY);
	for(int x=1; x<width; x+=gridSize){
	    for(int y=1; y<height; y+=gridSize){
		if(x>1 && x<width-gridSize-1 && y>1 && y<height-gridSize-1) continue;
		g.fillRect(x, y, gridSize-2, gridSize-2);
	    }
	}
    }
    
    public BufferedImage getImage(){
	return image;
    }

}
