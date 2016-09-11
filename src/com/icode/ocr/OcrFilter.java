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
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.icode.util.ImageIOHelper;

/**
 * OcrFilter.有用的滤镜。
 * @author 帮杰
 *
 */
public class OcrFilter {

	private int[] pixels;
	private int iw;
	private int ih;
	
	public OcrFilter(int[] pixels,int iw,int ih){
		set(pixels, iw, ih);
	}
	
	public OcrFilter(BufferedImage bufferedImage) {
		set(bufferedImage);
	}
	
	public OcrFilter set(BufferedImage bufferedImage) {
		iw = bufferedImage.getWidth();
		ih = bufferedImage.getHeight();
		pixels = new int[iw * ih];
		PixelGrabber pg = new PixelGrabber(bufferedImage.getSource(), 0, 0, iw, ih, pixels, 0, iw);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			LOG.log(Level.SEVERE, null, e);
		}
		return this;
	}
	
	public OcrFilter set(int[] pixels,int iw,int ih) {
		this.pixels = pixels;
		this.iw = iw;
		this.ih = ih;
		return this;
	}
	
	public int[] getPixels() {
		return pixels;
	}

	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

	public int getIw() {
		return iw;
	}

	public void setIw(int iw) {
		this.iw = iw;
	}

	public int getIh() {
		return ih;
	}

	public void setIh(int ih) {
		this.ih = ih;
	}

	/**
	 * X方向中值滤波
	 * @return
	 */
	public OcrFilter medianX() {
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 1; i < ih - 1; i++) {
			for (int j = 1; j < iw - 1; j++) {
				int red, green, blue;
				int alpha = cm.getAlpha(pixels[i * iw + j]);
	
				int red4 = cm.getRed(pixels[i * iw + j - 1]);
				int red5 = cm.getRed(pixels[i * iw + j]);
				int red6 = cm.getRed(pixels[i * iw + j + 1]);
	
				if (red4 >= red5) {
					if (red5 >= red6) {
						red = red5;
					} else {
						if (red4 >= red6) {
							red = red6;
						} else {
							red = red4;
						}
					}
				} else {
					if (red4 > red6) {
						red = red4;
					} else {
						if (red5 > red6) {
							red = red6;
						} else {
							red = red5;
						}
					}
				}
	
				int green4 = cm.getGreen(pixels[i * iw + j - 1]);
				int green5 = cm.getGreen(pixels[i * iw + j]);
				int green6 = cm.getGreen(pixels[i * iw + j + 1]);
	
				if (green4 >= green5) {
					if (green5 >= green6) {
						green = green5;
					} else {
						if (green4 >= green6) {
							green = green6;
						} else {
							green = green4;
						}
					}
				} else {
					if (green4 > green6) {
						green = green4;
					} else {
						if (green5 > green6) {
							green = green6;
						} else {
							green = green5;
						}
					}
				}
	
				int blue4 = cm.getBlue(pixels[i * iw + j - 1]);
				int blue5 = cm.getBlue(pixels[i * iw + j]);
				int blue6 = cm.getBlue(pixels[i * iw + j + 1]);
	
				if (blue4 >= blue5) {
					if (blue5 >= blue6) {
						blue = blue5;
					} else {
						if (blue4 >= blue6) {
							blue = blue6;
						} else {
							blue = blue4;
						}
					}
				} else {
					if (blue4 > blue6) {
						blue = blue4;
					} else {
						if (blue5 > blue6) {
							blue = blue6;
						} else {
							blue = blue5;
						}
					}
				}
				pixels[i * iw + j] = alpha << 24 | red << 16 | green << 8 | blue;
			}
		}
		return this;
	}
	
	/**
	 * Y方向中值滤波
	 * @return
	 */
	public OcrFilter medianY() {
		ColorModel cm = ColorModel.getRGBdefault();
		for (int j = 1; j < iw - 1; j++) {
			for (int i = 1; i < ih - 1; i++) {
				int red, green, blue;
				int alpha = cm.getAlpha(pixels[i * iw + j]);

				int red4 = cm.getRed(pixels[(i - 1) * iw + j]);
				int red5 = cm.getRed(pixels[i * iw + j]);
				int red6 = cm.getRed(pixels[(i + 1) * iw + j]);

				if (red4 >= red5) {
					if (red5 >= red6) {
						red = red5;
					} else {
						if (red4 >= red6) {
							red = red6;
						} else {
							red = red4;
						}
					}
				} else {
					if (red4 > red6) {
						red = red4;
					} else {
						if (red5 > red6) {
							red = red6;
						} else {
							red = red5;
						}
					}
				}

				int green4 = cm.getGreen(pixels[(i - 1) * iw + j]);
				int green5 = cm.getGreen(pixels[i * iw + j]);
				int green6 = cm.getGreen(pixels[(i + 1) * iw + j]);

				if (green4 >= green5) {
					if (green5 >= green6) {
						green = green5;
					} else {
						if (green4 >= green6) {
							green = green6;
						} else {
							green = green4;
						}
					}
				} else {
					if (green4 > green6) {
						green = green4;
					} else {
						if (green5 > green6) {
							green = green6;
						} else {
							green = green5;
						}
					}
				}

				int blue4 = cm.getBlue(pixels[(i - 1) * iw + j]);
				int blue5 = cm.getBlue(pixels[i * iw + j]);
				int blue6 = cm.getBlue(pixels[(i + 1) * iw + j]);

				if (blue4 >= blue5) {
					if (blue5 >= blue6) {
						blue = blue5;
					} else {
						if (blue4 >= blue6) {
							blue = blue6;
						} else {
							blue = blue4;
						}
					}
				} else {
					if (blue4 > blue6) {
						blue = blue4;
					} else {
						if (blue5 > blue6) {
							blue = blue6;
						} else {
							blue = blue5;
						}
					}
				}
				pixels[i * iw + j] = alpha << 24 | red << 16 | green << 8 | blue;
			}
		}
		return this;
	}
	
	/**
	 * 单门限RGB极值化
	 * @param th 阈值
	 * @return
	 */
	public OcrFilter extrem(int th) {
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < iw * ih; i++) {
			int red = cm.getRed(pixels[i]);
			int green = cm.getGreen(pixels[i]);
			int blue = cm.getBlue(pixels[i]);
			int alpha = cm.getAlpha(pixels[i]);
			if (red > th) {
				red = 255;
			} else {
				red = 0;
			}

			if (green > th) {
				green = 255;
			} else {
				green = 0;
			}

			if (blue > th) {
				blue = 255;
			} else {
				blue = 0;
			}

			pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}
		return this;
	}
	
	/**
	 * 双门限RGB极值化
	 * @param th1 下限阈值
	 * @param th2 上限阈值
	 * @return
	 */
	public OcrFilter doubleExtrem(int th1,int th2) {
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < iw * ih; i++) {
			int red = cm.getRed(pixels[i]);
			int green = cm.getGreen(pixels[i]);
			int blue = cm.getBlue(pixels[i]);
			int alpha = cm.getAlpha(pixels[i]);
			if (red >= th1&&red <= th2) {
				red = 255;
			} else {
				red = 0;
			}

			if (green >= th1&&green <= th2) {
				green = 255;
			} else {
				green = 0;
			}

			if (blue >= th1&&blue <= th2) {
				blue = 255;
			} else {
				blue = 0;
			}

			pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}
		return this;
	}
	
	/**
	 * 保留指定RGB色值，移除其他
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public OcrFilter keepRGB(int r,int g,int b) {
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < iw * ih; i++) {
			int alpha = cm.getAlpha(pixels[i]);
			int red = cm.getRed(pixels[i]);
			int green = cm.getGreen(pixels[i]);
			int blue = cm.getBlue(pixels[i]);
			if(red!=r||green!=g||blue!=b){
				red = 255;
				green = 255;
				blue = 255;
			}
			pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}
		return this;
	}
	
	/**
	 * 移除指定RGB色值
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public OcrFilter removeRGB(int r,int g,int b){
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < iw * ih; i++) {
			int alpha = cm.getAlpha(pixels[i]);
			int red = cm.getRed(pixels[i]);
			int green = cm.getGreen(pixels[i]);
			int blue = cm.getBlue(pixels[i]);
			if(red==r&&green==g&&blue==b){
				red = 255;
				green = 255;
				blue = 255;
			}
			pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}
		return this;
	}
	
	/**
	 * 灰度化
	 * @return
	 */
	public OcrFilter greyScale() {
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < iw * ih; i++) {
			int alpha = cm.getAlpha(pixels[i]);
			int red = cm.getRed(pixels[i]);
			int green = cm.getGreen(pixels[i]);
			int blue = cm.getBlue(pixels[i]);
			if(!(red==green&&green==blue)){
				red=green=blue=(int) (0.3 * red + 0.59 * green + 0.11 * blue);
			}
			pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}
		return this;
	}
	
	/**
	 * 黑白单色化
	 * @param th 阈值
	 * @return
	 */
	public OcrFilter mono(int th) {
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < iw * ih; i++) {
			int alpha = cm.getAlpha(pixels[i]);
			int red = cm.getRed(pixels[i]);
			int green = cm.getGreen(pixels[i]);
			int blue = cm.getBlue(pixels[i]);
			if(!(red==green&&green==blue)){
				red=green=blue=(int) (0.3 * red + 0.59 * green + 0.11 * blue);
			}
			if(red>th)
				red=green=blue=255;
			else
				red=green=blue=0;
			pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}
		return this;
	}
	
	/**
	 * 输出图片
	 * @return
	 */
	public BufferedImage optBufferedImage() {
		return ImageIOHelper.imageProducerToBufferedImage(new MemoryImageSource(iw, ih, pixels, 0, iw));
	}
	
	private static final Logger LOG = Logger.getLogger(OcrFilter.class.getName());
}
