package br.unip.pandora;

import br.unip.pandora.engine.Engine;
import br.unip.pandora.box.Pandora;

public class Laucher {

    public static void main(String[] args) {
	new Engine(new Pandora()).start();
    } 
    
}
