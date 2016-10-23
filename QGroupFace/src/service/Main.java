package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static void main(String[] args) {
		if(args.length == 2){
			Config.initConfig(args[0],args[1],false,Config.threadNum);
		}else if(args.length == 3){
			Config.initConfig(args[0],args[1],args[2].toUpperCase().equals("Y"),Config.threadNum);
		}else if(args.length == 4){
			Config.initConfig(args[0],args[1],args[2].toUpperCase().equals("Y"),Integer.parseInt(args[3]));
		}
		FileUtils.initDestFileDir(Config.destPath);

		try {
			List<File> list = getFliteredFileList(Config.srcPath);
			System.out.println("After fliter files number: "+list.size());
			System.out.println("Thread number: " + Config.threadNum );
			System.out.println("Delete sources picture: " + Config.delSrcFile);
			System.out.println();
			
			FileThread fileThread = new FileThread();
			fileThread.setList(list);
			
			Thread[] threads = new Thread[Config.threadNum];
			for (int i = 0; i < threads.length; i++) {
				threads[i] = new Thread(fileThread,""+i);
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
