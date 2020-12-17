package br.com.nrsjnet.dbanco.balanco.apresentacao;

import br.com.nrsjnet.dbanco.balanco.dominio.dto.RetornoContaDTO;
import br.com.nrsjnet.dbanco.balanco.service.ContaReactiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Api(
        value = Paths.CONTAS,
        description = "Gerir Conta",
        consumes="application/json",
        produces="application/json"
)
@RestController
@RequestMapping(value = Paths.CONTAS + "/reactive",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ContaReactiveResource {

    private final ContaReactiveService contaReactiveService;

    public ContaReactiveResource(ContaReactiveService contaReactiveService) {
        this.contaReactiveService = contaReactiveService;
    }

    @ApiOperation( value = "Obter informacoes do saldo da conta corrente")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Retorna do saldo da conta corrente"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção inesperada")
    })
    @GetMapping("{cpf}/saldo")
    @ResponseStatus(HttpStatus.OK)
    public Mono<RetornoContaDTO> obterSaldo(@PathVariable("cpf") String cpf){
        return this.contaReactiveService.obter(Mono.just(cpf));
    }

}
