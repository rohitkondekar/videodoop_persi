package opencv;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;

public class Histogram 
{	
	public static void generateHist2D(double[][] mat,long[][] hist,long no_pixels) 
	{		
		for(int i=0;i<Initializer.NUM_BINS_DIST_2D;i++)
			for(int j=0;j<Initializer.NUM_BINS_ANGLE_2D;j++)
				hist[i][j]=0;
		for(int i=0;i<mat.length;i++)
		{	
			double val1 = mat[i][0];// distance
			double val2 = mat[i][1]; // angle
			
			if(val1>0)
			{
				int m=0;
				while(m<(Initializer.NUM_BINS_DIST_2D-1) && val1>Initializer.BINS_DIST_2D[m])
					m++;
				int n=0;
				while(n<(Initializer.NUM_BINS_ANGLE_2D-1) && val2>Initializer.BINS_ANGLE_2D[n])
					n++;
				
				hist[m][n]++;
			}
		}
		for(int i=0;i<Initializer.NUM_BINS_DIST_2D;i++)
			for(int j=0;j<Initializer.NUM_BINS_ANGLE_2D;j++)
				hist[i][j]= (long)(1000000*(hist[i][j]*1.0)/no_pixels);
	}
	
	public static void generateHist1D(float arr[],long[] hist,int no_corners)
	{	
		for(int i = 0;i <Initializer.NUM_BINS_DIST_1D;i++)
    		hist[i] = 0;
		for(int i=0;i<arr.length;i++)
		{
			double val=arr[i];
			
			int m=0;
			while(m<(Initializer.NUM_BINS_DIST_1D-1) && val>Initializer.BINS_DIST_1D[m])
				m++;
			hist[m]++;
			
		}
		for(int i=0;i<hist.length;i++)
			hist[i]=(long)(1000000.0*hist[i]/no_corners);
	}
	
	
	public static void generateHistAPIDQ(IplImage img,long[][] hist)
	{
		for(int i=0;i<Initializer.NUM_BINS_DIST_APIDQ;i++)
			for(int j=0;j<Initializer.NUM_BINS_ANGLE_APIDQ;j++)
				hist[i][j]=0;
		
		int[][] pixels = new int [Initializer.IMAGE_HEIGHT][Initializer.IMAGE_WIDTH];
		if(img!=null)
		{
			for(int i=0;i<Initializer.IMAGE_HEIGHT;i++)
			{
				for(int j=0;j<Initializer.IMAGE_WIDTH;j++)
				{
					CvScalar cs;
					cs=cvGet2D(img, i, j);
					
					pixels[i][j]=(int)cs.val(0);
				}
			}
		}
		
		int dx,dy,count=0;//1
		double r,minr=0,maxr=0;
		double theta,mint=0,maxt=0;
		
		for(int i=0;i<Initializer.IMAGE_HEIGHT-1;i++)
		{
			for(int j=0;j<Initializer.IMAGE_WIDTH-1;j++)
			{
				dx = pixels[i][j+1]-pixels[i][j];
				dy = pixels[i+1][j]-pixels[i][j];
				
				
				r = (double)Math.sqrt(dx*dx + dy*dy);
				theta =Math.atan2(dy,dx);
				
				if (theta<0)
					theta=2*Math.PI+theta;
				
				minr=Math.min(minr, r);
				mint=Math.min(mint, theta);
				maxr=Math.max(maxr, r);
				maxt=Math.max(maxt, theta);
				
				int m=0;
				while(m<(Initializer.NUM_BINS_DIST_APIDQ-1) && r>Initializer.BINS_DIST_APIDQ[m])
					m++;
				int n=0;
				while(n<(Initializer.NUM_BINS_ANGLE_APIDQ-1) && theta>Initializer.BINS_ANGLE_APIDQ[n])
					n++;
				
				hist[m][n]++;
				
			}
		}
						
	}
	
	
	public static void generateHistHSV(IplImage img,long[][] hist)
	{
		CvSize img_sz=null;
		if(img!=null)
			img_sz = cvGetSize(img);
		IplImage hsv_img=null;
		if(img_sz!=null)
			hsv_img = cvCreateImage(img_sz, IPL_DEPTH_8U, 3);
		else
			System.out.println("couldnt create image");
        
        cvCvtColor(img, hsv_img, CV_BGR2HSV);
        
        for(int i=0;i<Initializer.NUM_BINS_HSV_HUE;i++)
			for(int j=0;j<Initializer.NUM_BINS_HSV_SAT;j++)
				hist[i][j]=0;
        
        for(int i=0;i<Initializer.IMAGE_HEIGHT;i++)
			for(int j=0;j<Initializer.IMAGE_WIDTH;j++)
			{
				CvScalar cs;
				cs=cvGet2D(hsv_img, i, j);
				
				int h=(int)cs.val(0);
				int s=(int)cs.val(1);
				int v=(int)cs.val(2);
				
				int l=0;
				while(l<(Initializer.NUM_BINS_HSV_HUE-1) && h>Initializer.BINS_HSV_HUE[l])
					l++;
				
				int m=0;
				while(m<(Initializer.NUM_BINS_HSV_SAT-1) && s>Initializer.BINS_HSV_SAT[m])
					m++;
				
				hist[l][m]++;
			}
        
        cvReleaseImage(hsv_img);
		
	}
}
