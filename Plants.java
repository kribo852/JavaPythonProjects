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

/**
 * 
 *I know it is a messy ****, the program itself contains a fair deal of junk DNA.
 * It is an atempt to make evolving plants, judge yourself the success. 
 * Play around with the settings of intrest, sun intency, reproduction and growth energy consumption and such.
 * Also, the foliage sun uptake calculation made in Foliage and addLeaves() are a bit fishy, feel free to improve
 * 
 */


class main{
	
static Random RND=new Random();
	
public static void main(String[] args){
	
	int size=1200, longestgenseq=0;
	
	BufferedImage buffer=new BufferedImage( size, size,1); 
	Graphics buffergraphics=buffer.getGraphics();
	
	SaveFrame jframe=new SaveFrame(buffer, false);
	
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setVisible(true);
	jframe.setSize(size , size);
	
	
	Graphics jframegraphics=jframe.getGraphics(); 
	
	Plant[] plantmap=new Plant[size]; 
		
	Foliage[] canopy=new Foliage[size];
	
	while(true){
		buffergraphics.setColor(Color.black);
		buffergraphics.fillRect(0,0,size,size);
		
		{
			boolean allnull=true;
			for(int i=0; i<plantmap.length; i++){if(plantmap[i]!=null){allnull=false; break;}}
			if(allnull){
				System.out.println("new life");
				int position=RND.nextInt(size);
				plantmap[position]=new Plant(makeGenes() , position , 25);
			}
		}
		
		for(int i=0; i<0; i++){
			int position=RND.nextInt(size);
			
			plantmap[position]=null;
			//plantmap[position]=new Plant(makeGenes() , position , 25);
			//addLeaves(plantmap[position],plantmap[position].returnBase(position),canopy,size);
		}//remove and add simultaenuslu
		
		for(int i=0; i<canopy.length; i++){
			if(canopy[i]!=null)canopy[i].photoSynthesize();
		}
		
		
		for(int i=0; i<plantmap.length; i++){
			if(plantmap[i]!=null){
				
				plantmap[i].energyConsume();
				addLeaves(plantmap[i], plantmap[i].update(i), canopy, size);
				
				ArrayList<Gene> getgenome=plantmap[i].spread();
				
				if(getgenome!=null){
					for(int mutaterate=0; mutaterate<8; mutaterate++)mutateGenes(getgenome);
					
					int tmpx=i+(RND.nextInt(size)-RND.nextInt(size))/10;//spread area calculation
					tmpx=Math.max(0, Math.min(tmpx, size-1));
					if(plantmap[tmpx]==null){
						plantmap[tmpx]=new Plant(getgenome , tmpx, plantmap[i].returnSeedEnergy());
						addLeaves(plantmap[tmpx],plantmap[tmpx].returnBase(tmpx),canopy,size);
					}
					
					if(getgenome.size()>longestgenseq){
						System.out.println("genome size"+getgenome.size());
						for(Gene g: getgenome){
							System.out.print(g.type+" ");
						}	
						System.out.println();
						longestgenseq=getgenome.size();
					}
				}
				
				
				if(plantmap[i].getEnergy()<0){
					plantmap[i]=null;
				}	
			}
		}
		
		clearDeadPlants(plantmap, canopy);
		
		for(int i=0; i<size; i++){
			if(plantmap[i]!=null){
				plantmap[i].paint(buffergraphics, size);//position already present	
			}	
		}
		
		jframegraphics.drawImage(buffer , 0 , 0 , size , size , null);	
		try {
				Thread.sleep(250);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	static ArrayList<Gene> makeGenes(){
		ArrayList<Gene> rtn=new ArrayList<Gene>();
		
		{
			Gene gene=new Gene();
			gene.length=RND.nextDouble()*100;
			gene.angle=(Math.PI/2)+(Math.PI/2)*(RND.nextDouble()-0.5);
			gene.type=Type.wait;
			gene.symmetry=RND.nextBoolean();
			rtn.add(gene);
		}
		
		for(int i=0; i<12; i++){
			rtn.add(makeGene());
		}
		return rtn;
	}
	
	static Gene makeGene(){
		
		int choice=RND.nextInt(3);
		Gene gene=new Gene();
		switch(choice){
		
			case(0)://std growth gene
				gene.length=10+RND.nextDouble()*125;
				gene.angle=(Math.PI*2)*(RND.nextDouble());
				gene.type=Type.grow;
				gene.symmetry=RND.nextInt(5)!=0;
			break;
		
			case(1):
				gene.type=Type.seed;
			break;
		
			case(2):
				gene.type=Type.wait;
			break;
			case(3):
				gene.type=Type.die;
			break;
		
		}
		return gene;
	}
	
	public static void mutateGenes(ArrayList<Gene> genes){
		if(RND.nextInt(2)==0){//otherwise don't mutate
			if(RND.nextInt(9)<4){
				genes.add(RND.nextInt(genes.size()), makeGene());
			}else if(genes.size()>5){
				genes.remove(RND.nextInt(genes.size()));
			}
		}
	}
		

	
	public static void addLeaves(Plant plant, ArrayList<Rectangle> stalk, Foliage[] canopy, int size){
		
		for(Rectangle rect: stalk){
			for(double i=0; i<rect.getWidth(); i++){
				double height=rect.getY()+(i/rect.getWidth())*rect.getHeight();
				int x=(int)(i+rect.getX());
				
				if(x>=0 && x<size && height>=0 && height<size){
					if(canopy[x]==null){
						canopy[x]=new Foliage();
					}
					canopy[x].addFoliage(plant, height);
				}
			}
		}
	}
	
	static void clearDeadPlants(Plant[] plantmap, Foliage[] canopy){
		HashSet<Plant> tmp= new HashSet<Plant>();
		for(Plant p: plantmap){
			if(p!=null){
				tmp.add(p);
			}
		}
		for(Foliage f: canopy){
			if(f!=null){
				f.removeFoliage(tmp);
			}
		}
	}
	
}

enum Type{wait, grow, seed, sense, die}

class Plant{
	double energydeposit;
	
	Random RND=new Random();
	ArrayList<Gene> genepool; 
	Iterator<Gene> developphase;
	HashSet<Rectangle> LineSet;
	PlantPart base;
	int position;
	boolean spreadflag;
	static final double seedenergyfraction=0.1;
	static final double maxeddeposit=10000;
	final int reproductiveenergy=1;
	
	public Plant(final ArrayList<Gene> genes, int x, double energydeposit){
		genepool=new ArrayList<Gene>();
		genepool.addAll(genes);
		developphase=genepool.iterator();
		
		base=new PlantPart(genepool.get(0).length, genepool.get(0).angle);//does not care bout Type, but why not
		developphase.next();
		LineSet=new HashSet<Rectangle>();
		LineSet.add(base.calculateLeaves(x,0));
		
		this.energydeposit=energydeposit;
		position=x;
		spreadflag=false;
	}
	
	public void energyConsume(){energydeposit-=Math.pow(base.calculateArea(), 3.0/4.0);}
	
	public double getEnergy(){return energydeposit;}
	
	public void addEnergy(double energy){energydeposit+=energy;}
	
	void paint(Graphics g, int screensize){
		g.setColor(Color.green);
		for(Rectangle line: LineSet){
			g.drawLine((int)line.getX(), screensize-300-(int)line.getY() ,(int)(line.getX()+line.getWidth()) ,screensize-300-(int)(line.getY()+line.getHeight()));
		}
	} 
	
	public ArrayList<Rectangle> returnBase(int x){
		ArrayList<Rectangle> rtn=new ArrayList<Rectangle>();
		rtn.add(base.calculateLeaves(x,0));
		return rtn;
	}
	
	public ArrayList<Rectangle> update(int x){
		
		ArrayList<Rectangle> rtn=new ArrayList<Rectangle>();
		
		if(developphase.hasNext()){
			Gene g=developphase.next();
			if(g.type==Type.grow){
				base.grow(g,rtn,x,0);
				energyConsume();//energy consume when growth
			}
			
			if(g.type==Type.seed){
				energydeposit-=reproductiveenergy;
				spreadflag=true;
			}
			
			if(g.type==Type.die){
				energydeposit=-1000000;
			}
			
		}else{energydeposit=-1000000;/*spreadflag=true;*/}
		LineSet.addAll(rtn);
		
		if(energydeposit>maxeddeposit)energydeposit=maxeddeposit;
		
		return rtn;
	}
	
	public ArrayList<Gene> spread(){
		ArrayList<Gene> rtn=null;
		if(spreadflag){
			spreadflag=false;
			rtn=new ArrayList<Gene>(); 
			rtn.addAll(genepool);
		}
		return rtn;
	}
	
	public double returnSeedEnergy(){
		double rtn=energydeposit*seedenergyfraction;
		energydeposit-=rtn;
		return rtn;
	}
	
	class PlantPart{
		double length, angle, depth;//depth is way to the top
		PlantPart[] next;
		
		public PlantPart(double length, double angle){
			this.length=length;
			this.angle=angle;
			depth=1;
			next=null;
		}
		//int calculateDepth(){return (next==null ? 1 : 1+Math.max(next[0].calculateDepth(), next[1].calculateDepth()));}
		double calculateArea(){
			
			if(next==null){
				return length;
			}else{
				double rtn=depth*length;
				for(PlantPart pp: next){rtn+=pp.calculateArea();}
				return rtn;
			}
			
		}
		
		void grow(Gene gene, ArrayList<Rectangle> newbranches, double x, double y){
			
			if(next==null){
				if(gene.symmetry){
					next=new PlantPart[2];
					next[0]=new PlantPart(gene.length, gene.angle-angle);
					next[1]=new PlantPart(gene.length, gene.angle+angle);
				}else{
					next=new PlantPart[1];
					next[0]=new PlantPart(gene.length, gene.angle+angle);
				}
				
				for(PlantPart p: next){
					newbranches.add(p.calculateLeaves(x+length*Math.cos(angle),y+length*Math.sin(angle)));
				}
			}else{
				for( PlantPart pp: next)pp.grow(gene, newbranches, x+length*Math.cos(angle), y+length*Math.sin(angle));
				//next[RND.nextInt(next.length)].grow(gene, newbranches, x+length*Math.cos(angle), y+length*Math.sin(angle));//only one of the branches
				//might shift between trees with same genome
			}
			depth++;//increase depth for all branches
		}
		
		Rectangle calculateLeaves(double x, double y){
			return new Rectangle((int)x,(int)y,(int)(length*Math.cos(angle)),(int)(length*Math.sin(angle)));
		}	
	}
}
	
class Gene{		
	public double length;//wait or growlength, or seedinterval
	public double angle;
	public boolean symmetry;
	public Type type=Type.wait;
}

class SaveFrame extends JFrame{
	
	BufferedImage buffer;
	String savename="save.png";
		
	public SaveFrame(){
			
		addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent e){
			try{
				ImageIO.write(buffer, "png" ,new File(savename));
			}catch(IOException moon){}
				
		}
	});
	}
	
	public SaveFrame(BufferedImage buffer, boolean save){
		
		this.buffer=buffer;
		
		if(!save)return;	
		
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

class Foliage{
	
	TreeMap<Double,Plant>leafheights;//hello  
	static final double sunshine=16;
	static final double sunuptake=0.2;
	
	public Foliage(){
		leafheights=new TreeMap<Double,Plant>();
	}
	
	public void addFoliage(Plant plant, Double height){
		leafheights.put(height, plant);
	}
	
	public void removeFoliage(HashSet<Plant> keep){
		TreeMap<Double,Plant>newmap=new TreeMap<Double,Plant>();
		
		for(Entry<Double,Plant> entry : leafheights.entrySet()){
			if(keep.contains(entry.getValue())){	
				newmap.put(entry.getKey(), entry.getValue());
			}
		}
		leafheights=newmap;
	}
	
	public void photoSynthesize(){
		double remaininglight=sunshine;
		for(Entry<Double,Plant> entry: leafheights.descendingMap().entrySet()){
			((Plant)entry.getValue()).addEnergy(remaininglight*sunuptake);
			remaininglight*=(1-sunuptake);
		}
	}	
}
