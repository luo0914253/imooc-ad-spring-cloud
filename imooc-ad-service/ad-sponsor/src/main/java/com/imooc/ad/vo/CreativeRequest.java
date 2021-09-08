package com.imooc.ad.vo;

import com.imooc.ad.entity.Creative;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeRequest {
    private String name;
//  创意类型,如图片、视频、文本
    private Integer type;
//  物料类型，如jpg、bmp
    private Integer materialType;
    private Integer height;
    private Integer width;
    private Long size;
//  持续时长
    private Integer duration;
    private Long userId;
    private String url;
    public Creative convertToEntity(){
        Creative creative = new Creative();
        creative.setName(name);
        creative.setType(type);
        creative.setMaterialType(materialType);
        creative.setHeight(height);
        creative.setWidth(width);
        creative.setSize(size);
        creative.setDuration(duration);
        creative.setUserId(userId);
        creative.setUrl(url);
        return creative;
    }
}
