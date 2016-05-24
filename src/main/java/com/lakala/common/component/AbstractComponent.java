package com.lakala.common.component;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractComponent {
	public Properties settings = null;
	public final Logger logger = LoggerFactory.getLogger(getClass());
	
	public AbstractComponent(Properties settings) {
		this.settings = settings;
	}
	
	protected Properties getProperties() {
		return this.settings;
	}
	
}
