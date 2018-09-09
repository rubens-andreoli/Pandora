package br.unip.pandora;

import br.unip.pandora.engine.Engine;
import br.unip.pandora.world.Box;

public class Laucher {

    public static void main(String[] args) {
	new Engine(new Box()).start();
    } 
    
}
