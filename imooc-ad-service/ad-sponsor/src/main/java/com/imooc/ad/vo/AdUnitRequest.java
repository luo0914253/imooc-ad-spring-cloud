package com.imooc.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitRequest {
    private Long planId;//关联的推广计划
    private String unitName;

    private Integer positionType;//物料类型，比如可以是jpg，bmp
    private Long budget;//预算

    public boolean createValidate(){
        return planId != null && !StringUtils.isEmpty(unitName) && positionType != null && budget != null;
    }
}
