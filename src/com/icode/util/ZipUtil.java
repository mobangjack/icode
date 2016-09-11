/**
 * Copyright (c) 2011-2015, Mobangjack 莫帮杰 (mobangjack@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.icode.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author 帮杰
 *
 */
public class ZipUtil {

	public static void zipFiles(String zipFile, String... srcFiles) throws IOException {
		File zip = new File(zipFile);
		for(String src:srcFiles)
			zipFiles(zip, new File(src));
	}
	
	public static void zipFiles(File zipFile, File... srcFiles) throws IOException {
		if(!zipFile.exists())
			zipFile.createNewFile();
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        byte[] buf = new byte[1024];
        for (int i = 0; i < srcFiles.length; i++) {
        	System.out.println("zipping file '"+srcFiles[i].getAbsolutePath()+"' ...");
            out.putNextEntry(new ZipEntry(srcFiles[i].getName()));
            FileInputStream in = new FileInputStream(srcFiles[i]);
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.close();
        System.out.println("Done!All files are zipped into '"+zipFile.getAbsolutePath()+"'.");
    }
 
	/**
	 * 解压到指定目录
	 * @param zipPath
	 * @param destDir
	 */
	public static void unZipFiles(String zipPath,String destDir)throws IOException{
		unZipFiles(new File(zipPath), new File(destDir));
	}
	
	/**
	 * 解压文件到指定目录
	 * @param zipFile
	 * @param destDir
	 */
	@SuppressWarnings("rawtypes")
	public static void unZipFiles(File zipFile,File destDir)throws IOException{
		if(!destDir.exists()){
			destDir.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for(Enumeration entries = zip.entries();entries.hasMoreElements();){
			ZipEntry entry = (ZipEntry)entries.nextElement();
			String entryName = entry.getName();
			System.out.println("unzipping file '"+entryName+"' to folder '"+destDir.getAbsolutePath()+" ...");
			InputStream in = zip.getInputStream(entry);
			OutputStream out = new FileOutputStream(destDir.getAbsolutePath()+File.separator+entryName);
			byte[] buf1 = new byte[1024];
			int len;
			while((len=in.read(buf1))>0){
				out.write(buf1,0,len);
			}
			in.close();
			out.close();
		}
		zip.close();
		System.out.println("Done!ZipFile '"+zipFile.getAbsolutePath()+"' is unzipped.");
	}

	public static void main(String[] args) throws IOException {
		//zipFiles(new File("C:/Users/帮杰/Desktop/test.zip"), new File("C:/Users/帮杰/Desktop/test"));
		//unZipFiles("C:/Users/帮杰/Desktop/test.zip", "C:/Users/帮杰/Desktop");
		System.out.println(new File("C:/Users/帮杰/Desktop/test.zip").getPath());
	}
}
