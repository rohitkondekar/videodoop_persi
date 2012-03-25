package hadoop;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import InputFormats.VideoObject;

public class reducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterator<Text> values,
			OutputCollector<Text, Text> output, Reporter arg3) throws IOException {
		//output.collect(new Text("oooo"), new Text("sup"));
		while(values.hasNext())
		{
			System.err.println("--------------reduce");
			output.collect(key, values.next());
			//output.collect(key, new Text(values.next().getVideoStream().toString()));
		}
		
	}
	
	 
}
