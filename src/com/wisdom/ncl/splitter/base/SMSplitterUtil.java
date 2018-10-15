package com.wisdom.ncl.splitter.base;

import java.util.ArrayList;
import java.util.Hashtable;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * <p>
 * Title: 短信拆分工具类
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: 北京东方般若科技有限公司
 * </p>
 * 
 * @author ：Jacky Wang
 * @version 1.0
 */
public class SMSplitterUtil {
	private static String[] m_match_string = new String[6]; // 号码段1-5;
	private static String[][] m_strArray = new String[6][];
	private static int[] m_length = new int[6]; // 号码段1-5允许的一条短信的最大长度
	private static int[] m_support_mms = new int[6]; // 是否支持彩信发送
	private static int m_is_need_splitter_info; // 是否需要分条信息，0：不需要（忽略后续配置项），强制按最大字符拆分，不加任何分条信息
												// 1：需要
	private static int m_is_first_have_info; // 首条是否有分条信息，0：不需要（忽略后续配置项） 1：需要
	private static String m_splitter_info_templ; // 分条信息模板，模板＝[分条信息][T][N]，T代表所有条数，N代表当前条序列号，N-代表N-1，例如：续N-：|：代表第2条的分条信息前置且内容为“续1：”
	private static int m_is_critical_zone_choice; // 关键区间算法选择,1：关键区间不使用分条信息
													// 0：关键区间使用分条信息

	private static String m_org_id; // 公司标识
	private static ArrayList m_mail_server_list = new ArrayList(); // 邮件服务器列表
	private static String m_feed_back_server; // 打开邮件统计服务器
	private static ArrayList m_feed_back_mailbox_list = new ArrayList(); // 退信邮箱列表
	private static String m_mail_trace_server_url;
	private static String m_stat_server_url;
	private static String m_resource_server_url;

	private static int m_mms_need_white_list; // 彩信发送需要白名单
	static {
		String file_path = ConfigurationFile.getConfigFilePath();
		System.out.println("SMCS Path:" + file_path);
		String[] str_length = new String[6];
		String[] str_support_mms = new String[6];
		try {
			for (int i = 1; i <= 5; i++) {
				m_match_string[i] = ConfigurationFile.getProfile(file_path,
						"SplitSMS", "MatchString" + i, "");
				str_length[i] = ConfigurationFile.getProfile(file_path,
						"SplitSMS", "Length" + i, "");
				str_support_mms[i] = ConfigurationFile.getProfile(file_path,
						"SplitSMS", "SupportMMS" + i, "");

				if (!m_match_string[i].equals("")) {
					m_strArray[i] = m_match_string[i].split("\\;");
				}
				try {
					m_length[i] = Integer.parseInt(str_length[i]);
				} catch (Exception ee) {
					m_length[i] = 0;
				}
				try {
					m_support_mms[i] = Integer.parseInt(str_support_mms[i]);
				} catch (Exception ee) {
					m_support_mms[i] = 0;
				}

			}

			String str_is_need_splitter_info = ConfigurationFile.getProfile(
					file_path, "SplitSMS", "IsNeedSplitterInfo", "");
			try {
				m_is_need_splitter_info = Integer
						.parseInt(str_is_need_splitter_info);
			} catch (Exception ee) {
				m_is_need_splitter_info = 0;
			}

			String str_is_first_have_info = ConfigurationFile.getProfile(
					file_path, "SplitSMS", "IsFirstHaveInfo", "");
			try {
				m_is_first_have_info = Integer.parseInt(str_is_first_have_info);
			} catch (Exception ee) {
				m_is_first_have_info = 0;
			}

			m_splitter_info_templ = ConfigurationFile.getProfile(file_path,
					"SplitSMS", "SplitterInfoTempl", "");
			String str_is_critical_zone_choice = ConfigurationFile.getProfile(
					file_path, "SplitSMS", "IsCriticalZoneUseNoneSplitterInfo",
					"");
			try {
				m_is_critical_zone_choice = Integer
						.parseInt(str_is_critical_zone_choice);
			} catch (Exception ee) {
				m_is_critical_zone_choice = 0;
			}

			m_org_id = ConfigurationFile.getProfile(file_path, "Mail", "OrgID",
					"8888");
			m_feed_back_server = ConfigurationFile.getProfile(file_path,
					"Mail", "FeedBackServer", "");
			m_mail_trace_server_url = ConfigurationFile.getProfile(file_path,
					"Mail", "MailTraceServerURL", "");
			m_stat_server_url = ConfigurationFile.getProfile(file_path, "Mail",
					"StatServerURL", "");
			m_resource_server_url = ConfigurationFile.getProfile(file_path,
					"Mail", "ResourceServerURL", "");

			for (int i = 1; i <= 10; i++) {
				String mail_server = ConfigurationFile.getProfile(file_path,
						"Mail", "MailServerIP" + i, "");
				if (!"".equals(mail_server)) {
					m_mail_server_list.add(mail_server);
				}
				String feed_back_mailbox = ConfigurationFile.getProfile(
						file_path, "Mail", "FeedBackMailBox" + i, "");
				if (!"".equals(feed_back_mailbox)) {
					m_feed_back_mailbox_list.add(feed_back_mailbox);
				}
			}
			String mms_need_white_list = ConfigurationFile.getProfile(
					file_path, "MMS", "MMSNeedWhiteList", "0");

			try {
				m_mms_need_white_list = Integer.parseInt(mms_need_white_list);
			} catch (Exception e) {
				m_mms_need_white_list = 0;
			}

		} catch (Exception e) {

		}
	}

