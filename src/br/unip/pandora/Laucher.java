package br.unip.pandora;

import br.unip.pandora.world.Box;

public class Laucher {

    public static void main(String[] args) {
	Game game = new Box();
	Display display = new Display(game);
	display.setTitle("PANDORA'S BOX");
	new Loop(game, display).start();
    } 
    
}
