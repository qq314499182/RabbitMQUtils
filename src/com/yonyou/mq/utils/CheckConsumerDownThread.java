package com.yonyou.mq.utils;

import com.rabbitmq.client.Channel;
import org.apache.log4j.Logger;

/**
 * @Auther: zhaokep
 * @Date: 2018/5/15 20:53
 * @Description:
 */
public class CheckConsumerDownThread  implements  Runnable {

    private EnhanceConsumer consumer;

    private static Logger logger = Logger.getLogger(CheckConsumerDownThread.class);

    private Channel channel;

    public CheckConsumerDownThread(EnhanceConsumer consumer, Channel channel) {
        this.consumer = consumer;
        this.channel = channel;
    }
    @Override
    public void run() {
        while(true) {
            if (!channel.isOpen()) {
                logger.info("开始重新连接MQ服务");
                MQUtils.receiveByDeclare(consumer);
                logger.info("MQ服务连接恢复");

            } else {
                try {
                    Thread.sleep(30 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
