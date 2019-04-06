package com.greelee.imageing.gm;

import com.google.common.base.Strings;
import com.greelee.imageing.ImageException;
import gl.tool.util.file.FileUtil;
import gl.tool.util.lang.SysUtil;
import org.im4java.core.*;
import org.im4java.process.ArrayListOutputConsumer;

import java.awt.image.ImagingOpException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author: gl
 * @Email: 110.com
 * @version: 1.0
 * @Date: 2019/4/5
 * @describe: im4java+graphicsMagic 图像操作
 */
public class ImageOperation implements Serializable {


    private static final long serialVersionUID = 1115129771844723407L;

    /**
     * graphicsMagic 安装地址
     */
    private final String gmPath;

    private ImageOperation(String path) {
        this.gmPath = path;
    }


    public static ImageOperation newImageOperation(String path) {
        return new ImageOperation(path);
    }


    /**
     * 转换格式
     * @param srcImagePath 目标图片地址
     * @param destImagePath 转换后的图片地址
     * @throws Exception
     */
    public void conversionFormat(String srcImagePath, String destImagePath) throws Exception {
        rotate(srcImagePath,destImagePath,0);
    }


    /**
     * 图片旋转
     *
     * @param srcImagePath  原图片地址
     * @param destImagePath 旋转后图片地址
     * @param angle         角度
     */
    public void rotate(String srcImagePath, String destImagePath, double angle) throws Exception {
        if (Strings.isNullOrEmpty(srcImagePath)) {
            throw new ImageException("The picture was not found");
        }
        if (Strings.isNullOrEmpty(destImagePath)) {
            destImagePath = FileUtil.getFileName(srcImagePath) + "-rotate." + FileUtil.getSuffix(srcImagePath);
        }
        isValidPath();
        IMOperation op = new IMOperation();
        op.rotate(angle);
        op.addImage(srcImagePath);
        op.addImage(destImagePath);
        convertCmdRun(op);
    }

    /**
     * 给图片加文字水印,暂时只执行英文
     * 可以加英文水印，中文会报错或者乱码，变通方法是将文字生成图片，然后加水印
     * 需要安装字体
     * 需要修改安装的字体目录路径  type-ghostscript.mgk ->n019003l.pfb 改为字体库的路径 例如：
     * metrics="D:\Fonts\n019003l.afm"
     * glyphs="D:\Fonts\n019003l.pfb"
     *
     * @param srcPath       原图片地址
     * @param destImagePath 生成后图片地址
     * @param content       水印内容
     * @param font          字体
     * @param fill          颜色
     * @throws Exception e
     */
    public void addImgText(String srcPath, String destImagePath, String content, String font, String fill) throws Exception {
        if (Strings.isNullOrEmpty(font)) {
            font = "宋体";
        }
        if (Strings.isNullOrEmpty(fill)) {
            fill = "#000000";
        }
        GMOperation op = new GMOperation();
        op.font(font);
        op.gravity("southeast");
        //("x1 x2 x3 x4") x1 格式，x2 x轴距离 x3 y轴距离  x4名称，文字内容
        op.pointsize(38).fill(fill).draw("text 10,10 " + new String(content.getBytes(StandardCharsets.UTF_8), "gbk"));
        op.addImage(srcPath);
        op.addImage(destImagePath);
        convertCmdRun(op);
    }


    /**
     * 添加图片水印
     *
     * @param srcPath      原图片路径
     * @param distPath     新图片路径
     * @param watermarkImg 水印图片路径
     * @param width        水印宽度（可以于水印图片大小不同）
     * @param height       水印高度（可以于水印图片大小不同）
     * @param x            水印开始X坐标
     * @param y            水印开始Y坐标
     * @param alpha        透明度[0-100]
     * @throws Exception e
     */
    public void addWatermarkToImg(String srcPath, String distPath, String watermarkImg, int width,
                                  int height, int x, int y, int alpha) throws Exception {
        if (Strings.isNullOrEmpty(srcPath)) {
            throw new ImageException("Target picture path is null or empty");
        }
        if (Strings.isNullOrEmpty(watermarkImg)) {
            throw new ImageException("watermark picture path is null or empty");
        }
        if (Strings.isNullOrEmpty(distPath)) {
            distPath = FileUtil.getFileName(srcPath) + "-watermark." + FileUtil.getSuffix(srcPath);
        }
        IMOperation op = new IMOperation();
        op.dissolve(alpha);
        op.geometry(width, height, x, y);
        op.addImage(watermarkImg, srcPath, distPath);
        compositeCmdRun(op);
    }


    /**
     * 根据坐标裁剪图片
     *
     * @param srcPath    要裁剪图片的路径
     * @param newPath    裁剪图片后的路径
     * @param startXAxis 起始横坐标
     * @param startYAxis 起始纵坐标
     * @param endXAxis   结束横坐标
     * @param endYAxis   结束纵坐标
     */
    public void cutImage(String srcPath, String newPath, int startXAxis, int startYAxis, int endXAxis, int endYAxis) throws Exception {
        if (Strings.isNullOrEmpty(srcPath)) {
            throw new ImageException("Target picture path is null or empty");
        }
        int width = endXAxis - startXAxis;
        int height = endYAxis - startYAxis;
        IMOperation op = new IMOperation();
        op.addImage(srcPath);
        // width：裁剪的宽度    * height：裁剪的高度 * startXAxis：裁剪的横坐标 * startYAxis：裁剪纵坐标
        op.crop(width, height, startXAxis, startYAxis);
        op.addImage(newPath);
        convertCmdRun(op);
    }

