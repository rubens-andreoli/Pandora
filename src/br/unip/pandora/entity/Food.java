package br.unip.pandora.entity;

import br.unip.pandora.Main;

public class Food implements Entity{

    public static final int ID = 2;

    private int saturation;
    private static int degradationRate = 1;
    private static int min_saturation = 10;
    private static int max_saturation = 50;

    public Food() {
	this.saturation = Main.randomBetween(min_saturation, max_saturation);
    }
    
    public void update(){
	if(saturation>0) saturation--;
    }

    public @Override int getId() {return ID;}
    public @Override int getMetadata() {return saturation;} //nutrition

}
