package com.icode.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This is a helper class for file read&write operate
 * @author 帮杰
 *
 */
public class FileUtil {

	public static void append(File file,String content) throws IOException {
		FileWriter fw = new FileWriter(file, true);
		PrintWriter pw = new PrintWriter(fw);
		pw.print(content);
		pw.flush();
		fw.flush();
		pw.close();
		fw.close();
	}
	
    public static String readTextFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuffer buffer = new StringBuffer();
        String s = null;
        while ((s = reader.readLine()) != null) {
            buffer.append(s).append("\r\n");
        }
        reader.close();
        return buffer.toString();
    }
    
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
    	if(!targetFile.exists())
    		targetFile.createNewFile();
        BufferedInputStream inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
        BufferedOutputStream outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        outBuff.flush();
        outBuff.close();
        inBuff.close();
    }
    
    public static void main(String[] args) throws IOException {
    	copyFile(new File("C:/Users/帮杰/Desktop/test/math.doc"), new File("C:/Users/帮杰/Desktop/test/copyOf_math.doc"));
	}
}
