package com.wisdom.ncl.splitter.tools;

public class Log {
	private static EncapsulationLog4j encapsulationLog4j = EncapsulationLog4j
			.getInstances();

	public static void LogError(String msg) {
		encapsulationLog4j.log("Error", msg);
	}

	public static void LogError(Throwable e) {
		encapsulationLog4j.log("Error", null, e);
	}

	public static void LogWarn(String msg) {
		encapsulationLog4j.log("Warn", msg);
	}

	public static void LogWarn(Throwable e) {
		encapsulationLog4j.log("Warn", null, e);
	}

	public static void LogInfo(String msg) {
		encapsulationLog4j.log("Info", msg);
	}

	public static void LogInfo(Throwable e) {
		encapsulationLog4j.log("Info", null, e);
	}

	public static void LogDebug(String msg) {
		encapsulationLog4j.log("Debug", msg);
	}

	public static void LogDebug(Throwable e) {
		encapsulationLog4j.log("Debug", null, e);
	}
}
