package com.magic.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by jamen on 17/2/8.
 */
public class ZxingUtils {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
    private static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }
    public static String decodeQRCode(InputStream ins) {
        try {
            BufferedImage bufferedImage = ImageIO.read(ins);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap bitmap = new BinaryBitmap(binarizer);
            HashMap<DecodeHintType, Object> hintTypeObjectHashMap = new HashMap<>();
            hintTypeObjectHashMap.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(bitmap, hintTypeObjectHashMap);
            String ret = result.getText();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String decodeORCode(byte[] data) {
        return decodeQRCode(new ByteArrayInputStream(data));
    }
    public static byte[] generateImageData(String content, int w, int border) {
        int width = w;
        int height = w;
        String format = "gif";
        try {

            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");   // 内容所使用字符集编码
            hints.put(EncodeHintType.MARGIN, border);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            writeToStream(bitMatrix, format, bout);
            return bout.toByteArray();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }
    public static String generateBase64QRCode(String content) {
        try {

            byte[] data = generateImageData(content, 300, 4);
            String base64String = Base64Utils.encodeToString(data);
            String ret = "data:image/gif;charset=utf-8;base64," + base64String;
            return ret;
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }
}
