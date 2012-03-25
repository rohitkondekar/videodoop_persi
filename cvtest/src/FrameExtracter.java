import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;



public class FrameExtracter 
{
    int w ,h;
    static FileWriter f1;
    
    public FrameExtracter(int w,int h) 
    {
    	this.w = w;
    	this.h = h;
	}
    
	public static void extract_frames() throws Exception 
	{	
		String vid_src_dir=Initializer.VIDEO_SOURCE_DIR;
		
		File directory = new File(vid_src_dir);
		String[] infiles = directory.list();
		FrameExtracter  cfg = new FrameExtracter (256,256);
		
		System.out.println("Extracting Frames ... ");
		for(int i=0;i<infiles.length;i++)
			cfg.getPicture(directory.toString()+"/"+infiles[i],infiles[i]);
		System.out.println("Frame Extraction Complete!!");
		
	}
	
	void getPicture(String infile,String videoname) throws Exception//infile is video path
    {

		IContainer container = IContainer.make();		
		container.open(infile,IContainer.Type.READ,null);
		
		IStreamCoder videoCoder = null;
		
		int numstreams = container.getNumStreams();
		int videostreamid=-1;
		for(int i=0;i<numstreams;i++)
		{
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			if(coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
			{
				videoCoder = coder;
				videostreamid = i;
				break;
			}			
		}
		
		if(videostreamid==-1)throw new RuntimeException("No video stream");		
		if(videoCoder.open()<0)throw new RuntimeException("could not open video coder");
		
		IVideoResampler resampler = null;		
		
		if(videoCoder.getPixelType() != IPixelFormat.Type.BGR24)
		{
			resampler = IVideoResampler.make(videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24, videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
			if(resampler==null) throw new RuntimeException("Resampler error");
		}
		
		int frame=0;
		IPacket packet = IPacket.make();
				
		while(container.readNextPacket(packet) >=0)
		{
			if(packet.getStreamIndex() == videostreamid)
			{
				IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),videoCoder.getWidth(),videoCoder.getHeight());
				int offset = 0;
				while(offset<packet.getSize() && packet.isKeyPacket())
				{
					int bytesDecoded = videoCoder.decodeVideo(picture, packet, 0);
					if (bytesDecoded < 0)
			            throw new RuntimeException("got error decoding video");
					offset+=bytesDecoded;
					
					if(picture.isComplete() && picture.isKeyFrame())
					{			
						frame++;
						IVideoPicture newPic = picture;
						
				        if (resampler != null)
				        {
				           newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), picture.getWidth(),picture.getHeight());
				              if (resampler.resample(newPic, picture) < 0)
				                throw new RuntimeException("could not resample video");
				        }
				        
				        if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
				              throw new RuntimeException("could not decode video as BGR 24 bit data");
				        
				        
				        BufferedImage image = new BufferedImage(w,h, BufferedImage.TYPE_3BYTE_BGR); 
				        IConverter converter = ConverterFactory.createConverter(image,IPixelFormat.Type.BGR24);
				        image = converter.toImage(newPic);
				        
				        File fl = new File(Initializer.VIDEO_FRAME_SOURCE_DIR+videoname+"/"+frame+".png");
				        fl.mkdirs();
				        ImageIO.write(image, "png", fl);
				        image.flush();
                       }			      
					}
				}			
			}
		}	
}