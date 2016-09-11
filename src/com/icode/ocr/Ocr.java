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
import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.List;

import com.icode.ocr.plugin.CharacterExtractor;
import com.icode.ocr.plugin.LineExtractor;

/**
 * Optical Character Recognizer.
 * <p>1.filter
 * <p>2.extractLines
 * <p>3.extractChars
 * <p>4.code
 * <p>5.store codes or recognize(greedyOcr or meanOcr.Best for meanOcr.).
 * <P>说明：本程序只可做学习使用，不可用于网络攻击和强力验证码爆破。学习和交流：<b>mobangjack@foxmail.com</b>
 * <p>鸣谢：Ronald B. Cemer
 * @author 帮杰
 *
 */
public abstract class Ocr {

	static final int START_INDEX = 2;
	static final int DIFFERENCE = 2;
	
	private int stdWidth = 20;
	private int stdHeight = 20;
	
	public Ocr(){
	}
	
	public int getStdWidth() {
		return stdWidth;
	}

	public void setStdWidth(int stdWidth) {
		this.stdWidth = stdWidth;
	}

	public int getStdHeight() {
		return stdHeight;
	}

	public void setStdHeight(int stdHeight) {
		this.stdHeight = stdHeight;
	}

	private List<BufferedImage> extractLines(BufferedImage filteredOriginalImage) {
		return new LineExtractor().slice(filteredOriginalImage);
	}
	
	private List<BufferedImage> extractChars(BufferedImage line) {
		return new CharacterExtractor().slice(line, stdWidth, stdHeight);
	}
	
	/**
	 * do something.
	 * @param image
	 * @return filtered image.
	 */
	protected abstract BufferedImage filter(BufferedImage image);
	
	/**
	 * this method should work with filter method.（该方法需与filter方法配合）
	 * @param pixels char image slice pixels.
	 * @param w char image slice width.
	 * @param h char image slice height.
	 * @param i current index.
	 * @return True if current pixel pixels[i] needs coded.
	 */
	protected abstract boolean code(int[] pixels,int w,int h,int i);
	
	/**
	 * coder for character image slice.
	 * @param character
	 * @return
	 */
	protected String coder(BufferedImage character) {
		int w = character.getWidth();
		int h = character.getHeight();
		int[] pixels = new int[w*h];
		PixelGrabber grabber = new PixelGrabber(character.getSource(), 0, 0, w, h, pixels, 0, w);
        try {
            grabber.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new OcrException(e);
        }
        String code = "";
        for(int i=0;i<w*h;i++){
        	code += code(pixels, w, h, i)?"1":"0";
        }
		return code;
	}
	
	/**
	 * code.
	 * <p>1.filter
	 * <p>2.extractLines
	 * <p>3.extractChars
	 * <p>4.code
	 * @author 帮杰
	 *
	 */
	public final List<String> code(BufferedImage originalImage) {
		
		//1.filter
		BufferedImage filteredImage = filter(originalImage);
		
		//2.extractLines
		List<BufferedImage> lines = extractLines(filteredImage);
		
		//3.extractChars
		List<BufferedImage> chars = new ArrayList<BufferedImage>();
		for(BufferedImage line:lines){
			chars.addAll(extractChars(line));
		}
		
		//4.code
		List<String> codes = new ArrayList<String>();
		for(BufferedImage character:chars){
			codes.add(coder(character));
		}
		
		return codes;
	}
	
	/**
	 * greedyOcr selects the corresponding result that has the most pixels overlapped.
	 * @param originalImage originalImage
	 * @param libs code libraries
	 * @return result String
	 */
	public final String greedyOcr(BufferedImage originalImage,List<String> libs){
		String result = "";
		List<String> codes = code(originalImage);
		for(String code : codes){
			int MaxCount = 0;
			String target = "";
			for(String lib : libs){
				int count = 0;
				for(int i=-DIFFERENCE;i<=DIFFERENCE;i++){
					int tmp = 0;
					int dif = Math.abs(i);
					for(int j=0;j<code.length()-dif;j++){
						if(i<0){
							if(code.charAt(j+dif)=='1'&&lib.charAt(START_INDEX+j)=='1'){
								tmp++;
							}
						}else if (i==0) {
							if(code.charAt(j)=='1'&&lib.charAt(START_INDEX+j)=='1'){
								tmp++;
							}
						}else {
							if(lib.charAt(START_INDEX+dif+j)=='1'&&code.charAt(j)=='1'){
								tmp++;
							}
						}
						
					}
					if(tmp>count){
						count = tmp;
					}
				}
				if(count>MaxCount){
					MaxCount = count;
					target = lib;
				}
			}
			result+=target.charAt(0);
		}
		return result;
	}
	
	/**
	 * meanOcr selects the corresponding result that has the least pixels non-overlapped.
	 * @param originalImage originalImage
	 * @param libs code libraries
	 * @return result String
	 */
	public final String meanOcr(BufferedImage originalImage,List<String> libs){
		String result = "";
		List<String> codes = code(originalImage);
		for(String code : codes){
			int MinCount = stdWidth*stdHeight;
			String target = "";
			for(String lib : libs){
				int count = stdWidth*stdHeight;
				for(int i=-DIFFERENCE;i<=DIFFERENCE;i++){
					int tmp = 0;
					int dif = Math.abs(i);
					for(int j=0;j<code.length()-dif;j++){
						if(i<0){
							if(code.charAt(j+dif)!=lib.charAt(START_INDEX+j)){
								tmp++;
							}
						}else if (i==0) {
							if(code.charAt(j)!=lib.charAt(START_INDEX+j)){
								tmp++;
							}
						}else {
							if(lib.charAt(START_INDEX+dif+j)!=code.charAt(j)){
								tmp++;
							}
						}
						
					}
					if(tmp<count){
						count = tmp;
					}
				}
				if(count<MinCount){
					MinCount = count;
					target = lib;
				}
			}
			result+=target.charAt(0);
		}
		return result;
	}
	
}
