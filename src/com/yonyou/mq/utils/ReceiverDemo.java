package com.yonyou.mq.utils;

/**
 * @Auther: zhaokep
 * @Date: 2018/5/15 09:49
 * @Description:
 */
public class ReceiverDemo {

    public static void main(String[] args) {
        try {
            MQUtils.receiveByFanout(new BaseConsumer("occ","occ-exchange"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
