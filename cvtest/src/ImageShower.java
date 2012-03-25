import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ImageShower
{
	public static void showimage(IplImage img)
	{		
		// display image in canvas
		CanvasFrame canvas = new CanvasFrame(img.toString());
		canvas.setDefaultCloseOperation(CanvasFrame.DO_NOTHING_ON_CLOSE);
		canvas.showImage(img);
		canvas.waitKey();
		canvas.dispose();
	} 
}
// wait for keypress on canvas
// end of ShowImage class
