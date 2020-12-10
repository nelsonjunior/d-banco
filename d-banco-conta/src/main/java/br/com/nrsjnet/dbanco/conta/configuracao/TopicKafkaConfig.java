package br.com.nrsjnet.dbanco.conta.configuracao;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicKafkaConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${topic.cadastro.conta.producer}")
    private String topicAtualizacaoConta;

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name(topicAtualizacaoConta)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
