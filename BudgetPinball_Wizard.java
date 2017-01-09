import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.Map.Entry;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.lang.Thread;
import java.lang.Math;
import java.util.ArrayList;
import java.awt.event.*;
import java.io.IOException;
import java.io.File;
import java.awt.Rectangle;

class main{
	
    static Random RND=new Random();
	
    public static void main(String[] args){
	
	PiFrame jframe=new PiFrame();
	
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setVisible(true);
       
	jframe.run();
	
    }
}

class PiFrame extends JFrame implements MouseListener{

    final int size=800;
    final double dsize=800;
    ArrayList<Wall> walls;
    BufferedImage buffer;
    Graphics graphics;

    public PiFrame(){
	super();
	setSize(size,size);
	walls=new ArrayList<Wall>();
	walls.add(new Wall(0 , 0 , size/8 , size));
	walls.add(new Wall(size , 0 , 7*size/8  , size));

	walls.add(new Wall(0 , size/32 , size  , size/32));//ceiling

	Flipper flipper1=new Flipper(size/8, 7*size/8, -Math.PI/16, size/3, KeyEvent.VK_A);
	Flipper flipper2=new Flipper(size-size/8, 7*size/8, Math.PI+Math.PI/16 , size/3, KeyEvent.VK_D);

	walls.add(flipper1);
	walls.add(flipper2);

	addKeyListener(flipper1);
	addKeyListener(flipper2);

	buffer=new BufferedImage(size,size,1);
	graphics=buffer.getGraphics();
    }

    public void run(){

	Ball ball=new Ball(size/4+(new Random()).nextDouble()*size/2, size/4, 0, 0);

	while(true){

	    graphics.setColor(Color.black);
	    graphics.fillRect(0,0,size,size);

	    graphics.setColor(Color.black);		
	    graphics.fillRect((int)ball.x,(int)ball.y,4,4);

	    ball.dy+=0.01;
	    ball.update();

	    if(ball.y>dsize+200){
		ball=new Ball(size/4+(new Random()).nextDouble()*size/2, size/4, 0, 0);
	    }

	    graphics.setColor(Color.green);		
	    graphics.fillRect((int)ball.x,(int)ball.y,4,4);

	    for(Wall wall: walls){
		if(wall instanceof Flipper)((Flipper)wall).update();
	    }

	    for(Wall wall: walls){
		if(wall.collision(ball.x,ball.y,ball.dx,ball.dy)){
		    wall.redirect(ball);
		}
		wall.paint(graphics);
	    }

	    getGraphics().drawImage(buffer, 0,0,size,size, null);

	    try {
		Thread.sleep(5);
	    } catch(InterruptedException ex) {
		Thread.currentThread().interrupt();
	    }
	}
    }

    public void mouseExited(MouseEvent e){

    }

    public void mouseEntered(MouseEvent e){

    }

    public void mouseReleased(MouseEvent e){

    }

    public void mousePressed(MouseEvent e){

    }

    public void mouseClicked(MouseEvent e){
	
    }

}

class Wall{

    double x1,x2,y1,y2;
    double deltaangle=0;
    double length;

    public Wall(double x1, double y1, double x2, double y2){
	this.x1=x1;
	this.y1=y1;
	this.x2=x2;
	this.y2=y2;
	length=Math.hypot(x2-x1,y2-y1);
    }

    double calculateFraction(double c1 , double c2 , double point){return (point-c1)/(c2-c1);}

    private double calculateAngle(double dx, double dy){
	double angle =Math.atan(dy/dx);

	if(dx<0)angle+=Math.PI;

	return angle;
    }

    public boolean collision(double ballx, double bally, double deltax, double deltay){

	if(Math.hypot(ballx-x1, bally-y1)>Math.hypot(x2-x1, y2-y1))return false;
	
	
	double wallangle=calculateAngle(x1-x2,y1-y2);
	double ballangle=calculateAngle(x1-ballx,y1-bally);

	double wallmoveangle=calculateAngle(x1-x2,y1-y2)-deltaangle;
	double ballmoveangle=calculateAngle(x1-(ballx+deltax),y1-(bally+deltay));
	
	boolean colided=false;

	colided|=((wallangle-ballangle)<0 ^ (wallangle-ballmoveangle)<0);

	colided|=((wallangle-ballangle)<0 ^ (wallmoveangle-ballangle)<0);

	colided|=((wallangle-ballangle)<0 ^ (wallmoveangle-ballmoveangle)<0);
	
	//reset d-angle in redirect to be able to use speed 
	return colided;
    }
    

