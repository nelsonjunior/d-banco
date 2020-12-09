package br.com.nrsjnet.dbanco.transacao.dominio.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DepositarCommand implements Command{

    private String cpf;
    private BigDecimal valor;
}
