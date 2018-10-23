package br.unip.pandora.box.entity;

import java.awt.Color;

public class Food extends Entity {

    public static final byte ID = 2;

    public Food(int x, int y) {
	super(ID, x, y, Color.RED);
    }
    
}
