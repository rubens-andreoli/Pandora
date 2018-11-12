package br.unip.pandora.box;

import br.unip.pandora.box.ai.QBot;
import br.unip.pandora.box.entity.Creature;
import br.unip.pandora.box.entity.Entity;
import br.unip.pandora.box.entity.Food;
import br.unip.pandora.box.entity.Water;
import br.unip.pandora.engine.Generator;
import br.unip.pandora.engine.SoundManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Properties;

public class World {
    
    //terrain generation
    private BufferedImage terrain;
    private int gridSize = 6;
    private int terrainWidth, terrainHeight;
    private Color grassColor = new Color(87, 142, 96);
    private int numCenters = 120; //terrain patch sizes/number
    private int waterChance = 8; //waterbody chance
    private int dirtChance = 40; //dirt chance
    private Color dirtColor = new Color(145, 118, 83);
    private HashSet<Point> dirtPoints;
    private HashSet<Point> waterPoints;
    private boolean drawGrid = true;
    private Color gridColor = new Color(230, 230, 230, 90);
    private boolean drawNumbers = false;
    private Font numFont = new Font(Font.MONOSPACED, Font.PLAIN, 8);
    private Color numColor = Color.YELLOW;
    
    //minimap
    private BufferedImage minimap;
    private int minimapWidth, minimapHeight;
    private float minimapXScale, minimapYScale;
    
    //drawmap
    private BufferedImage drawMap;
    private int drawWidth, drawHeight;

    //entity control
    private int rows; //77
    private int cols;
    private Entity[][] entityMap;
    private Creature creature;
    private QBot qBot;
    private int foodLimit = 4;
    private HashSet<Food> foodSet;
    
    public World(Properties p, int drawWidth, int drawHeight, int minimapWidth, int minimapHeight, SoundManager sound) {
	this.drawWidth = drawWidth;
	this.drawHeight = drawHeight;
	this.minimapWidth = minimapWidth;
	this.minimapHeight = minimapHeight;
	
	rows = Integer.parseInt((String)p.get("worldRows"));
	cols = Integer.parseInt((String)p.get("worldCols"));
	
	terrainWidth = gridSize*cols+1;
	terrainHeight = gridSize*rows+1;
	if(Math.min(terrainWidth, terrainHeight) < drawWidth){
	    gridSize = (int)Math.ceil(drawWidth/Math.min((float)rows, cols)) ;
	    terrainWidth = gridSize*cols+1;
	    terrainHeight = gridSize*rows+1;
	}
	minimapXScale = minimapWidth/(float)terrainWidth;
	minimapYScale = minimapHeight/(float)terrainHeight;

	entityMap = new Entity[cols][rows];
	terrain = new BufferedImage(terrainWidth, terrainHeight, BufferedImage.TYPE_INT_RGB);
	drawMap = new BufferedImage(drawWidth, drawHeight, BufferedImage.TYPE_INT_RGB);
	minimap = new BufferedImage(minimapWidth, minimapHeight, BufferedImage.TYPE_INT_RGB);
	dirtPoints = new HashSet<>();
	waterPoints = new HashSet<>();
	foodSet = new HashSet<>();
	
	generateTerrain(); 
	drawTerrain();
	generateFood();
	creature = new Creature(0, 0, entityMap, sound);
	qBot = new QBot(creature);
	
	dirtPoints = null; //only used for generate-draw
	waterPoints = null; //only used for draw
    }
    
    public World(Properties p, int drawSize, int minimapSize, SoundManager sound){
	this(p, drawSize, drawSize, minimapSize, minimapSize, sound);
    }

