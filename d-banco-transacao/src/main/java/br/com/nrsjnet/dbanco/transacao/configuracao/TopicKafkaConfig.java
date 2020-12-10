package br.com.nrsjnet.dbanco.transacao.configuracao;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicKafkaConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${topic.lancamento.conta.producer}")
    private String topicLancamentoConta;

    @Value(value = "${topic.atualizacao.conta.producer}")
    private String topicAtualizacaoConta;

    @Bean
    public NewTopic generalTopic() {
        return TopicBuilder.name(topicLancamentoConta)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name(topicAtualizacaoConta)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
