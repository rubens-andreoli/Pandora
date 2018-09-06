package br.unip.pandora.entity;


public class Water implements Entity{

    public static final int ID = 1;
    
    @Override
    public int getId() {
	return ID;
    }

    @Override
    public int getMetadata() {
	return 0;
    }
 
}
