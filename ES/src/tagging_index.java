

import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;

import java.io.File;
import java.io.FileWriter;

import opencv.ContourFinder;
import opencv.EdgeFinder;
import opencv.Initializer;


import com.googlecode.javacv.JavaCvErrorCallback;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_LOAD_IMAGE_COLOR;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;


import com.googlecode.javacv.cpp.opencv_core.CvSize;


import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import opencv.*;

public class tagging_index {
 
  // The cascade definition to be used for detection.
  private static final String CASCADE_FILE = "haarcascade_frontalface_alt.xml";
  
  static String[] Color = {"WHITE","RED","ORANGE","YELLOW","YG","GREEN","GC","CYAN","CB","BLUE","BP","PINK","PR","BLACK","GRAY"}; 
  
  public static String getColor(IplImage img,String nm)
  {
		int []arr=new int[15];int max=0;int index=0;
		
		CvSize img_sz=null;
		if(img!=null)
			img_sz = cvGetSize(img);
		IplImage hsv_img=null;
		if(img_sz!=null)
			hsv_img = cvCreateImage(img_sz, IPL_DEPTH_8U, 3);
		else
			System.out.println("couldnt create image");
      
      cvCvtColor(img, hsv_img, CV_BGR2HSV);
      
		for(int i=0;i<Initializer.IMAGE_HEIGHT;i++)
			for(int j=0;j<Initializer.IMAGE_WIDTH;j++)
			{
				CvScalar cs;
				cs=cvGet2D(hsv_img, i, j);
				
				int h=(int)cs.val(0);
				int s=(int)cs.val(1);
				int v=(int)cs.val(2);
				//System.out.println(nm+"   "+h+"  "+s+"   "+v);
				if(v<70)				// black
					arr[arr.length-2]++;
				else if(v>230 && s<32) //white
					arr[0]++;				
				else if(s<32)			//gray
					arr[arr.length-1]++;
				else
				{		
					h=2*h;
					if(h>345 || h<=15)
						arr[1]++;
					else
					{
						int ub=15;
						int bin_no=1;//System.out.println("hue "+h);
						while(h>ub)
						{
							ub+=30;
							bin_no++;
						}
						arr[bin_no]++;
					}
				}				
			}
		
		for(int i=0;i<arr.length;i++)
			if(arr[i]>max)
			{  
				max=arr[i];
				index=i;
			}
		cvReleaseImage(hsv_img);
		return Color[index];		  
  }
  
  public static boolean faceDetect(IplImage originalImage)
  { 
	    // We need a grayscale image in order to do the recognition, so we
	    // create a new image of the same size as the original one.
	    IplImage grayImage = IplImage.create(originalImage.width(),
	    originalImage.height(), IPL_DEPTH_8U, 1);
	 
	    // We convert the original image to grayscale.
	    cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);
	 
	    CvMemStorage storage = CvMemStorage.create();
	 
