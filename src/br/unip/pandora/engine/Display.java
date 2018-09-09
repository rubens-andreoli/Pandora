package br.unip.pandora.engine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display {

    public static final int DEFAULT_WIDTH = 320;
    public static final int DEFAULT_HEIGHT = 240;
    public static final int DEFAULT_SCALE = 2;
    
    private String title;
    private int width, height, scale;
    
    private JFrame frame;
    private JPanel panel;
    private BufferedImage buffer;
    private Graphics2D gBuffer;
    
    public Display(String title, int width, int height, int scale) {
	this.title = title;
	this.width = width;
	this.height = height;
	this.scale = scale;
	
	panel = new JPanel();
	Dimension d = new Dimension(width*scale, height*scale);
	panel.setPreferredSize(d);
	panel.setPreferredSize(d);
	panel.setPreferredSize(d);
	panel.setFocusable(false);
	
	buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	gBuffer = (Graphics2D) buffer.getGraphics();
	
	frame = new JFrame(title);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	frame.setContentPane(panel);
    }
    
    public Display(String title){
	this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SCALE);
    }
    
    public void setVisible(boolean b){
	if(b){ 
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.requestFocus();
	    java.awt.EventQueue.invokeLater(() -> {
		frame.setVisible(true);
	    });
	}else frame.setVisible(false);
    }
 
    public Graphics2D getGraphics(){
	return gBuffer;
    }
   
    public void show() {
	Graphics gShow = panel.getGraphics();
	gShow.drawImage(buffer, 0, 0, width*scale, height*scale, null);
	gShow.dispose();
    }
    
    public void close(){
	gBuffer.dispose();
	buffer.flush();
	frame.dispose();
    }
   
    public void addWindowListener(WindowListener l) {
	frame.addWindowListener(l);
    }
    
    public void addInputHandler(InputHandler i) {
	frame.addKeyListener(i.getKeyListener());
	panel.addMouseListener(i.getMouseListener());
	panel.addMouseMotionListener(i.getMouseMotionListener());
    }

    public void appendTitle(String s){
	frame.setTitle(String.format("%s [%s]", title, s));
    }
  
}