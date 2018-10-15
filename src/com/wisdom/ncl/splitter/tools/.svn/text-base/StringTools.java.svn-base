package com.wisdom.ncl.splitter.tools;

/**
 * 
 * @author yuhao
 * @description 字符串处理类
 * @date 2011-07-26
 * @updater
 * @version 1.0.0
 * 
 */
public class StringTools {
	
	/**
	 * 
	 * 功能说明:  把字符串中的点引号转化成双引号  
	 * @param src
	 * @return
	 */
	public static String changeSqToDq(String src)
	{
		if(src == null || "".equals(src))
		{
			return src;
		}
		src = src.replaceAll("'", "\"");	
		return src;
	}

	/**
	 * 
	 * 功能说明: 头两个字母小写
	 * 
	 * @param src
	 * @return
	 */
	public static String doubleLetterLowerCase(String src) {
		if (src == null || "".equals(src)) {
			return src;
		} else {
			return src.substring(0, 2).toLowerCase() + src.substring(2);
		}
	}

	public static String oneUpperTwoLower(String src) {
		if (src == null || "".equals(src)) {
			return src;
		} else {
			return src.substring(0, 1) + src.substring(1, 2).toLowerCase()
					+ src.substring(2);
		}
	}

	/**
	 * 
	 * 功能说明: 首字母小写
	 * 
	 * @param src
	 * @return
	 */
	public static String firstLetterLowercase(String src) {
		if (src == null || "".equals(src)) {
			return src;
		} else {
			return src.substring(0, 1).toLowerCase() + src.substring(1);
		}
	}

	/**
	 * 
	 * 功能说明: 首字母大写
	 * 
	 * @param src
	 * @return
	 */
	public static String firstLetterUppercase(String src) {
		if (src == null || "".equals(src)) {
			return src;
		} else {
			return src.substring(0, 1).toUpperCase() + src.substring(1);
		}
	}

	/**
	 * @description 字符串两端去空格 其中null会转化为空
	 * @param 待处理字符串
	 * @return 处理之后字符串
	 */
	public static String toTrim(String str) {
		if (str == null) {
			return "";
		}
		if (str.trim().equalsIgnoreCase("null")) {
			return "";
		}
		return str.trim();
	}

	/**
	 * @description 判断字符串是否为空 其中null也会返回 true
	 * @param 待处理字符串
	 * @return 为空返回true 不为空返回false
	 */

	public static boolean isEmpty(String str) {
		boolean ret = false;
		if (toTrim(str).equals("")) {
			ret = true;
		}
		return ret;
	}

	/**
	 * @description 转换null 如为null 转换为"" 其他按原样输出
	 * @param 待处理字符串
	 * @return 如为null转换为"" 其他按原样输出
	 */
	public static String dealNull(String str) {
		String returnstr = null;
		if (str == null) {
			returnstr = "";
		} else {
			returnstr = str;
		}
		return returnstr;
	}

	/**
	 * @description 转换null 如为null 转换为"" 其他按原样输出
	 * @param 待处理对象
	 * @return 如为null转换为"" 其他按原样输出
	 */
	public static Object dealNull(Object obj) {
		Object returnstr = null;
		if (obj == null) {
			returnstr = "";
		} else {
			returnstr = obj;
		}
		return returnstr;
	}

	/**
	 * @description 把字符串从ISO8859_1转换到GBK
	 * @param 待转化字符串
	 * @return 转化之后的结果
	 */
	public static String iso2gbk(String isostr) {
		if (isostr != null) {
			byte[] tmpbyte = (byte[]) null;
			try {
				tmpbyte = isostr.getBytes("ISO8859_1");
			} catch (Exception e) {
				System.err.println("Error: Method: StringTools.iso2gbk :"
						+ e.getMessage());
			}
			try {
				isostr = new String(tmpbyte, "GBK");
			} catch (Exception e) {
				System.err.println("Error: Method: StringTools.iso2gbk :"
						+ e.getMessage());
			}
		}
		return isostr;
	}

	/**
	 * @description 把字符串从GBK转换到ISO8859_1
	 * @param 待转化字符串
	 * @return 转化之后的结果
	 */
	public static String gbk2iso(String gbkstr) {
		if (gbkstr != null) {
			byte[] tmpbyte = (byte[]) null;
			try {
				tmpbyte = gbkstr.getBytes("GBK");
			} catch (Exception e) {
				System.err.println("Error: Method: StringTools.gbk2iso :"
						+ e.getMessage());
			}
			try {
				gbkstr = new String(tmpbyte, "ISO8859_1");
			} catch (Exception e) {
				System.err.println("Error: Method: StringTools.gbk2iso :"
						+ e.getMessage());
			}
		}
		return gbkstr;
	}

