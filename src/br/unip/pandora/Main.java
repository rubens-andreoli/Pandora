package br.unip.pandora;

import java.util.Random;

public class Main {
    
    public static final String NAME = "PANDORA";
    public static final Random GENERATOR = new Random();
    
    public static void main(String[] args) {
	Thread t = new Thread(new Loop(new Renderer()));
	t.setName(NAME);
	t.start();
    }
    
    public static int randomBetween(int low, int high){
	return GENERATOR.nextInt(high-low)+low;
    }

    public static boolean randomBoolean(int probability) {
	return GENERATOR.nextInt(10)+1 <= probability/10? true:false;
    }
    
}
