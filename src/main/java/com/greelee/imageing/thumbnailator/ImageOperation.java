package com.greelee.imageing.thumbnailator;

import com.google.common.base.Strings;
import com.greelee.imageing.ImageException;
import com.greelee.imageing.ImageTypeEnum;
import gl.tool.util.file.FileUtil;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author: gl
 * @Email: 110.com
 * @version: 1.0
 * @Date: 2019/4/6
 * @describe: thumbnailator 框架操作图像
 */
public class ImageOperation implements Serializable {

    private static final long serialVersionUID = 1858080657537021731L;


    /**
     * 生成固定大小的图片
     *
     * @param srcPath  目标图片路径
     * @param distPath 生成图片路径
     * @param width    宽
     * @param height   高
     * @throws IOException e
     */
    public static void generateFixedSize(String srcPath, String distPath, int width, int height) throws Exception {
        if (Strings.isNullOrEmpty(srcPath)) {
            throw new ImageException("The target picture was not found");
        }
        if (width < 1 || height < 1) {
            throw new ImageException("Destination image width or height must not be less than or equal to 0 pixels");
        }
        if (Strings.isNullOrEmpty(distPath)) {
            distPath = FileUtil.getFileName(srcPath) + "-generateFixedSize." + FileUtil.getSuffix(srcPath);
        }
        Thumbnails.of(srcPath)
                .size(width, height)
                .toFile(distPath);
    }


    /**
     * 按照比例缩放
     *
     * @param srcPath  目标图片路径
     * @param distPath 生成图片路径
     * @param scale    比例缩放
     */
    public static void scale(String srcPath, String distPath, double scale) throws Exception {
        if (Strings.isNullOrEmpty(srcPath)) {
            throw new ImageException("The target picture was not found");
        }
        if (scale <= 0) {
            throw new ImageException("The scaling factor is equal to or less than 0");
        }
        if (Strings.isNullOrEmpty(distPath)) {
            distPath = FileUtil.getFileName(srcPath) + "-scale." + FileUtil.getSuffix(srcPath);
        }
        Thumbnails.of(srcPath)
                .scale(scale)
                .toFile(distPath);
    }

    /**
     * 旋转图像
     *
     * @param srcPath  目标图片路径
     * @param distPath 生成图片路径
     * @param angle    旋转角度 正数：顺时针 负数：逆时针
     * @throws Exception
     */
    public static void rotate(String srcPath, String distPath, double angle) throws Exception {
        if (Strings.isNullOrEmpty(srcPath)) {
            throw new ImageException("The target picture was not found");
        }
        if (Strings.isNullOrEmpty(distPath)) {
            distPath = FileUtil.getFileName(srcPath) + "-rotate." + FileUtil.getSuffix(srcPath);
        }
        Thumbnails.of(srcPath)
                .scale(1)
                .rotate(angle)
                .toFile(distPath);
    }

    /**
     * 添加水印图片
     *
     * @param srcPath       目标地址
     * @param distPath      生成地址
     * @param watermarkPath 水印地址
     * @param width         宽
     * @param height        高
     * @param alpha         透明度
     * @param quality       质量 0.0-1.0
     * @param position      水印的方向
     * @throws Exception e
     */
    public static void watermark(String srcPath, String distPath, String watermarkPath,
                                 int width, int height, float alpha, float quality,
                                 Positions position) throws Exception {
        //watermark 位置，水印图，透明度
        Thumbnails.of(srcPath).
                size(width, height)
                .watermark(
                        position, ImageIO.read(new File(watermarkPath)), alpha)
                .outputQuality(quality)
                .toFile(distPath);
    }


    /**
     * 转换图片类型
     *
     * @param srcPath  目标地址
     * @param distPath 生成地址
     * @param type     图片类型
     * @throws Exception e
     */
    public static void convert(String srcPath, String distPath, ImageTypeEnum type) throws Exception {
        Thumbnails.of(srcPath)
                .scale(1)
                .outputFormat(type.name())
                .toFile(distPath);
    }


    /**
     * 裁剪
     * @param srcPath  目标地址
     * @param distPath 生成地址
     * @param positions 位置
     * @param width 在该位置中的宽
     * @param height  在该位置中的宽
     * @throws Exception e
     */
    public static void cutImage(String srcPath, String distPath, Positions positions,
                          int width, int height) throws Exception {
        Thumbnails.of(srcPath)
                .sourceRegion(positions,width,height)
                .scale(1)
                .toFile(distPath);
    }

    /**
     * 指定坐标裁剪
     * @param srcPath 目标地址
     * @param distPath 生成地址
     * @param x x轴
     * @param y y轴
     * @param width 宽
     * @param height 高
     * @throws Exception e
     */
    public static void cutImageSpecifiedCoordinates(String srcPath, String distPath,int x, int y, int width, int height) throws Exception{
        Thumbnails.of(srcPath)
                .sourceRegion(x, y, width, height)
                .scale(1)
                .toFile(distPath);
    }


    public static void main(String[] args) throws Exception {
        cutImageSpecifiedCoordinates("C:\\Users\\gelin\\Desktop\\0.jpg", "C:\\Users\\gelin\\Desktop\\1.jpg",0,0,200,200);
    }


}
