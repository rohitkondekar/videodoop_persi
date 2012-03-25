
public class VideoProcessor 
{
	public static void main(String[] args) throws Exception
	{
		Initializer.initialize();
		System.out.println("start");
		long startTime = System.currentTimeMillis();
		
		FrameExtracter.extract_frames();
		Resizer.resize();
		
		long endTime = System.currentTimeMillis();
		System.out.println("finished");
		System.out.println("That took " + (endTime - startTime) + " milliseconds");

	}
}
