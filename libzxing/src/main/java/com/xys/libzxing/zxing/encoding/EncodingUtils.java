package com.xys.libzxing.zxing.encoding;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 二维码生成工具类
 */
public class EncodingUtils {

    /**
     * 创建二维码
     *
     * @param content   content
     * @param widthPix  widthPix
     * @param heightPix heightPix
     * @param logoBm    logoBm
     * @return 二维码
     */
    public static Bitmap createQRCode(String content, int widthPix, int heightPix, Bitmap logoBm,int colorNum) {
        try {
            if (content == null || "".equals(content)) {
                return null;
            }
            // 配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
                    heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            //第一秒的情况下
            if (colorNum%4==1){
                for (int y = 0; y < heightPix; y++) {
                    for (int x = 0; x < widthPix; x++) {
                        if (bitMatrix.get(x, y)) {
                            if (x<widthPix / 2 && y < heightPix/ 2) {
                                pixels[y * widthPix+ x] = 0xFF0094FF;// 蓝色
                                Integer.toHexString(new Random().nextInt());
                            }else if (x <widthPix / 2 && y > heightPix / 2) {
                                pixels[y * widthPix + x] = 0xFFFED545;// 黄色
                            }else if (x > widthPix / 2 && y > heightPix / 2) {
                                pixels[y * widthPix + x] = 0xFF5ACF00;// 绿色
                            }else {
                                pixels[y * widthPix + x] = 0xFF000000;// 黑色
                            }
                        } else {
                            pixels[y * widthPix + x] = 0xffffffff;// 白色
                        }

                    }
                }
            }
            //第二秒的情况下
            else if (colorNum%4==2){
                for (int y = 0; y < heightPix; y++) {
                    for (int x = 0; x < widthPix; x++) {
                        if (bitMatrix.get(x, y)) {
                            if (x<widthPix / 2 && y < heightPix/ 2) {
                                pixels[y * widthPix+ x] = 0xFF000000;// 蓝色
                                Integer.toHexString(new Random().nextInt());
                            }else if (x <widthPix / 2 && y > heightPix / 2) {
                                pixels[y * widthPix + x] =0xFF0094FF;// 黄色
                            }else if (x > widthPix / 2 && y > heightPix / 2) {
                                pixels[y * widthPix + x] = 0xFFFED545;// 绿色
                            }else {
                                pixels[y * widthPix + x] =  0xFF5ACF00;// 黑色
                            }
                        } else {
                            pixels[y * widthPix + x] = 0xffffffff;// 白色
                        }
                    }
                }

            }
            //第三秒的情况下
            else if (colorNum%4==3){
                for (int y = 0; y < heightPix; y++) {
                    for (int x = 0; x < widthPix; x++) {
                        if (bitMatrix.get(x, y)) {
                            if (x<widthPix / 2 && y < heightPix/ 2) {
                                pixels[y * widthPix+ x] = 0xFF5ACF00;// 蓝色
                                Integer.toHexString(new Random().nextInt());
                            }else if (x <widthPix / 2 && y > heightPix / 2) {
                                pixels[y * widthPix + x] =0xFF000000;// 黄色
                            }else if (x > widthPix / 2 && y > heightPix / 2) {
                                pixels[y * widthPix + x] =0xFF0094FF;// 绿色
                            }else {
                                pixels[y * widthPix + x] =  0xFFFED545 ;// 黑色
                            }
                        } else {
                            pixels[y * widthPix + x] = 0xffffffff;// 白色
                        }
                    }
                }
            }
            //第四秒的情况下
            else if (colorNum%4==0){
                for (int y = 0; y < heightPix; y++) {
                    for (int x = 0; x < widthPix; x++) {
                        if (bitMatrix.get(x, y)) {
                            if (x<widthPix / 2 && y < heightPix/ 2) {
                                pixels[y * widthPix+ x] = 0xFFFED545;// 蓝色
                                Integer.toHexString(new Random().nextInt());
                            }else if (x <widthPix / 2 && y > heightPix / 2) {
                                pixels[y * widthPix + x] =0xFF5ACF00;// 黄色
                            }else if (x > widthPix / 2 && y > heightPix / 2) {
                                pixels[y * widthPix + x] =0xFF000000;// 绿色
                            }else {
                                pixels[y * widthPix + x] = 0xFF0094FF  ;// 黑色
                            }
                        } else {
                            pixels[y * widthPix + x] = 0xffffffff;// 白色
                        }
                    }
                }
            }

//            for (int y = 0; y < heightPix; y++) {
//                for (int x = 0; x < widthPix; x++) {
//                   if (bitMatrix.get(x, y)) {
//                      if (x<widthPix / 2 && y < heightPix/ 2) {
//                    pixels[y * widthPix+ x] = 0xFF0094FF;// 蓝色
//              Integer.toHexString(new Random().nextInt());
//                }else if (x <widthPix / 2 && y > heightPix / 2) {
//                    pixels[y * widthPix + x] = 0xFFFED545;// 黄色
//                }else if (x > widthPix / 2 && y > heightPix / 2) {
//                    pixels[y * widthPix + x] = 0xFF5ACF00;// 绿色
//                }else {
//                    pixels[y * widthPix + x] = 0xFF000000;// 黑色
//                }
//                   } else {
//                       pixels[y * widthPix + x] = 0xffffffff;// 白色
//                  }
//
//
//
//
//
//
//                }
//            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }


}
