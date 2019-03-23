import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;


public class Foam {
	
	static int[][] points;
	static final int size=1000;

	public static void main(String[] args) {
		
		points=new int[500][2];
		Color colours[] = new Color[500];
		
		JFrame jframe=new JFrame();
		
		jframe.setSize(size, size);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Graphics graphics=jframe.getGraphics();
		Random RND=new Random();
		
		for(int i=0; i<points.length; i++){
			points[i][0]=RND.nextInt(size);
			points[i][1]=RND.nextInt(size);
			colours[i] = new Color(RND.nextInt(128)+128 , RND.nextInt(64), RND.nextInt(96));

			if(RND.nextInt(5)==0) {
					int yellow =RND.nextInt(98)+32;
					colours[i] = new Color(255 , yellow, RND.nextInt(32));
			}			

			if(sqrlen(points[i][0], size/2, points[i][1], size/2)>(size*size/8)) {

				if(RND.nextInt(10)==0) {
					colours[i] = new Color(RND.nextInt(32)+32 , RND.nextInt(128)+32, RND.nextInt(32));
				} else {
					i--;
				}

			}
		}
			
		for(int x=0; x<size; x++)for(int y=0; y<size; y++) {
			int minpointintdex = 0;
			int minlength = size*size;

			for(int i=0; i<points.length; i++) {

				int[] point = points[i];

				int sqrlen = sqrlen(x, point[0] , y , point[1]);
				if(sqrlen < minlength){
					minpointintdex = i;
					minlength = sqrlen;
				}
			}

			Color c = colours[minpointintdex];

			graphics.setColor(c);
			graphics.fillRect(x,y,1,1);

		}
			
	}

	public static int sqrlen(int x1, int x2, int y1, int y2) {
		return (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
	}
	
}
