/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.config;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.manager.job.DispatchExecuterJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.quartz.CronExpression;
import org.springframework.util.StringUtils;

/**
 * @author wangwendi
 * @version $Id: AsynJobConfig.java, v 0.1 2019年07月16日 上午11:24 wangwendi Exp $
 */
public class AsynJobConfig {
    /**
     * zookeeper地址
     */
    private String zookeeperUrl;
    /**
     * 调度任务名称 不同应用需要不同
     */
    private String jobName;
    /**
     * 任务执行频率 默认每隔3秒执行一次
     */
    private String cron = "0/3 * * * * ?";


    public void init(int tableNum){

        try {
            //效验cron表达式是否正确
            CronExpression.validateExpression(cron);
        }catch (Exception e){
            throw new AsynException(AsynExCode.CRON_ILLEGAL);
        }

        //如果没有分表 则设置1个分片
        if (tableNum == 0){
            tableNum = 1;
        }
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, tableNum).build();
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, DispatchExecuterJob.class.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        new JobScheduler(createRegistryCenter(zookeeperUrl), simpleJobRootConfig).init();

    }

    private static CoordinatorRegistryCenter createRegistryCenter(String zookeeperUrl) {
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration(zookeeperUrl, "asyn-executer"));
        regCenter.init();
        return regCenter;
    }

    public void setZookeeperUrl(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}