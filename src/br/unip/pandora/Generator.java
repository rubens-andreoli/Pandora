package br.unip.pandora;

import java.util.Random;

public class Generator {
    
    private Generator(){};
    
    public static final Random RANDOM = new Random();
    
    public static int randomBetween(int low, int high){
	return RANDOM.nextInt(high-low)+low;
    }

    public static boolean randomBoolean(int probability) {
	return RANDOM.nextFloat() <= probability/100.0? true:false;
    }
    
}
