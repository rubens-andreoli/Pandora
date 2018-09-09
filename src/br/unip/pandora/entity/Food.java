package br.unip.pandora.entity;

import br.unip.pandora.engine.Generator;
import java.awt.Graphics;

public class Food extends Entity{

    private static int degradationRate = 1;
    private static int min_saturation = 10;
    private static int max_saturation = 50;

    public Food(int x, int y) {
	super(2, x, y);
	metadata = Generator.randomBetween(min_saturation, max_saturation);
    }
    
    @Override
    public void update(){
	if(metadata>0) metadata--;
    }

    @Override
    public void draw(Graphics g) {
	
    }

}
