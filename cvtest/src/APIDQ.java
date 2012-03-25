import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class APIDQ 
{
	public static void calcAPIDQ(IplImage img,long[][] hist)//input image should be grayscale
	{
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
			//Histogram.generateHistAPIDQ(pixels,hist);
		}
	}
}