	/**
	 * @description 把字符串从UTF-8转换到ISO8859_1
	 * @param 待转化字符串
	 * @return 转化之后的结果
	 */
	public static String utf82iso(String utfstr) {
		if (utfstr != null) {
			byte[] tmpbyte = (byte[]) null;
			try {
				tmpbyte = utfstr.getBytes("UTF-8");
			} catch (Exception e) {
				System.err.println("Error: Method: StringTools.utf82iso :"
						+ e.getMessage());
			}
			try {
				utfstr = new String(tmpbyte, "ISO8859_1");
			} catch (Exception e) {
				System.err.println("Error: Method: StringTools.utf82iso :"
						+ e.getMessage());
			}
		}
		return utfstr;
	}

	/**
	 * @description 把字符串从ISO8859_1转换到UTF-8
	 * @param 待转化字符串
	 * @return 转化之后的结果
	 */
	public static String iso2utf8(String iosStr) {
		if (iosStr != null) {
			byte[] tmpbyte = (byte[]) null;
			try {
				tmpbyte = iosStr.getBytes("ISO8859-1");
			} catch (Exception e) {
				System.err.println("Error: Method: StringTools.iso2utf8 :"
						+ e.getMessage());
			}
			try {
				iosStr = new String(tmpbyte, "UTF-8");
			} catch (Exception e) {
				System.err.println("Error: Method: StringTools.iso2utf8 :"
						+ e.getMessage());
			}
		}
		return iosStr;
	}

	/**
	 * @description 判断字符串是否全是数字
	 * @param 待校验字符串
	 * @return 全是数字返回true 否则返回false
	 */
	public static boolean isNumber(String validString) {
		boolean flag = true;
		try {
			byte[] tempbyte = validString.getBytes();
			for (int i = 0; i < validString.length(); ++i) {
				if ((tempbyte[i] < 48) || (tempbyte[i] > 57)) {
					return false;
				}
			}
		} catch (Exception e) {
			System.err.println("Error: Method: StringTools.isNumber :"
					+ e.getMessage());
		}
		return flag;
	}

	/**
	 * @description 判断字符串是否全是字符(包含数字与英文字母的大小写)组成
	 * @param 待校验字符串
	 * @return 全是字符返回true 否则返回false
	 */
	public static boolean isChar(String validString) {
		byte[] tempbyte = validString.getBytes();
		for (int i = 0; i < validString.length(); ++i) {
			if (tempbyte[i] >= 48)
				if (((((tempbyte[i] > 57) ? 1 : 0) & ((tempbyte[i] < 65) ? 1
						: 0)) == 0)
						&& (tempbyte[i] <= 122))
					if ((((tempbyte[i] > 90) ? 1 : 0) & ((tempbyte[i] < 95) ? 1
							: 0)) == 0)
						if ((((tempbyte[i] > 95) ? 1 : 0) & ((tempbyte[i] < 97) ? 1
								: 0)) == 0)
							continue;
			return false;
		}
		return true;
	}

	/**
	 * @description 判断字符串是否全是字母(英文字母的大小写)组成
	 * @param 待校验字符串
	 * @return 全是字母返回true 否则返回false
	 */
	public static boolean isLetter(String validString) {
		byte[] tempbyte = validString.getBytes();
		for (int i = 0; i < validString.length(); ++i) {
			if ((tempbyte[i] >= 65) && (tempbyte[i] <= 122))
				if ((((tempbyte[i] > 90) ? 1 : 0) & ((tempbyte[i] < 97) ? 1 : 0)) == 0)
					continue;
			return false;
		}
		return true;
	}

	/**
	 * @description 字符串替换
	 * @param sourceString
	 *            原始字符串 toReplaceString 要被替换的字符串 replaceString 被替换成的字符串
	 * @return 替换之后的 结果
	 */
	public static String strReplace(String sourceString,
			String toReplaceString, String replaceString) {
		String returnString = sourceString;
		int stringLength = 0;
		if (toReplaceString != null)
			stringLength = toReplaceString.length();
		if ((returnString != null) && (returnString.length() > stringLength)) {
			int max = 0;
			String S4 = "";
			for (int i = 0; i < sourceString.length(); ++i) {
				max = (i + toReplaceString.length() <= sourceString.length()) ? i
						+ stringLength
						: sourceString.length();
				String S3 = sourceString.substring(i, max);
				if (!S3.equals(toReplaceString)) {
					S4 = S4 + S3.substring(0, 1);
				} else {
					S4 = S4 + replaceString;
					i += stringLength - 1;
				}
			}

			returnString = S4;
		}
		return returnString;
	}

