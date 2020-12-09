package br.com.nrsjnet.dbanco.transacao.dominio.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransferenciaCommand implements Command{

    private String cpfOrigem;
    private String cpfDestino;
    private BigDecimal valor;
}
