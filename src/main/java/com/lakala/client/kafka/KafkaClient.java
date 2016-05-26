package com.lakala.client.kafka;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.lakala.support.AbstractClient;

public class KafkaClient extends AbstractClient {
	
	
	private KafkaProducer<Object, Object> producer = null;
	
	public KafkaClient() {
		this(new Properties());
	}
	
	public KafkaClient(Properties settings) {
		super(settings);
	}
	
	@Override
	public void initClient() throws Exception {
		if (this.settings.keySet().size() == 0) {
			try {
				this.settings = readProperties();
				logger.info("--------->" + this.settings.toString());
			} catch (FileNotFoundException e) {
				if (logger.isErrorEnabled()) {
					logger.error("file not exist, please check");
				}
				throw new Exception("configuration read failed");
			} catch (IOException e) {
				if (logger.isErrorEnabled()) {
					logger.error("io exception, please retry");
				}
				throw new Exception("configuration read failed");
			}
		}
		
		if(this.producer == null) {
			this.producer = this.createKafkaProducer();
		}
	}
	
	public KafkaProducer<Object, Object> createKafkaProducer() throws Exception {
		if (this.settings.keySet().size() == 0) {
			try {
				this.settings = readProperties();
				logger.info("----------->" + settings.toString());
			} catch (FileNotFoundException e) {
				if (logger.isErrorEnabled()) {
					logger.error("file not exist, please check");
				}
				throw new Exception("configuration read failed");
			} catch (IOException e) {
				if (logger.isErrorEnabled()) {
					logger.error("io exception, please retry");
				}
				throw new Exception("configuration read failed");
			}
		}
		return new KafkaProducer<Object, Object>(this.settings);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object putRecord(Object o) throws Exception {
		if(o instanceof ProducerRecord) {
			return this.producer.send((ProducerRecord<Object, Object>) o);
		} else {
			throw new Exception("please check the record format");
		}
	}
	

}
