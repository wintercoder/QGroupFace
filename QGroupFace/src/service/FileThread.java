package service;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

public class FileThread implements Runnable {
	private List<File> list;
	private AtomicInteger maleIndex = new AtomicInteger(1);
	private AtomicInteger femaleIndex = new AtomicInteger(1);
	
	private HttpRequests httpRequests = new HttpRequests(
			Config.API_KEY, Config.API_SECRET, true, true);
	
	private static String getGenderOrNull(JSONObject result) {
		String gender;
		
		try {
			gender = result.getJSONArray("face").getJSONObject(0)
					.getJSONObject("attribute").getJSONObject("gender")
					.getString("value").toString();
		} catch (JSONException e) {
			return null;
		}
		return gender;
	}
	
	/**
	 * 无锁并发：对需要处理的文件id取模分段，不同线程处理不同的文件
	 * 如m个文件n个线程则 1号线程处理 1,1+n,1+2n...1+kn 号文件(1+kn<m)
	 */
	@Override
	public void run() {
		int threadId = Integer.parseInt(Thread.currentThread().getName());
		int fileId = threadId;
		while(fileId < list.size()){
			File file = list.get(fileId);
			fileId += Config.threadNum;
			
			//过滤
			JSONObject result = null;
			try {
				result = httpRequests.detectionDetect(new PostParameters()
				.setImg(file).setAttribute("gender"));
			} catch (FaceppParseException e) {
				System.out.println("Error:\tThread-" + threadId + "\tFile-"+fileId + "\t" + file.getName() + " => " + e.getErrorMessage());
				continue;
			}
			//检查是不是很多白色像素来去表情包
			if(file.length() < Config.whiteRGBFileSizeMax){
				if(FileUtils.isImageWhite(file,Config.whiteRGBMin,Config.whiteRGBRate)){
					continue;
				}
			}
			
			String gender = getGenderOrNull(result);
			if (null == gender) {
			}else{
				int index = -9999;
				if("Female".equals(gender)){
					index = femaleIndex.getAndIncrement();
				}else{
					index = maleIndex.getAndIncrement();
				}
				
				//有些图片是.null后缀如 : ZJ0P65PZN$CW6IU)2PJVMDQ.null
				String destSuffix = FileUtils.getFileSuffix(file);
				if(destSuffix.equals("null")){
					destSuffix = "jpg";
				}
				FileUtils.fileChannelCopy(file, new File(Config.destPath
						+ "\\" + gender + "\\" + index + "." + destSuffix));

				if(Config.delSrcFile){	//删源文件
					file.delete();
				}
			}
		}
	}
	
	public void setList(List<File> list) {
		this.list = list;
	}
}
