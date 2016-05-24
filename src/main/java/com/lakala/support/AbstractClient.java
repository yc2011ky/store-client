package com.lakala.support;

import java.util.Properties;

import com.lakala.client.Client;
import com.lakala.common.component.AbstractComponent;

public abstract class AbstractClient extends AbstractComponent implements Client {

	
	@Override
	public Object putRecord(Object key, Object value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRecord(Object key) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public AbstractClient(Properties settings) {
		super(settings);
	}
	
	@Override
	public void initClient() throws Exception {
		
	}

	@Override
	public Object putRecord(Object o) throws Exception {
		return null;
	}

	@Override
	public Object queryRecord(Object key) {
		return null;
	}

	@Override
	public Object updateRecord(Object key, Object o) {
		return null;
	}

	@Override
	public void delete(Object key) throws Exception {
		
	}
	
	@Override
	public void setProperties(String key, String value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() {
		
	}
	
	
//	
//	public void initClient() throws Exception {
//		return;
//	}
//	
//	//abstract protected void newTransportClient() throws UnknownHostException;
//	
//	public Object putRecord(Object o) {
//		return null;
//	}
//	
//	public Object queryRecord(Object o) {
//		return null;
//	}
//	
//	public Object updateRecord(Object o) {
//		return null;
//	}
//	
//	public void delete(Object o) {
//		;
//	}
	
	
	
}
