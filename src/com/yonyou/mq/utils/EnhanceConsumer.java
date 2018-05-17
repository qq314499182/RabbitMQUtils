package com.yonyou.mq.utils;

import com.rabbitmq.client.Consumer;

/**
 * @Auther: zhaokep
 * @Date: 2018/5/15 20:41
 * @Description:
 */
public interface EnhanceConsumer extends Consumer {

     String getQueue() ;

     String getExchange();

}
