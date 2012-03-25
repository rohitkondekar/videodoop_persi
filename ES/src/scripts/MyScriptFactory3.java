package scripts;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import opencv.Initializer;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

public class MyScriptFactory3 implements NativeScriptFactory {
	
	static long[] vec1;
	static long[] cvec1;
	static long[] avec1;
	static long[] cor_vec1;
	static long[] toArray(String s,long[] arr)
	{
        int i=0;
		StringTokenizer token = new StringTokenizer(s, "a");
        while(token.hasMoreTokens())
        	arr[i++] = Long.parseLong(token.nextToken());
		return arr;        
	}
	
	@Override public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        vec1= new long[128];        
        toArray(params.get("vec1").toString(), vec1);     
        cvec1= new long[64];
        toArray(params.get("cvec1").toString(), cvec1);  
        avec1= new long[128];        
        toArray(params.get("avec1").toString(), avec1);    
        cor_vec1= new long[8];        
        toArray(params.get("cor_vec1").toString(), cor_vec1);    
		return new MySearchScript1();
    }
	
	public static float compareHist2D(long[] hist1,long[] hist2)
	{
		long min,sum=0;
		for(int i=0;i<hist1.length;i++)
		{
			if(hist1[i]<hist2[i])
				min=hist1[i];
			else
				min=hist2[i];
			 sum+=min;
		}
		return sum/10000;
	}
	
	public static float compareHist(long[] hist1,long[] hist2)
	{
		long min=0,sum=0;
		for(int i=0;i<hist1.length;i++)
		{
			if(hist1[i]<hist2[i])
				min=hist1[i];
			else
				min=hist2[i];
			sum+=min;
		}
		return (float)(sum*100.0/Initializer.TOTAL_PIXELS);
	}
	
	public static float compareHist1D(long[] hist1,long[] hist2)
	{
		double min,sum=0;
		for(int i=0;i<Initializer.NUM_BINS_DIST_1D;i++)
		{
			if(hist1[i]<hist2[i])
				min=hist1[i];
			else
				min=hist2[i];
			sum+=min;
		}
		return (float)(sum/10000.0);
	}
	
	
	static class MySearchScript1 extends AbstractFloatSearchScript {
		
		float computeEdge()
		{
			long[] vec2 = new long[128];
			StringBuffer buf = new StringBuffer("");
			for(int i=1;i<=8;i++)
				buf.append(doc().field("edge"+i).getStringValue()+"a");
			buf.deleteCharAt(buf.length()-1);
			toArray(buf.toString(), vec2);
			return compareHist2D(vec1, vec2);
		}
		
		float computeColor()
		{
			long[] cvec2 = new long[64];
			StringBuffer buf = new StringBuffer("");
			for(int i=1;i<=4;i++)
				buf.append(doc().field("color"+i).getStringValue()+"a");
			buf.deleteCharAt(buf.length()-1);
			toArray(buf.toString(),cvec2);
			return compareHist(cvec1, cvec2);
		}
		
		float computeAPIDQ()
		{
			long[] avec2 = new long[128];
			StringBuffer buf = new StringBuffer("");
			for(int i=1;i<=8;i++)
				buf.append(doc().field("apidq"+i).getStringValue()+"a");
			buf.deleteCharAt(buf.length()-1);
			toArray(buf.toString(),avec2);
			return compareHist(avec1, avec2);
		}

		float computeCorner()
		{
			long[] cor_vec2 = new long[8];
			StringBuffer buf = new StringBuffer("");
			buf.append(doc().field("corner").getStringValue());
			toArray(buf.toString(),cor_vec2);
			return compareHist1D(cor_vec1, cor_vec2);
		}
		
		
		@Override
		  public float runAsFloat() {
			
			float sim_cor = computeCorner(),sim_apidq,sim_color,sim_edge;
			
			if(sim_cor>=70)
			{sim_apidq = computeAPIDQ();
				if(sim_apidq>=75)
				{sim_color = computeColor();
					if(sim_color>=65)
					{sim_edge = computeEdge();
						if(sim_edge>=65)
							return (float)((.7*sim_cor+.8*sim_apidq+.9*sim_color+sim_edge)/3.4);
					}
				}
			}
			return 0;
			
			//return computeEdge();	  // 65
			//return computeCorner(); //70
			//return computeColor();	  //65
			//return computeAPIDQ(); //75
			//return doc().field("color1").getStringValue().lastIndexOf('a');
		}
    }
}
