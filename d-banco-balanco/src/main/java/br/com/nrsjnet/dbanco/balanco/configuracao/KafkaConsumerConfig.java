package br.com.nrsjnet.dbanco.balanco.configuracao;

import br.com.nrsjnet.dbanco.balanco.dominio.dto.ContaDTO;
import br.com.nrsjnet.dbanco.balanco.dominio.dto.LancamentoDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${topic.group-id}")
    private String groupId;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ContaDTO> contaKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ContaDTO> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(contaConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ContaDTO> contaConsumerFactory() {
        Map<String, Object> props = propriedadesComuns();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ContaDTO.class.getCanonicalName());
        return new DefaultKafkaConsumerFactory<>(props);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LancamentoDTO> lancamentoKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LancamentoDTO> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(lancamentoConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, LancamentoDTO> lancamentoConsumerFactory() {
        Map<String, Object> props = propriedadesComuns();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LancamentoDTO.class.getCanonicalName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    private Map<String, Object> propriedadesComuns() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }
}
