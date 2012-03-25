import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_GRAY2BGR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class Functions 
{
	public static void generateHistHSV(IplImage img,int[][] hist)//class::Histogram
	{
		CvSize img_sz=null;
		if(img!=null)
			img_sz = cvGetSize(img);
		IplImage hsv_img=null;
		if(img_sz!=null)
		{
			hsv_img = cvCreateImage(img_sz, IPL_DEPTH_8U, 3);
			
		}
		else
			System.out.println("couldnt create image");
        if(img.nChannels()==3)
        	cvCvtColor(img, hsv_img, CV_BGR2HSV);
        else
        {
        	IplImage col_img = cvCreateImage(img_sz, IPL_DEPTH_8U, 3);
        	cvCvtColor(img, col_img, CV_GRAY2BGR);
        	cvCvtColor(col_img, hsv_img, CV_BGR2HSV);
        	
        }
        
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
				//System.err.println(h+"--"+s+"--"+v);
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
	
	public static void generateHistBGR(IplImage img,int[][][] hist)//class::Histogram
	{
		if(img == null) throw new RuntimeException("image null");
		for(int i=0;i<Initializer.NUM_BINS_BGR_BLU;i++)
			for(int j=0;j<Initializer.NUM_BINS_BGR_GRN;j++)
				for(int k=0;k<Initializer.NUM_BINS_BGR_RED;k++)
					hist[i][j][k]=0;
		for(int i=0;i<Initializer.IMAGE_HEIGHT;i++)
		{
			for(int j=0;j<Initializer.IMAGE_WIDTH;j++)
			{
				CvScalar cs;
				cs=cvGet2D(img, i, j);
				
				int b=(int)cs.val(0);
				int g=(int)cs.val(1);
				int r=(int)cs.val(2);
				
				int l=0;
				while(l<(Initializer.NUM_BINS_BGR_BLU-1) && b>Initializer.BINS_BGR_BLU[l])
					l++;
				
				int m=0;
				while(m<(Initializer.NUM_BINS_BGR_GRN-1) && g>Initializer.BINS_BGR_GRN[m])
					m++;
				
				int n=0;
				while(n<(Initializer.NUM_BINS_BGR_RED-1) && r>Initializer.BINS_BGR_RED[n])
					n++;
				
				hist[l][m][n]++;
			}
		}
	}
	
	public static double CompareHistBGR(int[][][] hist1,int[][][] hist2)//class::Histogram
	{
		double min,sum=0;
		
		
		for(int i=0;i<Initializer.NUM_BINS_BGR_BLU;i++)
			for(int j=0;j<Initializer.NUM_BINS_BGR_GRN;j++)
				for(int k=0;k<Initializer.NUM_BINS_BGR_RED;k++)
				{	
					if(hist1[i][j][k]<hist2[i][j][k])
						min=hist1[i][j][k];
					else
						min=hist2[i][j][k];
					sum+=min;
					//System.out.print(hist2[i][j][k]+",");
				}
		//if(fn.equals("11.flv/13.png"))
			//System.out.println(sum);
		
		
		return sum/Initializer.TOTAL_PIXELS;
	}

}
