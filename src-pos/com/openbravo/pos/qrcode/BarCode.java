//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2014 uniCenta & previous Openbravo POS works
//    http://www.unicenta.com
//
//    This file is part of uniCenta oPOS
//
//    uniCenta oPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   uniCenta oPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.
//    BarCode generator John L 07.03.2015 uses zxing.jar
package com.openbravo.pos.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.*;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BarCode {

    private BitMatrix byteMatrix;
    private BufferedImage image;

    public BarCode() {

    }

    public BufferedImage getQRCode(String codeText, Integer size) {
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            byteMatrix = qrCodeWriter.encode(codeText, BarcodeFormat.QR_CODE, size, size, hintMap);
            int imageWidth = byteMatrix.getWidth();
            image = new BufferedImage(imageWidth, imageWidth, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, imageWidth, imageWidth);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < imageWidth; i++) {
                for (int j = 0; j < imageWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            return image;
        } catch (WriterException e) {
            return null;
        }
    }

    public BufferedImage getBarcode(String codeText, String codeType, Integer bcWidth, Integer bcHeight) {
        bcWidth = (bcWidth == 0) ? 150 : bcWidth;
        bcHeight = (bcHeight == 0) ? 20 : bcHeight;

        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            switch (codeType) {
                case "QR_CODE":
                    return getQRCode(codeText, bcWidth);
                case "CODE_39":
                    Code39Writer codeWriter = new Code39Writer();
                    byteMatrix = codeWriter.encode(codeText, BarcodeFormat.CODE_39, bcWidth, bcHeight, hintMap);
                    return createBC();
                case "CODE_128":
                    Code128Writer code128Writer = new Code128Writer();
                    byteMatrix = code128Writer.encode(codeText, BarcodeFormat.CODE_128, bcWidth, bcHeight, hintMap);
                    return createBC();
                case "EAN_13":
                    EAN13Writer ean13Writer = new EAN13Writer();
                    byteMatrix = ean13Writer.encode(codeText, BarcodeFormat.EAN_13, bcWidth, bcHeight, hintMap);
                    return createBC();
                case "EAN_8":
                    EAN8Writer ean8Writer = new EAN8Writer();
                    byteMatrix = ean8Writer.encode(codeText, BarcodeFormat.EAN_8, bcWidth, bcHeight, hintMap);
                    return createBC();
                case "CODABAR":
                    CodaBarWriter CodaBarWriter = new CodaBarWriter();
                    byteMatrix = CodaBarWriter.encode(codeText, BarcodeFormat.CODABAR, bcWidth, bcHeight, hintMap);
                    return createBC();
                case "UPC_A":
                    UPCAWriter UPCAWriter = new UPCAWriter();
                    byteMatrix = UPCAWriter.encode(codeText, BarcodeFormat.UPC_A, bcWidth, bcHeight, hintMap);
                    return createBC();
            }
        } catch (WriterException ex) {
            Logger.getLogger(BarCode.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private BufferedImage createBC() {
        int imageWidth = byteMatrix.getWidth();
        int imageHeight = byteMatrix.getHeight();
        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        return image;
    }

}
