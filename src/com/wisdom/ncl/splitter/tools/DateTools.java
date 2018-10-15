package com.wisdom.ncl.splitter.tools;


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author yuhao
 * @description 日期处理类
 * @date 2011-07-26
 * @updater
 * @version 1.0.0
 * 
 */
public class DateTools {
	/**
	 * @description 返回当前日期 默认格式为 yyyyMMddHHmmss
	 * @return 字符串型日期
	 */
	public static String getDate() {
		return getDate("yyyyMMddHHmmss");
	}

	/**
	 * @description 根据格式返回对应格式的日期
	 * @param 要得到的日期的格式
	 * @return 字符串型日期
	 */
	public static String getDate(String fmt) {
		Date myDate = new Date(System.currentTimeMillis());
		SimpleDateFormat sDateformat = new SimpleDateFormat(fmt);
		return sDateformat.format(myDate).toString();
	}

	/**
	 * @description 根据字符串以及对应的格式生成Calender
	 * @param strdate
	 *            字符串型日期数据
	 * @param fmt
	 *            格式
	 * @return 对应Calendar对象
	 */
	private static Calendar getCal(String strdate, String fmt) {
		Calendar cal = null;
		try {
			if ((strdate == null) || (fmt == null)) {
				return cal;
			}
			SimpleDateFormat nowDate = new SimpleDateFormat(fmt);
			Date d = nowDate.parse(strdate, new ParsePosition(0));
			if (d == null) {
				System.out.println("DateTools.getCal :Format Error");
				return cal;
			}
			cal = Calendar.getInstance();
			cal.clear();
			cal.setTime(d);
		} catch (Exception e) {
			System.out.println("Error: Method: DateTools.getCal:"
					+ e.getMessage());
		}
		return cal;
	}

	/**
	 * @description 根据字符串日期值以及格式得到此日期是当年的第几周
	 * @param strdate
	 *            字符串日期值
	 * @param fmt
	 *            格式
	 * @return 第几周
	 */

	public static int getWeekOfYear(String strdate, String fmt) {
		int ret = -1;
		try {
			if ((strdate == null) || (fmt == null)) {
				System.out
						.println("Error: Method: DateTools.getWeekOfYear :Incorrect para");
				return ret;
			}
			Calendar cal = getCal(strdate, fmt);
			if (cal == null) {
				System.out
						.println("Error: Method: DateTools.getWeekOfYear :Incorrect Calendar");
				return ret;
			}
			ret = cal.get(3);
		} catch (Exception e) {
			System.out.println("Fatal: Method: DateTools.getWeekOfYear :"
					+ e.getMessage());
		}
		return ret;
	}

	/**
	 * @description 获取此日起所在的周的所有日期
	 * @param strdate
	 *            字符串型日期值
	 * @param oldfmt
	 *            老格式 需要与strdate对应否则出错
	 * @param newfmt
	 *            返回的日期的格式
	 * @return 一周的日期值对应的数组
	 */
	public static String[] getWeekDay(String strdate, String oldfmt,
			String newfmt) {
		String[] weekday = new String[7];
		try {
			if ((strdate == null) || (oldfmt == null) || (newfmt == null)) {
				System.out
						.println("Error: Method: DateTools.getWeekDay :Incorrect para");
				return weekday;
			}
			Calendar cal = getCal(strdate, oldfmt);
			if (cal == null) {
				System.out
						.println("Error: Method: DateTools.getWeekDay :Incorrect Calendar");
				return weekday;
			}
			int dayOfWeek = cal.get(7);
			cal.add(5, -dayOfWeek + 2);
			SimpleDateFormat sdf = new SimpleDateFormat(newfmt);
			weekday[0] = sdf.format(cal.getTime());
			for (int i = 1; i < 7; ++i) {
				cal.add(5, 1);
				weekday[i] = sdf.format(cal.getTime());
			}
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("Fatal: Method: DateTools.getWeekDay :"
					+ iobe.getMessage());
		}
		return weekday;
	}

