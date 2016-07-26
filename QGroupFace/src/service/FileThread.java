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
	private volatile int cnt = 0;
	private AtomicInteger maleIndex = new AtomicInteger(1);
	private AtomicInteger femaleIndex = new AtomicInteger(1);
	
	private HttpRequests httpRequests = new HttpRequests(
			Main.API_KEY, Main.API_SECRET, true, true);
	
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
	

	@Override
	public void run() {
		File file = null;
		while (cnt <= list.size() - 1) {
			synchronized (this) {
				if (cnt <= list.size() - 1) {
					file = list.get(cnt);cnt++;
				}
			}	//unlock 
			
			JSONObject result = null;
			try {
				result = httpRequests.detectionDetect(new PostParameters()
				.setImg(file).setAttribute("gender"));
			} catch (FaceppParseException e) {
				e.printStackTrace();
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
				
				//some file are ends with null : ZJ0P65PZN$CW6IU)2PJVMDQ.null
				String destSuffix = FileUtils.getFileSuffix(file);
				if(destSuffix.equals("null")){
					destSuffix = "jpg";
				}
				FileUtils.fileChannelCopy(file, new File(Main.destPath
						+ "\\" + gender + "\\" + index + "." + destSuffix));
				
				System.out.println(Thread.currentThread().getName() + "\t"
				+ file.getName() + " -> "
				+ gender + "\\" + index + "." + destSuffix );
			}
			
		}
	}
	
	public void setList(List<File> list) {
		this.list = list;
		cnt = 0;
	}
}