	/**
	 * @description 字符串截断 位置从1开始计数
	 * @param str
	 *            原始字符串 pstart 起始位置 pend 结束位置
	 * @return 截断之后的字符串
	 */
	public static String getSubString(String str, int pstart, int pend) {
		String resu = "";
		int beg = 0;
		int end = 0;
		int count1 = 0;
		char[] temp = new char[str.length()];
		str.getChars(0, str.length(), temp, 0);
		boolean[] bol = new boolean[str.length()];
		for (int i = 0; i < temp.length; i++) {
			bol[i] = false;
			if ((int) temp[i] > 255) {// 说明是中文
				count1++;
				bol[i] = true;
			}
		}

		if (pstart > str.length() + count1) {
			resu = null;
		}
		if (pstart > pend) {
			resu = null;
		}
		if (pstart < 1) {
			beg = 0;
		} else {
			beg = pstart - 1;
		}
		if (pend > str.length() + count1) {
			end = str.length() + count1;
		} else {
			end = pend;// 在substring的末尾一样
		}
		// 下面开始求应该返回的字符串
		if (resu != null) {
			if (beg == end) {
				int count = 0;
				if (beg == 0) {
					if (bol[0] == true)
						resu = null;
					else
						resu = new String(temp, 0, 1);
				} else {
					int len = beg;// zheli
					for (int y = 0; y < len; y++) {// 表示他前面是否有中文,不管自己
						if (bol[y] == true)
							count++;
						len--;// 想明白为什么len--
					}
					// for循环运行完毕后，len的值就代表在正常字符串中，目标beg的上一字符的索引值
					if (count == 0) {// 说明前面没有中文
						if ((int) temp[beg] > 255)// 说明自己是中文
							resu = null;// 返回空
						else
							resu = new String(temp, beg, 1);
					} else {// 前面有中文，那么一个中文应与2个字符相对
						if ((int) temp[len + 1] > 255)// 说明自己是中文
							resu = null;// 返回空
						else
							resu = new String(temp, len + 1, 1);
					}
				}
			} else {// 下面是正常情况下的比较
				int temSt = beg;
				int temEd = end - 1;// 这里减掉一
				for (int i = 0; i < temSt; i++) {
					if (bol[i] == true)
						temSt--;
				}// 循环完毕后temSt表示前字符的正常索引
				for (int j = 0; j < temEd; j++) {
					if (bol[j] == true)
						temEd--;
				}// 循环完毕后temEd-1表示最后字符的正常索引
				if (bol[temSt] == true)// 说明是字符，说明索引本身是汉字的后半部分，那么应该是不能取的
				{
					int cont = 0;
					for (int i = 0; i <= temSt; i++) {
						cont++;
						if (bol[i] == true)
							cont++;
					}
					if (pstart == cont)// 是偶数不应包含,如果pstart<cont则要包含
						temSt++;// 从下一位开始
				}
				if (bol[temEd] == true) {// 因为temEd表示substring
					// 的最面参数，此处是一个汉字，下面要确定是否应该含这个汉字
					int cont = 0;
					for (int i = 0; i <= temEd; i++) {
						cont++;
						if (bol[i] == true)
							cont++;
					}
					if (pend < cont)// 是汉字的前半部分不应包含
						temEd--;// 所以只取到前一个
				}
				if (temSt == temEd) {
					resu = new String(temp, temSt, 1);
				} else if (temSt > temEd) {
					resu = null;
				} else {
					resu = str.substring(temSt, temEd + 1);
				}
			}
		}
		return resu;// 返回结果
	}

	/**
	 * @description 截断字符串 超出指定长度的字符串后面显示...
	 * @param str
	 *            原始字符串 pend 截断的位置
	 * @return 截断之后的字符串
	 */