	/**
	 * @description 根据年 指定的周数 按照指定格式返回此周的所有日期
	 * @param year
	 *            指定的年
	 * @param week
	 *            指定的周
	 * @param newfmt
	 *            指定日期的格式
	 * @return 指定周的所有日期
	 */
	public static String[] getWeekDate(String year, int week, String newfmt) {
		String[] jweekday = new String[7];
		try {
			if ((year == null) || (year.length() < 4) || (week <= 0)
					|| (newfmt == null)) {
				System.out
						.println("Error: Method: DateTools.getWeekDate :Incorrect para");
				return jweekday;
			}
			Calendar cal = getCal(year + "0101", "yyyyMMdd");
			if (cal == null) {
				System.out
						.println("Error: Method: DateTools.getWeekDate :Incorrect Calendar");
				return jweekday;
			}
			--week;
			cal.add(5, week * 7 - cal.get(7) + 2);
			SimpleDateFormat sdf = new SimpleDateFormat(newfmt);
			jweekday[0] = sdf.format(cal.getTime());
			for (int i = 1; i < 7; ++i) {
				cal.add(5, 1);
				jweekday[i] = sdf.format(cal.getTime());
			}
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("Fatal: Method: DateTools.getWeekDate :"
					+ iobe.getMessage());
		}
		return jweekday;
	}

	/**
	 * @description 根据日期得到此日期是所在周的第几天
	 * @param strdate
	 *            指定的日期
	 * @param oldfmt
	 *            此日期的格式
	 * @return 此日期是第几天
	 */
	public static String getDayOfWeek(String strdate, String oldfmt) {
		String sWeek = null;
		try {
			if ((strdate == null) || (oldfmt == null)) {
				System.out
						.println("Error: Method: DateTools.getDayOfWeek :Incorrect para");
				return sWeek;
			}
			Calendar cal = getCal(strdate, oldfmt);
			if (cal == null) {
				System.out
						.println("Error: Method: DateTools.getDayOfWeek :Incorrect Calendar");
				return sWeek;
			}
			int iWeek = cal.get(7);
			sWeek = "" + ((iWeek - 1 != 0) ? iWeek - 1 : 7);
		} catch (Exception e) {
			System.out.println("Fatal: Method: DateTools.getDayOfWeek :"
					+ e.getMessage());
		}
		return sWeek;
	}

	/**
	 * @description 指定年共有多少周
	 * @param year
	 *            指定的年数
	 * @return 总周数
	 */
	public static int getWeekNum(String year) {
		int weeknum = -1;
		try {
			if (year == null) {
				System.out
						.println("Error: Method: DateTools.getWeekNum :Incorrect para");
				return weeknum;
			}
			Calendar cal = getCal(year + "1231", "yyyyMMdd");
			if (cal == null) {
				System.out
						.println("Error: Method: DateTools.getWeekNum :Incorrect Calendar");
				return weeknum;
			}
			if (cal.get(3) == 1)
				cal.add(5, -7);
			weeknum = cal.get(3);
		} catch (Exception e) {
			System.out.println("Fatal: Method: DateTools.getWeekNum :"
					+ e.getMessage());
		}
		return weeknum;
	}

	/**
	 * @DESCription 当天0点算起指定小时后的日期
	 * @param deftime
	 *            当前日期
	 * @param oldfmt
	 *            原日期格式
	 * @param timediff
	 *            时间差(小时)
	 * @param newfmt
	 *            新日期格式
	 * @return 新格式下的新的日期
	 * @Example System.out.println(getBeforeTime("2011-07-26", "yyyy-MM-dd", 7,
	 *          "yyyyMMdd")); 结果为 20110725
	 */
	public static String getBeforeTime(String deftime, String oldfmt,
			int timediff, String newfmt) {
		return getBeforeTimeBym(deftime, oldfmt, timediff * 60, newfmt);
	}

	/**
	 * @description 指定日期前指定月份的日期
	 * @param deftime
	 *            原日期
	 * @param oldfmt
	 *            原数据格式
	 * @param timediff
	 *            月数差
	 * @param newfmt
	 *            新的格式
	 * @return 新格式下的日期值
	 */
	public static String getBeforeTimeBym(String deftime, String oldfmt,
			int timediff, String newfmt) {
		String rq = null;
		try {
			if ((deftime == null) || (deftime.equals(""))) {
				System.out
						.println("Error: Method: DateTools.getBeforeTime :Incorrect para");
				return rq;
			}
			Calendar cal = getCal(deftime, oldfmt);
			if (cal == null) {
				System.out
						.println("Error: Method: DateTools.getBeforeTime :Incorrect Calendar");
				return rq;
			}
			cal.add(12, -timediff);
			SimpleDateFormat sdf = new SimpleDateFormat(newfmt);
			rq = sdf.format(cal.getTime());
		} catch (Exception e) {
			System.out.println("Fatal: Method: DateTools.getBeforeTime :"
					+ e.getMessage());
		}
		return rq;
	}

