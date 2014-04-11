package me.wakao.app.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import android.content.Context;

public class PropertyManger {
	
	public final static String PATH_USERINFO = "userinfo";
	
	public String pros_path = "userinfo";
	private Context mContext;

	
	public PropertyManger(Context mContext) {
		this.mContext = mContext;
	}
	
	public void setProsPath(String path){
		this.pros_path = path;
	}
	
	
	public Properties getProperties() {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			// 读取files目录下的config
			// fis = activity.openFileInput(APP_CONFIG);

			// 读取app_config目录下的config
			File dirConf = mContext.getDir(pros_path, Context.MODE_PRIVATE);
			fis = new FileInputStream(dirConf.getPath() + File.separator
					+ pros_path);
			props.load(fis);
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return props;
	}
	
	private void setProperties(Properties p) {
		FileOutputStream fos = null;
		try {
			// 把config建在files目录下
			// fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

			// 把config建在(自定义)app_config的目录下
			File dirConf = mContext.getDir(pros_path, Context.MODE_PRIVATE);
			File conf = new File(dirConf, pros_path);
			fos = new FileOutputStream(conf);
			p.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}
	
	public void setProperty(String key, String value){
		Properties props = getProperties();
		props.setProperty(key, value);
		setProperties(props);
	}
	
	public String getProperty(String key){
		Properties props = getProperties();
		return (props != null) ? props.getProperty(key) : null;
	}
	
	public void removeProperty(String...key){
		Properties props = getProperties();
		for (String k : key)
			props.remove(k);
		setProperties(props);
	}
}
