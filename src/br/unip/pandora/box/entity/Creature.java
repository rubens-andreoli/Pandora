package br.unip.pandora.box.entity;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Creature extends Entity {
    
    public static final byte ID = 3;
    
    private Point head;
    private ArrayList<Point> snakeParts;
    private int tailLenght = 5;
    
    //status
    public enum Status {
	OK, //not hungry; not thirst; nothing in front
	HUNGRY, //hungry; not thirst; nothing in front
	CAN_EAT, //not hungry; any; food in front
	MUST_EAT, //hungry; any; food in front
	THIRST, //not hungry; thirst; nothing in front
	CAN_DRINK, //any; not thirst; water in front
	MUST_DRINK, //any; thirst; water in front
	NEEDY; //hungry; thirst; nothing in front
    }
    
    private float thirst;
    private final float thirstMax = 10;
    private final float thirstRate = (float) 0.1;
    private float hunger;
    private final float hungerMax = 10;
    private final float hungerRate = (float) 0.1;
    
    //movement
    public static final int UP=0, DOWN=1, LEFT=2, RIGHT=3;
    private int direction = DOWN;
    private int targetX, targetY;
    private boolean targeting;

    public Creature(int x, int y) {
	super(ID, x, y, Color.YELLOW);
	snakeParts = new ArrayList<>();
	head = new Point(x, y);
	for(int i=0; i < tailLenght; i++){
	    snakeParts.add(new Point(x, y));
	}
    }

    public void update() {
	if(thirst < thirstMax) thirst+=thirstRate;
	if(hunger < hungerMax) hunger+=hungerRate;
	targeting = targetX != x || targetY != y;
	if(targeting) move();
    }
    
    private void move() {
	if(targetX > x) x++;
	else if(targetX < x) x--;
	if(targetY > y) y++;
	else if(targetY < y) y--;
    }
    
    public void interact(Entity e, Entity[][] map) {
	switch(e.id){
	    case 1: thirst = 0;
		break;
	    case 2: hunger = 0;
		map[e.x][e.y] = null;
		break;
	}
    }
    
    public void target(int id, Entity[][] map){
	double minDistance = -1;
	for(int x=0; x<map.length; x++){
	    for(int y=0; y<map[x].length; y++){
		Entity e = map[x][y];
		if(e != null && e.id == id){
		    double distance = Math.hypot(this.x-e.x, this.y-e.y);
		    if(distance < minDistance || minDistance == -1){
			minDistance = distance;
			targetX = e.x;
			targetY = e.y;
		    }
		}
	    }
	}
    }
    
    public Status getStatus(Entity[][] map){
	Status s = Status.OK;
	if(thirst >= thirstMax/2) s = Status.THIRST;
	if(hunger >= hungerMax/2){
	    if (s == Status.OK) s = Status.HUNGRY;
	    else s = Status.NEEDY;
	}
	switch(map[x][y].id){
	    case 1: if(s == Status.THIRST) s = Status.MUST_DRINK;
		    else s = Status.CAN_DRINK;
		break;
	    case 2: if(s == Status.HUNGRY) s = Status.MUST_EAT;
		    else s = Status.CAN_EAT;
		break;
	}
	return s;
    }

    public float getThirst() {return thirst;}
    public float getHunger() {return hunger;}
    
}
