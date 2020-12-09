package br.com.nrsjnet.dbanco.balanco.repository;


import br.com.nrsjnet.dbanco.balanco.dominio.entidade.Lancamento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LancamentoRepository extends MongoRepository<Lancamento, UUID> {

}
