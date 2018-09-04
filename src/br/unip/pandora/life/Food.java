package br.unip.pandora.life;

import br.unip.pandora.Main;
import java.awt.Point;

public class Food {
    
    private Point location;
    private int degradation;
    private int saturation;
    private boolean isDecomposed;
    private static final int DEGRADATION_RATE = 1;
    private static final int MIN_SATURATION = 10;
    private static final int MAX_SATURATION = 50;

    public Food(Point location) {
	this.location = location;
	this.saturation = Main.randomBetween(MIN_SATURATION, MAX_SATURATION);
    }
    
    public void update(){
	if(isDecomposed) return;
	degradation+=DEGRADATION_RATE;
	if(saturation-degradation <=0 ) isDecomposed = true;
    }
    
    public int getNutrition(){return saturation-degradation;}
    public Point getLocation() {return location;}
    public boolean isDecomposed() {return isDecomposed;}

}
