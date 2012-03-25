import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_LOAD_IMAGE_COLOR;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class FeatureGenerator 
{	
	public static void main(String[] args) throws IOException
	{	
		System.out.println("start");
		long startTime = System.currentTimeMillis();
		Initializer.initialize();
								
		FileWriter corner_file = new FileWriter(Initializer.CORNER_FEATURE_FILE);//append mode
		FileWriter apidq_file = new FileWriter(Initializer.APIDQ_FEATURE_FILE);
		FileWriter color_file = new FileWriter(Initializer.COLOR_FEATURE_FILE);
		FileWriter edge_file = new FileWriter(Initializer.EDGE_FEATURE_FILE);
				
		float[] corner_hist=new float[Initializer.NUM_BINS_DIST_1D];
		float[][] edge_hist=new float[Initializer.NUM_BINS_DIST_2D][Initializer.NUM_BINS_ANGLE_2D];
		int[][] hsv_hist=new int[Initializer.NUM_BINS_HSV_HUE][Initializer.NUM_BINS_HSV_SAT];//[Initializer.NUM_BINS_HSV_VAL];
		int[][] apidq_hist =new int[Initializer.NUM_BINS_DIST_APIDQ][Initializer.NUM_BINS_ANGLE_APIDQ];
		
		String source_dir=Initializer.VIDEO_FRAME_RESIZED_SOURCE_DIR;
		File directory = new File(source_dir);
		String[] infiles = directory.list();
		for(int i=0;i<infiles.length;i++)
		{
			String int_dir=source_dir+infiles[i]+"/";
			File dir=new File(int_dir);
			String[] int_infiles=dir.list();
			
			for(int j=0;j<int_infiles.length;j++)
			{
				String filename=int_dir+int_infiles[j];
				String fn=infiles[i]+"/"+int_infiles[j];
				
				IplImage img = null;
				IplImage img_edge = null;
				
				img=cvLoadImage(filename,CV_LOAD_IMAGE_COLOR); 
				CornerDetector.findCorners(img,corner_hist);
				corner_file.append(fn+"\t");
				corner_file.append(corner_hist[0]+"");
				for(int k=1;k<Initializer.NUM_BINS_DIST_1D;k++)
					corner_file.append(","+corner_hist[k]);
				corner_file.append("\n");
				
				
		        
				
				img = cvLoadImage(filename,CV_LOAD_IMAGE_GRAYSCALE);
				Histogram.generateHistAPIDQ(img, apidq_hist);
				apidq_file.append(fn+"\t");
				for(int k=0;k<Initializer.NUM_BINS_DIST_APIDQ;k++)
				{
					apidq_file.append(apidq_hist[k][0]+"");
					for(int l=1;l<Initializer.NUM_BINS_ANGLE_APIDQ;l++)
						apidq_file.append(","+apidq_hist[k][l]);
					apidq_file.append("\t");
				}
				apidq_file.append("\n");
				
				
				img=cvLoadImage(filename,CV_LOAD_IMAGE_COLOR);				
				Histogram.generateHistHSV(img, hsv_hist);
				color_file.append(fn+"\t");
				for(int k=0;k<Initializer.NUM_BINS_HSV_HUE;k++)
				{
					color_file.append(hsv_hist[k][0]+"");
					for(int l=1;l<Initializer.NUM_BINS_HSV_SAT;l++)
					{
						color_file.append(","+hsv_hist[k][l]);
						
					}
					color_file.append("\t");
				}
				color_file.append("\n");
				
				img=cvLoadImage(filename,CV_LOAD_IMAGE_GRAYSCALE);
				img_edge = cvCreateImage(cvGetSize(img), img.depth(), 1);
				EdgeFinder.getegdes(img,img_edge);
				ContourFinder.findContour(img_edge,edge_hist);
				edge_file.append(fn+"\t");
				for(int k=0;k<Initializer.NUM_BINS_DIST_2D;k++)
				{
					edge_file.append(edge_hist[k][0]+"");
					for(int l=1;l<Initializer.NUM_BINS_ANGLE_2D;l++)
						edge_file.append(","+edge_hist[k][l]);
					edge_file.append("\t");
				}
				edge_file.append("\n");
					
				cvReleaseImage(img_edge);
				cvReleaseImage(img);
			
			}
		}
		corner_file.flush();
		corner_file.close();
		apidq_file.flush();
		apidq_file.close();
		color_file.flush();
		color_file.close();
		edge_file.flush();
		edge_file.close();
		long endTime = System.currentTimeMillis();
		System.out.println("finished");
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
	}

}
