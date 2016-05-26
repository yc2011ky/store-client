package com.lakala.utils;

import com.lakala.client.Client;
import com.lakala.client.elasticsearch.ESClient;
import com.lakala.client.kafka.KafkaClient;

public class StoreFactory {
	
	private final static String ES_TYPE = "elasticsearch";
	private final static String ES_S_TYPE = "es";
	private final static String KAFKA_TYPE = "kafka";
	
	
	public synchronized static Client getClient(String clientType) {
		if(clientType.equalsIgnoreCase(ES_TYPE) || clientType.equalsIgnoreCase(ES_S_TYPE)) {
			return new ESClient();
		}else if((clientType.equalsIgnoreCase(KAFKA_TYPE))) {
			return new KafkaClient();
		}else return null;
//		} else throw new Exception("type: " + clientType + "does not integrated");
	}
	
	public static void main(String[] args) {
		Client client = StoreFactory.getClient("es");
		try {
			client.initClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("hello yc");
		client.close();
	}
}
