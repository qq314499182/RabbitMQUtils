package com.yonyou.mq.utils;

import com.rabbitmq.client.*;
import jdk.nashorn.internal.runtime.options.LoggingOption;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: zhaokep
 * @Date: 2018/5/11 08:53
 * @Description: MQ数据操作工具类
 */
public class MQUtils {

    private static ExecutorService service = Executors.newFixedThreadPool(10);

    private static Logger logger = Logger.getLogger(MQUtils.class);

    /**
     * @return Connection 连接对象
     * @throws Exception IO异常,连接超时异常
     */
    private static Connection getConnection() throws Exception {
        logger.info("开始读取ＭＱ服务器配置信息");
        Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        InputStream in = MQUtils.class.getClassLoader().getResourceAsStream("config-mq.properties");
        properties.load(in);
        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setRequestedHeartbeat(20);
        factory.setHost(host);
        factory.setPort(Integer.parseInt(port));
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setSharedExecutor(service);

        // 网络异常自动连接恢复
        factory.setAutomaticRecoveryEnabled(true);
        //每10秒尝试重试连接一次
        factory.setNetworkRecoveryInterval(20);
        //创建连接
        Connection connection = factory.newConnection();
        logger.info("与MQ服务器连接创建成功");
        return connection;
    }

    private static Integer counnt = 0;

    /**
     * 向MQ发送JSON数据 (点对点模式)
     *
     * @param connection 连接对象
     * @param queueName  队列名称
     * @param JSON       传递数据
     */
    public static void sendByDeclare(String queueName, String JSON) {
        try {
            Connection connection = getConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicPublish("", queueName, null, JSON.getBytes());
            logger.info("生产者启动,当前模式为[[点对点模式]]");
            logger.info("生产者向 队列 ["+queueName+"] 发送消息成功");
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从MQ中取出JSON数据 (点对点模式)
     * @param connection 连接对象
     * @param queueName  队列名称
     * @return String
     */
    public static void receiveByDeclare(EnhanceConsumer consumer) {
        try {
            Channel channel = getConnection().createChannel();
            channel.queueDeclare(consumer.getQueue(), true, false, false, null);
            logger.info(" 消费者启动,当前模式[[点对点模式]] : 等待接收队列["+ consumer.getQueue() +"]消息");
            channel.basicConsume(consumer.getQueue(), true, consumer);
            channel.addShutdownListener(new ShutdownListener() {
                @Override
                public void shutdownCompleted(ShutdownSignalException e) {
                    logger.error("MQ服务器异常");
                }
            });
           new CheckConsumerDownThread(consumer, channel).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向MQ发送JSON数据 (广播模式)
     * @param connection
     * @param queueName
     * @param JSON
     */
    public static void sendByFanout(String exchangeName, String JSON)  {
        try {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName,"fanout");
        channel.basicPublish(exchangeName, "", null, JSON.getBytes());
        logger.info("生产者启动,当前模式为[[广播模式]]");
        logger.info("生产者向交换机 ["+exchangeName+"] 发送消息成功");
        channel.close();
        connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从MQ中取出JSON数据 (广播模式)
     * @param consumer
     */
    public static void receiveByFanout(EnhanceConsumer consumer){
        try {
            Channel channel = getConnection().createChannel();
            channel.exchangeDeclare(consumer.getExchange(), "fanout");
            channel.queueBind(consumer.getQueue(), consumer.getExchange(), "");
            logger.info("已将队列[["+consumer.getExchange()+"]]与队列[["+consumer.getQueue()+"]]进行绑定");
            logger.info(" 消费者启动,当前模式为[[广播模式]] : 等待接收队列["+ consumer.getQueue() +"]消息");
            channel.basicConsume(consumer.getQueue(), true, consumer);
            channel.addShutdownListener(new ShutdownListener() {
                @Override
                public void shutdownCompleted(ShutdownSignalException e) {
                    logger.error("MQ服务器异常");
                }
            });
            new CheckConsumerDownThread(consumer, channel).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
