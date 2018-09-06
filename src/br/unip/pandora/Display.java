package br.unip.pandora;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
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
    private InitializedDisplay initDisplay;

    public Display(String title, int scale){
	this.title = title;
	this.scale = scale;
    }
    
    public void init(int width, int height){
	if(initDisplay != null) return;
	this.width = width;
	this.height = height;
	
	panel = new JPanel();
	panel.setPreferredSize(new Dimension(width*scale, height*scale));
	panel.setFocusable(false);
	
	buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	gBuffer = (Graphics2D) buffer.getGraphics();
	
	frame = new JFrame(title);
	frame.setContentPane(panel);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	frame.pack();
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	frame.requestFocus();

	initDisplay = new InitializedDisplay();
    }

    public InitializedDisplay getInitDisplay() {return initDisplay;}

    public class InitializedDisplay {
	
	private InitializedDisplay(){}
	
	public void show(){
	    Graphics gShow = panel.getGraphics();
	    gShow.drawImage(buffer, 0, 0, width*scale, height*scale, null);
	    gShow.dispose();
	}
	
	public void appendTitle(String text){
	    frame.setTitle(String.format("%s [%s]", title, text));
	}
    
	public Graphics2D getGraphics(){return gBuffer;}  
	public void addMouseListener(MouseListener l){frame.addMouseListener(l);}
	public void addKeyListener(KeyListener l){frame.addKeyListener(l);}
	public void close(){frame.dispose();}
    }

}