    //<editor-fold defaultstate="collapsed" desc="TERRAIN GENERATION">
    private void generateTerrain() { //Voronoi Diagram
	int[] centerX = new int[numCenters];
	int[] centerY = new int[numCenters];
	boolean[] isWater = new boolean[numCenters];
	boolean[] isDirt = new boolean[numCenters];
	for(int i = 0; i < numCenters; i++) {
	    centerX[i] = Generator.RANDOM.nextInt(cols);
	    centerY[i] = Generator.RANDOM.nextInt(rows);
	    boolean water = Generator.randomBoolean(waterChance);
	    isWater[i] = water;
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
		    waterPoints.add(new Point(x, y));
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
	for(Point p : waterPoints){
	    gT.fillRect(p.x*gridSize, p.y*gridSize, gridSize+1, gridSize+1);
	}
	
	//dirt
	gT.setColor(dirtColor);
	for(Point p : dirtPoints){
	    gT.fillRect(p.x*gridSize, p.y*gridSize, gridSize+1, gridSize+1);
	}
	
	gM.drawImage(terrain, 0, 0, minimapWidth, minimapHeight, null);
	
	//grid
	if(drawGrid){
	    gT.setColor(gridColor);
    	    gT.setFont(numFont);
	    for(int x=0; x<=terrainWidth; x+=gridSize){
		gT.drawLine(x, 0, x, terrainHeight);
	    }
    	    int rowNum = 1;
	    for(int y=0; y<terrainHeight; y+=gridSize){ //draw one after -> needed for line, not for number
		if(drawNumbers){
		    gT.setColor(numColor);
		    gT.drawString((rowNum++)+"", 2, y+gridSize);
		    gT.setColor(gridColor);
		}
		gT.drawLine(0, y, terrainWidth, y);
	    }
	}
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="FOOD GENERATION">
    private void generateFood(){
	int x, y;
	while(foodSet.size()<foodLimit){
	    do{
		x = Generator.RANDOM.nextInt(cols);
		y = Generator.RANDOM.nextInt(rows);
	    }while(entityMap[x][y] != null);
	    Food f = new Food(x, y);
	    entityMap[x][y] = f;
	    foodSet.add(f);
	}
    }
    //</editor-fold>
 
    public void update() {
	//remove consumed food
	Iterator<Food> itr = foodSet.iterator();
	while(itr.hasNext()){
	    Food f = itr.next();
	    if(f.shouldRemove()){
		entityMap[f.getX()][f.getY()] = null;
		itr.remove();
	    }
	}
	generateFood();
	
	//update creature bot
	qBot.update();
    }

    public BufferedImage drawImage(float xOffset, float yOffset) {
	Graphics g = drawMap.getGraphics();
	
	//draw terrain
	g.drawImage(terrain.getSubimage((int)xOffset, (int)yOffset, Math.min(drawWidth, terrainWidth), 
		Math.min(drawWidth, terrainHeight)), 0, 0, null);
	
	//draw visible entities
	int viewXStart = (int)(xOffset/gridSize);
	int viewYStart = (int)(yOffset/gridSize);
	int viewXEnd = (int) Math.min(viewXStart+(drawWidth/gridSize)+1, cols);
	int viewYEnd = (int) Math.min(viewYStart+(drawHeight/gridSize)+1, rows);
	for (int x=viewXStart; x < viewXEnd; x++) {
	    for (int y=viewYStart; y < viewYEnd; y++) {
		Entity e = entityMap[x][y];
		if(e != null && e.id != 1){
		    g.setColor(e.getColor());
		    g.fillRect(
			    (int)(x*gridSize-xOffset)+1, 
			    (int)(y*gridSize-yOffset)+1, 
			    gridSize-1, 
			    gridSize-1
		    );
		}
	    }
	}
	
	//draw creature
	g.setColor(creature.getColor());
	g.fillRect((int)(creature.getX()*gridSize-xOffset)+1, 
		(int)(creature.getY()*gridSize-yOffset)+1, 
		gridSize-1, 
		gridSize-1
	);
	ListIterator<Point> li = creature.getTail().listIterator(creature.getTail().size());
	while(li.hasPrevious()){
	    Point t = li.previous();
	    if(entityMap[t.x][t.y] != null && entityMap[t.x][t.y].id == 1) break;
	    g.fillRect((int)(t.x*gridSize-xOffset)+1, 
		    (int)(t.y*gridSize-yOffset)+1, 
		    gridSize-1, 
		    gridSize-1
	    );
	}

	return drawMap;
    }

    public Creature getCreature(){return creature;}
    public float getMinimapXScale(){return minimapXScale;}
    public float getMinimapYScale(){return minimapYScale;}
    public int getTerrainWidth(){return terrainWidth;}
    public int getTerrainHeight(){return terrainHeight;}
    public BufferedImage getMinimap(){return minimap;}
    public int getGridSize(){return gridSize;}
    public void saveQValues(){qBot.saveQTable();}
    
}
