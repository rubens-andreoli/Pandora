package br.unip.pandora.entity;

public class Soul {
    
    private final int name;
    private final Body body;
    
    public Soul(final int name, Body dM, Body dF){
	this.name = name;
	body = new Body(dM, dF);
    }

	    
}
