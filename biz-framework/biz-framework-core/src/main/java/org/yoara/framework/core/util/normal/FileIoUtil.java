package org.yoara.framework.core.util.normal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**============================================================<br>
 <p>* @version 1.0.0</p>
 <p>* IO操作相关工具类</p>
 * ============================================================*/
public class FileIoUtil {

	/**读取jar包里指定文件的内容**/
	public static String getJarText(String file, String entryname){
		BufferedReader br = null;
		ZipFile zip = null;
		StringBuilder sb = new StringBuilder();
		try {
			zip = new ZipFile(file);
			ZipEntry ze = zip.getEntry(entryname);//根据文件名取得压缩包中的对应条目
			br= new BufferedReader(new InputStreamReader(zip.getInputStream(ze)));
			String line;
			while((line = br.readLine()) != null){	
				sb.append(line+"\n");
			}
			return sb.toString();
		} catch (ZipException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				if(br!=null){
					br.close();
					br = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(zip!=null){
					zip.close();
					zip = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
