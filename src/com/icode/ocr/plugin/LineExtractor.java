// LineExtractor.java
// Copyright (c) 2010 William Whitney
// All rights reserved.
// This software is released under the BSD license.
// Please see the accompanying LICENSE.txt for details.
package com.icode.ocr.plugin;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.icode.ocr.scanner.DocumentScanner;
import com.icode.ocr.scanner.DocumentScannerListenerAdaptor;
import com.icode.ocr.scanner.PixelImage;

/**
 * Saves all the characters in an image to an output directory individually.
 * @author William Whitney
 */
public class LineExtractor extends DocumentScannerListenerAdaptor
{

    private DocumentScanner documentScanner = new DocumentScanner();
    private BufferedImage inputImage = null;
    private List<BufferedImage> slices = new ArrayList<BufferedImage>();

    public List<BufferedImage> slice(BufferedImage inputImage)
    {
    	this.inputImage = inputImage;
        PixelImage pixelImage = new PixelImage(inputImage);
        pixelImage.toGrayScale(true);
        pixelImage.filter();
        documentScanner.scan(pixelImage, this, 0, 0, pixelImage.width, pixelImage.height);
		return slices;
    }

    @Override
    public void beginRow(PixelImage pixelImage, int y1, int y2)
    {
    	int areaH = y2 - y1;
	    int areaW = inputImage.getWidth();
	    slices.add(inputImage.getSubimage(0, y1, areaW, areaH));
    }
}
