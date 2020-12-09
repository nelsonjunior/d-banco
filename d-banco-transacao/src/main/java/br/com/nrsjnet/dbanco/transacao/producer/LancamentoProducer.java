package br.com.nrsjnet.dbanco.transacao.producer;

import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Lancamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LancamentoProducer {

    private static Logger logger = LoggerFactory.getLogger(LancamentoProducer.class);

    @Value("${topic.lancamento.producer}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void enviar(List<Lancamento> lancamentos) {
        lancamentos.forEach(lancamento -> {
            logger.info("Payload enviado: " + lancamento);
            kafkaTemplate.send(topicName, lancamento);
        });
    }
}
