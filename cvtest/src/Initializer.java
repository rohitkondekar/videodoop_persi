
public class Initializer 
{
	public static final int IMAGE_WIDTH=256;
	public static final int IMAGE_HEIGHT=256;
	public static final int TOTAL_PIXELS=IMAGE_WIDTH*IMAGE_HEIGHT;
	
	public static final int NUM_BINS_DIST_1D=8;
	public static double BIN_SIZE_DIST_1D=0;//be careful
	public static double[] BINS_DIST_1D=null;//be careful
	
	
	public static final int NUM_BINS_DIST_2D=16;
	public static double BIN_SIZE_DIST_2D=0;//be careful
	public static final int NUM_BINS_ANGLE_2D=8;
	public static double BIN_SIZE_ANGLE_2D=0;//be careful
	public static double[] BINS_DIST_2D={40,54,64,72,82,94,100,106,112,118,126,132,138,144,150};//be careful
	public static double[] BINS_ANGLE_2D=null;//be careful
	
	public static final int NUM_BINS_HSV_HUE=8;//6;
	public static final int NUM_BINS_HSV_SAT=8;
	public static final int NUM_BINS_HSV_VAL=16;
	public static double[] BINS_HSV_HUE={22,45,66,90,110,135,154};//{8,16,24,32,40};//
	public static double[] BINS_HSV_SAT={32,64,96,128,160,192,224};
	public static double[] BINS_HSV_VAL={16,32,48,64,80,96,112,128,144,160,176,192,208,224,240};
	
	public static final int NUM_BINS_BGR_BLU=8;
	public static final int NUM_BINS_BGR_GRN=8;
	public static final int NUM_BINS_BGR_RED=8;
	public static double[] BINS_BGR_BLU={32,64,96,128,160,192,224};
	public static double[] BINS_BGR_GRN={40,80,100,120,140,180,200};
	public static double[] BINS_BGR_RED={32,64,96,128,160,192,224};
	
	public static final int NUM_BINS_DIST_APIDQ=16;
	public static final int NUM_BINS_ANGLE_APIDQ=NUM_BINS_ANGLE_2D;
	public static final double[] BINS_DIST_APIDQ={0.25,1,1.5,2,2.5,3,3.25,4,4.5,5,5.5,6,6.75,7.5,8.5};
	public static double[] BINS_ANGLE_APIDQ=null;
	
	public static final String USERNAME="user";
	public static final String VIDEO_SOURCE_DIR="/home/"+USERNAME+"/Videos/";
	public static final String VIDEO_FRAME_SOURCE_DIR="/home/"+USERNAME+"/Pictures/Video_Frames/";
	public static final String VIDEO_FRAME_RESIZED_SOURCE_DIR="/home/"+USERNAME+"/Pictures/Video_Frames_resized/";
	public static final String QUERY_DIR="/home/"+USERNAME+"/Desktop/images/";
	
	public static final String FEATURE_FILE="/home/"+USERNAME+"/Documents/features.txt";
	public static final String COMPARISON_RESULT_FILE="/home/"+USERNAME+"/Documents/result.txt";
	
	public static final String CORNER_FEATURE_FILE="/home/"+USERNAME+"/Documents/corners.txt";
	public static final String CORNER_COMPARISON_RESULT_FILE="/home/"+USERNAME+"/Documents/corner_result.txt";
	public static final String WEIGHTED_CORNER_FEATURE_FILE="/home/"+USERNAME+"/Documents/weighted_corner_result.txt";
	
	public static final String APIDQ_FEATURE_FILE="/home/"+USERNAME+"/Documents/APIDQ.txt";
	public static final String APIDQ_COMPARISON_RESULT_FILE="/home/"+USERNAME+"/Documents/apidq_result.txt";
	
	public static final String COLOR_FEATURE_FILE="/home/"+USERNAME+"/Documents/colors.txt";
	public static final String COLOR_COMPARISON_RESULT_FILE="/home/"+USERNAME+"/Documents/color_result.txt";
	
	public static final String EDGE_FEATURE_FILE="/home/"+USERNAME+"/Documents/edges.txt";
	public static final String EDGE_COMPARISON_RESULT_FILE="/home/"+USERNAME+"/Documents/edge_result.txt";
		
	public static final int THRESHOLD_1D=85;
	public static final int THRESHOLD_2D=70;//75
	public static final int THRESHOLD_CORNER=85;
	public static final int THRESHOLD_APIDQ = 85;
	public static final int THRESHOLD_BGR=60;
	public static final int THRESHOLD_HSV=70;
	
	public static void initialize()
	{
		double temp;
		
		BIN_SIZE_DIST_1D=(Math.sqrt(IMAGE_WIDTH*IMAGE_WIDTH+IMAGE_HEIGHT*IMAGE_HEIGHT)/2)/(NUM_BINS_DIST_1D-1);
		BINS_DIST_1D=new double[NUM_BINS_DIST_1D-1];
		temp=BIN_SIZE_DIST_1D;
		for(int i=1;i<NUM_BINS_DIST_1D;i++)
		{
			BINS_DIST_1D[i-1]=temp;
			temp+=BIN_SIZE_DIST_1D;
		}
		/*
		BIN_SIZE_DIST_2D=(Math.sqrt(IMAGE_WIDTH*IMAGE_WIDTH+IMAGE_HEIGHT*IMAGE_HEIGHT)/2)/(NUM_BINS_DIST_2D-1);
		BINS_DIST_2D=new double[NUM_BINS_DIST_2D-1];
		temp=BIN_SIZE_DIST_2D;
		for(int i=1;i<NUM_BINS_DIST_2D;i++)
		{
			BINS_DIST_2D[i-1]=temp;
			temp+=BIN_SIZE_DIST_2D;
		}
		*/
		BIN_SIZE_ANGLE_2D=(Math.PI/NUM_BINS_ANGLE_2D)*2;
		BINS_ANGLE_2D=new double[NUM_BINS_ANGLE_2D-1];
		BINS_ANGLE_APIDQ=new double[NUM_BINS_ANGLE_APIDQ-1];
		temp=BIN_SIZE_ANGLE_2D;
		for(int i=1;i<NUM_BINS_ANGLE_2D;i++)
		{
			BINS_ANGLE_2D[i-1]=temp;
			BINS_ANGLE_APIDQ[i-1]=temp;
			temp+=BIN_SIZE_ANGLE_2D;
		}
	}
}
