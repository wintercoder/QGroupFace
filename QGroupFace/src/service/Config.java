package service;

public class Config {
	public static String srcPath = "";
	public static String destPath = ""; // 绝对路径
	public static boolean delSrcFile = false;	//是否删除源图片
	public static int threadNum = 10;	//太多会在网络上处理不过来
	
	public static int whiteRGBFileSizeMax = 50*1024;	//50K 大于这个文件大小的就不检测白颜色了，提高速度
	public static int whiteRGBMin = 240;
	public static int whiteRGBRate = 50;
	
    public static String API_KEY = "4480afa9b8b364e30ba03819f3e9eff5";
    public static String API_SECRET = "Pz9VFT8AP3g_Pz8_dz84cRY_bz8_Pz8M";
    
    public static void initConfig(String srcPath,String destPath,boolean delSrcFile,int threadNum) {
    	Config.srcPath = srcPath;
    	Config.destPath = destPath;
    	Config.delSrcFile = delSrcFile;
    	Config.threadNum = threadNum;
	}
}
