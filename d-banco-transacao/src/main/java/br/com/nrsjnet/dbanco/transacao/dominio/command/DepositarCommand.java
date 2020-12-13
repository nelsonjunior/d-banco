package br.com.nrsjnet.dbanco.transacao.dominio.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DepositarCommand implements Command{

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.00", message = "Valor deve ser maior que 0")
    private BigDecimal valor;
}