	    // We instantiate a classifier cascade to be used for detection, using the cascade definition.
	    CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
	    cvLoad(CASCADE_FILE));
	 
	    // We detect the faces.
	    CvSeq faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1, 0);
	    
	    if(faces.total()>0)
	    	return true;
	    return false;	    
  }

  
  public static void main(String[] args) throws Exception {
	  
	  	Initializer.initialize();
	    FileWriter op_file = new FileWriter("/home/user/Data/features.txt");
	    
	    
	    String source_dir=Initializer.QUERY_DIR;
		File directory = new File(source_dir);
		File[] indirs = directory.listFiles();
	    long[][] color_hist=new long[Initializer.NUM_BINS_HSV_HUE][Initializer.NUM_BINS_HSV_SAT];
		long[] corner_hist=new long[Initializer.NUM_BINS_DIST_1D];
		
		for(int m=0;m<indirs.length;m++)
		{
			String[] infiles = indirs[m].list();
				for(int i=0;i<infiles.length;i++)
				{
					
					String query_file=indirs[m]+"/"+infiles[i];
					IplImage img=cvLoadImage(query_file,CV_LOAD_IMAGE_COLOR);
					IplImage imgResized = cvCreateImage(cvSize(Initializer.IMAGE_WIDTH,Initializer.IMAGE_HEIGHT), img.depth(), img.nChannels());
					cvResize(img,imgResized,img.nChannels());
					
					Histogram.generateHistHSV(imgResized, color_hist);
					
					CornerDetector.findCorners(imgResized,corner_hist);
					
					if(img!=null)
						op_file.append(infiles[i].replaceAll(".png","a"+indirs[m].getName())+"\t"+getColor(imgResized,infiles[i])+"\t"+faceDetect(imgResized)+"\t");
										
						CvSize img_sz=null;
						if(img!=null)
							img_sz = cvGetSize(img);
						IplImage gray_img=null;
						if(img_sz!=null)
							gray_img = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
						else
							System.out.println("couldnt create image");	 
				
				  cvCvtColor(img, gray_img, CV_BGR2GRAY);
				  
				  long[][] edge_hist=new long[Initializer.NUM_BINS_DIST_2D][Initializer.NUM_BINS_ANGLE_2D];
				  
				  img = cvCreateImage(cvGetSize(gray_img), gray_img.depth(), 1);
				  EdgeFinder.getegdes(gray_img,img);
				  ContourFinder.findContour(img,edge_hist);
				  
				  long[][] apidq_hist=new long[Initializer.NUM_BINS_DIST_APIDQ][Initializer.NUM_BINS_ANGLE_APIDQ];				  
				  Histogram.generateHistAPIDQ(gray_img, apidq_hist);
				  
				  StringBuffer buf = new StringBuffer("");		  
				  StringBuffer col_buf = new StringBuffer("");		
				  StringBuffer apidq_buf = new StringBuffer("");	
				  StringBuffer corner_buf = new StringBuffer("");
				  for(int k=0;k<Initializer.NUM_BINS_DIST_2D;k++)
				  {			
					if(k==0)
						buf.append(edge_hist[k][0]+"");
					else if(k%2==0)
					{ buf.append("\t");	
					  buf.append(edge_hist[k][0]+"");
					}
					else
						buf.append("a"+edge_hist[k][0]);
					
					for(int l=1;l<Initializer.NUM_BINS_ANGLE_2D;l++)
						buf.append("a"+edge_hist[k][l]);	
				  }
				  buf.append("\t");
				  
				  for(int k=0;k<Initializer.NUM_BINS_HSV_HUE;k++)
				  {			
					if(k==0)
						col_buf.append(color_hist[k][0]+"");
					else if(k%2==0)
					{ col_buf.append("\t");	
					col_buf.append(color_hist[k][0]+"");
					}
					else
						col_buf.append("a"+color_hist[k][0]);
					
					for(int l=1;l<Initializer.NUM_BINS_HSV_SAT;l++)
						col_buf.append("a"+color_hist[k][l]);	
				  }
				  col_buf.append("\t");
				  
				  for(int k=0;k<Initializer.NUM_BINS_DIST_APIDQ;k++)
				  {			
					if(k==0)
						apidq_buf.append(apidq_hist[k][0]+"");
					else if(k%2==0)
					{ apidq_buf.append("\t");	
					apidq_buf.append(apidq_hist[k][0]+"");
					}
					else
						apidq_buf.append("a"+apidq_hist[k][0]);
					
					for(int l=1;l<Initializer.NUM_BINS_ANGLE_APIDQ;l++)
						apidq_buf.append("a"+apidq_hist[k][l]);	
				  }
				  apidq_buf.append("\t");
				  
				  corner_buf.append(corner_hist[0]+"");
				  for(int k=1;k<Initializer.NUM_BINS_DIST_1D;k++)
					  corner_buf.append("a"+corner_hist[k]);				  
				  corner_buf.append("\n");
				  
				  op_file.append(buf.toString());
				  op_file.append(col_buf.toString());
				  op_file.append(apidq_buf.toString());
				  op_file.append(corner_buf.toString());
				  
				  cvReleaseImage(gray_img);
				  cvReleaseImage(img);
				  cvReleaseImage(imgResized);
				}   
		}
		op_file.flush();
		op_file.close();
  }
}