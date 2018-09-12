package br.unip.pandora.box;

import br.unip.pandora.box.entity.Entity;
import br.unip.pandora.box.entity.Water;
import br.unip.pandora.engine.Generator;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class World {
    
    //ui
    private Color grassColor = new Color(87, 142, 96);
    private Color dirtColor = new Color(145, 118, 83);
    private Color gridColor = new Color(230, 230, 230, 90);
    private int width, height;
//    private Font font = new Font(Font.MONOSPACED, Font.PLAIN, 8);
    private boolean drawGrid = true;
    
    //map generation
    private int numCenters = 120; //patch sizes
    private int waterChance = 8; //waterbody chance
//    private int waterLimit = 4; //waterbody limit
    private int dirtChance = 40; //waterbody chance
    private HashSet<Point> dirtPoints;
    
    //map
    private int gridSize = 6;
    private int rows = 200;
    private int cols = 200;
    private int terrainWidth, terrainHeight;
    private Entity[][] entityMap;

    private BufferedImage terrain, minimap, map;
    private int miniWidth, miniHeight;
    private float miniScale;
    
    World(int width, int height, int minimapWidth) {
	this.width = width;
	this.height = height;
	
	terrainWidth = gridSize*rows+1;
	terrainHeight = gridSize*cols+1;
	miniScale = minimapWidth/(float)terrainWidth;
	miniWidth = (int)(terrainWidth*miniScale);
	miniHeight = (int)(terrainHeight*miniScale);

	entityMap = new Entity[rows][cols];
	terrain = new BufferedImage(terrainWidth, terrainHeight, BufferedImage.TYPE_INT_RGB);
	minimap = new BufferedImage(miniWidth, miniHeight, BufferedImage.TYPE_INT_RGB);
	map = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
	dirtPoints = new HashSet<>();
	generateMap(); 
	//TODO: generate trees?
	drawTerrain();
	//TODO: add food
	//TODO: add souls
    }
    
    public float getMinimapScale(){return miniScale;}
    
    //<editor-fold defaultstate="collapsed" desc="WATER GENERATION">
    private void generateMap() { //Voronoi Diagram
	int[] centerX = new int[numCenters];
	int[] centerY = new int[numCenters];
	boolean[] isWater = new boolean[numCenters];
	boolean[] isDirt = new boolean[numCenters];
//	int numWater = 0;
	for(int i = 0; i < numCenters; i++) {
	    centerX[i] = Generator.RANDOM.nextInt(rows);
	    centerY[i] = Generator.RANDOM.nextInt(cols);
//	    if(numWater <= waterLimit){
		boolean water = Generator.randomBoolean(waterChance);
		isWater[i] = water;
//		numWater++;
//	    }
	    if(!water) isDirt[i] = Generator.randomBoolean(dirtChance);
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
	gT.setColor(grassColor);
	gT.fillRect(0, 0, terrainWidth, terrainHeight);
	
	//water
	gT.setColor(Water.COLOR);
	for (int x = 0; x < rows; x++) {
	    for (int y = 0; y < cols; y++) {
		if(entityMap[x][y] != null && entityMap[x][y].getId() == 1){
		    gT.fillRect(x*gridSize, y*gridSize, gridSize+1, gridSize+1);
		}
	    }
	}
	
	//dirt
	gT.setColor(dirtColor);
	for(Point p : dirtPoints){
	    gT.fillRect(p.x*gridSize, p.y*gridSize, gridSize+1, gridSize+1);
	}
	
	gM.drawImage(terrain, 0, 0, miniWidth, miniHeight, null);
	
	//grid
	if(drawGrid){
	    gT.setColor(gridColor);
    //	    g.setFont(font);
	    for(int x=0; x<=terrainWidth; x+=gridSize){
		gT.drawLine(x, 0, x, terrainHeight);
	    }
    //	    int rowNum = 1;
	    for(int y=0; y<terrainHeight; y+=gridSize){ //FIX:drawing one more -> needed for line, not for number
    //		g.setColor(Color.YELLOW);
    //		g.drawString((rowNum++)+"", 2, y+gridSize);
    //		g.setColor(gridColor);
		gT.drawLine(0, y, terrainWidth, y);
	    }
	}
    }
    //</editor-fold>
 
    public void update() {
	//TODO: update food; remove from map if degradated or eaten
	//TODO: update souls
    }

    public BufferedImage drawImage(float xOffset, float yOffset/*, float zoom*/) {
	Graphics g = map.getGraphics();
//	g.drawImage(terrain.getSubimage((int)xOffset, (int)yOffset, (int)(width*zoom), (int)(height*zoom)), 0, 0, width, height, null);
	g.drawImage(terrain.getSubimage((int)xOffset, (int)yOffset, width, height), 0, 0, null);
	//TODO: draw food
	//TODO: draw souls
	return map;
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
    
    public BufferedImage getMinimap(){
	return minimap;
    }

}
