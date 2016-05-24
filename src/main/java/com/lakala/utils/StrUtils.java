package com.lakala.utils;

public class StrUtils {
	public static String[] splitHosts(String quorum) {
		
		String[] nodes = quorum.substring(1, quorum.length() - 1).split(",");
		return nodes;
	}
	public static String[] splitPorts(String node) {
		String[] hostAndPort = node.split(":");
		return hostAndPort;
	}
}