    public void redirect(Ball ball){

	double ballangle=calculateAngle(ball.dx,ball.dy);
	double wallnormal=calculateAngle(x2-x1,y2-y1);
	
	double fraction=calculateFraction(x1, x2, ball.x);
	
	if(Math.abs(y2-y1)>Math.abs(x2-x1))fraction=calculateFraction(y1, y2, ball.y);

	double ballspeed=0.92*Math.hypot(ball.dy, ball.dx);

	//hack
	
	if(comingtowards(ball))
	    ballspeed+=0.65*Math.abs(length*fraction*deltaangle);//elastic bounce/~~~
	else{
	    ballspeed-=0.65*Math.abs(length*fraction*deltaangle);
	}

	deltaangle=0;

	double newangle=2*wallnormal-ballangle;

	ball.dx=ballspeed*Math.cos(newangle);
	ball.dy=ballspeed*Math.sin(newangle);

    } 

    private boolean comingtowards(Ball ball){
	if(deltaangle==0)return false;

	
	double miniangle=calculateAngle(x2-x1, y2-y1)+deltaangle/500;
	return Math.hypot(x2-ball.x,y2-ball.y)<Math.hypot(x1+length*Math.cos(miniangle)-ball.x,
							y1+length*Math.sin(miniangle)-ball.y);
    }

    public void paint(Graphics g){
	 g.setColor(Color.orange);
	 g.drawLine((int)x1,(int)y1,(int)x2,(int)y2); 
    }

}

class Flipper extends Wall implements KeyListener{

    static final double maxturn=2*Math.PI/3;
    static final double speed=1.0/65;
    boolean active=false;
    double angle,startangle;
    int keycode;

    public Flipper(double x, double y, double angle, double length, int keycode){
	super(x,y,x+length*Math.cos(angle),y-length*Math.sin(angle));
	this.keycode=keycode;
	startangle=angle;
	this.angle=angle;
    }

    public void update(){
	double direction=(startangle<Math.PI/2 || startangle>3*Math.PI/2 ? -1 : 1);
	
	if(direction>0){
	    if(active){
		if(deltaangle>-speed)deltaangle-=1/2000.0;
		    
		if(angle+maxturn>startangle){}else{
		    deltaangle=0;
		}
	    }else{
		if(deltaangle<speed)deltaangle+=1/2000.0;

		if(angle<startangle){}else{
		    deltaangle=0;
		}
	    }
	    
	}else{
	    
	    if(active){
		if(deltaangle<speed)deltaangle+=1/2000.0;

		if(angle<startangle+maxturn){}else{
		    deltaangle=0;
		}
	    }else{
		if(deltaangle>-speed)deltaangle-=1/2000.0;

		if(angle>startangle){}else{
		    deltaangle=0;
		}
	    }
	}

	angle+=deltaangle;

	x2=x1+length*Math.cos(angle);
	y2=y1-length*Math.sin(angle);
    }
    
    public void keyPressed(KeyEvent e){if(e.getKeyCode()==keycode)active=true;}

    public void keyReleased(KeyEvent e){if(e.getKeyCode()==keycode)active=false;}

    public void keyTyped(KeyEvent e){}
}

class Ball{

    public double x, y, dx, dy;
    final double maxspeed=Double.MAX_VALUE;
    
    public Ball(double x, double y, double dx, double dy){
	this.x=x;
	this.y=y;
	this.dx=dx;
	this.dy=dy;
    }

    public void update(){
	
	if(Math.hypot(dx,dy)>maxspeed){
	    double tmp=Math.hypot(dx,dy);

	    dx*=maxspeed/tmp;
	    dy*=maxspeed/tmp;
	}

	x+=dx;
	y+=dy;
    }

}