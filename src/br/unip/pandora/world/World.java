package br.unip.pandora.world;
import br.unip.pandora.life.Food;
import br.unip.pandora.life.Soul;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class World {

    private Color soilColor = new Color(87, 142, 96);
    private Color gridColor = new Color(123, 174, 131);
    private int width;
    private int height;
    private int gridSize;
    
    private Food[] foodArray = new Food[20];
    private ArrayList<Soul> soulList = new ArrayList<>();

    private BufferedImage terrain, image;
    
    World(int width, int height, int gridSize) {
	this.width = width;
	this.height = height;
	this.gridSize = gridSize;
	image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	this.generateTerrain();
	//TODO: add food
	//TODO: add souls
    }
    
    private void generateTerrain(){
	int padding = gridSize; //duplicated for readability
	terrain = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	Graphics2D g = (Graphics2D) terrain.getGraphics();
	g.setColor(soilColor);
	g.fillRect(padding, padding, width-(2*padding), height-(2*padding));
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
	//TODO: generate water
    }
    
    public BufferedImage drawImage(){
	Graphics2D g = (Graphics2D) image.getGraphics();
	g.drawImage(terrain, 0, 0, null);
	//TODO: draw food
	//TODO: draw souls
	return image;
    }
    
    public void update() {
	//TODO: update food
	//TODO: update souls
    }

    public int getNumSouls(){ return soulList.size();}
    
}
