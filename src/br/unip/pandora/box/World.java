package br.unip.pandora.box;

import br.unip.pandora.box.entity.Creature;
import br.unip.pandora.box.entity.Entity;
import br.unip.pandora.box.entity.Water;
import br.unip.pandora.engine.Generator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

public class World {
    
    //terrain generation
    private BufferedImage terrain;
    private int terrainWidth, terrainHeight;
    private static final Color GRASS_COLOR = new Color(87, 142, 96);
    private int numCenters = 120; //terrain patch sizes/number
    private int waterChance = 8; //waterbody chance
//  private int waterLimit = 4; //waterbody limit
    private int dirtChance = 40; //dirt chance
    private static final Color DIRT_COLOR = new Color(145, 118, 83);
    private HashSet<Point> dirtPoints;
    private boolean drawGrid = true;
    private static final Color GRID_COLOR = new Color(230, 230, 230, 90);
    private boolean drawNumbers = false;
    private static final Font NUM_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 8);
    private static final Color NUM_COLOR = Color.YELLOW;
    
    //map
    private BufferedImage map;
    private int gridSize = 6;
    private int rows;
    private int cols;
    private Entity[][] entityMap;
    private ArrayList<Creature> souls; 
    
    //minimap
    private BufferedImage minimap;
    private int minimapWidth, minimapHeight;
    private float minimapXScale, minimapYScale;
    
    public World(int rows, int cols, int minimapWidth, int minimapHeight) {
	this.rows = rows;
	this.cols = cols;
	this.minimapWidth = minimapWidth;
	this.minimapHeight = minimapHeight;
	
	terrainWidth = gridSize*cols+1;
	terrainHeight = gridSize*rows+1;
	minimapXScale = minimapWidth/(float)terrainWidth;
	minimapYScale = minimapHeight/(float)terrainHeight;

	entityMap = new Entity[cols][rows];
	terrain = new BufferedImage(terrainWidth, terrainHeight, BufferedImage.TYPE_INT_RGB);
	minimap = new BufferedImage(minimapWidth, minimapHeight, BufferedImage.TYPE_INT_RGB);
	souls = new ArrayList<>();
	dirtPoints = new HashSet<>();
	
	generateTerrain(); 
	drawTerrain();
	//TODO: add food
	//TODO: add souls
    }
    
    public World(int worldSize, int minimapSize){
	this(worldSize, worldSize, minimapSize, minimapSize);
    }

    //<editor-fold defaultstate="collapsed" desc="TERRAIN GENERATION">
    private void generateTerrain() { //Voronoi Diagram
	int[] centerX = new int[numCenters];
	int[] centerY = new int[numCenters];
	boolean[] isWater = new boolean[numCenters];
	boolean[] isDirt = new boolean[numCenters];
//	int numWater = 0;
	for(int i = 0; i < numCenters; i++) {
	    centerX[i] = Generator.RANDOM.nextInt(cols);
	    centerY[i] = Generator.RANDOM.nextInt(rows);
//	    if(numWater <= waterLimit){
		boolean water = Generator.randomBoolean(waterChance);
		isWater[i] = water;
//		numWater++;
//	    }
	    if(!water) isDirt[i] = Generator.randomBoolean(dirtChance);
	}
	int n = 0;
	Water water = new Water(); //all water are the same
	for (int x = 0; x < cols; x++) {
	    for (int y = 0; y < rows; y++) {
		n = 0;
		for (int i = 0; i < numCenters; i++) {
		    if (distance(centerX[i], x, centerY[i], y) < distance(centerX[n], x, centerY[n], y)) {
			n = i;
		    }
		}
		if(isWater[n]){
		    entityMap[x][y] = water;
		}else if(isDirt[n]){
		    dirtPoints.add(new Point(x, y));
		}
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
	Graphics gT = terrain.getGraphics();
	Graphics gM = minimap.getGraphics();

	//grass
	gT.setColor(GRASS_COLOR);
	gT.fillRect(0, 0, terrainWidth, terrainHeight);
	
	//water
	gT.setColor(Water.COLOR);
	for (int x = 0; x < cols; x++) {
	    for (int y = 0; y < rows; y++) {
		if(entityMap[x][y] != null && entityMap[x][y].getId() == 1){
		    gT.fillRect(x*gridSize, y*gridSize, gridSize+1, gridSize+1);
		}
	    }
	}
	
	//dirt
	gT.setColor(DIRT_COLOR);
	for(Point p : dirtPoints){
	    gT.fillRect(p.x*gridSize, p.y*gridSize, gridSize+1, gridSize+1);
	}
	
	gM.drawImage(terrain, 0, 0, minimapWidth, minimapHeight, null);
	
	//grid
	if(drawGrid){
	    gT.setColor(GRID_COLOR);
    	    gT.setFont(NUM_FONT);
	    for(int x=0; x<=terrainWidth; x+=gridSize){
		gT.drawLine(x, 0, x, terrainHeight);
	    }
    	    int rowNum = 1;
	    for(int y=0; y<terrainHeight; y+=gridSize){ //draw one after -> needed for line, not for number
		if(drawNumbers){
		    gT.setColor(NUM_COLOR);
		    gT.drawString((rowNum++)+"", 2, y+gridSize);
		    gT.setColor(GRID_COLOR);
		}
		gT.drawLine(0, y, terrainWidth, y);
	    }
	}
    }
    //</editor-fold>
 
    public void update() {
	//TODO: update food; remove from map if degradated or eaten
	//TODO: update souls
    }

    public BufferedImage drawImage(float xOffset, float yOffset, int width, int height) {
	if(map == null) map = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	Graphics g = map.getGraphics();
	g.drawImage(terrain.getSubimage((int)xOffset, (int)yOffset, width, height), 0, 0, null);
	//TODO: draw food in area
	//TODO: draw souls in area
//	g.dispose();
	return map;
    }

    public Entity getEntity(int x, int y){
	return entityMap[x][y]; //TODO: or entity close
    }
    
    public int getNumSouls(){ return souls.size();}
    public float getMinimapXScale(){return minimapXScale;}
    public float getMinimapYScale(){return minimapYScale;}
    public int getTerrainWidth() {return terrainWidth;}
    public int getTerrainHeight() {return terrainHeight;}
    public BufferedImage getMinimap(){return minimap;}

}
