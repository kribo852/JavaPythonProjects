import javax.swing.JFrame;
import java.util.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.Thread;
import java.lang.Math;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

enum Type{tree,player,empty}

class Tile{
	
	public boolean treasure;
	public Type type=Type.empty;
}

final class KeyBoard implements KeyListener{
	
		
	private static int event;
	
	public void keyPressed(KeyEvent e){
		event=e.getKeyCode();
	}
	
	public void keyReleased(KeyEvent e){
		event=0;
	}
	
	public void keyTyped(KeyEvent e){}
	public static int returnKeyTyped() {return event;} 
}

//this game makes claims of being the least playable game in the world

class main{
	
	static final int[] traversefunction={4,0,6,2,8,1,5,3,12,10,14,7,13,9,15,11};
	static final int[] inversetraverse=inverse(traversefunction);
	static Tile[][] map=new Tile[4][4];
	
	protected static final int[][]manimage=
	   {{0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0},
		{0,0,0,1,1,0,1,0,1,1,0,0,0,0,0,0},
		{0,0,0,1,1,0,1,0,1,1,0,0,0,0,0,0},
		{0,0,0,1,1,1,0,1,1,1,0,0,0,0,0,0},
		{0,0,0,0,1,1,0,1,1,0,0,0,0,0,0,0},
		{0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,1,1,1,0,0,0,0,0,1,0,0},
		{0,0,0,0,1,0,1,0,1,0,0,0,1,0,0,0},
		{0,0,0,1,0,0,1,0,0,1,0,1,0,0,0,0},
		{0,0,1,0,0,1,1,1,0,0,1,0,0,0,0,0},
		{0,1,0,0,1,1,1,1,0,0,0,0,0,0,0,0},
		{0,0,1,0,1,0,0,1,0,0,0,0,0,0,0,0},
		{0,0,0,0,1,0,0,1,1,0,0,0,0,0,0,0},
		{0,0,0,0,1,0,0,0,1,0,1,0,0,0,0,0},
		{0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0}};
		
	protected static final int[][]treeimage=
		{{0,0,0,1,0,0,0,0},
		 {0,0,0,1,1,0,0,0},
		 {0,1,0,1,0,0,1,0},
		 {0,0,1,1,0,1,0,0},
		 {0,1,0,1,1,0,0,0},
		 {0,0,1,1,0,0,0,0},
		 {0,0,0,1,1,0,0,0},
		 {0,0,0,1,1,0,0,0}};
		
