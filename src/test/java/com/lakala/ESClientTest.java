package com.lakala;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;

import com.lakala.client.Client;
import com.lakala.utils.StoreFactory;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class ESClientTest {
	public static void main(String[] args) throws Exception {
		Client client1 = StoreFactory.getClient("es");
		try {
			client1.initClient();
			client1.setProperties("index", "twitter");
			client1.setProperties("type", "tweet");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String json = "{" + "\"user\":\"yc2\"" + "}";
		System.out.println(client1.putRecord("4", json) + "---------------->");
		System.out.println(((Map<String, Object>) client1.getRecord("3")).toString());
	//	client1.delete("3");
	//	System.out.println(((Map<String, Object>) client1.getRecord("3")).toString());
		Thread.sleep(40000);
		client1.close();
	}

}
