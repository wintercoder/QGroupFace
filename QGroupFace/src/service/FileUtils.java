package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {
	private static final long FILE_SIZE_MIN = 12288;// 12kb
	private static final long FILE_SIZE_MAX = 1048576; // 1M

	/**
	 * 初步过滤不含皂片的文件
	 * @param file
	 * @return
	 */
	public static boolean canFliter(File file ) {
		long length = file.length();
		if (length < FILE_SIZE_MIN || length > FILE_SIZE_MAX) {
			return true;
		}
		String Suffix = getFileSuffix(file);
		if("gif".equals(Suffix))
			return true;
		return false;
	}

	/**
	 * 如果目标目录不存在就新建，包括其性别分类子目录
	 * @param destPath
	 */
	public static void initDestFileDir(String destPath) {
		File rootDir = new File(destPath);
		if (!rootDir.exists()) {
			rootDir.mkdirs();
			File maleDir = new File(destPath + "\\" + "Male");
			File femaleDir = new File(destPath + "\\" + "Female");
			if (!maleDir.exists())
				maleDir.mkdir();
			if (!femaleDir.exists())
				femaleDir.mkdir();
		}
	}

	public static String getFileSuffix(File file) {
		String fineName = file.getName();
		return fineName.substring(fineName.lastIndexOf(".") + 1,
				fineName.length());
	}

	/**
	 * 复制文件
	 * @param src
	 * @param dest
	 */
	public static void fileChannelCopy(File src, File dest) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null, out = null;
		try {
			fi = new FileInputStream(src);
			fo = new FileOutputStream(dest);
			in = fi.getChannel();
			out = fo.getChannel();
			in.transferTo(0, in.size(), out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
