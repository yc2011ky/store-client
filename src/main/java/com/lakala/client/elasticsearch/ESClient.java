package com.lakala.client.elasticsearch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.lakala.support.AbstractClient;
import com.lakala.utils.StrUtils;

public class ESClient extends AbstractClient {
	
	private String CLIENT_KEY = "es-nodes";
	private int DEFAULT_PORT = 9300;
	private Client client = null;
	private static final String defaultName = "elasticsearch";
	private String name = null;
	private String index = null;
	private String type = null;
	
	public ESClient() {
		this(new Properties());
	}
	public ESClient(Properties settings) {
		super(settings);
	}
	
	@Override
	public void initClient() throws Exception {
		if (this.settings.keySet().size() == 0) {
			try {
				this.settings = readProperties();
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
		if(this.settings.keySet().contains("index")) {
			this.index = settings.getProperty("index");
		} else {
			if(logger.isDebugEnabled()) {
				logger.debug("no index in properties file");
			}
		}
		if(this.settings.keySet().contains("type")) {
			this.type = settings.getProperty("type");
		} else {
			if(logger.isDebugEnabled()) {
				logger.debug("no type in properties file");
			}
		}
		
		if(this.settings.keySet().contains("cluster.name")) {
			this.name = settings.getProperty("cluster.name");
		} else {
			if(logger.isDebugEnabled()) {
				logger.debug("no cluster name provided, use default");
			}
		}
		newTransportClient();
	}
	
	@Override
	public Properties readProperties() throws IOException {
		return super.readProperties();
	}
	protected synchronized void newTransportClient() throws UnknownHostException {
		if(this.client == null) {
			Settings s = null;
			if (this.name == null) {
				s = Settings.settingsBuilder()
						.put("cluster.name", this.defaultName)
						.put("client.transport.sniff", true).build();
			} else {
				s = Settings.settingsBuilder()
						.put("cluster.name", this.name)
						.put("client.transport.sniff", true).build();
			}
			InetSocketTransportAddress[] allRemoteNodes = this.getTransportAddresses(getProperties());
			System.out.println("---> " + allRemoteNodes.length);
			Client client = TransportClient.builder().settings(s).build().addTransportAddresses(allRemoteNodes);
			setClient(client);
		}
	}
	
	private void setClient(Client c) {
		this.client = c;
	}
	
	@Override
	public void setProperties(String key, String value) {
		if(key.equalsIgnoreCase("index")) {
			this.index = value;
		} else if(key.equalsIgnoreCase("type")) {
			this.type = value;
		} else ;
	}
	
	private InetSocketTransportAddress[] getTransportAddresses(Properties p) {
		List<InetSocketTransportAddress> nodeList = new ArrayList<InetSocketTransportAddress>();
		String hostsAndPorts = p.getProperty(CLIENT_KEY);
		String[] nodes = StrUtils.splitHosts(hostsAndPorts);
		if(nodes.length >= 1) {
			for(String a : nodes) {
				String[] hostAndPort = StrUtils.splitPorts(a);
				if(hostAndPort.length == 2) {
					try {
						if(hostAndPort[1].length() != 0) {
							nodeList.add(new InetSocketTransportAddress(
									InetAddress.getByName(hostAndPort[0]), Integer.parseInt(hostAndPort[1])));
						}else {
							nodeList.add(new InetSocketTransportAddress(
									InetAddress.getByAddress(hostAndPort[0].getBytes()), DEFAULT_PORT));
						}
					} catch (UnknownHostException e) {
						if(logger.isWarnEnabled()) {
							logger.warn("this");
						}
					}
				}else if(hostAndPort.length == 1) {
					try {
						nodeList.add(new InetSocketTransportAddress(
								InetAddress.getByAddress(hostAndPort[0].getBytes()), DEFAULT_PORT));
					} catch (UnknownHostException e) {
						if(logger.isWarnEnabled()) {
							logger.warn("this");
						}
					}
				}
			}
		}
		return nodeList.toArray(new InetSocketTransportAddress[nodeList.size()]);
	}
	
	@Override
	public Object putRecord(Object value) throws Exception {
		if(this.client != null) {
			if(index == null || type == null) {
				if(logger.isErrorEnabled()) {
					logger.error("index or type is not provider in the client.properties file,"
							+ " please enter from interface");
					throw new Exception("index or type is not provided");
				}
			}
			String jsonData = (String) value;
			IndexResponse response = client.prepareIndex(index, type)
					.setSource(jsonData)
					.get();
			return response.isCreated();
		} else {
			if(logger.isWarnEnabled()) {
				logger.warn("client is null please initiate it");
			}
			return null;
		}
	}
	
	@Override
	public Object putRecord(Object key, Object value) throws Exception {
		if(this.client != null) {
			if(index == null || type == null) {
				if(logger.isErrorEnabled()) {
					logger.error("index or type is not provider in the client.properties file,"
							+ " please enter from interface");
					throw new Exception("index or type is not provided");
				}
			}
			String jsonData = (String) value;
			IndexResponse response = client.prepareIndex(index, type, (String) key)
					.setSource(jsonData)
					.get();
			return response.isCreated();
		} else {
			if(logger.isErrorEnabled()) {
				logger.error("client is null please initiate it");
			}
			return null;
		}
	}
	@Override
	public Object getRecord(Object key) throws Exception {
		if(this.client != null) {
			if(index == null || type == null) {
				if(logger.isErrorEnabled()) {
					logger.error("index or type is not provider in the client.properties file,"
							+ " please enter from interface");
					throw new Exception("index or type is not provided");
				}
			}
			GetResponse response = client.prepareGet(index, type, (String) key)
					.setOperationThreaded(false)
					.get();
			return response.getSourceAsMap();
		} else {
			if(logger.isErrorEnabled()) {
				logger.error("client is null please initiate it");
			}
			return null;
		}
	}
	@Override
	public Object queryRecord(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object updateRecord(Object key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void delete(Object o) throws Exception {
		if(this.client != null) {
			if(index == null || type == null) {
				if(logger.isErrorEnabled()) {
					logger.error("index or type is not provider in the client.properties file,"
							+ " please enter from interface");
					throw new Exception("index or type is not provided");
				}
			}
			DeleteResponse response = client.prepareDelete(index, type, (String) o)
					.get();
		} else {
			if(logger.isErrorEnabled()) {
				logger.error("client is null please initiate it");
			}
		}
	}
	@Override
	public void close() {
		if(client != null) {
			client.close();
		}
		client = null;
		if(logger.isInfoEnabled()) {
			logger.info("close ESClient, that is the Elasticsearch transport client...");
		}
	}

	
}
