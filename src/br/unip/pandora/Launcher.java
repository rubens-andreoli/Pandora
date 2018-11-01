package br.unip.pandora;

import br.unip.pandora.box.Pandora;
import br.unip.pandora.engine.Engine;

public class Launcher {

    public static void main(String[] args) {
	new Engine(new Pandora(), "PANDORA'S BOX", 2).start();
    } 
    
}
