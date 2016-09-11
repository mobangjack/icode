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
 * @author 帮杰
 *
 */
public class OcrListenerAdapter implements OcrListener {

	@Override
	public void beforeExtractingLines(BufferedImage filteredImage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterExtractingLines(List<BufferedImage> lines) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeExtractingChars(BufferedImage line) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterExtractingChars(List<BufferedImage> chars) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeCoder(BufferedImage character) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCoder(BufferedImage character, String code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBestCodeFound(BufferedImage character, String code) {
		// TODO Auto-generated method stub
		
	}

}
