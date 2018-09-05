package br.unip.pandora.world;
import br.unip.pandora.Main;
import br.unip.pandora.life.Food;
import br.unip.pandora.life.Soul;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class World {
    
    private Color soilColor = new Color(87, 142, 96);
//    private Color gridColor = new Color(123, 174, 131);
    private int width, height, gridSize;
    private int rows, cols;
    private int[][] map; //TODO: use sparse matrix?
    private Food[] foodArray = new Food[20];
    private ArrayList<Soul> soulList = new ArrayList<>();
    
    //water
    private int numCenters = 40; //water distribution
    private int waterProbability = 10; //water amount

    private BufferedImage terrain, image;
    
    World(int width, int height, int gridSize) {
	this.width = width;
	this.height = height;
	this.gridSize = gridSize;
	image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	terrain = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	rows = (width-2*gridSize)/gridSize;
	cols = (height-2*gridSize)/gridSize;
	map = new int[rows][cols];
	generateWater();
	drawTerrain();
	//TODO: add food
	//TODO: add souls
    }
    
    private void generateWater() { //Voronoi Diagram
	int[] centerX = new int[numCenters];
	int[] centerY = new int[numCenters];
	boolean[] isWater = new boolean[numCenters];
	for(int i = 0; i < numCenters; i++) {
	    centerX[i] = Main.GENERATOR.nextInt(rows);
	    centerY[i] = Main.GENERATOR.nextInt(cols);
	    isWater[i] = Main.randomBoolean(waterProbability);
	}
	int n = 0;
	for (int x = 0; x < rows; x++) {
	    for (int y = 0; y < cols; y++) {
		n = 0;
		for (int i = 0; i < numCenters; i++) {
		    if (distance(centerX[i], x, centerY[i], y) < distance(centerX[n], x, centerY[n], y)) {
			n = i;
		    }
		}
		map[x][y] = isWater[n]? 1:0;
	    }
	}
    }
    
    private double distance(int x1, int x2, int y1, int y2) {
	double d;
	d = Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
	return d;
    }

    private void drawTerrain(){
	int border = gridSize; //duplicated for readability
	Graphics2D g = (Graphics2D) terrain.getGraphics();
	g.setColor(soilColor);
	g.fillRect(border, border, width-(2*border), height-(2*border));
	g.setColor(Color.BLUE);
	for (int x = 0; x < rows; x++) {
	    for (int y = 0; y < cols; y++) {
		if(map[x][y] == 1){
		    g.fillRect((x+1)*gridSize, (y+1)*gridSize, gridSize+1, gridSize+1);
		}
	    }
	}
	g.setColor(new Color(230, 230, 230, 90));
	for(int x=gridSize+border; x<width-border; x+=gridSize){
	    g.drawLine(x, border, x, height-border-1);
	}
	for(int y=border+gridSize; y<height-border; y+=gridSize){
	    g.drawLine(border, y, width-border-1, y);
	}
	g.setColor(Color.GRAY);
	for(int x=1; x<width; x+=gridSize){
	    for(int y=1; y<height; y+=gridSize){
		if(x>1 && x<width-gridSize-1 && y>1 && y<height-gridSize-1) continue;
		g.fillRect(x, y, gridSize-2, gridSize-2);
	    }
	}
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
