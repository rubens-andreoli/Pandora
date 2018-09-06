package br.unip.pandora;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display{
 
    //properties
    private int width, height;
    private int scale;
    
    //components
    private JFrame frame;
    private JPanel panel;
    private BufferedImage buffer;
    private Graphics2D gBuffer;
  
    public Display(String title, int width, int height, int scale){
	this.width = width;
	this.height = height;
	this.scale = scale;
	
	panel = new JPanel();
	panel.setPreferredSize(new Dimension(width*scale, height*scale));
	panel.setFocusable(false);
	
	buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	gBuffer = (Graphics2D) buffer.getGraphics();
	
	frame = new JFrame(title);
	frame.setContentPane(panel);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
//	frame.setLocationRelativeTo(null);
	frame.pack();
	frame.setVisible(true);
	frame.requestFocus();
    }
    
    public void show(){
	Graphics gShow = panel.getGraphics();
	gShow.drawImage(buffer, 0, 0, width*scale, height*scale, null);
	gShow.dispose();
    }
    
    public Graphics2D getGraphics(){return gBuffer;}  
    public void setTitle(String title){frame.setTitle(title);}
    public void addMouseListener(MouseListener mouse){frame.addMouseListener(mouse);}
    public void addKeyListener(KeyListener key){frame.addKeyListener(key);}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

}
