package br.unip.pandora.box.entity;

import br.unip.pandora.engine.Generator;
import java.awt.Color;

public class Food extends Entity {

    public static final int ID = 2;
    public static final int NORMAL = 1;
    public static final int POISON = 2;
    
    private static final float DEGRADETION_RATE = 1;
    private static final int MIN_SATURATION = 10;
    private static final int MAX_SATURATION = 50;
    
    private float saturation;
    
    private Color[] colors;

    public Food(int x, int y) {
	super(ID, x, y);
	colors = new Color[]{
	    new Color(245, 220, 10),
	    new Color(245, 180, 10),
	    new Color(245, 145, 10),
	    new Color(188, 10, 240),
	    new Color(188, 50, 240),
	    new Color(188, 100, 240)
	};

	saturation = Generator.randomBetween(MIN_SATURATION, MAX_SATURATION);
    }
    
    @Override
    public void update(Entity[][] map){
	if(saturation>0) saturation -= DEGRADETION_RATE;
    }

    @Override
    public Color getColor() {
	int i = 0;
	if(metadata == POISON) i = 3;
	
	if(metadata >= 30) return colors[i];
	else if(metadata >= 10) return colors[i+1];
	else return colors[i+2];
    }

    @Override
    public void use(Creature c) {
	//
    }

}
