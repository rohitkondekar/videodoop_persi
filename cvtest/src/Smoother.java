
import static com.googlecode.javacv.cpp.opencv_imgproc.cvSmooth;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_GAUSSIAN;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Smoother 
{
    public static IplImage smooth(IplImage image) 
    { 
        if (image != null) 
        {
            cvSmooth(image, image, CV_GAUSSIAN, 3);
            /* param1=3 is the size of horizontal gaussian kernel param1 must always be odd
             * since param2 is not specified it is set as param1
             * subsequent parameters param3 and param4 are calculated from param1 and param2
             * which are sigma values of gaussian kernel in horizontal and vertical directions
             */
        }
        return image;
    }
    
}
