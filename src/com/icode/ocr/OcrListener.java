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
import java.util.List;

/**
 * OcrListener for OCR.
 * <p>1.extractLines
 * <p>2.extractChars
 * <p>3.code
 * <p>4.store codes or recognize(greedyOcr or meanOcr.Best for meanOcr.).
 * @author 帮杰
 *
 */
public interface OcrListener {
	
	//1.extractLines
	void beforeExtractingLines(BufferedImage filteredImage);
	void afterExtractingLines(List<BufferedImage> lines);
	
	//2.extractChars
	void beforeExtractingChars(BufferedImage line);
	void afterExtractingChars(List<BufferedImage> chars);
	
	//3.code
	void beforeCoder(BufferedImage character);
	void afterCoder(BufferedImage character,String code);
	
	void onBestCodeFound(BufferedImage character,String code);
	
}
