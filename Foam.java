import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;


public class Foam {
	
	static int[][] points;
	static final int size=800;

	public static void main(String[] args) {
		
		points=new int[1000][2];
		
		JFrame jframe=new JFrame();
		
		jframe.setSize(size, size);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Graphics graphics=jframe.getGraphics();
		Random RND=new Random();
		
		for(int i=0; i<points.length; i++){
			points[i][0]=RND.nextInt(size);
			points[i][1]=RND.nextInt(size);
		}
			
		ArrayList<Triangle>trianglelist=new ArrayList<Triangle>();
		
		trianglelist.add(new Triangle(points[RND.nextInt(points.length)], 
				points[RND.nextInt(points.length)], points[RND.nextInt(points.length)]));
		
		while(true){
			boolean listchange=false;
			ArrayList<Triangle>tmp=new ArrayList<Triangle>();
			
			while(!trianglelist.isEmpty()){
				Triangle t=trianglelist.remove(0);
				boolean change=false;

				for(int[] point:points){
					if(!t.isendpoint(point) && t.inside(point)){
						listchange=true;
						change=true;
						tmp.add(new Triangle(t.p1, t.p2, point));
						tmp.add(new Triangle(t.p1, t.p3, point));
						tmp.add(new Triangle(t.p2, t.p3, point));
						break;
					}

				}
				
				if(!change)tmp.add(t);
			}
			
			if(!listchange){
				
				trianglelist=tmp;
				break;
			}else{
				trianglelist=tmp;
			}
		}
			
		while(true){
		
			for(int[] point:points){
				graphics.setColor(Color.green);
				graphics.fillRect(point[0], point[1], 4, 4);	
			}
			
			for(Triangle t : trianglelist)
				t.paint(graphics);
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	Integer hashval(int v1, int v2){return (v2<<16)+v1;}

}

class Triangle{
	
	int[] p1,p2,p3;
	
	public Triangle(int p1[] , int p2[], int p3[]){
		this.p1=p1;
		this.p2=p2;
		this.p3=p3;
	}
	
	public void paint(Graphics g){
		g.drawLine(p1[0], p1[1], p2[0], p2[1]);
		g.drawLine(p1[0], p1[1], p3[0], p3[1]);
		g.drawLine(p2[0], p2[1], p3[0], p3[1]);
	}
	
	public boolean inside(int[] p4){
		double[][] pointperm1=new double[3][];
		
		pointperm1[0]=new double[]{p1[0],p1[1]};
		pointperm1[1]=new double[]{p2[0],p2[1]};
		pointperm1[2]=new double[]{p3[0],p3[1]};
		
		double[][] pointperm2=new double[3][];
		
		pointperm2[1]=new double[]{p1[0],p1[1]};
		pointperm2[2]=new double[]{p2[0],p2[1]};
		pointperm2[0]=new double[]{p3[0],p3[1]};
		
		double[][] pointperm3=new double[3][];
		
		pointperm3[2]=new double[]{p1[0],p1[1]};
		pointperm3[0]=new double[]{p2[0],p2[1]};
		pointperm3[1]=new double[]{p3[0],p3[1]};
		
		return inangle(pointperm1 ,p4)&&inangle(pointperm2 ,p4)&&inangle(pointperm3 ,p4);		
	}
	
	boolean inangle(double[][] triangle ,int[] p){
		
		double firstangle=angle(triangle[0], triangle[1]);
		double secondanangle=angle(triangle[0], triangle[2]);
		double testangle=angle(triangle[0], new double[]{p[0],p[1]});
		
		if(firstangle>secondanangle){
			double tmp=secondanangle;
			secondanangle=firstangle;
			firstangle=tmp;
		}
		
		if(secondanangle-firstangle<Math.PI){
			return firstangle<testangle && testangle<secondanangle;
		}else{
			
			return testangle<firstangle || secondanangle<testangle;
		}
		
	}
	
	double angle(double[] p1, double[] p2){
		double rtn=Math.atan((p2[1]-p1[1])/(p2[0]-p1[0]));
		
		if(p2[0]<p1[0]){
			if(rtn<Math.PI)rtn+=Math.PI;else rtn-=Math.PI;
		}
		
		return rtn;
	}
	
	public boolean isendpoint(int[] p){
		return (p1[0]==p[0]&&p1[1]==p[1])||(p2[0]==p[0]&&p2[1]==p[1])||(p3[0]==p[0]&&p3[1]==p[1]);
	}
	
}
