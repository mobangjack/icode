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
package com.icode.ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import com.icode.util.FileUtil;

/**
 * OcrMaster for training.
 * @author 帮杰
 *
 */
public class OcrMaster {

	public static final String ITEM_SEPERATOR = System.getProperty("line.separator");
	
	private final Ocr ocr;
	private String result = "";
	
	public OcrMaster(Ocr ocr) {
		this.ocr = ocr;
	}
	
	public Ocr getOcr() {
		return ocr;
	}

	public String getResult() {
		return result;
	}

	public OcrMaster feedBufferedImage(BufferedImage bufferedImage,String result) {
		List<String> codes = ocr.code(bufferedImage);
		if(codes.size()!=result.length()){
			System.err.println("The image is hard to slice.Skipped!");
			return this;
		}
		for(int i=0;i<result.length();i++){
			this.result += result.charAt(i)+"="+codes.get(i)+ITEM_SEPERATOR;
		}
		return this;
	}
	
	public OcrMaster feedImageFile(File imageFile) throws IOException {
		return feedBufferedImage(ImageIO.read(imageFile), imageFile.getName());
	}
	
	public OcrMaster feedImageFile(String imageFile) throws IOException {
		return feedImageFile(new File(imageFile));
	}
	
	public OcrMaster feedImageDir(File dir) throws IOException {
		File[] files = dir.listFiles();
		for(File file : files){
			String name = file.getName();
			String name2lowercase = name.toLowerCase();
			if(name2lowercase.endsWith(".jpg")||name2lowercase.endsWith(".png")||name2lowercase.endsWith(".gif")){
				BufferedImage bi = ImageIO.read(file);
				List<String> codes = ocr.code(bi);
				int num = codes.size();
				if(num!=name.lastIndexOf('.')){
					System.out.println("file:'"+name+"' is hard to slice.Skiped!");
					continue;
				}
				for(int i=0;i<num;i++){
					result += name.charAt(i)+"="+codes.get(i)+ITEM_SEPERATOR;
				}
			}
		}
		return this;
	}
	
	public OcrMaster feedImageDir(String dir) throws IOException {
		return feedImageDir(new File(dir));
	}
	
	public OcrMaster feedZipInputStream(ZipInputStream zipInputStream) throws IOException {
		ZipEntry zipEntry = null;
		File tmpFile = null;
		FileOutputStream fos = null;
		byte[] buf = new byte[1024];
		int len;
		while ((zipEntry=zipInputStream.getNextEntry())!=null) {
			String entryName = zipEntry.getName();
			int i = entryName.lastIndexOf(".");
			if(i<0)
				continue;
			String prefix = entryName.substring(0, i);
			String suffix = entryName.substring(i-1);
			tmpFile = File.createTempFile(prefix,suffix);
			fos = new FileOutputStream(tmpFile);
			while((len=zipInputStream.read(buf))>0)
				fos.write(buf, 0, len);
			fos.flush();
			fos.close();
			feedBufferedImage(ImageIO.read(tmpFile), prefix);
			tmpFile.delete();
		}
		return this;
	}
	
	public void output(File file) throws IOException {
		if(file.exists()&&file.isFile()){
			file.delete();
		}
		file.createNewFile();
		FileUtil.append(file, getResult());
	}

	public OcrMaster read(String file) throws IOException {
		return read(new File(file));
	}
	
	public OcrMaster read(File file) throws IOException {
		result = FileUtil.readTextFile(file);
		return this;
	}
	
	public String trim() {
		return result.endsWith(ITEM_SEPERATOR)?result.substring(0, result.lastIndexOf(ITEM_SEPERATOR)):result;
	}
	
	public OcrMaster sort(){
		String s = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String tmpResult = "";
		String items[] = trim().split(ITEM_SEPERATOR);
		for(int i=0;i<s.length();i++)
			for(String item:items)
				if(item.charAt(0)==s.charAt(i))
					tmpResult += item+ITEM_SEPERATOR;
		result = tmpResult;
		return this;
	}
	
	/**
	 * 转换成List,方便比对
	 * @return
	 */
	public List<String> list(){
		String items[] = trim().split(ITEM_SEPERATOR);
		List<String> list = new ArrayList<String>(items.length);
		Collections.addAll(list, items);
		return list;
	}
	
	/**
	 * 转换成矩阵，方便视觉校验
	 * @return
	 */
	public String optMatrix() {
		String items[] = trim().split(ITEM_SEPERATOR);
		String s = "";
		int w = ocr.getStdWidth();
		for(String e:items){
			s += e.substring(0, Ocr.START_INDEX)+ITEM_SEPERATOR;
			for(int i=Ocr.START_INDEX;i<e.length()-Ocr.START_INDEX-w;i+=w){
				s += e.substring(i, i+w)+ITEM_SEPERATOR;
			}
			s += ITEM_SEPERATOR;
		}
		return s;
	}
	
	/**
	 * 压缩，每个字母只保留一个
	 * @return
	 */
	public OcrMaster compress() {
		String s = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String tmpResult = "";
		String items[] = trim().split(ITEM_SEPERATOR);
		for(int i=0;i<s.length();i++)
			for(String item:items)
				if(item.charAt(0)==s.charAt(i))
					if(!tmpResult.contains(item.charAt(0)+"="))
						tmpResult += item+ITEM_SEPERATOR;
		result = tmpResult;
		return this;
	}
	
}
