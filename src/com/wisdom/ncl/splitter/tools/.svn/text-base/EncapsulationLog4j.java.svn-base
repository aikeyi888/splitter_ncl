package com.wisdom.ncl.splitter.tools;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class EncapsulationLog4j {

	private static EncapsulationLog4j impl = new EncapsulationLog4j();
	private Logger log4j;

	private EncapsulationLog4j() {
		this.log4j = LogManager.getLogger(EncapsulationLog4j.class);
	}

	public void log(String level, Object msg) {
		log(level, msg, null);
	}

	public void log(String level, Throwable e) {
		log(level, null, e);
	}

	public void log(String level, Object msg, Throwable e) {
		if (this.log4j != null)
			this.log4j.log(Level.toLevel(level), msg, e);
	}

	public static EncapsulationLog4j getInstances() {
		return impl;
	}
}
