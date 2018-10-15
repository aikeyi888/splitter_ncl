package com.wisdom.ncl.splitter.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yuhao
 * @description 文件处理类
 * @date 2011-07-26
 * @updater
 * @version 1.0.0
 * 
 */
public class FileTools {

	public synchronized static void deleteFile(String srcFile) {
		File file = new File(srcFile);
		file.delete();
	}

	/**
	 * @description 检查文件是否存在
	 * @param pathName
	 *            路径名
	 * @param fileName
	 *            文件名
	 * @return 文件存在返回true 否则返回false
	 */
	public synchronized static boolean checkFile(String pathName,
			String fileName) {
		int j = 0;
		boolean sign = false;
		File file = new File(pathName);
		File[] files = file.listFiles();
		if (files != null) {
			String[] s = new String[files.length];
			for (int i = 0; i < files.length; ++i) {
				s[i] = files[i].getName();
				if (s[i].equals(fileName)) {
					break;
				}
				++j;
			}
			if (j == files.length)
				sign = false;
			else {
				sign = true;
			}
		}
		return sign;
	}

	/**
	 * 
	 * 功能说明: 检查文件是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public synchronized static boolean checkFile(String filePath) {
		if (filePath != null && !"".equals(filePath)) {
			File file = new File(filePath);
			return file.exists();
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 函数名: copyFile 功能描述: 文件复制 修改历史:
	 * 
	 * @date 2011-10-25 下午01:08:13
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param srcFile
	 *            源文件路径
	 * @param targetFile
	 *            现文件路径
	 */
	public synchronized static void copyFile(String srcPath, String targetPath) {
		File f1 = new File(srcPath);
		File f2 = new File(targetPath);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(f1);
			out = new FileOutputStream(f2);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				in.close();
				out.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 函数名: copyFileReturnBoolean 功能描述: 复制文件 返回操作结果 修改历史:
	 * 
	 * @date 2011-11-9 下午04:41:18
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param srcFile
	 * @param targetFile
	 * @return
	 */
	public synchronized static boolean copyFileReturnBoolean(String srcFile,
			String targetFile) {
		boolean flag = false;
		File f1 = new File(srcFile);
		File f2 = new File(targetFile);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(f1);
			out = new FileOutputStream(f2);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 
	 * 函数名: readTxt 功能描述: 读txt文件 修改历史:
	 * 
	 * @date 2011-11-16 下午01:25:51
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param filePath
	 *            文件路径
	 * @param charSet
	 *            字符边防方式 建议使用GBK 不出现中文乱码
	 */
	public static List<String> readTxt(String filePath, Charset charSet) {
		BufferedReader br = null;
		List<String> list = null;

		try {
			list = new ArrayList<String>();
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File(filePath)), charSet));
			String content = "";
			while ((content = br.readLine()) != null) {
				list.add(content);
			}
		} catch (Exception e) {
			System.out.println("FileTools类的readTxt()方法错误。");
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 
	 * 函数名: readTxt 功能描述: 读取txt文件信息 默认编码方式为GBK 修改历史:
	 * 
	 * @date 2011-11-16 下午01:33:11
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static List<String> readTxt(String filePath) {
		Charset charSet = Charset.forName("GBK");
		return readTxt(filePath, charSet);
	}

	/**
	 * 
	 * 函数名: writeTxt 功能描述: 向写txt文件写内容 修改历史:
	 * 
	 * @date 2011-11-16 下午01:54:18
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param filePath
	 *            文件路径
	 * @param contentList
	 *            要写的内容
	 * @param charset
	 *            编码方式
	 */
	public static void writeTxt(String filePath, List<String> contentList,
			Charset charset) {
		if (contentList == null || contentList.size() < 1) {
			return;
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(filePath)), charset));
			for (int i = 0; i < contentList.size(); i++) {
				StringBuffer content = new StringBuffer().append(contentList
						.get(i));
				if (i != contentList.size() - 1) {
					// 根据操作系统的类型，决定换行符
					String osName = System.getProperties().getProperty(
							"os.name");
					if (osName.startsWith("Windows")) {
						content.append("\r\n");
					} else {
						content.append("\n");
					}
				}
				bw.write(content.toString());
			}
			bw.flush();
		} catch (Exception e) {
			System.out.println("FileTools类的writeTxt()方法错误。");
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * 函数名: writeTxt 功能描述: 向txt中写内容 默认的编码方式为GBk 修改历史:
	 * 
	 * @date 2011-11-16 下午01:55:12
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param filePath
	 *            文件路径
	 * @param contentList
	 *            要写的内容ing
	 */
	public static void writeTxt(String filePath, List<String> contentList) {
		Charset charset = Charset.forName("GBK");
		writeTxt(filePath, contentList, charset);
	}

	/**
	 * 
	 * 函数名: checkPathIsExist 功能描述: 判断路径是否存在 修改历史:
	 * 
	 * @date 2011-12-7 上午10:00:25
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param filePath
	 * @return
	 */
	public static boolean checkDirectoryIsExist(String directoryPath) {
		boolean flag = false;
		File file = new File(directoryPath);
		flag = file.isDirectory();
		return flag;
	}

	/**
	 * 
	 * 函数名: createDirectory 功能描述: 创建文件夹 修改历史:
	 * 
	 * @date 2011-12-7 上午10:04:43
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param directoryPath
	 */
	public static boolean createDirectory(String directoryPath) {
		boolean isExist = checkDirectoryIsExist(directoryPath);
		boolean flag = false;
		File file = new File(directoryPath);
		if (!isExist) {
			flag = file.mkdirs();
		} else {
			flag = true;
		}
		return flag;
	}

	public static void appentContent(String fileName, String content) {
		RandomAccessFile randomFile = null;
		try {
			// 打开一个随机访问文件流，按读写方式
			randomFile = new RandomAccessFile(fileName, "rw");
			// 将写文件指针移到文件尾。
			randomFile.seek(randomFile.length());
			randomFile.writeBytes(content + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				randomFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// System.out
		// .println(createDirectory("D:\\2011-09-301\\OVMMonitorSystem11"));
	}
}
