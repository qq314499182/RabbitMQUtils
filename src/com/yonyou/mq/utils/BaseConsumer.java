package com.yonyou.mq.utils;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

import java.io.UnsupportedEncodingException;

/**
 * @Auther: zhaokep
 * @Date: 2018/5/16 10:33
 * @Description:
 */
public class BaseConsumer extends UltimateConsumer{


    BaseConsumer(String queue) {
        super(queue);
    }

    BaseConsumer(String topic,String exchange) {
        super(topic,exchange);
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] body)  {
        try {
            String message = new String(body,"UTF-8");
            System.out.println(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
