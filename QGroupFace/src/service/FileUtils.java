package service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

public class FileUtils {
	private static final long FILE_SIZE_MIN = 12*1024;// 12kb
	private static final long FILE_SIZE_MAX = 1024*1024; // 1M

	
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
	 * 检测是不是大量白色的表情包，扫一次像素统计白色像素所占比例，超过则认为是表情包
	 * @param path
	 * @param whiteRGB 白色的RGB值下限，参考值：>=240 && <=255
	 * @param rateMax 白色像素个数/总像素数，参考值： >50
	 * @return
	 */
	public static boolean isImageWhite(File file,int whiteRGB,int rateMax) {
		try {
			BufferedImage img = ImageIO.read(file);
			int wdith = img.getWidth();
			int height = img.getHeight();
			int rgb[] = new int[3];
			int cnt = 0;
			for (int i = 0; i < wdith; i++) {
				for (int j = 0; j < height; j++) {
					int pixel = img.getRGB(i, j);
					rgb[0] = (pixel & 0xff0000) >> 16;
					rgb[1] = (pixel & 0xff00) >> 8;
					rgb[2] = (pixel & 0xff);
					if (rgb[0] >= whiteRGB && rgb[1] >= whiteRGB && rgb[2] >= whiteRGB) {
						cnt++;
					}
				}
			}
			double rate = 1.0 * cnt / (wdith * height) * 100;
			if(rate > rateMax){
				return true;
			}
		} catch (IOException e) {System.out.println("Exception :" + file.getPath());}
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
