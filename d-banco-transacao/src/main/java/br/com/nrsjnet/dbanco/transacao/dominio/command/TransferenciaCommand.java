package br.com.nrsjnet.dbanco.transacao.dominio.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransferenciaCommand implements Command {

    @NotBlank(message = "CPF origem é obrigatório")
    private String cpfOrigem;

    @NotBlank(message = "CPF destino é obrigatório")
    private String cpfDestino;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.00", message = "Valor deve ser maior que 0")
    private BigDecimal valor;
}
