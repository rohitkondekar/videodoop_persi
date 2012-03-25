import static com.googlecode.javacv.cpp.opencv_core.*;

import static com.googlecode.javacv.cpp.opencv_highgui.*;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class Comparator 
{
	public static void main(String[] args) throws IOException 
	{
		System.out.println("start");
		long startTime = System.currentTimeMillis();
		Initializer.initialize();
		int c1=0,c2=0,c3=0,c4=0,c5=0;
		int[] arr=new int[55751];
		for(int i=0;i<55751;i++)
			arr[i]=0;
		int idx=0;
		double sim_1D=0,sim_2D=0,sim_apidq=0,sim_HSV=0;
		String source_dir=Initializer.QUERY_DIR;
		File directory = new File(source_dir);
		String[] infiles = directory.list();
		for(int i=0;i<infiles.length;i++)
		{//System.out.println(infiles.length);
			String query_file=source_dir+infiles[i];//System.out.println(query_file);
			//String query_file = Initializer.QUERY_DIR+"347gray.png";
			IplImage query_img = null;
			IplImage query_img_edge = null;
			
			String infile;
			BufferedReader br;
			String strline="";
			
			idx=0;	
			query_img = cvLoadImage(query_file,CV_LOAD_IMAGE_COLOR);
			float[] query_corner_hist=new float[Initializer.NUM_BINS_DIST_1D];
			CornerDetector.findCorners(query_img,query_corner_hist);//
			//FileWriter corner_result_file = new FileWriter(Initializer.CORNER_COMPARISON_RESULT_FILE);
			float[] corner_hist=new float[Initializer.NUM_BINS_DIST_1D];
			infile = Initializer.CORNER_FEATURE_FILE;
			br = new BufferedReader(new FileReader(infile));
			while((strline=br.readLine())!=null)
			{
				String fn=getCornerFeatures(strline,corner_hist);
				
				sim_1D=Histogram.compareHist1D(query_corner_hist,corner_hist)*100;
				if(sim_1D>Initializer.THRESHOLD_1D){
					/*corner_result_file.append(fn+"\t"+sim_1D +"\n");*/c1++;arr[idx]++;}
				idx++;
			}
			//corner_result_file.flush();
			//corner_result_file.close();
			for(int j=0;j<55751;j++)
				if(arr[j]==1)
					c5++;
			
			
			idx=0;
			query_img = cvLoadImage(query_file,CV_LOAD_IMAGE_GRAYSCALE);
			int[][] query_apidq_hist=new int[Initializer.NUM_BINS_DIST_APIDQ][Initializer.NUM_BINS_ANGLE_APIDQ];
			Histogram.generateHistAPIDQ(query_img, query_apidq_hist);
			//FileWriter apidq_result_file = new FileWriter(Initializer.APIDQ_COMPARISON_RESULT_FILE);
			int[][] apidq_hist=new int[Initializer.NUM_BINS_DIST_APIDQ][Initializer.NUM_BINS_ANGLE_APIDQ];
			infile = Initializer.APIDQ_FEATURE_FILE;
			br = new BufferedReader(new FileReader(infile));
			while((strline=br.readLine())!=null)
			{if(arr[idx]==1){
				String fn =getAPIDQFeatures(strline,apidq_hist);
				
				sim_apidq = Histogram.compareHistAPIDQ(query_apidq_hist,apidq_hist)*100;//	System.out.print(fn+"\t"+sim_apidq+"\n");	
				if(sim_apidq > Initializer.THRESHOLD_APIDQ){
					/*apidq_result_file.append(fn+"\t"+sim_apidq+"\n");*/c2++;arr[idx]++;}
				}idx++;
			}
			//apidq_result_file.flush();
			//apidq_result_file.close();
			
			
			idx=0;
			query_img = cvLoadImage(query_file,CV_LOAD_IMAGE_COLOR);
			int[][] query_hsv_hist=new int[Initializer.NUM_BINS_HSV_HUE][Initializer.NUM_BINS_HSV_SAT];
			//int[][][] query_bgr_hist=new int[Initializer.NUM_BINS_BGR_BLU][Initializer.NUM_BINS_BGR_GRN][Initializer.NUM_BINS_BGR_RED];
			//Histogram.generateHistBGR(query_img, query_bgr_hist);
			Histogram.generateHistHSV(query_img, query_hsv_hist);
			//FileWriter color_result_file = new FileWriter(Initializer.COLOR_COMPARISON_RESULT_FILE);
			int[][] HSV_hist=new int[Initializer.NUM_BINS_HSV_HUE][Initializer.NUM_BINS_HSV_SAT];
			//int[][][] BGR_hist=new int[Initializer.NUM_BINS_BGR_BLU][Initializer.NUM_BINS_BGR_GRN][Initializer.NUM_BINS_BGR_RED];
			infile = Initializer.COLOR_FEATURE_FILE;
			br = new BufferedReader(new FileReader(infile));
			while((strline=br.readLine())!=null)
			{if(arr[idx]==2){
				String fn=getColorFeatures(strline, HSV_hist);
				//double sim_BGR=Histogram.CompareHistBGR(query_bgr_hist, BGR_hist)*100;
				sim_HSV=Histogram.CompareHistHSV(query_hsv_hist, HSV_hist)*100;
				if(sim_HSV>Initializer.THRESHOLD_HSV){
					/*color_result_file.append(fn+"\t"+ sim_HSV +"\n");*/c3++;arr[idx]++;}
				}idx++;
			}
			//color_result_file.flush();
			//color_result_file.close();
			
			
			idx=0;
			query_img = cvLoadImage(query_file,CV_LOAD_IMAGE_GRAYSCALE);
			query_img_edge = cvCreateImage(cvGetSize(query_img), query_img.depth(), 1);
			EdgeFinder.getegdes(query_img,query_img_edge);
			float[][] query_edge_hist=new float[Initializer.NUM_BINS_DIST_2D][Initializer.NUM_BINS_ANGLE_2D];
			ContourFinder.findContour(query_img_edge,query_edge_hist);
			//FileWriter edge_result_file = new FileWriter(Initializer.EDGE_COMPARISON_RESULT_FILE);
			float[][] edge_hist=new float[Initializer.NUM_BINS_DIST_2D][Initializer.NUM_BINS_ANGLE_2D];
			infile = Initializer.EDGE_FEATURE_FILE;
			br = new BufferedReader(new FileReader(infile));
			while((strline=br.readLine())!=null)
			{if(arr[idx]==3){
				String fn=getEdgeFeatures(strline, edge_hist);
				sim_2D=Histogram.compareHist2D(query_edge_hist, edge_hist)*100;
				if(sim_2D>Initializer.THRESHOLD_2D){
					/*edge_result_file.append(fn+"\t"+sim_2D +"\n");*/c4++;arr[idx]++;}
				}idx++;
			}
			//edge_result_file.flush();
			//edge_result_file.close();
			//if((sim_1D>Initializer.THRESHOLD_1D) && (sim_apidq>Initializer.THRESHOLD_APIDQ) &&	(sim_HSV>Initializer.THRESHOLD_HSV) && (sim_2D>Initializer.THRESHOLD_2D))
				//c5++;
			
			 
		}	
		System.out.println("filtered"+c5/10);
		System.out.println("corner:"+c1/10);
		System.out.println("apidq:"+c2/10);
		System.out.println("color:"+c3/10);
		System.out.println("edge:"+c4/10);
		for(int i=0;i<55751;i++)
			if(arr[i]==4)
				c5++;
		System.out.println("all:"+c5/10);
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		System.out.println("finished");	
	}

	public static String getEdgeFeatures(String strline,float[][] edge_hist) throws IOException
	{
		StringTokenizer st=new StringTokenizer(strline,"\t");
		String fn=null;
		
		if(st.hasMoreTokens())
			fn=st.nextToken();//filename
		int i=0;
		while(st.hasMoreTokens())
		{
			StringTokenizer st2=new StringTokenizer(st.nextToken().toString(),",");
						
			int j=0;
			while(j<Initializer.NUM_BINS_ANGLE_2D && i<Initializer.NUM_BINS_DIST_2D && st2.hasMoreTokens())
			{
				edge_hist[i][j]=Float.parseFloat(st2.nextToken().toString());
				j++;
			}
			i++;
		}
		return fn;
	}
	
	public static String getAPIDQFeatures(String strline,int[][] apidq_hist) throws IOException
	{
		StringTokenizer st=new StringTokenizer(strline,"\t");
		String fn=null;
		if(st.hasMoreTokens())
			fn=st.nextToken();//filename
		int i=0;
		while(st.hasMoreTokens())
		{
			StringTokenizer st2=new StringTokenizer(st.nextToken().toString(),",");
						
				int j=0;
				while(j<Initializer.NUM_BINS_ANGLE_APIDQ && i<Initializer.NUM_BINS_DIST_APIDQ && st2.hasMoreTokens())
				{
					apidq_hist[i][j]=Integer.parseInt(st2.nextToken().toString());
					j++;
				}
				i++;
			
		}
		return fn;
	}
	
	public static String getCornerFeatures(String strline,float[] corner_hist) throws IOException
	{
		StringTokenizer st=new StringTokenizer(strline,"\t");
		String fn=null;
		boolean flag=false;
		if(st.hasMoreTokens())
			fn=st.nextToken();//filename
		int i=0;
		while(st.hasMoreTokens())
		{
			StringTokenizer st2=new StringTokenizer(st.nextToken().toString(),",");
						
			if(!flag)
			{
				flag=true;
				while(i<Initializer.NUM_BINS_DIST_1D && st2.hasMoreTokens())
				{
					corner_hist[i]=Float.parseFloat(st2.nextToken().toString());
					i++;
				}
				i=0;
			}
		}
		return fn;
		
	}
	
	public static String getColorFeatures(String strline,int[][] HSV_hist) throws IOException
	{
		
		
		StringTokenizer st=new StringTokenizer(strline,"\t");
		String fn=null;
		boolean flag=false;
		if(st.hasMoreTokens())
			fn=st.nextToken();//filename
		//System.out.println(fn);
		int i=0,j=0,k=0;int count =0;
		while(st.hasMoreTokens())
		{	
			StringTokenizer st2=new StringTokenizer(st.nextToken().toString(),",");
						
			while(i<Initializer.NUM_BINS_HSV_HUE && j<Initializer.NUM_BINS_HSV_SAT && st2.hasMoreTokens())
			{
				HSV_hist[i][j]=Integer.parseInt(st2.nextToken().toString());
				j++;
			}
				
			if(j==Initializer.NUM_BINS_HSV_SAT)
			{
				j=0;
				i++;
			}
			if(i==Initializer.NUM_BINS_HSV_HUE)
			{
				i=j=0;
				flag=true;
			}
			
		}
		
		return fn;
	}
}
