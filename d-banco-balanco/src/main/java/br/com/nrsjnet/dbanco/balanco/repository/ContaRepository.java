package br.com.nrsjnet.dbanco.balanco.repository;


import br.com.nrsjnet.dbanco.balanco.dominio.entidade.Conta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContaRepository extends MongoRepository<Conta, UUID> {

    Optional<Conta> findByCpf(String cpf);

}
