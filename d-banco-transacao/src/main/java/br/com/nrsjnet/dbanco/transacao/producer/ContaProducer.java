package br.com.nrsjnet.dbanco.transacao.producer;

import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaProducer {

    private static Logger logger = LoggerFactory.getLogger(ContaProducer.class);

    @Value("${topic.atualizacao.conta.producer}")
    private String topicName;

    private final KafkaTemplate<String, Conta> contaKafkaTemplate;

    public ContaProducer(KafkaTemplate<String, Conta> contaKafkaTemplate) {
        this.contaKafkaTemplate = contaKafkaTemplate;
    }

    public void enviar(List<Conta> contas) {
        contas.forEach(conta -> {
            logger.info("Payload enviado: " + conta);
            contaKafkaTemplate.send(topicName, conta);
        });
    }
}
