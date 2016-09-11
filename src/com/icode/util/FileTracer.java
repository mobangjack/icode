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
import java.util.ArrayList;
import java.util.List;

/**
 * FileTracer trace all files from the given root file.
 * @author 帮杰
 *
 */
public class FileTracer {

	private File rootFile;
	private List<File> files;
	
	public FileTracer(String rootFile) {
		this(new File(rootFile));
	}
	
	public FileTracer(File rootFile) {
		if(!rootFile.exists())
			throw new IllegalArgumentException("root file '"+rootFile.getAbsolutePath()+"' doesn't exist!");
		this.rootFile = rootFile;
		this.files = new ArrayList<File>();
	}

	public List<File> trace() {
		if(rootFile.isDirectory()){
			File[] fs = rootFile.listFiles();
			if(fs==null)
				return null;
			for(File file:fs){
				FileTracer fileTracer = new FileTracer(file);
				List<File> fList = fileTracer.trace();
				if(fList!=null){
					this.files.addAll(fList);
				}
			}
		}else {
			this.files.add(rootFile);
		}
		return this.files;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileTracer fileTracer = new FileTracer("G:/教学资料");
		List<File> files = fileTracer.trace();
		if(files!=null){
			for(File f:files){
				System.out.println(f.getAbsolutePath());
			}
		}
	}

}
