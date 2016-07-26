package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static String srcPath = "";
	public static String destPath = ""; // absolute path better, no any other file better

    public static String API_KEY = "4480afa9b8b364e30ba03819f3e9eff5";
    public static String API_SECRET = "Pz9VFT8AP3g_Pz8_dz84cRY_bz8_Pz8M";

	private static final int threadNumber = 20;
	
	public static void main(String[] args) {
		if(args.length==2){
			srcPath = args[0];destPath = args[1];
		}
		FileUtils.initDestFileDir(destPath);

		try {
			List<File> list = getFliteredFileList(srcPath);
			System.out.println("After fliter "+list.size()+" files leave");
			
			FileThread fileThread = new FileThread();
			fileThread.setList(list);
			
			Thread[] threads = new Thread[threadNumber];
			for (int i = 0; i < threads.length; i++) {
				threads[i] = new Thread(fileThread,"Thread-"+ (int)(i+1));
				threads[i].start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static List<File> getFliteredFileList(String srcPath) {
		File root = new File(srcPath);
		File[] files = root.listFiles();
		
		List<File> list = new ArrayList<File>();
		for (File file : files) {
			if (!file.isDirectory()) {
				if(FileUtils.canFliter(file)){
					continue;
				}
				list.add(file);
			}
		}
		return list;
	}
}
