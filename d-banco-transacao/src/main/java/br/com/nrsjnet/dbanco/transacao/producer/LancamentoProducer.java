package br.com.nrsjnet.dbanco.transacao.producer;

import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Lancamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LancamentoProducer {

    private static Logger logger = LoggerFactory.getLogger(LancamentoProducer.class);

    @Value("${topic.lancamento.conta.producer}")
    private String topicName;

    private final KafkaTemplate<String, Lancamento> lancamentoKafkaTemplate;

    public LancamentoProducer(KafkaTemplate<String, Lancamento> lancamentoKafkaTemplate) {
        this.lancamentoKafkaTemplate = lancamentoKafkaTemplate;
    }

    public void enviar(List<Lancamento> lancamentos) {
        lancamentos.forEach(lancamento -> {
            logger.info("Payload enviado: " + lancamento);
            lancamentoKafkaTemplate.send(topicName, lancamento);
        });
    }
}
