package InputFormats;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.io.Writable;

public class VideoObject implements Writable {

		private byte[] videoByteArray= null;
        private InputStream in = null;
        
        public VideoObject(byte[] video){
                videoByteArray = video;         
        }
        public VideoObject(){}
        
        public void write(DataOutput out) throws IOException{
                out.write(videoByteArray);
        }
        
        public void readFields(DataInput in) throws IOException{
                
                in.readFully(videoByteArray);
        }
        
        public byte[] getVideoByteArray(){
                return this.videoByteArray;
        }
        
        public InputStream getVideoStream(){
                return in;
        }
        
        public int getLenght()
        {
        	return videoByteArray.length;
        }
}
