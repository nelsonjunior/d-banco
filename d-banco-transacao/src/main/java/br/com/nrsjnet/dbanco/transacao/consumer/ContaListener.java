package br.com.nrsjnet.dbanco.transacao.consumer;

import br.com.nrsjnet.dbanco.transacao.dominio.dto.ContaDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.repository.ContaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContaListener {

    private final ContaRepository contaRepository;

    private final ModelMapper modelMapper;

    public ContaListener(ContaRepository contaRepository, ModelMapper modelMapper) {
        this.contaRepository = contaRepository;
        this.modelMapper = modelMapper;
    }

    @KafkaListener(topics = "${topic.cadastro.conta.consumer}"
            , groupId = "${topic.group-id}"
            , containerFactory = "contaKafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, ContaDTO> payload){

        log.info("Receber informação cadastro contata" + payload);

        Conta conta = modelMapper.map(payload.value(), Conta.class);

        contaRepository.save(conta);

    }
}
