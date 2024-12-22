package ie.atu.taskmanagementregistration.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Bean
    public Queue logSendNotificationQueue() {
        return new Queue("logSendNotificationQueue", false);
    }

    @Bean
    public Queue logRecNotificationQueue() {
        return new Queue("logRecNotificationQueue", false);
    }

    @Bean
    public Queue regRecNotificationQueue() {
        return new Queue("regRecNotificationQueue", false);
    }

    @Bean
    public Queue regSendNotificationQueue() {
        return new Queue("regSendNotificationQueue", false);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}