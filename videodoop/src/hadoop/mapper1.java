package hadoop;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import InputFormats.VideoObject;

public class mapper1 extends MapReduceBase implements Mapper<Text, VideoObject, Text, Text> {

	@Override
	public void map(Text key, VideoObject value,
			OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		System.err.println(key.toString()+"-----------------------------------------------map-------"+value.getLenght());
		output.collect(key, new Text("--"));
	}

}