import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.Thread;
import java.lang.Math;
import java.util.HashSet;
import java.awt.event.*;
import java.io.IOException;
import java.io.File;

class Microbes{
	
public static void main(String[] args){
	
	
	SaveFrame jframe=new SaveFrame();
	
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setVisible(true);
	jframe.setSize(1000 , 500);
	
	Critter[][] animales=new Critter[jframe.returnBuffer().getWidth()][jframe.returnBuffer().getHeight()];//red,green,blue,foodamount 
	HashSet<int[]> active=new HashSet<int[]>();
	
	for(int i=0; i<jframe.returnBuffer().getWidth(); i++){
		for(int j=0; j<jframe.returnBuffer().getWidth(); j++){
			Color c=new Color(jframe.returnBuffer().getRGB(i,j));
			
			if(c.getRed()!=0 || c.getGreen()!=0 || c.getBlue()!=0){	
				animales[i][j]=new Critter(c.getRed(), c.getGreen(), c.getBlue(), 0);
				if(canMove(animales,i,j))
					active.add(new int[]{i,j});
			}
		}
	}
	

	int[][] foodsupply=new int[animales.length][animales[0].length];
	double waveangle=0;
	
	
	
	Random RND=new Random();
	
	Graphics graphics=jframe.getGraphics(); 
	BufferedImage buffer=new BufferedImage(animales.length, animales[0].length,1); 
	Graphics bufferGraphics=buffer.getGraphics();
	
	for(int i=0; i<foodsupply.length; i++){
		for(int j=0; j<foodsupply.length; j++){
			foodsupply[i][j]=400-(i+j)/4;
			foodsupply[i][j]=Math.max(25,foodsupply[i][j]);
		}
	}
	
		for(int j=0; j<foodsupply.length; j++){
			for(int i=100; i<150; i++)
				foodsupply[i][j]=200;
				
			for(int i=300; i<350; i++)
				foodsupply[i][j]=200;	
		}
		
		for(int i=0; i<foodsupply.length; i++)
			for(int j=300; j<350; j++){
				foodsupply[i][j]=225;
		}
		
	for(int i=0; i<foodsupply.length; i++){
		for(int j=0; j<foodsupply.length; j++){
			int brightness=Math.min(255, foodsupply[i][j]);
			bufferGraphics.setColor(new Color(brightness,brightness,brightness));
			bufferGraphics.fillRect(i,j,1,1);
		}
	}
	
	for(double i=0; i<foodsupply.length; i++){
		for(double j=0; j<foodsupply.length; j++){
			double angle=Math.atan((j-foodsupply[0].length/2)/-Math.abs(i-foodsupply.length/2));
			
			angle=Math.abs(angle-Math.PI/2);
			
			if(angle*75>Math.hypot(i-foodsupply.length/2,j-foodsupply[0].length/2))
				foodsupply[(int)i][(int)j]=360;
			
		}
	}
	
	while(true){
		
		for(int[] position: active){
			
			int x=position[0];
			int y=position[1];
			//System.out.println("x "+x+" y "+y);
			
			if(animales[x][y]!=null){
				
				int movex=-1+RND.nextInt(3);
				int movey=-1+RND.nextInt(3);
				while((movex==0 && movey==0) || x+movex<0 || x+movex>=animales.length ||
				 y+movey<0 || y+movey>=animales[0].length){
					movex=-1+RND.nextInt(3);
					movey=-1+RND.nextInt(3);
				}
				
				if(animales[x+movex][y+movey]!=null){
					if(animales[x+movex][y+movey].interact(animales[x][y])){
						animales[x][y]=null;
					}	
					
				}else{
					animales[x+movex][y+movey]=animales[x][y];
					animales[x][y]=null;
					
					if(RND.nextInt(256)<animales[x+movex][y+movey].returnColour().getBlue()-
					animales[x+movex][y+movey].returnColour().getRed()/4){
						animales[x][y]=animales[x+movex][y+movey].multiply();
					}
				}
			}
		}
		if(active.isEmpty()){
			int x=RND.nextInt(animales.length);
			int y=RND.nextInt(animales[0].length);
			animales[x][y]=new Critter();
		}
		waveangle=waveArea(foodsupply, waveangle);
		active=findActives(animales, foodsupply);
		
		Graphics jframeGraphics=jframe.returnBuffer().getGraphics();
		
		for(int i=0; i<animales.length; i++){
			for(int j=0; j<animales[0].length; j++){
				
				
				if(animales[i][j]!=null){
					jframeGraphics.setColor(animales[i][j].returnColour());
				}else{
					jframeGraphics.setColor(Color.black);
				}
				
				jframeGraphics.fillRect(i,j,1,1);
			}
		}
		
		graphics.drawImage(jframe.returnBuffer(), 0 , 0 , null);
		graphics.drawImage(buffer, 500 , 0 , null);
		try {
			Thread.sleep(25);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	}
	
	public static boolean canMove(Critter[][] animales, int x , int y){
		
		if(animales[x][y]==null)
			return false;
		
		for(int k=-1; k<2; k++){
			for(int l=-1; l<2; l++){
				if(Math.abs(k)+Math.abs(l)==1){
					if(x+k>=0 && x+k<animales.length && y+l>=0 && y+l<animales[0].length){
						if(animales[x+k][y+l]==null || animales[x][y].genomeDiffers(animales[x+k][y+l]))
							return true;
					}
				}
			}	
		}
		return false;
	}
	
	public static HashSet<int[]> findActives(Critter[][] animales, int foodsupply[][]){
		
		HashSet<int[]> rtn= new HashSet<int[]>();
		
		for(int i=0; i<animales.length; i++)
			for(int j=0; j<animales[0].length; j++){
				if(animales[i][j]==null)
					continue;
				
				int gainedfood=(int)(foodsupply[i][j]*animales[i][j].returnColour().getGreen()/255.0);
				int energyconsumption;
				{
					Color c=animales[i][j].returnColour();
					energyconsumption=c.getRed();
					energyconsumption+=c.getGreen()/2;
					energyconsumption+=c.getBlue()/2;
				}
				animales[i][j].setDeposit(animales[i][j].returnDeposit()+gainedfood-energyconsumption);
				
				if(animales[i][j].returnDeposit()<0){
					animales[i][j]=null;
					
				}else
				animales[i][j].setDeposit(Math.min(animales[i][j].returnColour().getGreen(), animales[i][j].returnDeposit()));
			}
		
		for(int i=0; i<animales.length; i++)
			for(int j=0; j<animales[0].length; j++)
				if(canMove(animales,i,j)){
					rtn.add(new int[]{i,j});
				}
		return rtn;
	}
	
	public static double waveArea(int[][] foodsupply, double waveangle){
		waveangle+=Math.PI/50;
		for(int i=0; i<foodsupply.length; i++){
			
			for(int j=0; j<75; j++){
				foodsupply[i][j]=(int)(250*(1.35+0.65*Math.sin(waveangle+i*Math.PI/100)));
				
				foodsupply[foodsupply.length-i-1][75+j]=(int)(250*(1.35+0.65*Math.sin(waveangle+i*Math.PI/100)));
			}	
		}
		return waveangle;	
	}
	
}

class Critter{
	Color c;
	int depositedfood;
	
	public Critter(){
		Random RND=new Random();
		c=new Color(RND.nextInt(256),RND.nextInt(256),RND.nextInt(256));
	}
	
	public Critter(int red, int green, int blue, int givefood){
		c=new Color(red, green, blue);
		depositedfood=givefood;
	}
	
	public boolean interact(Critter other){
		
		
		if(!genomeDiffers(other)){
			return false;//no critter eaten, they are too similar
		}
		
		if(c.getRed()<other.returnColour().getRed()){
			c=new Color(other.returnColour().getRGB());
			depositedfood+=other.returnDeposit();
			return true;//this critter is eaten
		}
		
		if(c.getRed()==other.returnColour().getRed()){
			return false;//no critter eaten, they are equally strong
		}
		
		depositedfood+=other.returnDeposit();
		return true;//this eats the other
	}
	
	public boolean genomeDiffers(Critter other){
		int difference= Math.abs(c.getRed()-other.returnColour().getRed());
		difference+= Math.abs(c.getGreen()-other.returnColour().getGreen());
		difference+= Math.abs(c.getBlue()-other.returnColour().getBlue());
		return difference>other.returnColour().getBlue();
	} 
	
	public Critter multiply(){
		Random RND=new Random();
		int red=(-15+RND.nextInt(31))+c.getRed();
		int green=(-15+RND.nextInt(31))+c.getGreen();
		int blue=(-15+RND.nextInt(31))+c.getBlue();
		int givefood=depositedfood/2;
		depositedfood=givefood;
		
		red=Math.max(0,Math.min(255, red));
		green=Math.max(0,Math.min(255, green));
		blue=Math.max(0,Math.min(255, blue));
		
		return new Critter(red, green, blue, givefood);
	}
	
	public Color returnColour(){return c;}
	public int returnDeposit(){return depositedfood;}
	public void setDeposit(int depositedfood){this.depositedfood=depositedfood;}
}

class SaveFrame extends JFrame{
	
		BufferedImage buffer;
		String savename="save.png";
		
		public SaveFrame(){
			
			try{
				buffer=ImageIO.read(new File(savename));
			}catch(IOException moon){
				buffer=new BufferedImage(500,500,1);
			
			for(int i=0; i<buffer.getWidth(); i++){
				for(int j=0; j<buffer.getHeight(); j++){
					buffer.setRGB(i,j,0);
				}
			}
			}
			
			addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				try{
					ImageIO.write(buffer, "png" ,new File(savename));
				}catch(IOException moon){}
				
			}
		});
		}
		
		public BufferedImage returnBuffer(){
			return buffer;
		}
		
		public Graphics returnBGraphics(){
			return buffer.getGraphics();
		}
	
}