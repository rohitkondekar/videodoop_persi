package InputFormats;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

public class VideoInputFormat extends FileInputFormat<Text, VideoObject>{

	@Override
	protected boolean isSplitable(FileSystem fs, Path file) {
		return false;
	}
	
	@Override
	public RecordReader<Text, VideoObject> getRecordReader(InputSplit split,
			JobConf conf, Reporter arg2) throws IOException {
		// TODO Auto-generated method stub
		RecordReader rr = new VideoRecordReader((FileSplit)split,conf);
		
		return rr;
	}
}
