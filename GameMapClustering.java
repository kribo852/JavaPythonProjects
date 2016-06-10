import javax.swing.JFrame;
import java.util.*;
import java.awt.Graphics;
import java.awt.Color;
import java.lang.Thread;
import java.lang.Math;

class main{

enum LandType{
	grass(0, 75, 150 ,50),water(1, 25, 100, 150),dirt(2 , 150 ,150 , 25);
	
	private int value, red, green, blue; 
	private LandType(int value, int red, int green, int blue) {
		 this.value = value;
		 this.red=red ;
		 this.green=green;
		 this.blue=blue;
	}
	
	public Color getColour(){
		return new Color(red,green,blue);
	}
	
	public int getValue(){
		return value;
	}

};

public static void main(String[] args){
	
	JFrame jframe=new JFrame();
	
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setVisible(true);
	jframe.setSize(800 , 800);
	
	LandType[][] map=new LandType[100][100];	
	
	Random RND=new Random();
	
	int scale=800/100;
	Graphics g=jframe.getGraphics(); 
	
	for(int i=0; i<map.length; i++){
		for(int j=0; j<map[0].length; j++){
			
			int squareNum=RND.nextInt(20); 
			
			if(squareNum<=7)
				map[i][j]=LandType.grass;
			else if(squareNum<=14)
				map[i][j]=LandType.water;
			else
				map[i][j]=LandType.dirt;
		}
	}
	
	while(true){
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[0].length; j++){
				
				g.setColor(map[i][j].getColour());
				g.fillRect(i*scale,j*scale, scale , scale);
			}	
		}
		clusterMap(map);
		
		try {
			Thread.sleep(250);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	}
	
	protected static void clusterMap(LandType[][] map){
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[0].length; j++){
				
				if((i+j)%2==0)
				{
					LandType newType=mostWeightNeighbours(i,j,map);
					map[i][j]=newType;
				}
			}
		}
		
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[0].length; j++){
				
				if((i+j)%2==1)
				{
					LandType newType=mostWeightNeighbours(i,j,map);
					map[i][j]=newType;
				}
			}
		}
			
	}

	protected static LandType mostWeightNeighbours(int x, int y, LandType[][] map){
		double sum[]={0,0,0};
		
		x+=map.length;
		y+=map[0].length;//to solve the modulus problem

		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
	
				if(Math.abs(i)+Math.abs(j)<2){
					sum[map[(x+i)%map.length][(y+j)%map[0].length].getValue()]++;
				}else{
					sum[map[(x+i)%map.length][(y+j)%map[0].length].getValue()]+=1/Math.sqrt(2.0);
				}
	
			}	
		}

		if(sum[2]>sum[1] && sum[2]>sum[0])
		return LandType.dirt;
		
		if(sum[1]>=sum[2] && sum[1]>sum[0])
		return LandType.water;
		
		if(sum[0]>=sum[2] && sum[0]>=sum[1])
		return LandType.grass;
		
		return map[x][y];
		
	}
}
