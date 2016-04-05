import java.lang.*;
import java.util.Random;

class JavaPi{

public static void main(String []args){

	System.out.println("MonteCarlo pi is "+monteCarloPi(1000));
	System.out.println("circumference sum pi is "+circumferenceSumPi(25));

}

public static double monteCarloPi(int iterations){

	double sum=0;

   for(int i=0; i<iterations; i++){
	Random RND=new Random();
	double x=2*RND.nextDouble()-1;//radius=1;
	double y=2*RND.nextDouble()-1;

	if(x*x+y*y<1)
		sum++;
   }


	return 4*(sum/iterations);
}

public static double circumferenceSumPi(int partitions){
	double sum=0;

	double triangleSide=1.0/Math.sqrt(2.0);// the side of a triangle with hypotenusa 1

	double partitionYLength=triangleSide/partitions;

	for(int i=0; i<partitions; i++){

		double yPos=i*partitionYLength;
		double yNextPos=(i+1)*partitionYLength;

		double xPos=Math.sqrt(1-yPos*yPos);
		double xNextPos=Math.sqrt(1-yNextPos*yNextPos);

		sum=sum+Math.sqrt((xPos-xNextPos)*(xPos-xNextPos)+partitionYLength*partitionYLength);
	}
	return sum*4;
}

}