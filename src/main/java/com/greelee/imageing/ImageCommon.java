package com.greelee.imageing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

/**
 * @author: gl
 * @Email: 110.com
 * @version: 1.0
 * @Date: 2019/4/6
 * @describe:
 */
public class ImageCommon implements Serializable {
    private static final long serialVersionUID = 3602366187033938408L;


    /**
     * 把文字转化为一张背景透明的png图片
     *
     * @param content  文字的内容
     * @param fontType 字体，例如宋体
     * @param fontSize 字体大小
     * @param colorStr 字体颜色，不带#号，例如"990033"
     * @param outfile  png图片的路径
     * @throws Exception e
     */
    public void converFontToImage(String content, String fontType, int fontSize, String colorStr, String outfile) throws Exception {
        Font font = new Font(fontType, Font.BOLD, fontSize);
        //获取font的样式应用在str上的整个矩形
        Rectangle2D r = font.getStringBounds(content, new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
        //获取单个字符的高度
        int unitHeight = (int) Math.floor(r.getHeight());
        //获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        int width = (int) Math.round(r.getWidth()) + 1;
        //把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        int height = unitHeight + 3;
        //创建图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        //在换成所需要的字体颜色
        g2d.setColor(new Color(Integer.parseInt(colorStr, 16)));
        g2d.setFont(font);
        g2d.drawString(content, 0, font.getSize());

        File file = new File(outfile);
        //输出png图片
        ImageIO.write(image, "png", file);
    }
}
