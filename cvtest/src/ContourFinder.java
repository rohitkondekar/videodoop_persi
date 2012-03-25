import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;


public class ContourFinder 
{
	public static void findContour(IplImage img,float[][] hist)
	{
		
		int w = Initializer.IMAGE_WIDTH;
		int h = Initializer.IMAGE_HEIGHT;
		int no_pix = Initializer.TOTAL_PIXELS;
		
		int[] pixelVal = new int[no_pix];
        int[] dummy = null;
        int[][] pixels = new int [h][w];
        
        int count = 0;
		long cent_i = 0,cent_j = 0;int pixelCount = 0;
		
		
		if(img!=null)
		{
			BufferedImage bufferImg = img.getBufferedImage();
			BufferedImage bi = new BufferedImage(w, h,BufferedImage.TYPE_BYTE_GRAY);
	        
			Graphics g = bi.getGraphics();
	        g.drawImage(bufferImg, 0, 0,w,h, null);
	        g.dispose();
	        
	        
	        
	        Raster data;
			data = bi.getData();		
			pixelVal = data.getPixels(0, 0, w,h, dummy);
			
			bi.flush();
			bufferImg.flush();
			
			for(int i=0;i<h;i++)
			{
				for(int j=0;j<w;j++)
				{
					pixels[i][j] = (char)pixelVal[count++];
					if(pixels[i][j] == 255)
					{
						cent_i += i;
						cent_j += j;
						pixelCount++;
					}
				}
			}
			if(pixelCount!=0)
			{
				cent_i /= pixelCount;
				cent_j /= pixelCount;
				
				double[][] polar=new double[pixelCount][2];
				int c=0;
				for(int i = 0;i<h;i++)
				{
					for(int j = 0;j<w;j++)
					{
						if(pixels[i][j] == 255)//only points contributing to edges are considered
						{
							polar[c][0] =Math.sqrt((cent_i-i)*(cent_i-i)+(cent_j-j)*(cent_j-j));
								
							double temp =Math.atan2((cent_i - i),(cent_j -j));
							
							if(temp>=0)
								polar[c][1]=temp;
							else
								polar[c][1]=2*Math.PI+temp;
							c++;
						}
					}
				}
				
				
				Histogram.generateHist2D(polar,hist,pixelCount);
			}
		}
	}
}