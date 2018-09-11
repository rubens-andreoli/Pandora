package br.unip.pandora.box.entity;

import java.awt.Color;

public class Rabbit extends Creature {

    public Rabbit(int id, int x, int y) {
	super(id, x, y);
    }

    @Override
    public void update(Entity[][] map) {
	//
    }

    @Override
    public Color getColor() {
	return null;
    }

    @Override
    public void use(Creature c) {
	//
    }
    
}
