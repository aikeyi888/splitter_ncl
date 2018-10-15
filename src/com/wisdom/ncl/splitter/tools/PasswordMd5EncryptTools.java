package com.wisdom.ncl.splitter.tools;
/**
 * MD5加密
 * 
 * @author yuhao
 */
public class PasswordMd5EncryptTools {
	public PasswordMd5EncryptTools() {
	}

	// 对外提供的方法
	public String pw_encrypt(String password) {
		if (password == null || password.equals("") || password.length() == 32) {
			return password;
		}
		String s = new String();
		try {
			java.security.MessageDigest alga = java.security.MessageDigest
					.getInstance("MD5");
			alga.update(password.getBytes());
			byte[] digesta = alga.digest();
			PasswordMd5EncryptTools sss = new PasswordMd5EncryptTools();
			s = sss.byte2hex(digesta);
			alga.reset();
		} catch (Exception e) {
			System.err.println("md5加密方法错误!");
		}
		return s;
	}

	// md5加密的根本方法
	public String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static void main(String[] args) {

		// String password = "admin";
		// java.security.MessageDigest alga;
		// try {
		// alga = java.security.MessageDigest.getInstance("MD5");
		// alga.update(password.getBytes());
		// byte[] digesta = alga.digest();
		// Password_encrypt sss = new Password_encrypt();
		// String newPassword = sss.byte2hex(digesta);
		// System.out.println(newPassword);
		//			
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		System.out.println(new PasswordMd5EncryptTools().pw_encrypt("admin"));
	}
}
