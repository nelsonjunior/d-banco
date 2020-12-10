package br.com.nrsjnet.dbanco.conta.producer;

import br.com.nrsjnet.dbanco.conta.dominio.dto.ContaCadastradaDTO;
import br.com.nrsjnet.dbanco.conta.dominio.entidade.Conta;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ContaProducer {

    private static Logger logger = LoggerFactory.getLogger(ContaProducer.class);

    @Value("${topic.cadastro.conta.producer}")
    private String topicName;

    private final KafkaTemplate<String, ContaCadastradaDTO> contaKafkaTemplate;

    private final ModelMapper modelMapper;

    public ContaProducer(KafkaTemplate<String, ContaCadastradaDTO> contaKafkaTemplate, ModelMapper modelMapper) {
        this.contaKafkaTemplate = contaKafkaTemplate;
        this.modelMapper = modelMapper;
    }

    public void enviar(Conta conta) {
        logger.info("Payload enviado: " + conta);
        contaKafkaTemplate.send(topicName, modelMapper.map(conta, ContaCadastradaDTO.class));
    }

}
