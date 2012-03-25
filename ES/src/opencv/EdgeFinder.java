package opencv;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCanny;
import static com.googlecode.javacv.cpp.opencv_core.*;

import static com.googlecode.javacv.cpp.opencv_highgui.*;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class EdgeFinder 
{
	public static void getegdes(IplImage image,IplImage returnImg)
	{
		
        //IplImage returnImg = null;
        if (image != null) 
        {
        	image=Smoother.smooth(image);        	
        	//returnImg =  cvCreateImage(cvGetSize(image), image.depth(), 1);
        	cvCanny(image, returnImg, 50, 200, 3);
        	/*the hightreshold to lowthreshold ratio should be 2:1 or 3:1 in the current situation 4:1
        	 * the last parameter the aparture size should be odd 3,5,7 only
        	 * more the aprture size more edges will be detected
        	 * image must be smoothed enough to detect main objects represented by edges
        	 * it represents size of square filter
        	 * depth of returnimage should be higher than input image to avoid overflows
        	 */
        }
       //cvCopy(returnImg,image);
        //cvReleaseImage(returnImg);
        
        //return returnImg;//image;
	}
    
}
