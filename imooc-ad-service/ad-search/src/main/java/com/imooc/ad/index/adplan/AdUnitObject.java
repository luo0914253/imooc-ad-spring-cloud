package com.imooc.ad.index.adplan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitObject {
    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;

    private AdPlanObject adPlanObject;

    public void update(AdUnitObject newObject){
        if (newObject.getUnitId() != null){
            this.unitId = newObject.getUnitId();
        }
        if (newObject.getUnitStatus()!=null){
            this.unitStatus = newObject.getUnitStatus();
        }
        if (newObject.getPositionType() !=null){
            this.positionType = newObject.getPositionType();
        }
        if (newObject.getPlanId()!= null){
            this.planId = newObject.getPlanId();
        }
    }
}