	static BufferedImage pirate;
	static BufferedImage tree;
	

public static void main(String[] args){
	
	int mapsize=4,tilesize=48,screensize=600;
	resetMap();
	JFrame jframe=new JFrame();
	
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setVisible(true);
	jframe.setSize(screensize , screensize);
	KeyBoard keyboard=new KeyBoard(); 
	jframe.addKeyListener(keyboard);
	
	Graphics g=jframe.getGraphics(); 
	BufferedImage grass=new BufferedImage(screensize,screensize,1);
	BufferedImage stars=new BufferedImage(screensize,screensize,1);
	pirate=new BufferedImage(tilesize,tilesize,BufferedImage.TYPE_INT_ARGB);
	tree=new BufferedImage(tilesize,tilesize,BufferedImage.TYPE_INT_ARGB);
	
	makeGrass(grass);
	makeStars(stars);
	makeImage(pirate,manimage,new Color(50,45,30));
	//makeImage(tree,treeimage,new Color(75,25,15));
	makeTree(tree);
	
	BufferedImage drawbuffer=new BufferedImage(screensize,screensize,BufferedImage.TYPE_INT_ARGB);
	Graphics drawbuffergraphics=drawbuffer.getGraphics();
	
	long millispassed=System.currentTimeMillis();
	boolean newgamereset=false;
	while(true){
		
		if(!newgamereset){
		
			drawbuffergraphics.drawImage(grass, 0, 0, null);
			for(int i=0; i<map.length; ++i){
				for(int j=0; j<map[i].length; ++j){
					drawbuffergraphics.setColor(new Color(40, 100 , 9));
					drawbuffergraphics.drawRect(200+i*tilesize,200+j*tilesize,tilesize,tilesize);

					if(map[i][j].type==Type.tree){
						drawbuffergraphics.drawImage(tree,200+i*tilesize,200+j*tilesize,null);
					}	
					/*if(map[i][j].treasure){
						drawbuffergraphics.setColor(new Color(225,150,150));
						drawbuffergraphics.drawRect(200+i*tilesize,200+j*tilesize,tilesize-1,tilesize-1);
					}*/	
				}
			}	
			drawbuffergraphics.drawImage(pirate,200+findPlayer(map)[0]*tilesize,200+findPlayer(map)[1]*tilesize,null);
		
			g.drawImage(drawbuffer, 0, 0 , null);
		}else{
			drawbuffergraphics.drawImage(stars,0,0,null);
			int[] playerposition=findPlayer(map);
			drawbuffergraphics.setColor(new Color(155,150,165));
			if(map[playerposition[0]][playerposition[1]].treasure){
				drawbuffergraphics.drawString("Congratulations, you found a treasure.", 200, 200);
			}else{
				drawbuffergraphics.drawString("Congratulations, you did not find a treasure.", 200, 200);
			}	
			g.drawImage(drawbuffer,0,0,null);
		}
		
		if(!newgamereset){
			movePlayer(map , keyboard);
		}
		if(keyboard.returnKeyTyped()==KeyEvent.VK_ENTER){
			newgamereset=true;
		}else if(newgamereset){
			resetMap();
			newgamereset=false;
		}
		
		try {
			Thread.sleep(Math.max(0, 80+millispassed-System.currentTimeMillis()));
			millispassed=System.currentTimeMillis();
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	}
	
	public static void threadSleep(){
		try {	
			Thread.sleep(Math.max(0, 500));
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	
	public static void makeGrass(BufferedImage bimg){
		Random RND=new Random();
		
		int grassiness=(bimg.getWidth()*bimg.getHeight())/25;
		Graphics buffergraphics=bimg.getGraphics();
		buffergraphics.setColor(new Color(20, 60 , 4));
		buffergraphics.fillRect(0,0,bimg.getWidth(),bimg.getHeight());
		
		
		buffergraphics.setColor(new Color(40, 100 , 9));
		
		for(int i=0; i<grassiness; i++){
			
			int xposition=RND.nextInt(bimg.getWidth());
			int yposition=RND.nextInt(bimg.getHeight());
			int sidelen=RND.nextInt(3);
			int uplen=RND.nextInt(7);
			
			buffergraphics.drawLine(xposition, yposition , xposition, yposition-uplen);
			buffergraphics.drawLine(xposition, yposition , xposition+sidelen,yposition);
		}
		/////////
		printHelp(buffergraphics, new Color(50, 150 , 15), 10 , 500);
		printHelp(buffergraphics, new Color(50, 150 , 15), 11 , 500);
	}
	
	public static void traverseOneStep(Tile[][]map, int[] function){
		
		Tile [] mapasarray=new Tile[map.length*map[0].length];
		Tile [] result=new Tile[map.length*map[0].length];
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[i].length; j++){
				int onedimention=i*map.length+j;
				mapasarray[onedimention]=map[i][j];
			}	
		}
		
		for(int i=0; i<mapasarray.length; i++){result[function[i]]=mapasarray[i];}
		
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[i].length; j++){
				int onedimention=i*map.length+j;
				map[i][j]=result[onedimention];
			}	
		}
	}
	
	public static int[] inverse(int[] array){
		int[] rtn=new int[array.length];
		for(int i=0; i<array.length; i++){
			rtn[array[i]]=i;
		}
		return rtn;
	}
	
	public static void plantTrees(Tile[][] map){
		Random RND=new Random();
		int planted=0;
		if(RND.nextBoolean()){
			map[1][1].type=Type.tree;
			map[3][3].type=Type.tree;

			map[1][3].type=Type.tree;
			map[3][1].type=Type.tree;
			planted++;

		}
		if(RND.nextBoolean()){
			map[2][1].type=Type.tree;
			map[1][2].type=Type.tree;
			map[3][3].type=Type.tree;
			planted++;
		}
		if(RND.nextBoolean()){
			map[1][1].type=Type.tree;
			map[2][3].type=Type.tree;
			map[3][2].type=Type.tree;
			planted++;
		}
		if(planted>0)
			map[2][2].treasure=true;//plant treasure
		else
			plantTrees(map);
	}
	
	public static void resetMap(){
		for(Tile[] t: map){
			for(int i=0; i<t.length; i++){t[i]=new Tile();}
		}
		map[0][0].type=Type.player;
		plantTrees(map);
		Random RND=new Random();
		
		for(int i=0; i<96; i++){
			int amount=-1+2*RND.nextInt(2);
			if(RND.nextBoolean()){move(map , amount , 0);}else{move(map , 0 , amount);}
		}
	}
	
