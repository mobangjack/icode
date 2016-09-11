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

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.icode.ocr.Ocr;
import com.icode.ocr.OcrFilter;
import com.icode.util.ImageIOHelper;

/**
 * Ocr示例（去干扰线，感谢其他oscer提出的issues）
 * <P>说明：本程序只可做学习使用，不可用于网络攻击和强力验证码爆破。学习和交流：<b>mobangjack@foxmail.com</b>
 * @author 帮杰
 *
 */
public class MyIcodeRecognizer extends Ocr {

	JFrame frame = new JFrame();
	
	public MyIcodeRecognizer() {
		frame.setLayout(new FlowLayout());
		frame.setBounds(200, 200, 200, 200);
	}

	@Override
	protected BufferedImage filter(BufferedImage image) {
		JLabel label = new JLabel("（OcrFilter处理前）",new ImageIcon(image), 0);
		frame.add(label);
		
		//无噪点可不用中值滤波，增强颜色后过滤即可
		image = new OcrFilter(image).extrem(100).keepRGB(0, 0, 255).optBufferedImage();
		
		JLabel label2 = new JLabel("（OcrFilter处理后）",new ImageIcon(image), 0);
		frame.add(label2);
		frame.setVisible(true);
		
		return image;
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
		if(r==0&&g==0&&b==255){
			return true;
		}else {
			return false;
		}
	}

	public static void main(String[] args) {
		BufferedImage target = ImageIOHelper.getImage(new File("demo/bqgxu.png"));
		MyIcodeRecognizer ocr = new MyIcodeRecognizer();
		List<String> codes = ocr.code(target);
		int w = ocr.getStdWidth();
		for(String code:codes){
			//System.out.println(code);
			
			for(int i=0;i<code.length()-w;i+=w){
				System.out.println(code.substring(i, i+w));
			}
			System.out.println("\n");
			
		}
		
	}
}
