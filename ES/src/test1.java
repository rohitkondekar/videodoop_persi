import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;


public class test1 {
	public static void main(String[] args) throws ElasticSearchException, IOException
	{		
		ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
		settings.put("index.number_of_shards",10);
		settings.put("index.number_of_replicas",0);
		settings.put("cluster.name","videodoop");
		//settings.put("node.name","vcomp");
		TransportClient tmp = new TransportClient(settings);
		tmp.addTransportAddress(new InetSocketTransportAddress("10.222.250.201", 9300));
		Client client = tmp;
		
		FileReader fr = new FileReader("/home/user/Data/features.txt"); 
		BufferedReader br = new BufferedReader(fr); 
		String s; 
		IndexResponse response = null;
		StringTokenizer token;
		String a,b,c,d;
		/*
		while((s = br.readLine()) != null) 
		{ 		
			token = new StringTokenizer(s,"\t");
			a=token.nextToken();//b=token.nextToken();c=token.nextToken();d=token.nextToken();
			response = client.prepareIndex("movie", "video",a)
		        .setSource(jsonBuilder()
		                    .startObject()
		                    .field("frame", a)
		                    .field("color", token.nextToken())
		                    .field("face",  token.nextToken())                   
		                    .field("edge1", token.nextToken())
		                    .field("edge2", token.nextToken())	
		                    .field("edge3", token.nextToken())	
		                    .field("edge4", token.nextToken())	
		                    .field("edge5", token.nextToken())	
		                    .field("edge6", token.nextToken())	
		                    .field("edge7", token.nextToken())	
		                    .field("edge8", token.nextToken())	
		                    .field("color1", token.nextToken())
		                    .field("color2", token.nextToken())
		                    .field("color3", token.nextToken())
		                    .field("color4", token.nextToken())
		                    .field("apidq1", token.nextToken())
		                    .field("apidq2", token.nextToken())	
		                    .field("apidq3", token.nextToken())	
		                    .field("apidq4", token.nextToken())	
		                    .field("apidq5", token.nextToken())	
		                    .field("apidq6", token.nextToken())	
		                    .field("apidq7", token.nextToken())	
		                    .field("apidq8", token.nextToken())	
		                    .field("corner", token.nextToken())	
		                    .endObject()
		                  )
		        .execute()
		        .actionGet();
			//System.out.println(d);
		}
		*/
		//System.out.println(response.getId().toString());
		Map<String,Object> map = new HashMap<String,Object>();
		Object vc1 = "34469a42994a7412a16679a15196a0a29280a22609a43736a14455a16679a9266a11119a7783a26686a11860a28169a5559a10378a5189a5189a370a20756a2965a12972a6300a4818a1853a0a0a7042a20756a5559a19644a5189a21868a1111a0a17049a13343a22238a16679a14825a18161a10748a10378a15937a7783a5559a2594a5189a7412a0a10007a4447a5559a1853a9266a3706a4077a0a7412a4818a14084a4077a4818a3706a3706a370a11489a13343a0a4818a3335a3335a2965a1111a10007a8895a370a9266a1853a2223a22238a10748a10748a10378a5930a8154a0a0a20756a18532a7042a370a0a4818a0a0a10748a6300a0a0a0a2965a0a0a6300a12601a370a0a0a2594a0a0a5930a8895a741a0a0a3706a0a0a370a0a0a0a0";
		Object vc2 = "14839a1876a4985a3573a3511a2293a1429a10849a32a24a23a52a32a57a28a608a52a22a15a4a2a4a6a244a128a2140a7623a84a17a41a10a199a56a758a1370a329a155a79a43a85a91a429a591a565a172a118a56a538a47a297a552a289a51a50a27a57a64a581a1171a1153a399a233a140a188";
		Object vc3 = "23487a0a0a0a0a0a0a0a2514a3345a0a1831a0a1295a0a0a2484a0a752a0a712a0a842a0a845a365a0a558a0a251a0a0a507a393a288a372a304a169a301a497a559a145a243a312a158a139a201a0a219a131a144a225a209a98a139a236a339a122a130a346a125a137a109a159a284a97a215a301a314a110a234a290a195a71a55a183a64a82a45a90a169a59a58a214a146a76a94a166a162a45a53a155a91a69a82a69a167a62a62a190a183a100a131a237a199a48a41a159a145a75a87a134a163a95a50a152a163a97a77a168a1655a1174a494a1515a2513a791a713a2110";
		Object vc4 = "62500a337500a87500a250000a162500a100000a0a0";
		map.put("vec1",vc1);		
		map.put("cvec1",vc2);
		map.put("avec1",vc3);
		map.put("cor_vec1",vc4);
		SearchRequestBuilder builder = client.prepareSearch("movie").setTypes("video");	
		QueryBuilder qb1 = QueryBuilders.customScoreQuery(QueryBuilders.queryString("color:BLACK AND face:true")).script("foo").lang("native").params(map);
		builder.setQuery(qb1);
		//builder.addScriptField(name, lang, script, params)
		//builder.addSort("hits",SortOrder.DESC);
		builder.setSize(1000);
		builder.setExplain(true);
		//builder.addScriptField("MyScriptFactory2", "foo");//("foo", "native", "foo", null);
		builder.execute();
		SearchResponse rsp = builder.execute().actionGet();
		//System.out.println(rsp.getHits().getHits()[0].getIndex());
		SearchHit[] docs = rsp.getHits().getHits();
		System.out.println(docs.length+"");
		for(SearchHit d1: docs)
		{
			System.out.println(d1.getScore()+" "+d1.getSource().remove("frame").toString());
		}
		
		
		// on shutdown
		//node.close();*/
		client.close();
	}
}
