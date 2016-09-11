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
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.icode.ocr.Ocr;
import com.icode.ocr.OcrFilter;
import com.icode.ocr.OcrMaster;
import com.icode.ocr.plugin.CharacterExtractor;
import com.icode.ocr.plugin.LineExtractor;
import com.icode.util.FileUtil;
import com.icode.util.ImageIOHelper;

/**
 * 各项功能测试
 * @author 帮杰
 *
 */
public class Test {

	/**训练样本集目录**/
	static String icodeDir = "demo/icode/icode";
	
	/**数据库文件**/
	static String dbFile = "demo/icode/db.txt";
	
	/**编码矩阵文件**/
	static String matrixFile = "demo/icode/matrix.txt";
	
	/**目标文件1**/
	static String target1 = "demo/icode/0-9.png";
	/**目标文件2**/
	static String target2 = "demo/icode/a-z.png";
	/**目标文件3**/
	static String target3 = "demo/icode/AA-ZZ.png";
	
	/**目标文件4**/
	static String target4 = "demo/icode/icode/2vvjl.jpg";
	
	JFrame frame = null;
	Ocr ocr = null;
	OcrMaster master = null;
	
	public Test() {
	}
	
	private void initFrame() {
		if(frame==null){
			frame = new JFrame();
			frame.setLayout(new FlowLayout());
			frame.setBounds(480, 240, 900, 600);
		}
	}
	
	private void initOcr() {
		if(ocr==null){
			ocr = new WhuEisIcodeRecognizer();
		}
	}
	
	private void initOcrTrainer() {
		initOcr();
		if(master==null){
			master = new OcrMaster(ocr);
		}
	}
	
	public void testOcrFilter(BufferedImage image) {
		initFrame();
		JTextArea textArea = new JTextArea("testOcrFilter");
		frame.add(textArea);
		JLabel label = new JLabel("（OcrFilter处理前）",new ImageIcon(image), 0);
		frame.add(label);
		image = new OcrFilter(image).medianX().medianY().extrem(100).keepRGB(255, 0, 0).optBufferedImage();
		JLabel label2 = new JLabel("（OcrFilter处理后）",new ImageIcon(image), 0);
		frame.add(label2);
		frame.setVisible(true);
		
		/*
		BufferedImage image = ImageIOHelper.getImage(new File(target4));
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setBounds(480, 240, 900, 600);
		JLabel label = new JLabel("（OcrFilter处理前）",new ImageIcon(image), 0);
		frame.add(label);
		image = new OcrFilter(image).medianX().medianY().extrem(100).keepRGB(255, 0, 0).optBufferedImage();
		JLabel label2 = new JLabel("（OcrFilter处理后）",new ImageIcon(image), 0);
		frame.add(label2);
		frame.setVisible(true);
		*/
	}
	
	public void testLineExtractor(BufferedImage image) {
		initFrame();
		JTextArea textArea = new JTextArea("testLineExtractor");
		frame.add(textArea);
		List<BufferedImage> slices = new LineExtractor().slice(image);
		for(BufferedImage e:slices){
			JLabel label = new JLabel("（LineExtractor行分割）",new ImageIcon(e), 0);
			frame.add(label);
			testCharExtractor(e);
		}
		frame.setVisible(true);
	}
	
	public void testCharExtractor(BufferedImage image) {
		initFrame();
		JTextArea textArea = new JTextArea("testCharExtractor");
		frame.add(textArea);
		List<BufferedImage> slices = new CharacterExtractor().slice(image, 16, 16);
		for(BufferedImage e:slices){
			JLabel label = new JLabel("（CharExtractor字符分割）",new ImageIcon(e), 0);
			frame.add(label);
		}
		frame.setVisible(true);
	}
	
	/**
	 * 训练
	 * @throws IOException
	 */
	public void train() throws IOException {
		
		File file = new File(dbFile);
		if(file.exists()&&file.isFile()){
			System.out.println("dbFile '"+dbFile+"' already exists.Method train() returned.");
			return;
		}
		System.out.println("training...");
		initOcrTrainer();
		master.feedImageDir(icodeDir).sort().compress().output(file);
		System.out.println("done");
	}
	
	/**
	 * 输出阵列
	 */
	public void optMatrix() throws IOException {
		File f = new File(matrixFile);
		if(f.exists()&&f.isFile()){
			System.out.println("matrixFile '"+matrixFile+"' already exists.Method optMatrix() returned.");
			return;
		}
		System.out.println("optMatrix...");
		f.createNewFile();
		initOcrTrainer();
		File file = new File(dbFile);
		if(!file.isFile()){
			master.feedImageDir(icodeDir);
			master.output(file);
		}
		master.read(dbFile);
		String s = master.optMatrix();
		FileUtil.append(f, s);
		System.out.println("done");
	}
	
	/**
	 * 识别测试
	 * @throws IOException
	 */
	public void testOcr() throws IOException {
		initOcrTrainer();
		
		File file = new File(dbFile);
		if((!file.exists())||(!file.isFile())){
			train();
		}
		master.read(dbFile);
		List<String> libs = master.list();
		
		
		String result1 = ocr.meanOcr(ImageIOHelper.getImage(new File(target1)), libs);
		System.out.println(result1);
		
		String result2 = ocr.meanOcr(ImageIOHelper.getImage(new File(target2)), libs);
		System.out.println(result2);
		
		String result3 = ocr.meanOcr(ImageIOHelper.getImage(new File(target3)), libs);
		System.out.println(result3);
		
	}
	
	public static void main(String[] args) throws IOException {
		Test test = new Test();
		//test.train();
		test.testOcr();
		//test.optMatrix();
		//test.testOcrFilter(ImageIOHelper.getImage(new File(target4)));
	}
	
}
