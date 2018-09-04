package br.unip.pandora.life;

import br.unip.pandora.life.movement_rules.Movement;
import java.awt.Point;

public class Body {
    
    private int age;
    private int health = 100;
   
    private int size;
    private final int maxSize;
    private int speed;
    private final int maxSpeed;
    private int strenght;
    private final int maxStrenght;    
    
    private int intelligence; //if can learn and how much at a time
    private final int maxIntelligence;
    private int reasoning; //interval learning is tested
    private final int maxReasoning;
    private int perceverance; //interval action change is tested
    private final int maxPerceverance;
    
    private int hunger;
    private int hungerThreashold; //interval of seeking for food
    
    private Movement movementType;
    private final int viewField;
    
    private Point head;
    private Point[] body;
    private final boolean isFemale;
    private boolean isPregnant;
    private long pregnancyStart;
    private static int pregnancyDuration;
    private boolean isDead;
    
    public Body() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    Body(Body gM, Body gF) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
