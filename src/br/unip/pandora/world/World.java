package br.unip.pandora.world;

import br.unip.pandora.engine.Generator;
import br.unip.pandora.entity.Entity;
import br.unip.pandora.entity.Soul;
import br.unip.pandora.entity.Water;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class World {
    
    //ui
    private Color soilColor = new Color(87, 142, 96);
    private Color waterColor = Color.BLUE;
    private Color borderColor = Color.GRAY;
    private int width, height;
    
    //map control
    private int gridSize = 6;
    private int rows, cols;
    private Entity[][] map;
//    private Food[] foodArray = new Food[20];
    private ArrayList<Soul> soulList = new ArrayList<>();
    
    //water generation
    private int numCenters = 60; //waterbody size
    private int waterChance = 8; //waterbody chance
//    private int waterLimit = 4; //waterbody limit

    private BufferedImage terrain, image;
    
    World(int width, int height) {
	this.width = width;
	this.height = height;
	terrain = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	rows = (width-(2*gridSize))/gridSize;
	cols = (height-(2*gridSize))/gridSize;
	map = new Entity[rows][cols];
	generateWater();
	drawTerrain();
	//TODO: add food
	//TODO: add souls
    }
    
    private void generateWater() { //Voronoi Diagram
	int[] centerX = new int[numCenters];
	int[] centerY = new int[numCenters];
	boolean[] isWater = new boolean[numCenters];
//	int numWater = 0;
	for(int i = 0; i < numCenters; i++) {
	    centerX[i] = Generator.RANDOM.nextInt(rows);
	    centerY[i] = Generator.RANDOM.nextInt(cols);
//	    if(numWater <= waterLimit){
		isWater[i] = Generator.randomBoolean(waterChance);
//		numWater++;
//	    }
	}
	int n = 0;
	Water water = new Water(); //all water are the same
	for (int x = 0; x < rows; x++) {
	    for (int y = 0; y < cols; y++) {
		n = 0;
		for (int i = 0; i < numCenters; i++) {
		    if (distance(centerX[i], x, centerY[i], y) < distance(centerX[n], x, centerY[n], y)) {
			n = i;
		    }
		}
		map[x][y] = isWater[n]? water:null;
	    }
	}
    }
    
    private double distance(int x1, int x2, int y1, int y2) {
	double d;
	d = Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
//	d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); // Euclidian
	return d;
    }

    private void drawTerrain(){
	int border = gridSize; //duplicated for readability
	Graphics g = terrain.getGraphics();
	
	//grass
	g.setColor(soilColor);
	g.fillRect(border, border, width-(2*border), height-(2*border));
	
	//water
	g.setColor(waterColor);
	for (int x = 0; x < rows; x++) {
	    for (int y = 0; y < cols; y++) {
		if(map[x][y] != null && map[x][y].getId() == 1){
		    g.fillRect((x+1)*gridSize, (y+1)*gridSize, gridSize+1, gridSize+1);
		}
	    }
	}
	
	//grid
	g.setColor(new Color(230, 230, 230, 90));
	for(int x=gridSize+border; x<width-border; x+=gridSize){
	    g.drawLine(x, border, x, height-border-1);
	}
	for(int y=border+gridSize; y<height-border; y+=gridSize){
	    g.drawLine(border, y, width-border-1, y);
	}
	
	//border
	g.setColor(borderColor);
	for(int x=1; x<width; x+=gridSize){
	    for(int y=1; y<height; y+=gridSize){
		if(x>1 && x<width-gridSize-1 && y>1 && y<height-gridSize-1) continue;
		g.fillRect(x, y, gridSize-2, gridSize-2);
	    }
	}

    }
    
    public BufferedImage drawImage(){
	Graphics g = image.getGraphics();
	g.drawImage(terrain, 0, 0, null); //if camera -> limit draw for visible here
	//TODO: draw food
	//TODO: draw souls
	return image;
    }
    
    public void update() {
	//TODO: update food; remove from map if degradated or eaten
	//TODO: update souls
    }

    public int getNumSouls(){ return soulList.size();}
    
}
