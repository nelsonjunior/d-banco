package br.com.nrsjnet.dbanco.balanco.repository;


import br.com.nrsjnet.dbanco.balanco.dominio.entidade.Conta;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContaReactiveRepository extends ReactiveMongoRepository<Conta, UUID> {

    Mono<Conta> findByCpf(Mono<String> cpf);

}
