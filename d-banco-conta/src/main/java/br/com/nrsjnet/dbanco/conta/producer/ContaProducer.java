package br.com.nrsjnet.dbanco.conta.producer;

import br.com.nrsjnet.dbanco.conta.dominio.entidade.Conta;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ContaProducer {

    private static Logger logger = LoggerFactory.getLogger(ContaProducer.class);

    @Value("${topic.conta.producer}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void enviar(Conta conta) {
        logger.info("Payload enviado: " + conta);
        kafkaTemplate.send(topicName, conta);
    }

}
