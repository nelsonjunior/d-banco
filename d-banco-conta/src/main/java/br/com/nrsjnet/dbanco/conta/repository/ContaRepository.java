package br.com.nrsjnet.dbanco.conta.repository;

import br.com.nrsjnet.dbanco.conta.dominio.entidade.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContaRepository extends JpaRepository<Conta, UUID> {

    Optional<Conta> findByCpf(String cpf);

}
