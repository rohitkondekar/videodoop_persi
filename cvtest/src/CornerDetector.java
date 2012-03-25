import static com.googlecode.javacv.cpp.opencv_core.*;

import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import com.googlecode.javacv.cpp.opencv_core.CvArr;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class CornerDetector 
{
	public static void findCorners(IplImage img,float[] Hist)
	{
		int sum_x = 0,sum_y = 0;
		double cent_x,cent_y;
		float[] cornerDist = null;
		int max_corners = 500;
		CvSize img_sz=null;
		if(img!=null)
			img_sz = cvGetSize(img);

		if(img_sz!=null)
		{	
	        IplImage gray = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
	        
	        cvCvtColor(img, gray, CV_BGR2GRAY);
	
	        IplImage eig_img = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
	        IplImage tmp_img = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
	        
	        int[] corner_count = { max_corners };
	        CvPoint2D32f corners = new CvPoint2D32f(max_corners);
	        double qltyLevel = 0.05;
	        double minDist = 5.0;
	        CvArr mask = null;
	        int blkSize = 6;
	        int useHarris = 0;
	        double k = 0.04;
	
	        cvGoodFeaturesToTrack(gray, eig_img, tmp_img, corners, corner_count, qltyLevel, minDist, mask, blkSize, useHarris, k);
	        
	        cvReleaseImage(eig_img);
	        cvReleaseImage(tmp_img);
	        cvReleaseImage(gray);
	        	        
	        
	        
	        for(int i = 0;i < corner_count[0]; i++)
	        {
	        	sum_x += corners.position(i).x();
	        	sum_y += corners.position(i).y();
	        }
	        if(corner_count[0]!=0)
	        {
		        cent_x = sum_x / corner_count[0];
		        cent_y = sum_y / corner_count[0];
		        
		        
		        cornerDist = new float[corner_count[0]];
		        
		        for(int i = 0;i < corner_count[0]; i++)
		        {
		        	cornerDist[i] = (float)Math.sqrt(((cent_x - corners.position(i).x()) * (cent_x - corners.position(i).x()))
		        							+ ((cent_y - corners.position(i).y()) * (cent_y - corners.position(i).y())));
		        	
		        }
		        	    	
		    	Histogram.generateHist1D(cornerDist, Hist,corner_count[0]);
	        }
		}

	}
}