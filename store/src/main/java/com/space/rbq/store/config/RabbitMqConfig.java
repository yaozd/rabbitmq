package com.space.rbq.store.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author zhuzhe
 * @date 2018/6/7 17:59
 * @email 1529949535@qq.com
 */
@Configuration
public class RabbitMqConfig {
    @Bean(name = "hospSyncConnectionFactory")
    @Primary
    public ConnectionFactory hospSyncConnectionFactory(
            @Value("${spring.rabbitmq.hospSync.host}") String host,
            @Value("${spring.rabbitmq.hospSync.port}") int port,
            @Value("${spring.rabbitmq.hospSync.username}") String username,
            @Value("${spring.rabbitmq.hospSync.password}") String password,
            @Value("${spring.rabbitmq.hospSync.virtual-host}") String virtualHost) {
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

    @Bean(name = "hospSyncRabbitTemplate")
    public RabbitTemplate firstRabbitTemplate(@Qualifier("hospSyncConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate hospSyncRabbitTemplate = new RabbitTemplate(connectionFactory);
        //使用外部事物
        //ydtRabbitTemplate.setChannelTransacted(true);
        return hospSyncRabbitTemplate;
    }

    /**
     * ContainerFactory必须与监听RabbitListener关联，才会起作用
     *
     * @param configurer
     * @param connectionFactory
     * @return
     * @RabbitListener(queues = {QUEUE_NAME1},containerFactory = "hospSyncContainerFactory")
     */
    @Bean(name = "hospSyncContainerFactory")
    public SimpleRabbitListenerContainerFactory hospSyncFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("hospSyncConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
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
