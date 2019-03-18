package com.space.rbq.store.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfigForPay {
    @Bean(name = "payConnectionFactory")
    public ConnectionFactory hospSyncConnectionFactory(
            @Value("${spring.rabbitmq.pay.host}") String host,
            @Value("${spring.rabbitmq.pay.port}") int port,
            @Value("${spring.rabbitmq.pay.username}") String username,
            @Value("${spring.rabbitmq.pay.password}") String password,
            @Value("${spring.rabbitmq.pay.virtual-host}") String virtualHost) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        if (virtualHost != null & virtualHost.trim().length() > 0) {
            connectionFactory.setVirtualHost(virtualHost);
        }
        return connectionFactory;
    }

    /**
     * ContainerFactory必须与监听RabbitListener关联，才会起作用
     *
     * @param configurer
     * @param connectionFactory
     * @return
     * @RabbitListener(queues = {QUEUE_NAME1},containerFactory = "hospSyncContainerFactory")
     */
    @Bean(name = "payContainerFactory")
    public SimpleRabbitListenerContainerFactory hospSyncFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("payConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //公平调度-prefetch与消息投递
        //https://blog.csdn.net/qq_41599820/article/details/88077497
        //通过设置Qos的prefetch count来控制consumer的流量。同时设置得当也会提高consumer的吞吐量。
        //prefetch允许为每个consumer指定最大的unacked messages数目。
        // 简单来说就是用来指定一个consumer一次可以从Rabbit中获取多少条message并缓存在client中(RabbitMQ提供的各种语言的client library)。
        // 一旦缓冲区满了，Rabbit将会停止投递新的message到该consumer中直到它发出ack。
        //关键业务-建议设置为1，保证均匀地分发消息
        factory.setPrefetchCount(1);
        //对比
        //无ack模式：效率高，存在丢失大量消息的风险。
        //有ack模式：效率低，不会丢消息。
        //factory.setAcknowledgeMode(AcknowledgeMode.NONE);//效率高，推送出去的消息不会暂存在server端
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//效率低，不会丢消息目前推荐此配置。
        //factory.setAcknowledgeMode(AcknowledgeMode.AUTO);//效率低
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
