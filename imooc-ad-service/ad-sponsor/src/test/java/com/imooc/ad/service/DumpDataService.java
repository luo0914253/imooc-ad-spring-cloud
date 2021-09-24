package com.imooc.ad.service;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.Application;
import com.imooc.ad.constant.CommonStatus;
import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdPlanRepository;
import com.imooc.ad.dao.AdUnitRepository;
import com.imooc.ad.dao.CreativeRepository;
import com.imooc.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.imooc.ad.dao.unit_condition.AdUnitItRepository;
import com.imooc.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.imooc.ad.dao.unit_condition.CreativeUnitRepository;
import com.imooc.ad.dump.DConstant;
import com.imooc.ad.dump.table.*;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUnit;
import com.imooc.ad.entity.Creative;
import com.imooc.ad.entity.unit_condition.AdUnitDistrict;
import com.imooc.ad.entity.unit_condition.AdUnitIt;
import com.imooc.ad.entity.unit_condition.AdUnitKeyword;
import com.imooc.ad.entity.unit_condition.CreativeUnit;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DumpDataService {
    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitDistrictRepository unitDistrictRepository;
    @Autowired
    private AdUnitItRepository unitItRepository;
    @Autowired
    private AdUnitKeywordRepository unitKeywordRepository;

    @Test
    public void dumpAdTableData(){
        dumpAdPlanTable(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_PLAN));
        dumpAdUnitTable(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT));
        dumpAdCreativeTable(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_CREATIVE));
        dumpAdCreativeUnitTable(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_CREATIVE_UNIT));
        dumpAdUnitDistrictTable(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_DISTRICT));
        dumpAdUnitItTable(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_IT));
        dumpAdUnitKeywordTable(String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_KEYWORD));
    }

    private void dumpAdPlanTable(String fileName){
        List<AdPlan> adPlans = planRepository.findAllByPlanStatus(CommonStatus.VALID.getStatus());
        if (CollectionUtils.isEmpty(adPlans)){
            return;
        }
        List<AdPlanTable> planTables = new ArrayList<>();
        adPlans.forEach(p->planTables.add(new AdPlanTable(p.getId(),p.getUserId(),p.getPlanStatus()
                ,p.getStartDate(),p.getEndDate())));
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (AdPlanTable planTable:planTables){
                writer.write(JSON.toJSONString(planTable));
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void dumpAdUnitTable(String fileName){
        List<AdUnit> adUnits = unitRepository.findAllByUnitStatus(CommonStatus.VALID.getStatus());
        if (CollectionUtils.isEmpty(adUnits)){
            return;
        }
        List<AdUnitTable> unitTables = new ArrayList<>();
        adUnits.forEach(u->unitTables.add(new AdUnitTable(u.getId(),u.getUnitStatus(),u.getPositionType(),u.getPlanId())));
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (AdUnitTable unitTable:unitTables){
                writer.write(JSON.toJSONString(unitTable));
                writer.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void dumpAdCreativeTable(String fileName){
        List<Creative> creatives = creativeRepository.findAll();
        if (CollectionUtils.isEmpty(creatives)){
            return;
        }
        List<AdCreativeTable> creativeTables = new ArrayList<>();
        creatives.forEach(c->creativeTables.add(new AdCreativeTable(c.getId(),c.getName(),c.getType()
                ,c.getMaterialType(),c.getHeight(),c.getWidth(),c.getAuditStatus(),c.getUrl())));
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (AdCreativeTable creativeTable :creativeTables){
                writer.write(JSON.toJSONString(creativeTable));
                writer.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void dumpAdCreativeUnitTable(String fileName){
        List<CreativeUnit> creativeUnits = creativeUnitRepository.findAll();
        if (CollectionUtils.isEmpty(creativeUnits)){
            return;
        }
        List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
        creativeUnits.forEach(c->creativeUnitTables.add(new AdCreativeUnitTable(c.getId(),c.getUnitId())));
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (AdCreativeUnitTable creativeUnitTable:creativeUnitTables){
                writer.write(JSON.toJSONString(creativeUnitTable));
                writer.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void dumpAdUnitDistrictTable(String fileName){
        List<AdUnitDistrict> unitDistricts = unitDistrictRepository.findAll();
        if (CollectionUtils.isEmpty(unitDistricts)){
            return;
        }
        List<AdUnitDistrictTable> unitDistrictTables = new ArrayList<>();
        unitDistricts.forEach(u->unitDistrictTables.add(new AdUnitDistrictTable(u.getUnitId(),u.getProvince(),u.getCity())));
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (AdUnitDistrictTable creativeUnitTable:unitDistrictTables){
                writer.write(JSON.toJSONString(creativeUnitTable));
                writer.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void dumpAdUnitItTable(String fileName){
        List<AdUnitIt> unitIts = unitItRepository.findAll();
        if (CollectionUtils.isEmpty(unitIts)){
            return;
        }
        List<AdUnitItTable> unitItTables = new ArrayList<>();
        unitIts.forEach(u->unitItTables.add(new AdUnitItTable(u.getUnitId(),u.getItTag())));
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (AdUnitItTable unitItTable:unitItTables){
                writer.write(JSON.toJSONString(unitItTable));
                writer.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void dumpAdUnitKeywordTable(String fileName){
        List<AdUnitKeyword> unitKeywords = unitKeywordRepository.findAll();
        if (CollectionUtils.isEmpty(unitKeywords)){
            return;
        }
        List<AdUnitKeywordTable> unitKeywordTables = new ArrayList<>();
        unitKeywords.forEach(u->unitKeywordTables.add(new AdUnitKeywordTable(u.getUnitId(),u.getKeyword())));
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (AdUnitKeywordTable unitKeywordTable:unitKeywordTables){
                writer.write(JSON.toJSONString(unitKeywordTable));
                writer.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

