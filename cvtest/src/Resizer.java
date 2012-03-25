import static com.googlecode.javacv.cpp.opencv_core.*;

import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class Resizer 
{
	public static void resize() throws IOException
	{
		System.out.println("Resizing Frames...");
		String frame_source_dir=Initializer.VIDEO_FRAME_SOURCE_DIR;
		String frame_resized_source_dir=Initializer.VIDEO_FRAME_RESIZED_SOURCE_DIR;
		File directory = new File(frame_source_dir);
		String[] infiles = directory.list();
				
		for(int i=0;i<infiles.length;i++)
		{
			String video_dir_name=infiles[i]+"/";
						
			File dir=new File(frame_source_dir+video_dir_name);
			String[] image_files=dir.list();
						
			for(int j=0;j<image_files.length;j++)
			{
				IplImage loaded = cvLoadImage(frame_source_dir+video_dir_name+image_files[j]); 
				IplImage imgResized = cvCreateImage(cvSize(Initializer.IMAGE_WIDTH,Initializer.IMAGE_HEIGHT), loaded.depth(), loaded.nChannels());
				cvResize(loaded,imgResized,loaded.nChannels());
				
				BufferedImage image=imgResized.getBufferedImage();
				
				File fl = new File(frame_resized_source_dir+video_dir_name+image_files[j]);
		        fl.mkdirs();
		        ImageIO.write(image, "png", fl);
		        
		        cvReleaseImage(imgResized);
				cvReleaseImage(loaded);
				image.flush();
				
			}
		}
		System.out.println("Resizing Frames Complete!!");
		
	}
	

}
