package br.unip.pandora.engine;

import java.util.Random;

public class Generator {
    
    private Generator(){};
    
    public static final Random RANDOM = new Random();
    
    public static int randomBetween(int low, int high){
	return RANDOM.nextInt(high-low)+low;
    }
    
    public static int randomIn(int[] values){
	return values[RANDOM.nextInt(values.length)];
    }

    public static boolean randomBoolean(int probability){
	return RANDOM.nextFloat() <= probability/100.0;
    }
    
    public static boolean randomBoolean(double probability){
	return RANDOM.nextFloat() <= probability;
    }
    
}
