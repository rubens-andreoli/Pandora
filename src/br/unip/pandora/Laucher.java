package br.unip.pandora;

import br.unip.pandora.world.Box;

public class Laucher {

    public static void main(String[] args) {
	Game game = new Box("PANDORA", 640, 480);
	Display display = new Display("PANDORA'S BOX", 2);
	new Loop(game, display).start();
    } 
    
}
