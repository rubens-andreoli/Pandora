package br.unip.pandora.box.entity;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Creature extends Entity {
    
    public static final byte ID = 3;

    //body
    private ArrayList<Point> tail;
    private byte tailLenght = 5;
    
    //status
    private float life = 12;
    private float lifeMax = 12;
    private float demageRate = 0.05F;
    private float thirst;
    private float thirstMax = 12;
    private float thirstRate = 0.15F;
    private float hunger;
    private float hungerMax = 12;
    private float hungerRate = 0.1F;
    //<editor-fold defaultstate="collapsed" desc="Status">
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
    //</editor-fold>

    //movement
    private int targetX, targetY;
    private boolean searching;
    private Entity[][] map;
    
    //actions: NOTHING, SEARCH_WATER target(1), SEARCH_FOOD target(2), CONSUME consume(e)

    public Creature(int x, int y, Entity[][] map) {
	super(ID, x, y, Color.YELLOW);
	this.map = map;
	tail = new ArrayList<>();
	for(int i=0; i < tailLenght; i++){
	    tail.add(new Point(x, y));
	}
    }

    public void update() {
	if(thirst < thirstMax) thirst+=thirstRate;
	if(hunger < hungerMax) hunger+=hungerRate;
	if(hunger >= hungerMax && life > 0) life-=demageRate;
	if(thirst >= thirstMax && life > 0) life-=demageRate;
	if(thirst > thirstMax) thirst = thirstMax;
	if(hunger > hungerMax) hunger = hungerMax;
	if(life < 0) life = 0;
	if(life < lifeMax && hunger <= hungerMax/2 && thirst <= thirstMax/2){
	    life++;
	    hunger++;
	    thirst++;
	}
	searching = targetX != x || targetY != y;
	move();
    }
    
    private void move() {
	if(searching){
	    tail.add(new Point(x, y));
	    if(targetX > x) x++;
	    else if(targetX < x)x--;
	    if(targetY > y)y++;
	    else if(targetY < y)y--;
	    tail.remove(0);
	}
    }
    
    public int consume(Entity e) {
	if(e == null) return 0;
	int delta = 0;
	switch(e.id){
	    case 1: 
		delta = (int) thirst;
		thirst = 0;
		break;
	    case 2: 
		delta = (int) hunger;
		hunger = 0;
		break;
	    default:
		delta = 0;
	}
	return delta;
    }
    
    public void target(int id){
	double minDistance = -1;
	for(int x=0; x<map.length; x++){
	    for(int y=0; y<map[x].length; y++){
		Entity e = map[x][y];
		if(e != null && e.id == id){
		    double distance = Math.hypot(this.x-x, this.y-y); //all water is x=0/y=0 so get x,y from matriz, not entity
		    if(distance < minDistance || minDistance == -1){
			minDistance = distance;
			targetX = x;
			targetY = y;
		    }
		}
	    }
	}
    }
    
    public Status getStatus(){ //TODO: dying?
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
    public float getLife() {return life;}
    public float getLifeMax() {return lifeMax;}
    public float getThirstMax() {return thirstMax;}
    public float getHungerMax() {return hungerMax;} 
    public ArrayList<Point> getTail() {return tail;}
    public boolean isSearching() {return searching;}
    
}
