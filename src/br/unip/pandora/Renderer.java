package br.unip.pandora;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Renderer extends JPanel{
 
    //dimensions
    public static final int WIDTH = 640; //800
    public static final int HEIGHT = 480;
    public static final int SCALE = 2;
    
    //image
    private BufferedImage image;
    private Graphics2D g;
  
    public Renderer(){
	this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
	this.setFocusable(false);
	image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	g = (Graphics2D) image.getGraphics();
    }
    
    public void drawToScreen(){
	Graphics g2 = this.getGraphics();
	g2.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
	g2.dispose();
    }
    
    public Graphics2D getG2D(){
	return g;
    }
    
}