	/**
	 * 重新设置参数配置
	 */
	public static void reset() {
		String file_path = ConfigurationFile.getConfigFilePath();

		System.out.println("SMCS Path:" + file_path);
		String[] str_length = new String[6];
		try {
			for (int i = 1; i <= 5; i++) {
				m_match_string[i] = ConfigurationFile.getProfile(file_path,
						"SplitSMS", "MatchString" + i, "");
				str_length[i] = ConfigurationFile.getProfile(file_path,
						"SplitSMS", "Length" + i, "");
				if (!m_match_string[i].equals("")) {
					m_strArray[i] = m_match_string[i].split("\\;");
				}
				try {
					m_length[i] = Integer.parseInt(str_length[i]);
				} catch (Exception ee) {
					m_length[i] = 0;
				}

			}

			String str_is_need_splitter_info = ConfigurationFile.getProfile(
					file_path, "SplitSMS", "IsNeedSplitterInfo", "");
			try {
				m_is_need_splitter_info = Integer
						.parseInt(str_is_need_splitter_info);
			} catch (Exception ee) {
				m_is_need_splitter_info = 0;
			}

			String str_is_first_have_info = ConfigurationFile.getProfile(
					file_path, "SplitSMS", "IsFirstHaveInfo", "");
			try {
				m_is_first_have_info = Integer.parseInt(str_is_first_have_info);
			} catch (Exception ee) {
				m_is_first_have_info = 0;
			}

			m_splitter_info_templ = ConfigurationFile.getProfile(file_path,
					"SplitSMS", "SplitterInfoTempl", "");
			String str_is_critical_zone_choice = ConfigurationFile.getProfile(
					file_path, "SplitSMS", "IsCriticalZoneUseNoneSplitterInfo",
					"");
			try {
				m_is_critical_zone_choice = Integer
						.parseInt(str_is_critical_zone_choice);
			} catch (Exception ee) {
				m_is_critical_zone_choice = 0;
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 查找匹配的号码段
	 * 
	 * @param i
	 *            int
	 * @param mobile_phone
	 *            String
	 * @return boolean
	 */
	private static boolean matchString(int i, String mobile_phone) {
		if (mobile_phone == null || mobile_phone.length() == 0)
			return false;

		String str_match = "";
		int posPercentStart = -1;
		int posQuestionStart = -1;
		int posQuestionEnd = -1;
		boolean bret = false;
		int pos = -1;
		for (int j = 8; j >= 3; j--) {
			if (mobile_phone.length() < j) {
				break;
			}
			String str_seek = mobile_phone.substring(0, j);
			pos = HashTableFind(m_strArray[i], str_seek);
			if (pos >= 0) {
				bret = true;
				break;
			}
		}

		if (bret) {
			str_match = m_strArray[i][pos];
			posPercentStart = str_match.indexOf("%", 0);
			posQuestionStart = str_match.indexOf("?", 0);
			posQuestionEnd = str_match.lastIndexOf("?");
			if (posQuestionStart > 0) {
				if (mobile_phone.length() == posQuestionEnd + 1) {
					return true;
				} else {
					return false;
				}
			} else {
				if (posPercentStart == -1) {
					return true;
				} else {
					if (mobile_phone.length() >= posQuestionEnd + 1) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 通过HashTable查找
	 * 
	 * @param str_array
	 *            String[]
	 * @param str_seek
	 *            String
	 * @return int
	 */
	private static int HashTableFind(String[] str_array, String str_seek) {
		if (str_array == null || str_seek == null || str_array.length == 0
				|| str_seek.length() == 0)
			return -1;
		int ret = -1;
		Hashtable temp = new Hashtable();
		int length = str_seek.length();
		for (int i = 0; i < str_array.length; i++) {
			if (str_array[i].length() >= str_seek.length()) {
				temp.put((String) str_array[i].substring(0, length), String
						.valueOf(i));
			}
		}
		String obj = (String) temp.get(str_seek);
		if (obj != null) {
			try {
				ret = Integer.parseInt(obj);
			} catch (Exception e) {
				ret = -1;
			}
		} else {
			ret = -1;
		}
		temp.clear();

		return ret;
	}

	/**
	 * 获取拆分长度
	 * 
	 * @param mobile_phone
	 *            String
	 * @return int
	 */
	public static int getSplitLength(String mobile_phone) {
		int length = 0;
		if (mobile_phone.length() < 11)
			return 0;
		for (int i = 1; i <= 5; i++) {
			if (matchString(i, mobile_phone)) {
				length = m_length[i];
				break;
			}
		}
		return length;
	}

	/**
	 * 获取拆分长度
	 * 
	 * @param i
	 *            int
	 * @return int
	 */
	public static int getSplitLength(int i) {
		if (i < 0 || i > 5)
			return 0;

		return m_length[i];
	}

	/**
	 * 获取支持彩信发送标志
	 * 
	 * @param i
	 *            int
	 * @return int
	 */
	public static int getSupportMMS(int i) {
		if (i < 0 || i > 5)
			return 0;

		return m_support_mms[i];

	}

	/**
	 * 获取支持彩信发送标志
	 * 
	 * @param mobile_phone
	 *            String
	 * @return int
	 */
	public static int getSupportMMS(String mobile_phone) {
		int support_mms = 0;
		if (mobile_phone.length() < 11)
			return 0;
		for (int i = 1; i <= 5; i++) {
			if (matchString(i, mobile_phone)) {
				support_mms = m_support_mms[i];
				break;
			}
		}
		return support_mms;
	}

	/**
	 * 判断邮件地址的合法性
	 * 
	 * @param mail
	 *            String
	 * @return boolean
	 */
	public static boolean isValidEmailAddress(String email) {
		boolean tag = true;
		if (email == null)
			return false;
		final String pattern1 =
		// "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		"^[_a-zA-Z0-9-]+(.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	/**
	 * 判断手机号的合法性
	 * 
	 * @param mobile_phone
	 *            String
	 * @return boolean
	 */
	public static boolean isValidMobilePhone(String mobile_phone) {
		if (mobile_phone == null || mobile_phone.equals(""))
			return false;
		try {
			Long.parseLong(mobile_phone);
		} catch (Exception er) {
			return false;
		}

		if (mobile_phone.length() < 11)
			return false;

		for (int i = 1; i <= 5; i++) {
			if (matchString(i, mobile_phone)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据手机号拆分短信
	 * 
	 * @param mobile_phone
	 *            String
	 * @param sm_content
	 *            String
	 * @return ArrayList
	 */
	public static ArrayList SplitSM(String mobile_phone, String sm_content) {
		if (mobile_phone == null || mobile_phone.length() == 0
				|| sm_content == null || sm_content.length() == 0)
			return null;

		int split_length = getSplitLength(mobile_phone);
		return SplitSM(sm_content, split_length);
	}

	/**
	 * 根据号码段拆分短信
	 * 
	 * @param i
	 *            int（1-5）
	 * @param sm_content
	 *            String
	 * @return ArrayList
	 */
	public static ArrayList SplitSM(int i, String sm_content) {
		if (i < 0 || i > 5 || sm_content == null || sm_content.length() == 0)
			return null;

		int split_length = getSplitLength(i);
		return SplitSM(sm_content, split_length);
	}

	/**
	 * 按指定的长度拆分短信，并加上分条信息。
	 * 
	 * @param sm_content
	 *            String
	 * @param split_length
	 *            int
	 * @return ArrayList
	 */
	private static ArrayList SplitSM(String sm_content, int split_length) {
		if (split_length == 0)
			return null;

		ArrayList sm_list = new ArrayList();
		if (sm_content.length() <= split_length) {
			sm_list.add(sm_content);
		} else {
			if (m_is_need_splitter_info == 0) {
				String temp = sm_content;
				String cur_content = "";
				int len = temp.length();

				while (len > split_length) {
					cur_content = temp.substring(0, split_length);
					sm_list.add(cur_content);
					temp = temp.substring(split_length);
					len = temp.length();
				}
				cur_content = temp;
				sm_list.add(cur_content);

			} else if (m_is_need_splitter_info == 1) {
				String template = m_splitter_info_templ;
				String temp = sm_content;
				int template_len = template.length();
				int pos = template.indexOf("N-");
				if (pos >= 0)
					template_len--;
				pos = template.indexOf("|");
				if (pos >= 0)
					template_len--;

				int sm_len = temp.length();
				int sm_count = 0;
				if (sm_len % split_length == 0) {
					sm_count = sm_len / split_length;
				} else {
					sm_count = sm_len / split_length + 1;
				}
				int last_sm_len = (sm_len + template_len * sm_count)
						% split_length;

				if (m_is_first_have_info == 0) {
					if (sm_count > 1) {
						sm_list.add(temp.substring(0, split_length));
						temp = temp.substring(split_length);
						sm_len = temp.length();
						if (sm_len % split_length == 0) {
							sm_count = sm_len / split_length;
						} else {
							sm_count = sm_len / split_length + 1;
						}

						last_sm_len = (sm_len + template_len * sm_count)
								% split_length;
					} else {
						sm_list.add(temp);
					}
				}

				if (m_is_critical_zone_choice == 1
						&& template_len * sm_count >= last_sm_len
						&& last_sm_len != 0) {
					String cur_content = "";
					int len = temp.length();

					while (len > split_length) {
						cur_content = temp.substring(0, split_length);
						sm_list.add(cur_content);
						temp = temp.substring(split_length);
						len = temp.length();
					}
					cur_content = temp;
					sm_list.add(cur_content);

				} else {
					if (m_is_critical_zone_choice == 0
							&& (template_len * sm_count >= last_sm_len && last_sm_len != 0)) {
						sm_count++;
					}

					String cur_content = "";
					String temp_template = template;
					int len = temp.length();
					int i = 0;
					if (sm_list.size() == 1) {
						i = 2;
						temp_template = temp_template.replaceAll("T",
								(sm_count + 1) + "");
					} else {
						i = 1;
						temp_template = temp_template.replaceAll("T", sm_count
								+ "");
					}

					int n_pos = temp_template.indexOf("N");
					int v_pos = temp_template.indexOf("|");
					if (v_pos == -1)
						v_pos = n_pos + 1;
					String cur_template = "";
					while (len > split_length - template_len) {
						cur_template = temp_template;
						cur_template = cur_template.replaceAll("N-", (i - 1)
								+ "");
						cur_template = cur_template.replaceAll("N", i + "");
						cur_template = cur_template.replaceAll("\\|", "");

						if (n_pos < v_pos) {
							cur_content = cur_template
									+ temp.substring(0, split_length
											- template_len);
						} else {
							cur_content = temp.substring(0, split_length
									- template_len)
									+ cur_template;
						}
						sm_list.add(cur_content);
						temp = temp.substring(split_length - template_len);
						len = temp.length();
						i++;
					}

					cur_template = temp_template;
					cur_template = cur_template.replaceAll("N-", (i - 1) + "");
					cur_template = cur_template.replaceAll("N", i + "");
					cur_template = cur_template.replaceAll("\\|", "");

					if (n_pos < v_pos) {
						cur_content = cur_template + temp;
					} else {
						cur_content = temp + cur_template;
					}
					sm_list.add(cur_content);
				}
			}
		}
		return sm_list;
	}

	public static String getOrgID() {
		return m_org_id;
	}

	public static ArrayList getMailServerList() {
		return m_mail_server_list;
	}

	public static String getFeedBackServer() {
		return m_feed_back_server;
	}

	public static ArrayList getFeedBackMailBoxList() {
		return m_feed_back_mailbox_list;
	}

	public static String getMailTraceServerURL() {
		return m_mail_trace_server_url;
	}

	public static String getStatServerURL() {
		return m_stat_server_url;
	}

	public static String getResourceServerURL() {
		return m_resource_server_url;
	}

	public static int getMMSNeedWhiteList() {
		return m_mms_need_white_list;
	}

	public static void main(String[] args) {
		SMSplitterUtil su = new SMSplitterUtil();
		boolean b = su.isValidEmailAddress("12345@qq.com");
		int length = su.getSplitLength("13321691417");
		boolean valid = su.isValidMobilePhone("13321691417");
		ArrayList sm = new ArrayList();
		sm = SplitSM(
				"13521691417",
				"随着CBD最新规划的完成，CBD核心区各项目也加快了进度，中心区的商业物业供给更呈现出炙手可热的发展趋势。而作为面向中心区的商业配套，万余平的合生国际花园临街商铺即国际走廊，也以前瞻型的姿态一跃登上CBD的舞台，对CBD商业现状形成了新一轮的");
		for (int i = 0; i < sm.size(); i++) {
			String s = (String) sm.get(i);
			System.out.println(s);
		}
	}
}
