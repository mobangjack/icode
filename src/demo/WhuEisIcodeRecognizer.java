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
package demo;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import com.icode.ocr.Ocr;
import com.icode.ocr.OcrFilter;

/**
 * Ocr示例：武汉大学教务系统验证码识别程序
 * <P>说明：本程序只可做学习使用，不可用于网络攻击和强力验证码爆破。学习和交流：<b>mobangjack@foxmail.com</b>
 * @author 帮杰
 *
 */
public class WhuEisIcodeRecognizer extends Ocr {

	public WhuEisIcodeRecognizer() {
	}

	@Override
	protected BufferedImage filter(BufferedImage image) {
		return new OcrFilter(image).medianX().medianY().extrem(100).keepRGB(255, 0, 0).optBufferedImage();
	}
	
	/* (non-Javadoc)
	 * @see com.icode.ocr.Ocr#code(int[], int, int, int)
	 */
	@Override
	protected boolean code(int[] pixels, int w, int h, int i) {
		ColorModel cm = ColorModel.getRGBdefault();
		int r = cm.getRed(pixels[i]);
		int g = cm.getGreen(pixels[i]);
		int b = cm.getBlue(pixels[i]);
		if(r==255&&g==0&&b==0){
			return true;
		}else {
			return false;
		}
	}

}
