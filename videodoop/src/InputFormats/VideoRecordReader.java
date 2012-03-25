package InputFormats;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.SequenceFileRecordReader;
import org.apache.hadoop.mapred.TaskAttemptContext;

public class VideoRecordReader implements RecordReader<Text, VideoObject>{

	//private static final Log LOG = LogFactory.getLog(VideoRecordReader.class);
    private SequenceFileRecordReader fileRecordReader;
    private CompressionCodecFactory compressionCodecs = null;
    private VideoReader videoReader;

    private String filename;
    private String format;
    
    private long end;
    private int maxLineLength;
  
    private FSDataInputStream fileIn;
 //   private Text key = null;
  //  private VideoObject value = null;
    private VideoDivider videoDivider;
	
    private FileSplit fileSplit;
    private Configuration conf;
    
    boolean process = false;
    
    
    public VideoRecordReader(FileSplit fileSplit, Configuration conf) throws IOException
    {
    	this.fileSplit = fileSplit;
    	this.conf = conf;
    	     
        final Path file = fileSplit.getPath();        
        FileSystem fs = file.getFileSystem(conf);
        fileIn = fs.open(file);       
        
        filename = file.getName().substring(0,file.getName().indexOf('.'));
        format = file.getName().substring(file.getName().lastIndexOf('.')+1);
        videoReader = new VideoReader(fileIn,conf);
    }
    
    @Override
	public Text createKey() {
		// TODO Auto-generated method stub
		return new Text();
	}
    
    @Override
	public VideoObject createValue() {
		// TODO Auto-generated method stub
		try {
			return new VideoObject(videoReader.readVideoFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
      
	@Override
    public synchronized void close() throws IOException {
        if (videoReader != null) {
            videoReader.close(); 
        }
    }	

	@Override
	public long getPos() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean next(Text key, VideoObject value) throws IOException {
		// TODO Auto-generated method stub
		if(!process)
		{
			// key = new Text();
	         key.set(filename);
	         System.err.println(filename);
	         System.err.println(value.getLenght());
	         //this.key = key;
	         //this.value = value;
	         System.err.println(key.toString()+"-*******-"+value.getLenght());
	         process = true;
	         return true;
		}
		return false;
	}

	@Override
	public float getProgress() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
