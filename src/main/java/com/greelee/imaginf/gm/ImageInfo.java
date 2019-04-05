package com.greelee.imaginf.gm;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: gl
 * @Email: 110.com
 * @version: 1.0
 * @Date: 2019/4/6
 * @describe:
 */
@Data
@Builder
public class ImageInfo implements Serializable {
    private static final long serialVersionUID = 4331043285689691275L;

    /**
     * 宽
     */
    private String width;
    /**
     * 高
     */
    private String height;
    /**
     * 路径
     */
    private String path;
    /**
     * 质量
     */
    private String quality;
    /**
     * 大小
     */
    private String size;
    /**
     * 格式
     */
    private String format;
}
