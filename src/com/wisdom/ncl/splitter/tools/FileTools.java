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
 * @description �ļ�������
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
	 * @description ����ļ��Ƿ����
	 * @param pathName
	 *            ·����
	 * @param fileName
	 *            �ļ���
	 * @return �ļ����ڷ���true ���򷵻�false
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
	 * ����˵��: ����ļ��Ƿ����
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
	 * ������: copyFile ��������: �ļ����� �޸���ʷ:
	 * 
	 * @date 2011-10-25 ����01:08:13
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
	 * @param srcFile
	 *            Դ�ļ�·��
	 * @param targetFile
	 *            ���ļ�·��
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
	 * ������: copyFileReturnBoolean ��������: �����ļ� ���ز������ �޸���ʷ:
	 * 
	 * @date 2011-11-9 ����04:41:18
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: readTxt ��������: ��txt�ļ� �޸���ʷ:
	 * 
	 * @date 2011-11-16 ����01:25:51
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
	 * @param filePath
	 *            �ļ�·��
	 * @param charSet
	 *            �ַ��߷���ʽ ����ʹ��GBK ��������������
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
			System.out.println("FileTools���readTxt()��������");
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
	 * ������: readTxt ��������: ��ȡtxt�ļ���Ϣ Ĭ�ϱ��뷽ʽΪGBK �޸���ʷ:
	 * 
	 * @date 2011-11-16 ����01:33:11
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
	 * @param filePath
	 *            �ļ�·��
	 * @return
	 */
	public static List<String> readTxt(String filePath) {
		Charset charSet = Charset.forName("GBK");
		return readTxt(filePath, charSet);
	}

	/**
	 * 
	 * ������: writeTxt ��������: ��дtxt�ļ�д���� �޸���ʷ:
	 * 
	 * @date 2011-11-16 ����01:54:18
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
	 * @param filePath
	 *            �ļ�·��
	 * @param contentList
	 *            Ҫд������
	 * @param charset
	 *            ���뷽ʽ
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
					// ���ݲ���ϵͳ�����ͣ��������з�
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
			System.out.println("FileTools���writeTxt()��������");
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
	 * ������: writeTxt ��������: ��txt��д���� Ĭ�ϵı��뷽ʽΪGBk �޸���ʷ:
	 * 
	 * @date 2011-11-16 ����01:55:12
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
	 * @param filePath
	 *            �ļ�·��
	 * @param contentList
	 *            Ҫд������ing
	 */
	public static void writeTxt(String filePath, List<String> contentList) {
		Charset charset = Charset.forName("GBK");
		writeTxt(filePath, contentList, charset);
	}

	/**
	 * 
	 * ������: checkPathIsExist ��������: �ж�·���Ƿ���� �޸���ʷ:
	 * 
	 * @date 2011-12-7 ����10:00:25
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: createDirectory ��������: �����ļ��� �޸���ʷ:
	 * 
	 * @date 2011-12-7 ����10:04:43
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
			// ��һ����������ļ���������д��ʽ
			randomFile = new RandomAccessFile(fileName, "rw");
			// ��д�ļ�ָ���Ƶ��ļ�β��
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
