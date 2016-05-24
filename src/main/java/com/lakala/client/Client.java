package com.lakala.client;

public interface Client {
	
	public final static String FILENAME = "client.properties";
	
	public void initClient() throws Exception;
	
	public void setProperties(String key, String value);
	
	public Object putRecord(Object value) throws Exception;
	
	public Object putRecord(Object key, Object value) throws Exception;
	
	public Object getRecord(Object key) throws Exception;
	
	public Object queryRecord(Object key);
	
	public Object updateRecord(Object key, Object value);
	
	public void delete(Object key) throws Exception;
	
	public void close();
	
}