	public static String fmtString(String str, int pend) {
		String resu = "";
		int beg = 0;
		int end = 0;
		int count1 = 0;
		char[] temp = new char[str.length()];
		str.getChars(0, str.length(), temp, 0);
		boolean[] bol = new boolean[str.length()];
		for (int i = 0; i < temp.length; i++) {
			bol[i] = false;
			if ((int) temp[i] > 255) {// 说明是中文
				count1++;
				bol[i] = true;
			}
		}

		if (1 > str.length() + count1) {
			resu = null;
		}
		if (1 > pend) {
			resu = null;
		}
		if (pend > str.length() + count1) {
			end = str.length() + count1;
		} else {
			end = pend;// 在substring的末尾一样
		}
		// 下面开始求应该返回的字符串
		if (resu != null) {
			if (beg == end) {
				int count = 0;
				if (beg == 0) {
					if (bol[0] == true)
						resu = null;
					else
						resu = new String(temp, 0, 1);
				} else {
					int len = beg;// zheli
					for (int y = 0; y < len; y++) {// 表示他前面是否有中文,不管自己
						if (bol[y] == true)
							count++;
						len--;// 想明白为什么len--
					}
					// for循环运行完毕后，len的值就代表在正常字符串中，目标beg的上一字符的索引值
					if (count == 0) {// 说明前面没有中文
						if ((int) temp[beg] > 255)// 说明自己是中文
							resu = null;// 返回空
						else
							resu = new String(temp, beg, 1);
					} else {// 前面有中文，那么一个中文应与2个字符相对
						if ((int) temp[len + 1] > 255)// 说明自己是中文
							resu = null;// 返回空
						else
							resu = new String(temp, len + 1, 1);
					}
				}
			} else {// 下面是正常情况下的比较
				int temSt = beg;
				int temEd = end - 1;// 这里减掉一
				for (int i = 0; i < temSt; i++) {
					if (bol[i] == true)
						temSt--;
				}// 循环完毕后temSt表示前字符的正常索引
				for (int j = 0; j < temEd; j++) {
					if (bol[j] == true)
						temEd--;
				}// 循环完毕后temEd-1表示最后字符的正常索引
				if (bol[temSt] == true)// 说明是字符，说明索引本身是汉字的后半部分，那么应该是不能取的
				{
					int cont = 0;
					for (int i = 0; i <= temSt; i++) {
						cont++;
						if (bol[i] == true)
							cont++;
					}
					if (1 == cont)// 是偶数不应包含,如果pstart<cont则要包含
						temSt++;// 从下一位开始
				}
				if (bol[temEd] == true) {// 因为temEd表示substring
					// 的最面参数，此处是一个汉字，下面要确定是否应该含这个汉字
					int cont = 0;
					for (int i = 0; i <= temEd; i++) {
						cont++;
						if (bol[i] == true)
							cont++;
					}
					if (pend < cont)// 是汉字的前半部分不应包含
						temEd--;// 所以只取到前一个
				}
				if (temSt == temEd) {
					resu = new String(temp, temSt, 1);
				} else if (temSt > temEd) {
					resu = null;
				} else {
					resu = str.substring(temSt, temEd + 1);
				}
			}
		}
		return resu + "...";// 返回结果
	}

	/**
	 * @description 字符串转换成数组
	 * @param str
	 *            原始字符串 pend 数组的长度
	 * @return 返回的数组 汉字看做是单个字符长度
	 */

	public static String[] strToArray(String str, int len) {
		try {
			if (len <= 0)
				return null;
			int alen = 0;
			if (str == null)
				return null;
			if ((str.trim().equals("null")) || (str.trim().equals("")))
				return null;
			alen = str.length();
			if (alen == 0)
				return null;
			alen = str.length() / len;
			String[] temp = new String[alen];
			for (int i = 0; i < alen; ++i) {
				temp[i] = str.substring(i * len, (i + 1) * len);
			}
			return temp;
		} catch (Exception e) {
			System.out.println("getArray is err" + e.getMessage());
		}
		return null;
	}

	/**
	 * @description 文本转html
	 * @param 待转换文本
	 * @return 转换之后的文本
	 */
	public static String textToHtml(String sourcestr) {
		int strlen;
		StringBuffer restring = new StringBuffer();
		String destr = "";
		strlen = sourcestr.length();
		for (int i = 0; i < strlen; i++) {
			char ch = sourcestr.charAt(i);
			switch (ch) {
			case '<':
				destr = "&lt;";
				break;
			case '>':
				destr = "&gt;";
				break;
			case '\"':
				destr = "&quot;";
				break;
			case '&':
				destr = "&amp;";
				break;
			case 13:
				destr = "<br/>";
				break;
			case 32:
				destr = "&nbsp;";
				break;
			default:
				destr = "" + ch;
				break;
			}
			restring.append(destr);
		}
		return restring.toString();
	}

	/**
	 * @description 在页面上显示文本截断的方法
	 * @param input
	 *            待处理的字符串 length 要截断的长度
	 * @return 无值返回&nbsp; 有值返回<span>截断的字符串...</span>
	 */
	public static String format(String input, int length) {
		int realLength = getRealLength(input);
		if (realLength == 0) {
			return "&nbsp;";
		}
		if (realLength <= length) {
			return input;
		}
		String temp = "<SPAN title=\"" + input + "\">";
		temp = temp + StringTools.fmtString(input, length);
		temp = temp + "</SPAN>";
		return temp;
	}

	public static int getRealLength(String str) {
		int length = 0;
		if ((str == null) || ("".equals(str))) {
			return 0;
		}
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) > 255)
				length += 2;
			else {
				++length;
			}
		}
		return length;
	}

	public static void main(String[] args) {
	}
}
