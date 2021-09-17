package com.imooc.ad.search.vo;

import com.imooc.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    public Map<String,List<Creative>> adSlot2Ads = new HashMap<>();
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative{
        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer positionType;
//      展示监测URL
        private List<String> showMonitorUrl = Arrays.asList("www.imooc.com","www.imooc.com");
//      点击监测URL
        private List<String> clickMonitorUrl = Arrays.asList("www.imooc.com","www.imooc.com");
    }
    public static Creative convert(CreativeObject object){
        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setAdUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setPositionType(object.getMaterialType());
        return creative;
    }
}
