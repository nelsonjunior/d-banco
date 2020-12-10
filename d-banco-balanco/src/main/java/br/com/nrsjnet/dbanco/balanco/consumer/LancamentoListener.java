package br.com.nrsjnet.dbanco.balanco.consumer;

import br.com.nrsjnet.dbanco.balanco.dominio.entidade.Lancamento;
import br.com.nrsjnet.dbanco.balanco.repository.LancamentoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LancamentoListener {

    private final LancamentoRepository lancamentoRepository;

    private final ModelMapper modelMapper;

    public LancamentoListener(LancamentoRepository lancamentoRepository, ModelMapper modelMapper) {
        this.lancamentoRepository = lancamentoRepository;
        this.modelMapper = modelMapper;
    }

    @KafkaListener(topics = "${topic.lancamento.conta.consumer}"
            , groupId = "${topic.group-id}"
            , containerFactory = "lancamentoKafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, Lancamento> payload) {

        log.info("Receber informação lancamentos" + payload);

        Lancamento lancamento = modelMapper.map(payload.value(), Lancamento.class);

        lancamentoRepository.save(lancamento);

    }
}