package br.unip.pandora.box;

import br.unip.pandora.box.entity.Entity;
import br.unip.pandora.box.entity.Water;
import br.unip.pandora.engine.Generator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class World {
    
    //ui
    private Color soilColor = new Color(87, 142, 96);
    private Color gridColor = new Color(230, 230, 230, 90);
    private int width, height;
    private Font font = new Font(Font.MONOSPACED, Font.PLAIN, 8);
    
    //water generation
    private int numCenters = 120; //waterbody size
    private int waterChance = 8; //waterbody chance
//    private int waterLimit = 4; //waterbody limit
    
    //map
    private int gridSize = 6;
    private int rows = 200;
    private int cols = 200;
    private int terrainWidth, terrainHeight;
    private Entity[][] map;

    private BufferedImage terrain, image;
    
    World(int width, int height) {
	this.width = width;
	this.height = height;

	terrainWidth = gridSize*rows+1;
	terrainHeight = gridSize*cols+1;
	terrain = new BufferedImage(terrainWidth, terrainHeight, BufferedImage.TYPE_INT_RGB);
	image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	map = new Entity[rows][cols];
	
	generateWater(); 
	//TODO: generate trees?
	drawTerrain();
	//TODO: add food
	//TODO: add souls
    }
    
    //<editor-fold defaultstate="collapsed" desc="WATER GENERATION">
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
    
    private static double distance(int x1, int x2, int y1, int y2) {
	double d;
	d = Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
//	d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); // Euclidian
	return d;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TERRAIN IMAGE">
    private void drawTerrain(){
	Graphics g = terrain.getGraphics();
	
	//grass
	g.setColor(soilColor);
	g.fillRect(0, 0, terrainWidth, terrainHeight);
	
	//water
	g.setColor(Water.COLOR);
	for (int x = 0; x < rows; x++) {
	    for (int y = 0; y < cols; y++) {
		if(map[x][y] != null && map[x][y].getId() == 1){
		    g.fillRect(x*gridSize, y*gridSize, gridSize+1, gridSize+1);
		}
	    }
	}
	
	//grid
	g.setColor(gridColor);
	g.setFont(font);
	for(int x=0; x<=terrainWidth; x+=gridSize){
	    g.drawLine(x, 0, x, terrainHeight);
	}
//	int rowNum = 1;
	for(int y=0; y<terrainHeight; y+=gridSize){ //FIX:drawing one more -> needed for line, not for number
//	    g.setColor(Color.YELLOW);
//	    g.drawString((rowNum++)+"", 2, y+gridSize);
//	    g.setColor(gridColor);
	    g.drawLine(0, y, terrainWidth, y);
	}
    }
    //</editor-fold>
 
    public void update() {
	//TODO: update food; remove from map if degradated or eaten
	//TODO: update souls
    }

    public BufferedImage drawImage(float xOffset, float yOffset) {
	Graphics g = image.getGraphics();
	g.drawImage(terrain.getSubimage((int)xOffset, (int)yOffset, width, height), 0, 0, null);
	//TODO: draw food
	//TODO: draw souls
	return image;
    }

    public Entity getEntity(int x, int y){
    
	return null;
    }
    
    public int getNumSouls(){ return 0;}

    public int getTerrainWidth() {
	return terrainWidth;
    }

    public int getTerrainHeight() {
	return terrainHeight;
    }

}
