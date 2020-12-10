package br.com.nrsjnet.dbanco.transacao.configuracao;

import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Lancamento;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public KafkaTemplate<String, Lancamento> lancamentoKafkaTemplate() {
        return new KafkaTemplate<>(lancamentoProducerFactory());
    }

    @Bean
    public ProducerFactory<String, Lancamento> lancamentoProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerProps());
    }

    @Bean
    public KafkaTemplate<String, Conta> contaKafkaTemplate() {
        return new KafkaTemplate<>(contaProducerFactory());
    }

    @Bean
    public ProducerFactory<String, Conta> contaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerProps());
    }


    private Map<String, Object> getProducerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }
}
