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

    @KafkaListener(topics = "${topic.lancamento.consumer}", groupId = "group_id"
        , properties = "spring.kafka.consumer.properties.spring.json.value.default.type=br.com.nrsjnet.dbanco.balanco.dominio.entidade.Lancamento\n")
    public void consume(ConsumerRecord<String, Lancamento> payload) {

        log.info("Receber informação cadastro contata" + payload);

        Lancamento lancamento = modelMapper.map(payload.value(), Lancamento.class);

        lancamentoRepository.save(lancamento);

    }
}