	public static void makeStars(BufferedImage bimg){
		Random RND=new Random();
		
		int stariness=(bimg.getWidth()*bimg.getHeight())/150;
		Graphics buffergraphics=bimg.getGraphics();
		buffergraphics.setColor(new Color(13,10,16));
		buffergraphics.fillRect(0,0,bimg.getWidth(),bimg.getHeight());
		
		
		
		for(int i=0; i<stariness; i++){
			double strength=Math.sqrt(200)*RND.nextDouble();
			strength*=strength;
			buffergraphics.setColor(new Color(13+(int)strength,10+(int)strength,16+(int)strength));
			int xposition=RND.nextInt(bimg.getWidth());
			int yposition=RND.nextInt(bimg.getHeight());
			
			buffergraphics.fillRect(xposition, yposition , 2, 2);
		}
		
	}
	
	public static int[] findPlayer(Tile[][] map){
		
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[i].length; j++){
				if(map[i][j].type==Type.player)return new int[]{i,j};
			}
		}
		return new int[]{-1,-1};
	} 
	
	public static void movePlayer(Tile[][] map , KeyBoard keyboard){
		if(keyboard.returnKeyTyped()==KeyEvent.VK_LEFT)
			traverseOneStep(map,traversefunction);
			
		if(keyboard.returnKeyTyped()==KeyEvent.VK_RIGHT)
			traverseOneStep(map,inversetraverse);
			
		if(keyboard.returnKeyTyped()==KeyEvent.VK_A)
			move(map, -1 , 0);
			
		if(keyboard.returnKeyTyped()==KeyEvent.VK_S)
			move(map, 0 , 1);
			
		if(keyboard.returnKeyTyped()==KeyEvent.VK_D)
			move(map, 1 , 0);
			
		if(keyboard.returnKeyTyped()==KeyEvent.VK_W)
			move(map, 0 , -1);
	}
	
	public static void move(Tile[][] map , int xdir , int ydir){
		int[] playerposition=findPlayer(map);
		
		if(playerposition[0]+xdir<0 || playerposition[1]+ydir<0 || playerposition[0]+xdir>=map.length || playerposition[1]+ydir>=map[playerposition[0]].length)
			return;
			
		if(contains(map, traversefunction, playerposition, xdir, ydir)){
			traverseOneStep(map,traversefunction);
			return;
		}
		
		if(contains(map, inversetraverse, playerposition, xdir, ydir)){
			traverseOneStep(map,inversetraverse);
			return;
		}
		
		if(map[playerposition[0]+xdir][playerposition[1]+ydir].type==Type.empty){
			Type tmp=map[playerposition[0]+xdir][playerposition[1]+ydir].type;
			map[playerposition[0]+xdir][playerposition[1]+ydir].type=map[playerposition[0]][playerposition[1]].type;
			map[playerposition[0]][playerposition[1]].type=tmp;
		}
		
	}
	
	public static boolean contains(final Tile[][] map, final int[] traversefun, final int[] playerposition , int xdir , int ydir){
		
		int index=playerposition[0]*map.length+playerposition[1];
		int newindex=index+xdir*map.length+ydir;
		return traversefun[index]==newindex;
	}
	
	public static void makeImage(BufferedImage image, int[][] frame, Color c){
		Graphics graphics=image.getGraphics();
		
		int ratio=image.getWidth()/frame.length;
		for(int i=0; i<frame.length; i++){
			for(int j=0; j<frame[i].length; j++){
				if(frame[j][i]==1){
					graphics.setColor(c);
				}else{
					graphics.setColor(new Color(0,0,0,0));
				}
				graphics.fillRect(i*ratio,j*ratio,ratio,ratio);
			}
		}
	}
	
	public static void printHelp(Graphics g, Color c, int x ,int y){
		g.setColor(c);
		// read the rules
		g.drawString("You are a pirate.", x, y);
		g.drawString("The treasure is in the middle of a small forest, according to an old map.", x, y+12);
		g.drawString("Use, wasd, right and left keys to navigate.", x, y+24);
		g.drawString("Dig up the treasue using the Enter key.", x, y+36);
	}
	
	public static void makeTree(BufferedImage image){
		Graphics g=image.getGraphics();
		int wi=image.getWidth();
		int he=image.getHeight();
		Random RND=new Random();
		g.setColor(new Color(0,0,0,0));
		g.fillRect(0,0,wi,he);
		
		g.setColor(new Color(75,25,15));
		g.fillRect(wi/2,0,2,he);
		
		g.setColor(Color.green);
		
		for(int i=0; i<5*he/6; i+=2){
			int rilen=1+(2*i)/5+RND.nextInt(5);
			int lelen=1+(2*i)/5+RND.nextInt(5);
			g.setColor(new Color(25-RND.nextInt(25),125-RND.nextInt(25),65-RND.nextInt(25)));
			g.fillRect(wi/2,i,rilen,1);
			g.setColor(new Color(25-RND.nextInt(25),125-RND.nextInt(25),65-RND.nextInt(25)));
			g.fillRect(wi/2-lelen,i,lelen,1);
		}	
	}
}
