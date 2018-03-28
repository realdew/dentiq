package dentiq.api;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ServerConfig {
	
	private ServerConfig() {}	
	
	private static class Singleton {
		private static final ServerConfig instance = new ServerConfig();
	}
	
	public static ServerConfig getInstance() {
		return Singleton.instance;
	}
	
	
	private Properties prop = new Properties();
	
	public String get(String name) throws Exception {
		String val = prop.getProperty(name);
		if ( val == null ) throw new Exception("Config에 해당 Key가 존재하지 않습니다. [" + name + "]");
		
		return val;
	}
	
	
	public void init(String configFileFullPath) throws Exception {
		
		File file = new File(configFileFullPath);
		if ( !file.exists() )	throw new Exception("Config 파일이 존재하지 않습니다. [" + configFileFullPath + "]");
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			
			prop.load(fis);
			
			fis.close();
		} catch(Exception ex) {			
			if ( fis != null ) try { fis.close(); } catch(Exception ignore) {}
			ex.printStackTrace();
			throw new Exception("Config 파일\" + configFileFullPath + \")을 읽을 수 없습니다. [" + ex + "]");
		}
		
		System.out.println("총 " + prop.size() + "개의 설정이 입력되었습니다.");
		System.out.println(prop);
		System.out.println("Config 파일 로딩 완료");
	}
	
	/*
	private String USER_RESOURCE_SERVER_URL;
	private String USER_RESOURCE_URL_BASE;
	private String USER_RESOURCE_SAVED_DIR_ROOT;

	private String HOSPITAL_RESOURCE_SERVER_URL;
	private String HOSPITAL_RESOURCE_URL_BASE;
	private String HOSPITAL_RESOURCE_SAVED_DIR_ROOT;
	*/
	

}

/*

# if null, every resources is in a web(http) server.
USER_RESOURCE_SERVER_URL=

USER_RESOURCE_URL_BASE=resources/user

USER_RESOURCE_SAVED_DIR_ROOT=c:/work/dentalplus
#USER_RESOURCE_SAVED_DIR_ROOT=/var/www/dentalplus.enqual.co.kr


####################################################
# Definition for Hospital Resources
####################################################

# if null, every resources is in a web(http) server.
HOSPITAL_RESOURCE_SERVER_URL=

HOSPITAL_RESOURCE_URL_BASE=resources/hospital

HOSPITAL_RESOURCE_SAVED_DIR_ROOT=c:/work/dentalplus
#USER_RESOURCE_SAVED_DIR_ROOT=/var/www/dentalplus.enqual.co.kr

*/