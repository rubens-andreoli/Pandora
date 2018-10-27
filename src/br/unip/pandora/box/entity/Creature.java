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
    private State crtState = State.OK;
    //<editor-fold defaultstate="collapsed" desc="State">
    public enum State {
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

    //actions
    private int targetX, targetY;
    private boolean moving;
    private Entity[][] map;
    private Action crtAction = Action.NOTHING;
    //<editor-fold defaultstate="collapsed" desc="Action">
    public enum Action {
	NOTHING, 
	SEARCH_WATER, //target(1)
	SEARCH_FOOD, //target(2)
	INTERACT; //interact()
    }
    //</editor-fold>

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
	if(thirst > thirstMax) thirst = thirstMax;
	if(hunger > hungerMax) hunger = hungerMax;
	
	if(hunger >= hungerMax && life > 0) life-=demageRate;
	if(thirst >= thirstMax && life > 0) life-=demageRate;
	if(life < 0) life = 0;
	if(life > lifeMax) life = lifeMax;

	moving = targetX != x || targetY != y;
	if(moving) move();
	
	crtState = getStatus();
    }
    
    private void move() {
	tail.add(new Point(x, y));
	if(targetX > x) x++;
	else if(targetX < x)x--;
	if(targetY > y)y++;
	else if(targetY < y)y--;
	tail.remove(0);
    }
    
    public int doAction(Action a){
	int ret = -1;
	crtAction = a;
	switch(a){
	    case SEARCH_WATER:
		target(1);
		break;
	    case SEARCH_FOOD:
		target(2);
		break;
	    case INTERACT:
		ret = interact();
		break;
	}
	return ret;
    }
    
    private int interact() {
	Entity e = map[x][y];
	if(e == null) return 0;
	
	int delta = 0;
	switch(e.id){
	    case 1: 
		delta = (int) thirst;
		thirst = 0;
		break;
	    case 2: 
		if(life < lifeMax){
		    life++;
		}
		delta = (int) hunger;
		hunger = 0; 
		e.remove();
		break;
	    default:
		delta = 0;
	}
	return delta;
    }
    
    private void target(int id){
	//if not targetting
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
    
    public State getStatus(){ //TODO: dying?
	State s = State.OK;
	if(thirst >= thirstMax/2) s = State.THIRST;
	if(hunger >= hungerMax/2){
	    if (s == State.OK) s = State.HUNGRY;
	    else s = State.NEEDY;
	}
	if(map[x][y] == null) return s;
	switch(map[x][y].id){
	    case 1: if(s == State.THIRST) s = State.MUST_DRINK;
		    else s = State.CAN_DRINK;
		break;
	    case 2: if(s == State.HUNGRY) s = State.MUST_EAT;
		    else s = State.CAN_EAT;
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
    public boolean isMoving() {return moving;}
    public State getCurrentState() {return crtState;}
    public Action getCurrentAction() {return crtAction;}
    public int getActionCount(){return Action.values().length;}
    public int getStateCount(){return State.values().length;}
     
}
