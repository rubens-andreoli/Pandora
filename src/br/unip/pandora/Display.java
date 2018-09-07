package br.unip.pandora;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display {
 
    //properties
    private String title;
    private int width, height;
    private int scale;
    
    //components
    private JFrame frame;
    private JPanel panel;
    private BufferedImage buffer;
    private Graphics2D gBuffer;

    public Display(Game game){
	title = game.title;
	width = game.width;
	height = game.height;
	scale = game.scale;
	
	panel = new JPanel();
	Dimension d = new Dimension(width*scale, height*scale);
	panel.setPreferredSize(d);
	panel.setPreferredSize(d);
	panel.setPreferredSize(d);
	panel.setFocusable(false);
	
	buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	gBuffer = (Graphics2D) buffer.getGraphics();
	
	frame = new JFrame(title);
	frame.setContentPane(panel);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	frame.pack();
	frame.setLocationRelativeTo(null);
    }
    
    public void setVisible(boolean b){
	frame.setVisible(b);
	if(b) frame.requestFocus();
    }

    public void show(){
	Graphics gShow = panel.getGraphics();
	gShow.drawImage(buffer, 0, 0, width*scale, height*scale, null);
	gShow.dispose();
    }

    public void appendTitle(String text){
	frame.setTitle(String.format("%s [%s]", title, text));
    }

    public void setTitle(String title) {
	this.title = title;
	frame.setTitle(title);
    }
    
    public Graphics2D getGraphics(){return gBuffer;}  
    public void addWindowListener(WindowListener l) {frame.addWindowListener(l);}
    public void addMouseListener(MouseListener l){frame.addMouseListener(l);}
    public void addKeyListener(KeyListener l){frame.addKeyListener(l);}
    public void close(){frame.dispose();}

}
