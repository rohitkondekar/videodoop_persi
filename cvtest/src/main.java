import static com.googlecode.javacv.cpp.opencv_core.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_highgui.*;
import com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;

public class main
{
	public static void main(String[] args) throws IOException 
	{
		System.out.println("start");
		long startTime = System.currentTimeMillis();
		Initializer.initialize();
				
		String query_file = Initializer.QUERY_DIR+"347gray.png";
		IplImage query_img = null;
		IplImage query_img_edge = null;
		
		String infile;
		BufferedReader br,br2;
		String strline="";int c=0;
		
		boolean[] indices=new boolean[55751];
		
		
			
		query_img = cvLoadImage(query_file,CV_LOAD_IMAGE_COLOR);
		float[] query_corner_hist=new float[Initializer.NUM_BINS_DIST_1D];
		CornerDetector.findCorners(query_img,query_corner_hist);//
		FileWriter corner_result_file = new FileWriter(Initializer.CORNER_COMPARISON_RESULT_FILE);
		float[] corner_hist=new float[Initializer.NUM_BINS_DIST_1D];
		infile = Initializer.CORNER_FEATURE_FILE;
		br = new BufferedReader(new FileReader(infile));
		while((strline=br.readLine())!=null)
		{
			String fn=Comparator.getCornerFeatures(strline,corner_hist);
			
			double sim_1D=Histogram.compareHist1D(query_corner_hist,corner_hist)*100;
			if(sim_1D>Initializer.THRESHOLD_1D)
				corner_result_file.append(fn+"\t"+sim_1D +"\n");
		}System.out.println("corner:"+c);
		corner_result_file.flush();
		corner_result_file.close();
		br2 = new BufferedReader(new FileReader(Initializer.CORNER_COMPARISON_RESULT_FILE));
		while((strline=br2.readLine())!=null)
		{
			StringTokenizer st=new StringTokenizer(strline,"\t");
			String fn2=null;
			
			if(st.hasMoreTokens())
				fn2=st.nextToken();
			
			
			query_img = cvLoadImage(query_file,CV_LOAD_IMAGE_GRAYSCALE);
			int[][] query_apidq_hist=new int[Initializer.NUM_BINS_DIST_APIDQ][Initializer.NUM_BINS_ANGLE_APIDQ];
			Histogram.generateHistAPIDQ(query_img, query_apidq_hist);
			FileWriter apidq_result_file = new FileWriter(Initializer.APIDQ_COMPARISON_RESULT_FILE);
			int[][] apidq_hist=new int[Initializer.NUM_BINS_DIST_APIDQ][Initializer.NUM_BINS_ANGLE_APIDQ];
			infile = Initializer.APIDQ_FEATURE_FILE;
			br = new BufferedReader(new FileReader(infile));
			while((strline=br.readLine())!=null)
			{
				StringTokenizer st1=new StringTokenizer(strline,"\t");
				String fn1=null;
				
				if(st1.hasMoreTokens())
					fn1=st1.nextToken();
				
				if(fn2.equals(fn1))
				{
					Comparator.getAPIDQFeatures(strline,apidq_hist);
				double sim_apidq = Histogram.compareHistAPIDQ(query_apidq_hist,apidq_hist)*100;//	System.out.print(fn+"\t"+sim_apidq+"\n");	
				if(sim_apidq > Initializer.THRESHOLD_APIDQ)
					apidq_result_file.append(fn1+"\t"+sim_apidq+"\n");
				}
			}
			apidq_result_file.flush();
			apidq_result_file.close();
		}System.out.println("apidq:"+c);
		br2 = new BufferedReader(new FileReader(Initializer.APIDQ_COMPARISON_RESULT_FILE));
		while((strline=br2.readLine())!=null)
		{
			StringTokenizer st=new StringTokenizer(strline,"\t");
			String fn2=null;
			
			if(st.hasMoreTokens())
				fn2=st.nextToken();
			query_img = cvLoadImage(query_file,CV_LOAD_IMAGE_COLOR);
			int[][] query_hsv_hist=new int[Initializer.NUM_BINS_HSV_HUE][Initializer.NUM_BINS_HSV_SAT];
			//int[][][] query_bgr_hist=new int[Initializer.NUM_BINS_BGR_BLU][Initializer.NUM_BINS_BGR_GRN][Initializer.NUM_BINS_BGR_RED];
			//Histogram.generateHistBGR(query_img, query_bgr_hist);
			Histogram.generateHistHSV(query_img, query_hsv_hist);
			FileWriter color_result_file = new FileWriter(Initializer.COLOR_COMPARISON_RESULT_FILE);
			int[][] HSV_hist=new int[Initializer.NUM_BINS_HSV_HUE][Initializer.NUM_BINS_HSV_SAT];
			//int[][][] BGR_hist=new int[Initializer.NUM_BINS_BGR_BLU][Initializer.NUM_BINS_BGR_GRN][Initializer.NUM_BINS_BGR_RED];
			infile = Initializer.COLOR_FEATURE_FILE;
			br = new BufferedReader(new FileReader(infile));
			while((strline=br.readLine())!=null)
			{
				StringTokenizer st1=new StringTokenizer(strline,"\t");
				String fn1=null;
				
				if(st1.hasMoreTokens())
					fn1=st1.nextToken();
				
				if(fn2.equals(fn1))
				{
					Comparator.getColorFeatures(strline, HSV_hist);
				double sim_HSV=Histogram.CompareHistHSV(query_hsv_hist, HSV_hist)*100;
				if(sim_HSV>Initializer.THRESHOLD_HSV)
					color_result_file.append(fn1+"\t"+ sim_HSV +"\n");
				}
			}System.out.println("color:"+c);
			color_result_file.flush();
			color_result_file.close();
		}
		br2 = new BufferedReader(new FileReader(Initializer.APIDQ_COMPARISON_RESULT_FILE));
		while((strline=br2.readLine())!=null)
		{
			StringTokenizer st=new StringTokenizer(strline,"\t");
			String fn2=null;
			
			if(st.hasMoreTokens())
				fn2=st.nextToken();
			query_img = cvLoadImage(query_file,CV_LOAD_IMAGE_GRAYSCALE);
			query_img_edge = cvCreateImage(cvGetSize(query_img), query_img.depth(), 1);
			EdgeFinder.getegdes(query_img,query_img_edge);
			float[][] query_edge_hist=new float[Initializer.NUM_BINS_DIST_2D][Initializer.NUM_BINS_ANGLE_2D];
			ContourFinder.findContour(query_img_edge,query_edge_hist);
			FileWriter edge_result_file = new FileWriter(Initializer.EDGE_COMPARISON_RESULT_FILE);
			float[][] edge_hist=new float[Initializer.NUM_BINS_DIST_2D][Initializer.NUM_BINS_ANGLE_2D];
			infile = Initializer.EDGE_FEATURE_FILE;
			br = new BufferedReader(new FileReader(infile));
			while((strline=br.readLine())!=null)
			{
				StringTokenizer st1=new StringTokenizer(strline,"\t");
				String fn1=null;
				
				if(st1.hasMoreTokens())
					fn1=st1.nextToken();
				
				
				if(fn2.equals(fn1))
				{
					Comparator.getEdgeFeatures(strline, edge_hist);
				double sim_2D=Histogram.compareHist2D(query_edge_hist, edge_hist)*100;
				if(sim_2D>Initializer.THRESHOLD_2D)
					edge_result_file.append(fn1+"\t"+sim_2D +"\n");
				}
			}
			edge_result_file.flush();
			edge_result_file.close();
		}System.out.println("apidq:"+c);
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		System.out.println("finished");
		
  	}

}
/*
CvScalar s;
s=cvGet2D(imgRGB,i,j); // get the (i,j) pixel value
printf("B=%f, G=%f, R=%f\n",s.val[0],s.val[1],s.val[2]);
s.val[0]=240;
s.val[1]=100;
s.val[2]=100;
image converted to hsv then::
s.val[0] is the hue.[0-180]
s.val[1] is the saturation.[0-255]
s.val[2] is the value.[0-255]
*/