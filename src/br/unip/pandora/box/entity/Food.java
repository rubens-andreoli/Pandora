package br.unip.pandora.box.entity;

import br.unip.pandora.engine.Generator;
import java.awt.Color;

public class Food extends Entity {

    public static final int ID = 2;
    
    private static int degradationRate = 1;
    private static int min_saturation = 10;
    private static int max_saturation = 50;
    
    private Color[] colors;

    public Food(int x, int y) {
	super(ID, x, y);
	colors = new Color[]{
	    new Color(245, 220, 10),
	    new Color(245, 180, 10),
	    new Color(245, 145, 10)
	};
	metadata = Generator.randomBetween(min_saturation, max_saturation);
    }
    
    @Override
    public void update(Entity[][] map){
	if(metadata>0) metadata--;
	if(metadata == 0)
	    map[x][y] = null;
    }

    @Override
    public Color getColor() {
	if(metadata >= 30) return colors[0]; //TODO: color.darker()?
	else if(metadata >= 10) return colors[1];
	else return colors[2];
    }

    @Override
    public void use(Creature c) {
	//
    }

}
