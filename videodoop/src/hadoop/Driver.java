package hadoop;

import javax.security.auth.callback.TextInputCallback;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import InputFormats.VideoInputFormat;
import InputFormats.VideoObject;

public class Driver extends Configured implements Tool {
	
		//private static final Log LOG = LogFactory.getLog(VideoConverter.class);   
	   
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Driver(), args);
		System.exit(exitCode);
	}
	
	
	
	public int run(String[] arg0) throws Exception {
         
        JobConf conf = new JobConf(getConf(), getClass());;
        
        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(Text.class);
        
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);
        conf.setMapperClass(mapper1.class);
        conf.setReducerClass(reducer.class);
         
        conf.setInputFormat(VideoInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
            
        
        System.out.println(conf.getMapperClass().getName());
       
        FileInputFormat.addInputPath(conf, new Path("/user/user/user/input"));
        
        Path outputPath = new Path("/user/user/user/output");
        FileSystem fs = outputPath.getFileSystem(conf);
        if(fs.exists(outputPath)){
            fs.delete(outputPath, true);
        }
        FileOutputFormat.setOutputPath(conf, new Path("/user/user/user/output"));            
        
        RunningJob job; 
		job = JobClient.runJob(conf);
		job.waitForCompletion();
        return 0;
     }
}
