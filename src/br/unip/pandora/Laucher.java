package br.unip.pandora;

public class Laucher {
    
    public static final String NAME = "PANDORA";

    public static void main(String[] args) {
	Thread t = new Thread(new Loop(new Display(NAME, 640, 480, 2)));
	t.setName(NAME);
	t.start();
    } 
    
}
