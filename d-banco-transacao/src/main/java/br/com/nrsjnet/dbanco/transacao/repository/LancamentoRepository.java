package br.com.nrsjnet.dbanco.transacao.repository;


import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, UUID> {

}
