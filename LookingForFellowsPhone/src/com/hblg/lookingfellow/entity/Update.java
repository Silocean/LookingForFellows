package com.hblg.lookingfellow.entity;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Ӧ�ó������ʵ����
 * @author Fay
 */
public class Update implements Serializable{
	
	
	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "YXCampus";
	
	private int versionCode;
	private String versionName;
	private String downloadUrl;
	private String updateLog;
	
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getUpdateLog() {
		return updateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
	
	//private static ObjectMapper objectMapper=new ObjectMapper();
	/**
	 * Json�����������
	 * @param getFromWebByHttpClient
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static Update parse(String response) throws Exception {
		/*Update mUpdate=new Update();
		
		if(("".equals(response))||(null==response)||(response.equals(null))){
			return null;
		}
		Map<String,String>values=objectMapper.readValue(response,Map.class);
	    int version=Integer.parseInt(values.get("version"));
	    String downUrl=values.get("url");
	    
	    mUpdate.setUpdateLog("�ٺ٣�Ҫ���¿�����ǰ�汾��Ϊ1�����°汾"+version);//û�õ����
		mUpdate.setVersionName("1.0");//Ҳû�õ�
		mUpdate.setDownloadUrl(downUrl);
		mUpdate.setVersionCode(version);
		
		return mUpdate;*/
		
		//��ʱ����
		Update mUpdate=new Update();
		mUpdate.setUpdateLog("�ף����¿�����ǰ�汾��Ϊ1��Ҫ���µİ汾Ϊ2");
		mUpdate.setDownloadUrl("http://www.yxcampus.com/Upload/soft/YXCampus2.0.apk");
		mUpdate.setVersionCode(1);
		mUpdate.setVersionName("1.0");
		return mUpdate;
	}
	
}
