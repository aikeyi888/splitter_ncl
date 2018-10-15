package com.wisdom.ncl.splitter.tools;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;

public class Base64Tools {
	// ±àÂë
	public static String encode(String input) {
		try {
			byte[] bytes = input.getBytes("utf-8");
			byte[] encode = Base64.encodeBase64(bytes);
			String str_encode = new String(encode, "utf-8");

			return str_encode;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	// ±àÂë
	public static String encode(byte[] input) {
		try {
			byte[] encode = Base64.encodeBase64(input);
			String str_encode = new String(encode, "utf-8");

			return str_encode;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	// ½âÂë
	public static String decode(String input) {
		try {
			byte[] bytes = input.getBytes("utf-8");
			byte[] encode = Base64.decodeBase64(bytes);
			String str_decode = new String(encode, "utf-8");

			return str_decode;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	// ½âÂë
	public static byte[] decodeToByte(String input) {
		try {
			byte[] bytes = input.getBytes("utf-8");
			byte[] decode = Base64.decodeBase64(bytes);
			return decode;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		String result = Base64Tools.encode("#234567890");
		System.out.println(result);
		System.out.println(Base64Tools.decode(result));
	}

}
