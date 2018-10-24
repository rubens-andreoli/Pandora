package br.unip.pandora.engine;

import java.awt.Cursor;
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

    //config
    private String title;
    private int width, height, scale;
    
    //parts
    private static JFrame frame = new JFrame();
    private static Cursor cursor;
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
	panel.setMaximumSize(d);
	panel.setMinimumSize(d);
	panel.setFocusable(false);
	
	buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	gBuffer = (Graphics2D) buffer.getGraphics();
	
	frame.setTitle(title);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	frame.setContentPane(panel);
	frame.pack();
	frame.setLocationRelativeTo(null);
    }
    
    public Display(String title, int width, int height){
	this(title, width, height, DEFAULT_SCALE);
    }
    
    public Display(String title){
	this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SCALE);
    }
    
    public void setVisible(boolean b){
	if(b){ 
	    frame.pack(); //packed again to prevent frame with wrong size
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
    
    public void addKeyHandler(KeyHandler k) {
	frame.addKeyListener(k.getKeyListener());
    }
    
    public void addMouseHandler(MouseHandler m){
    	panel.addMouseListener(m.getMouseListener());
	panel.addMouseMotionListener(m.getMouseMotionListener());
	panel.addMouseWheelListener(m.getMouseWheelListener());
    }

    public void appendTitle(String s){
	frame.setTitle(String.format("%s [%s]", title, s));
    }
  
    public static void setCursor(Cursor c){
	if(cursor == null || cursor != c){ 
	    cursor = c;
	    frame.setCursor(c);
	}
    }
    
}
