package br.unip.pandora.box.entity;

import br.unip.pandora.engine.Generator;
import java.awt.Color;

public class Water extends Entity {
  
    public static final int ID = 1;
    public static final Color COLOR = Color.BLUE; //because water is rendered with terrain
    
    public Water(){
	super(ID, 0, 0);
    }
    
    @Override
    public void update(Entity[][] map) {}
    
    @Override
    public void use(Creature c) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Color getColor() {
	return COLOR;
    }
    
    //<editor-fold defaultstate="collapsed" desc="WATER-GENERATION">
    private static int numCenters = 60; //waterbody size
    private static int waterChance = 8; //waterbody chance
//    private static int waterLimit = 4; //waterbody limit
    
    public static void generateWater(Entity[][] map, int rows, int cols) { //Voronoi Diagram
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
    
}
