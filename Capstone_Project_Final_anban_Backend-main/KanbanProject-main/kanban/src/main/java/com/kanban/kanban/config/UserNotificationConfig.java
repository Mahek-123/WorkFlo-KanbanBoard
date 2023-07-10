package com.kanban.kanban.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserNotificationConfig {
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("user-notification-exchange");
    }

    @Bean
    public Queue registerQueue() {
        return new Queue("user-notification-queue");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Binding binding(DirectExchange exchange, Queue registerQueue) {
        return BindingBuilder.bind(registerQueue()).to(exchange).with("user-routing");
    }

//
//private String exchangeName="user-notification-exchange";
//    private String queueName="user-notification-queue";
//
//    @Bean
//    public DirectExchange getDirectExchange(){
//        return new DirectExchange(exchangeName);
//    }
//
//    @Bean
//    public Queue getQueue(){
//        return new Queue(queueName);
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter getJackson2JsonMessageConverter(){
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public RabbitTemplate getRabbitTemplate(final ConnectionFactory connectionFactory){
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(getJackson2JsonMessageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public Binding getBinding(){
//        return BindingBuilder.bind(getQueue()).to(getDirectExchange()).with("user-routing");
//    }

}