    /**
     * 根据尺寸缩放图片
     *
     * @param width   缩放后的图片宽度
     * @param height  缩放后的图片高度
     * @param srcPath 源图片路径
     * @param newPath 缩放后图片的路径
     */
    public void zoomImage(Integer width, Integer height, String srcPath, String newPath) throws Exception {
        if (Objects.isNull(width) && Objects.isNull(height)) {
            throw new ImageException("Zoom width and height cannot all be empty");
        }
        IMOperation op = new IMOperation();
        op.addImage(srcPath);
        op.resize(width, height);
        op.addImage(newPath);
        convertCmdRun(op);
    }

    /**
     * 压缩图片
     *
     * @param srcImagePath 原图片地址
     * @param newImagePath 新图片地址
     * @param quality      压缩比例
     *                     图片压缩比，有效值范围是0.0-100.0，数值越大，缩略图越清晰。
     */
    public void compressImage(String srcImagePath, String newImagePath, double quality) throws Exception {
        if (Strings.isNullOrEmpty(srcImagePath)) {
            throw new ImageException("Target picture path is null or empty");
        }
        if (Strings.isNullOrEmpty(newImagePath)) {
            newImagePath = FileUtil.getFileName(srcImagePath) + "-compressImage." + FileUtil.getSuffix(srcImagePath);
        }
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.quality(quality);
        op.addImage(newImagePath);
        convertCmdRun(op);
    }


    public enum MontageDirected {
        /**
         * 横向
         */
        TRANSVERSE(1),
        /**
         * 竖向
         */
        VERTICAL(2);

        private MontageDirected(Integer value) {
            this.value = value;
        }

        private Integer value;

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 图片合成
     *
     * @param args      图片地址数组
     * @param maxWidth  宽度
     * @param maxHeight 高度
     * @param newPath   生成地址
     * @param mrg       暂不清楚该参数
     * @param type      方向
     */
    public void montage(String[] args, Integer maxWidth, Integer maxHeight, String newPath, Integer mrg, MontageDirected type) throws Exception {
        IMOperation op = new IMOperation();
        String thumbSize = maxWidth + "x" + maxHeight + "^";
        String extent = maxWidth + "x" + maxHeight;
        int transverse = 1;
        int vertical = 2;
        if (type.getValue() == transverse) {
            op.addRawArgs("+append");
        } else if (type.getValue() == vertical) {
            op.addRawArgs("-append");
        }

        op.addRawArgs("-thumbnail", thumbSize);
        op.addRawArgs("-gravity", "center");
        op.addRawArgs("-extent", extent);

        int borderW = maxWidth / 40;
        op.addRawArgs("-border", borderW + "x" + borderW);
        op.addRawArgs("-bordercolor", "#ccc");

        op.addRawArgs("-border", 1 + "x" + 1);
        op.addRawArgs("-bordercolor", "#fff");

        for (String img : args) {
            op.addImage(img);
        }
        if (type.getValue() == transverse) {
            int wholeWidth = ((mrg / 2) + 1 + borderW + maxWidth + borderW + (mrg / 2) + 1) * args.length - mrg;
            int wholeHeight = maxHeight + borderW + 1;
            op.addRawArgs("-extent", wholeWidth + "x" + wholeHeight);
        } else if (type.getValue() == vertical) {
            int wholeWidth = maxWidth + borderW + 1;
            int wholeHeight = ((mrg / 2) + 1 + borderW + maxHeight + borderW + (mrg / 2) + 1) * args.length - mrg;
            op.addRawArgs("-extent", wholeWidth + "x" + wholeHeight);
        }
        op.addImage(newPath);
        convertCmdRun(op);
    }


    /**
     * 获取图片信息
     *
     * @param imagePath 图片地址
     * @return
     */
    public ImageInfo getImageInfo(String imagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.format("%w,%h,%d/%f,%Q,%b,%e");
        op.addImage(imagePath);
        ImageCommand identifyCmd = new IdentifyCmd(true);
        identifyCmd.setSearchPath(gmPath);
        ArrayListOutputConsumer output = new ArrayListOutputConsumer();
        identifyCmd.setOutputConsumer(output);
        identifyCmd.run(op);
        ArrayList<String> cmdOutput = output.getOutput();
        String[] result = cmdOutput.get(0).split(",");
        return ImageInfo.builder()
                .width(result[0])
                .height(result[1])
                .path(result[2])
                .quality(result[3])
                .size(result[4])
                .format(result[5])
                .build();
    }


    private void convertCmdRun(Operation op) throws Exception {
        //true 使用graphicsMagic 否者使用的是imageMagic
        ConvertCmd convert = new ConvertCmd(true);
        //windows下需要设置，linux下不要设置
        if (SysUtil.isWindowsSys()) {
            convert.setSearchPath(gmPath);
        }
        convert.run(op);
    }

    private void identifyCmdRun(Operation op) throws Exception {
        ImageCommand identifyCmd = new IdentifyCmd(true);
        if (SysUtil.isWindowsSys()) {
            identifyCmd.setSearchPath(gmPath);
        }
        identifyCmd.run(op);
    }

    private void compositeCmdRun(Operation op) throws Exception {
        CompositeCmd cmd = new CompositeCmd(true);
        if (SysUtil.isWindowsSys()) {
            cmd.setSearchPath(gmPath);
        }
        cmd.run(op);
    }

    private void isValidPath() throws ImagingOpException {
        if (SysUtil.isWindowsSys() && Strings.isNullOrEmpty(gmPath)) {
            throw new ImagingOpException("graphicsMagic Installation path not found,you should structure address");
        }
    }
}
