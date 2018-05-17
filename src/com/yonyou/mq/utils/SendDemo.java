package com.yonyou.mq.utils;

import org.junit.Test;

/**
 * @Auther: zhaokep
 * @Date: 2018/5/15 10:32
 * @Description:
 */
public class SendDemo {

    @Test
    public void sendMq() {
        String json = "你好";
        MQUtils.sendByFanout("occ-exchange",json);

    }
}
