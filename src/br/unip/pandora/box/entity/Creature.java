package br.unip.pandora.box.entity;

import br.unip.pandora.engine.SoundManager;
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
    private float thirstRate = 0.1F;
    private float hunger;
    private float hungerMax = 12;
    private float hungerRate = 0.075F;
    private State currentState = State.OK;
    //<editor-fold defaultstate="collapsed" desc="State">
    public enum State {
	OK, //not hungry; not thirst; nothing to interact
	HUNGRY, //hungry; not thirst; nothing to interact
	CAN_EAT, //not hungry; any; food to interact
	MUST_EAT, //hungry; any; food to interact
	THIRST, //not hungry; thirst; nothing to interact
	CAN_DRINK, //any; not thirst; water to interact
	MUST_DRINK, //any; thirst; water to interact
	NEEDY; //hungry; thirst; nothing to interact
    }
    //</editor-fold>

    //actions
    private Entity[][] map;
    private int targetX, targetY;
    private float distance;
    private boolean moving;
    private Action currentAction = Action.NOTHING; 
    private int fatigue;
    private int fatigueMax = 15; //stop moving after x updates
    private SoundManager sound;
    public static final String GRASS_SOUND = "grass";
    public static final String WATER_SOUND = "swim";
    public static final String DRINK_SOUND = "drink";
    public static final String EAT_SOUND = "food";
    //<editor-fold defaultstate="collapsed" desc="Action">
    public enum Action {
	NOTHING, 
	SEARCH_WATER, //target(1)
	SEARCH_FOOD, //target(2)
	INTERACT; //interact()
    }
    //</editor-fold>

    public Creature(int x, int y, Entity[][] map, SoundManager sound){
	super(ID, x, y, Color.YELLOW);
	this.map = map;
	this.sound = sound;
	sound.load("/sounds/grass.mp3", GRASS_SOUND);
	sound.load("/sounds/swim.mp3", WATER_SOUND);
	sound.load("/sounds/food.mp3", EAT_SOUND);
	sound.load("/sounds/drink.mp3", DRINK_SOUND);
	tail = new ArrayList<>();
	for(int i=0; i < tailLenght; i++){
	    tail.add(new Point(x, y));
	}
    }

    public void update(){
	if(thirst < thirstMax) thirst+=thirstRate;
	if(hunger < hungerMax) hunger+=hungerRate;
	if(thirst > thirstMax) thirst = thirstMax;
	if(hunger > hungerMax) hunger = hungerMax;
	
	if(hunger >= hungerMax && life > 0) life-=demageRate;
	if(thirst >= thirstMax && life > 0) life-=demageRate;
	if(life < 0) life = 0;
	if(life > lifeMax) life = lifeMax;

	fatigue++;
	moving = targetX != x || targetY != y && fatigue <= fatigueMax;
	if(moving){
	    move();
	    if(map[x][y] == null){
		sound.stop(WATER_SOUND);
		sound.replay(GRASS_SOUND);
	    }else if(map[x][y].id == Water.ID){
		sound.stop(GRASS_SOUND);
		sound.replay(WATER_SOUND);
	    }
	}else{
	    sound.stop(GRASS_SOUND);
	    sound.stop(WATER_SOUND);
	}

	setState();
    }
    
    private void move(){
	tail.add(new Point(x, y));
	if(targetX > x) x++;
	else if(targetX < x)x--;
	if(targetY > y)y++;
	else if(targetY < y)y--;
	tail.remove(0);
    }
    
    public void doAction(Action a){
	currentAction = a;
	switch(a){
	    case SEARCH_WATER:
		fatigue = 0;
		target(Water.ID);
		break;
	    case SEARCH_FOOD:
		fatigue = 0;
		target(Food.ID);
		break;
	    case INTERACT:
		interact();
		break;
	}
    }
    
    private void interact(){
	Entity e = map[x][y];
	if(e == null) return;
	
	switch(e.id){
	    case Water.ID: 
		thirst = 0;
		sound.play(DRINK_SOUND);
		break;
	    case Food.ID: 
		if(life < lifeMax){
		    life++;
		}
		sound.play(EAT_SOUND);
		hunger = 0; 
		e.remove();
		break;
	}
    }
    
    private void target(int id){
	double minD = -1;
	for(int x=0; x<map.length; x++){
	    for(int y=0; y<map[x].length; y++){
		Entity e = map[x][y];
		if(e != null && e.id == id){
		    double d = Math.hypot(this.x-x, this.y-y); //all water is x=0/y=0 so get x,y from matriz, not entity
		    if(d < minD || minD == -1){
			minD = d;
			targetX = x;
			targetY = y;
			distance = (float) d;
		    }
		}
	    }
	}
    }
    
    private void setState(){
	State s = State.OK;
	if(thirst >= thirstMax/2) s = State.THIRST;
	if(hunger >= hungerMax/2){
	    if (s == State.OK) s = State.HUNGRY;
	    else s = State.NEEDY;
	}
	if(map[x][y] != null){
	    switch(map[x][y].id){
		case 1: if(s == State.THIRST) s = State.MUST_DRINK;
			else s = State.CAN_DRINK;
		    break;
		case 2: if(s == State.HUNGRY) s = State.MUST_EAT;
			else s = State.CAN_EAT;
		    break;
	    }
	}
	currentState = s;
    }
    
    public int getReward(Action a){
	if(life == 0) return -1;
	int r = 0;
	switch(a){
	    case NOTHING:
		if(currentState == State.OK) r = (int) life;
		break;
	    case INTERACT:
		Entity e = map[x][y];
		if(e == null) r = -1;
		else if(e.id == Water.ID) r = (int) thirst;
		else if(e.id == Food.ID) r = (int) hunger;
		break;
	}
	return r;
    }
    
    public State getNextState(Action a){
	State s = currentState;
	switch(a){
	    case SEARCH_WATER:
		if(distance <= fatigueMax){
		    if(currentState == State.THIRST || currentState == State.NEEDY) s = State.MUST_DRINK;
		    else s = State.CAN_DRINK;
		}
		break;
	    case SEARCH_FOOD:
		if(distance <= fatigueMax){
		    if(currentState == State.HUNGRY || currentState == State.NEEDY) s = State.MUST_EAT;
		    else s = State.CAN_EAT;
		}
		break;
	    case INTERACT:
		if(currentState == State.MUST_DRINK) s = State.CAN_DRINK;
		else if(currentState == State.MUST_EAT){
		    if(thirst > thirstMax/2) s = State.THIRST;
		    else s = State.OK;
		}
		break;
	}
	return s;
    }
    
    public float getLife(){return life;}
    public float getLifeMax(){return lifeMax;}
    public float getThirst(){return thirst;}
    public float getThirstMax(){return thirstMax;}
    public float getHunger(){return hunger;}
    public float getHungerMax(){return hungerMax;} 
    public ArrayList<Point> getTail(){return tail;}
    public boolean isMoving(){return moving;}
    public State getCurrentState(){return currentState;}
    public Action getCurrentAction(){return currentAction;}
    public int getActionCount(){return Action.values().length;}
    public int getStateCount(){return State.values().length;}
     
}
