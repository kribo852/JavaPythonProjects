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

    TreeSet<Double>lines;

    public PiFrame(){
	super();
	addMouseListener(this);
	setSize(size+10,size+10);
	lines=new TreeSet<Double>();
	lines.add(0.0);
	lines.add(1.0);
    }


    public void run(){
	BufferedImage bimg=new BufferedImage(size+10,size+10,1); 
	Graphics g=bimg.getGraphics();

	while(true){
	    g.setColor(Color.black);
	    g.fillRect(0,0,bimg.getWidth(),bimg.getHeight());
	    g.setColor(Color.green);
	    //g.drawOval(-size,-size,2*size,2*size);
	    g.setColor(Color.orange);
	    {
		for(Double ycoordinate: lines){
		    double x=dsize*Math.sqrt(1-ycoordinate*ycoordinate);
		    double y=dsize*ycoordinate;
		    g.drawLine(0,(int)y,(int)x,(int)y);
		}

		double area=0;

		double last=-1;
		for(Double ycoordinate: lines){
		    if(last>=0){
			double x1=Math.sqrt(1-last*last);
			double x2=Math.sqrt(1-ycoordinate*ycoordinate);
			double y1=last;
			double y2=ycoordinate;
			
			g.drawLine((int)(dsize*x1),(int)(dsize*y1),(int)(dsize*x2),(int)(dsize*y2));

			area+=(y2-y1)*(x1+x2)/2.0;

		    }
		    last=ycoordinate;
		}

		g.setColor(Color.green);
		g.drawString("Click to place rectangleoid",size-200, size-120);
		g.drawString("Area of rectangleoids: "+area ,size-200, size-100);
		g.drawString("Area=((r²)*Pi)/4, r=1",size-200, size-80);
		g.drawString("Implied Pi: "+area*4 ,size-200, size-60);

	    }

	    getGraphics().drawImage(bimg, 0,0,bimg.getWidth(),bimg.getHeight(),null);
	   
	    try {
		Thread.sleep(50);
	    } catch(InterruptedException ex) {
		Thread.currentThread().interrupt();
	    }
	}
    }

    public void mouseExited(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    
    public void mouseClicked(MouseEvent e){
	
	if (e.getButton() == MouseEvent.BUTTON1) {
	    lines.add(e.getY()/dsize);
	    
	}

	if (e.getButton() == MouseEvent.BUTTON3) {
	   	lines=new TreeSet<Double>();
		lines.add(0.0);
		lines.add(1.0);
	}
    }
}