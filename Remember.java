//A homework from school 

import java.io.*;
import javax.swing.*;
import java.lang.StringBuilder;
import java.util.ArrayList;

class Remember{

    public static void main(String[] args){
	JFileChooser jfilechooser=new JFileChooser();
	JFrame jframe=new JFrame();
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	int returnVal = jfilechooser.showOpenDialog(jframe);
	if(returnVal != JFileChooser.APPROVE_OPTION) {
	    return;
	}

  
	String instring="";

	try{
	    FileReader filereader=new FileReader(jfilechooser.getSelectedFile());
	    StringBuilder sb=new StringBuilder();
	    while(filereader.ready())sb.append((char)filereader.read());
	    instring=sb.toString();
	}catch(IOException io){
	    return;
	}

	String phrase="Remember a day before today";

	char[] c=reConfigure(instring.toCharArray(), phrase.toCharArray());
	for(int i=0; i<1000; i++)
	    System.out.print(c[i]);

	System.out.println();
	c=reConfigure(c, phrase.toCharArray());

	for(int i=0; i<1000; i++)
	    System.out.print(c[i]);
    }

    public static char[] reConfigure(char[] in, char[] phrase){
	
	int[] countlen={0};
	for(int i=0;  i<in.length; i++){
	    ArrayList<Character> phraselist=new ArrayList<Character>();
	    for(char c: phrase)phraselist.add(c);
	    in[i]=calcCombination(in[i],countlen,phraselist,phraselist.size()/2-1);
	    //  System.out.println();
	}
	
	
	return in;
    }

    public static char calcCombination(char inchar , int[] previous,  ArrayList<Character> phraselist, int halfsize){
	
	char tmp=phraselist.remove(previous[0]%phraselist.size());

	inchar=(char)(inchar^tmp);
	
	//System.out.print(previous[0]%phraselist.size()+" ");
	previous[0]=(previous[0]+tmp+1);
	
	if(halfsize<phraselist.size())
	    return calcCombination(inchar , previous , phraselist, halfsize);

	return inchar;
    }

    public static boolean relative(int a, int b){
	{
	    int tmp=Math.min(a,b);
	    b=Math.max(a,b);
	    a=tmp;
	} 
	
	int c=b%a;
	if(c==1)return true;
	if(c==0)return false;

	return relative(c,a);
    }


}
