package com.pressx.editors.formationeditor;

import java.awt.Color;
import java.awt.Graphics;	
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.pressx.editors.shared.Vector2;
import com.pressx.editors.shared._G;

import java.util.Date;
import java.awt.Point;

enum Keys{
    ESCAPE,
    SHIFT,CONTROL,
    DELETE,
    Q,A,W,S,E,D,T,G,O,N,R,B,X,H,
    UP,DOWN,LEFT,RIGHT,//left and right are not used
    NONE,
}

public abstract class GameDriver extends Canvas implements KeyListener,MouseListener,MouseWheelListener,Runnable{/////Handles the low-level stuff
    static final int NUMKEYS = 23;
    
    protected boolean[] keys;
    protected boolean[] keypress;

    protected BufferedImage back;
    protected int timer = 6;

/////////////////////////INITIALIZE
    public GameDriver(){
	keys = new boolean[NUMKEYS];
	keypress = new boolean[keys.length];
    }
    
    public void start(){
	setBackground(Color.WHITE);
	setVisible(true);

	new Thread(this).start();
	addKeyListener(this);
	addMouseListener(this);
	addMouseWheelListener(this);

	setFocusable(true);
    }


   public void setTimer(int value) {
       timer = value;
   }
/////////////////////////MOUSE
   static boolean leftMouseIsDown;
   static int lastLeftMouseClicked = -1;
   static boolean rightMouseIsDown;
   static int lastRightMouseClicked = -1;
   static boolean middleMouseIsDown;
   static int lastMiddleMouseClicked = -1;
   static Vector2 mousepos = Vector2.zero();
   static int wheelRotation;
   public static boolean leftMouseDown(){
       return leftMouseIsDown;
   }
   public static boolean leftMouseClicked(){
       return lastLeftMouseClicked == _G.cycle;
   }
   public static boolean rightMouseDown(){
       return rightMouseIsDown;
   }
   public static boolean rightMouseClicked(){
       return lastRightMouseClicked == _G.cycle;
   }
   public static boolean middleMouseDown(){
       return middleMouseIsDown;
   }
   public static boolean middleMouseClicked(){
       return lastMiddleMouseClicked == _G.cycle;
   }
   public static Vector2 mousePosition(){
       return mousepos;
   }
   public static int getWheelRotation(){
       return wheelRotation;
   }
   public static void resetLeftMouse(){
       leftMouseIsDown = false;
   }
   
    public void mouseClicked(MouseEvent e){}

    public void mousePressed(MouseEvent e){
	if(e.getButton() == MouseEvent.BUTTON1){
	    leftMouseIsDown = true;
	    lastLeftMouseClicked = _G.cycle;
	}else if(e.getButton() == MouseEvent.BUTTON3){
	    rightMouseIsDown = true;
	    lastRightMouseClicked = _G.cycle;
	}else{
	    middleMouseIsDown = true;
	    lastMiddleMouseClicked = _G.cycle;
	}
    }
    public void mouseReleased(MouseEvent e){
	if(e.getButton() == MouseEvent.BUTTON1){
	    leftMouseIsDown = false;
	}else if(e.getButton() == MouseEvent.BUTTON3){
	    rightMouseIsDown = false;
	}else{
	    middleMouseIsDown = false;
	}
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    
    public void mouseWheelMoved(MouseWheelEvent e){
	wheelRotation = e.getWheelRotation();
    }
    
    void update_mouse(){
	Point p = this.getMousePosition();
	if(p != null)
	    mousepos = new Vector2(p);
    }

/////////////////////////KEYS
    Keys keyEventToKeys(KeyEvent e){
	switch(e.getKeyCode()){
	    case KeyEvent.VK_ESCAPE: return Keys.ESCAPE;
	    case KeyEvent.VK_SHIFT: return Keys.SHIFT;
	    case KeyEvent.VK_DELETE: return Keys.DELETE;
	    case KeyEvent.VK_CONTROL: return Keys.CONTROL;
	    case KeyEvent.VK_Q: return Keys.Q;
	    case KeyEvent.VK_A: return Keys.A;
	    case KeyEvent.VK_W: return Keys.W;
	    case KeyEvent.VK_S: return Keys.S;
	    case KeyEvent.VK_E: return Keys.E;
	    case KeyEvent.VK_D: return Keys.D;
	    case KeyEvent.VK_T: return Keys.T;
	    case KeyEvent.VK_G: return Keys.G;
	    case KeyEvent.VK_O: return Keys.O;
	    case KeyEvent.VK_N: return Keys.N;
	    case KeyEvent.VK_R: return Keys.R;
	    case KeyEvent.VK_B: return Keys.B;
	    case KeyEvent.VK_X: return Keys.X;
	    case KeyEvent.VK_H: return Keys.H;
	    case KeyEvent.VK_UP: return Keys.UP;
	    case KeyEvent.VK_DOWN: return Keys.DOWN;
	    case KeyEvent.VK_LEFT: return Keys.LEFT;
	    case KeyEvent.VK_RIGHT: return Keys.RIGHT;
	}
	return Keys.NONE;
    }
    public void keyPressed(KeyEvent e){
	Keys a = keyEventToKeys(e);
	if(a == Keys.NONE) return;
	keys[a.ordinal()] = true;
	keypress[a.ordinal()] = true;
    }

    public void keyReleased(KeyEvent e){
	Keys a = keyEventToKeys(e);
	if(a == Keys.NONE) return;
	keys[a.ordinal()] = false;
    }

    public void keyTyped(KeyEvent e){}

/////////////////////////IMAGES
    public BufferedImage addImage(String name){
	BufferedImage img = null;
	try{
	    img = ImageIO.read(new File(name));
	}catch(IOException e){
	    System.out.println(e);
	}
	return img;
    }
    
/////////////////////////RUN
    static final long FRAMETIME = 33;
    public void run(){
	try{
	    long oldtime = new Date().getTime();
	    while(true){
		long newtime = new Date().getTime();
		long elapsed = newtime-oldtime;
		if(elapsed < FRAMETIME)
		    Thread.currentThread().sleep(FRAMETIME-elapsed);
		oldtime = newtime;
		repaint();
	    }
	}catch(Exception e){
	    System.out.println(e);
	}
    }
   public void update(Graphics window){
       update_mouse();
       paint(window);
   }
    
/////////////////////////DRAW
   public void paint(Graphics window){
	if(back==null)
	    back = (BufferedImage)(createImage(getWidth(),getHeight()));
	Graphics2D graphToBack = (Graphics2D) back.createGraphics();

	draw(graphToBack);

	Graphics2D win2D = (Graphics2D) window;
	win2D.drawImage(back, null, 0, 0);
    }
    public abstract void draw(Graphics2D win);
}
