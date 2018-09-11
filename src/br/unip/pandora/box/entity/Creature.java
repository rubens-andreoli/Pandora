package br.unip.pandora.box.entity;

import java.util.ArrayList;

public abstract class Creature extends Entity {

    private ArrayList<Entity> body;

    public Creature(int id, int x, int y) {
	super(id, x, y);
    }
    
    public void interact(Entity e){
	if(((e.x+1 == this.x || e.x-1 == this.x) && e.y == this.y) ||
	((e.y+1 == this.y || e.y-1 == this.y) && e.x == this.x)) e.use(this);
    }
    
}
