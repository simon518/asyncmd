/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.utils.convert;

import com.alibaba.fastjson.JSON;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynCmdDO;
import com.asyncmd.model.AsynCmdHistoryDO;
import com.asyncmd.utils.AsynExecuterUtil;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynCmdConvert.java, v 0.1 2019年07月09日 下午9:02 wangwendi Exp $
 */
public class AsynCmdConvert {

    public static AsynCmdDO toDo(AsynCmd asynCmd){
        if (asynCmd == null){
            return null;
        }
        AsynCmdDO asynCmdDO = new AsynCmdDO();
        asynCmdDO.setCmdId(asynCmd.getCmdId());
        asynCmdDO.setCmdType(asynCmd.getCmdType());
        asynCmdDO.setBizId(asynCmd.getBizId());
        asynCmdDO.setContent(asynCmd.objectToJson(asynCmd.getContent()));
        asynCmdDO.setGmtCreate(asynCmd.getGmtCreate());
        asynCmdDO.setGmtModify(asynCmd.getGmtModify());
        asynCmdDO.setExecuteNum(asynCmd.getExecuteNum());
        asynCmdDO.setNextTime(asynCmd.getNextTime());
        asynCmdDO.setStatus(asynCmd.getStatus());
        asynCmdDO.setCreateHostname(asynCmd.getCreateHostname());
        asynCmdDO.setCreateIp(asynCmd.getCreateIp());
        asynCmdDO.setCreateName(asynCmd.getCreateName());
        asynCmdDO.setUpdateHostname(asynCmd.getUpdateHostname());
        asynCmdDO.setUpdateIp(asynCmd.getUpdateIp());
        asynCmdDO.setSuccessExecuters(JSON.toJSONString(asynCmd.getSuccessExecuters()));
        return asynCmdDO;
    }
    public static List<AsynCmd> toCmdList(List<AsynCmdDO> asynCmdDOs){
        if (CollectionUtils.isEmpty(asynCmdDOs)){
            return Lists.newArrayList();
        }
        List<AsynCmd> asynCmdList = Lists.newArrayList();
        for (AsynCmdDO asynCmdDO : asynCmdDOs){
            Class<? extends AsynCmd> asynClass = AsynExecuterUtil.getAsynCmdNameMapping().get(asynCmdDO.getCmdType());
            asynCmdList.add(toCmd(asynCmdDO, asynClass));
        }
        return asynCmdList;

    }
    public static AsynCmd toCmd(AsynCmdDO asynCmdDO,Class<? extends AsynCmd>asynCmdClass){
        try {
            if (asynCmdDO == null){
                return null;
            }
            AsynCmd asynCmd = asynCmdClass.newInstance();
            asynCmd.setCmdId(asynCmdDO.getCmdId());
            asynCmd.setCmdType(asynCmdDO.getCmdType());
            asynCmd.setBizId(asynCmdDO.getBizId());
            asynCmd.setGmtCreate(asynCmdDO.getGmtCreate());
            asynCmd.setExecuteNum(asynCmdDO.getExecuteNum());
            asynCmd.setNextTime(asynCmdDO.getNextTime());
            asynCmd.setStatus(asynCmdDO.getStatus());
            asynCmd.setCreateHostname(asynCmdDO.getCreateHostname());
            asynCmd.setCreateIp(asynCmdDO.getCreateIp());
            asynCmd.setCreateName(asynCmdDO.getCreateName());
            asynCmd.setUpdateHostname(asynCmdDO.getUpdateHostname());
            asynCmd.setUpdateIp(asynCmdDO.getUpdateIp());
            asynCmd.setGmtModify(asynCmdDO.getGmtModify());
            asynCmd.setContent(asynCmd.jsonToObject(asynCmdDO.getContent()));
            asynCmd.setSuccessExecuters(JSON.parseObject(asynCmdDO.getSuccessExecuters(),ArrayList.class));
            return asynCmd;
        }catch (Exception e){
            throw new AsynException(AsynExCode.ILLEGAL);
        }
    }
    public static AsynCmdHistoryDO toHistoryCmd(AsynCmd asynCmd){
        AsynCmdHistoryDO asynCmdHistoryDO = new AsynCmdHistoryDO();
        asynCmdHistoryDO.setCmdId(asynCmd.getCmdId());
        asynCmdHistoryDO.setCmdType(asynCmd.getCmdType());
        asynCmdHistoryDO.setBizId(asynCmd.getBizId());
        asynCmdHistoryDO.setContent(String.valueOf(asynCmd.getContent()));
        asynCmdHistoryDO.setGmtCreate(new Date());
        asynCmdHistoryDO.setGmtModify(new Date());
        asynCmdHistoryDO.setExecuteNum(asynCmd.getExecuteNum());
        asynCmdHistoryDO.setNextTime(asynCmd.getNextTime());
        asynCmdHistoryDO.setStatus(AsynStatus.SUCCESS.getStatus());
        asynCmdHistoryDO.setCreateHostname(asynCmd.getCreateHostname());
        asynCmdHistoryDO.setCreateIp(asynCmd.getCreateIp());
        asynCmdHistoryDO.setCreateName(asynCmd.getCreateName());
        asynCmdHistoryDO.setUpdateHostname(asynCmd.getUpdateHostname());
        asynCmdHistoryDO.setUpdateIp(asynCmd.getUpdateIp());
        asynCmdHistoryDO.setSuccessExecuters(JSON.toJSONString(asynCmd.getSuccessExecuters()));
        return asynCmdHistoryDO;

    }

    public static List<String> toBizIds(List<AsynCmd> asynCmdList){
        if (CollectionUtils.isEmpty(asynCmdList)){
            return Lists.newArrayList();
        }
        List<String> bizIds = Lists.newArrayList();
        for (AsynCmd asynCmd : asynCmdList){
            bizIds.add(asynCmd.getBizId());
        }
        return bizIds;
    }


}