	/**
	 * @description 指定日期前指定月份的日期
	 * @param deftime
	 *            原日期
	 * @param oldfmt
	 *            原数据格式
	 * @param timediff
	 *            月数差
	 * @param newfmt
	 *            新的格式
	 * @return 新格式下的日期值
	 * @Example System.out.println(getBeforeTimeByM("2011-07-26", "yyyy-MM-dd",
	 *          -2, "yyyyMMdd")); 结果为：20110926
	 */
	public static String getBeforeTimeByM(String deftime, String oldfmt,
			int timediff, String newfmt) {
		String rq = null;
		try {
			if ((deftime == null) || (deftime.equals(""))) {
				System.out
						.println("Error: Method: DateTools.getBeforeTime :Incorrect para");
				return rq;
			}
			Calendar cal = getCal(deftime, oldfmt);
			if (cal == null) {
				System.out
						.println("Error: Method: DateTools.getBeforeTime :Incorrect Calendar");
				return rq;
			}
			cal.add(2, -timediff);
			SimpleDateFormat sdf = new SimpleDateFormat(newfmt);
			rq = sdf.format(cal.getTime());
		} catch (Exception e) {
			System.out.println("Fatal: Method: DateTools.getBeforeTime :"
					+ e.getMessage());
		}
		return rq;
	}

	/**
	 * @description 日期格式转化
	 * @param mydate
	 *            字符型日期
	 * @param oldfmt
	 *            老格式
	 * @param newfmt
	 *            新格式
	 * @return 新格式对应的日期
	 */
	public static String fmtDate(String mydate, String oldfmt, String newfmt) {
		String restr = null;
		try {
			if ((mydate == null) || (oldfmt == null) || (newfmt == null)) {
				System.out
						.println("Error: Method: DateTools.fmtDate :Incorrect para");
				return restr;
			}
			SimpleDateFormat newDate = new SimpleDateFormat(newfmt);
			Calendar cal = getCal(mydate, oldfmt);
			if (cal == null) {
				System.out
						.println("Error: Method: DateTools.fmtDate :Incorrect Calendar");
				return restr;
			}
			restr = newDate.format(cal.getTime());
		} catch (Exception e) {
			System.out.println("Fatal: Method: DateTools.fmtDate :"
					+ e.getMessage());
		}
		return restr;
	}

	/**
	 * 
	 * 功能说明: 得到给定时间之后的毫秒数的日期
	 * 
	 * @param date
	 * @param timespan
	 * @param newfmt
	 * @return
	 */
	public static String getAfterDateByMillisecond(Date date, long timespan,
			String newfmt) {
		long milli_second = date.getTime();
		Date new_Date = new Date(milli_second + timespan);
		SimpleDateFormat sDateformat = new SimpleDateFormat(newfmt);
		return sDateformat.format(new_Date).toString();
	}

	/**
	 * 
	 * 功能说明:  获取当前时间的值  根据输入格式  
	 * @param format
	 * @return
	 */
	public static String getCurrentTimeByFormat(String format) {
		if (format == null || "".equals(format)) {
			return "";
		}
		String result = "";
		String nowDate = getDate("yyyy-MM-dd HH:mm:ss.SSS");
		if ("yyyy".equalsIgnoreCase(format)) {
			result = nowDate.substring(0, 4);
		} else if ("MM".equals(format)) {
			result = nowDate.substring(nowDate.indexOf("-") + 1, nowDate
					.lastIndexOf("-"));
		} else if ("dd".equalsIgnoreCase(format)) {
			result = nowDate.substring(nowDate.lastIndexOf("-") + 1, nowDate
					.indexOf(" "));
		} else if ("HH".equalsIgnoreCase(format)) {
			result = nowDate.substring(nowDate.indexOf(" ") + 1, nowDate
					.indexOf(":"));
		} else if ("mm".equals(format)) {
			result = nowDate.substring(nowDate.indexOf(":") + 1, nowDate
					.lastIndexOf(":"));
		} else if ("ss".equalsIgnoreCase(format)) {
			result = nowDate.substring(nowDate.lastIndexOf(":") + 1, nowDate
					.indexOf("."));
		} else if ("SSS".equalsIgnoreCase(format)) {
			result = nowDate.substring(nowDate.indexOf(".") + 1);
		}
		return result;
	}

}
