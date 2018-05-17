package com.yonyou.mq.utils;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @Auther: zhaokep
 * @Date: 2018/5/15 20:27
 * @Description:
 */
public class UltimateConsumer implements EnhanceConsumer {

     String queue;

     String exchange;

      public UltimateConsumer(String queue) {
        this.queue = queue;
    }

    public UltimateConsumer(String queue,String exchange) {
        this.queue = queue;
        this.exchange = exchange;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    @Override
    public void handleConsumeOk(String s) {

    }

    @Override
    public void handleCancelOk(String s) {

    }

    @Override
    public void handleCancel(String s) {

    }

    @Override
    public void handleShutdownSignal(String s, ShutdownSignalException e) {

    }

    @Override
    public void handleRecoverOk(String s) {

    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] body){
    }
}
