package br.com.nrsjnet.dbanco.balanco.apresentacao;

import br.com.nrsjnet.dbanco.balanco.dominio.dto.RetornoContaDTO;
import br.com.nrsjnet.dbanco.balanco.dominio.dto.RetornoLancamentoDTO;
import br.com.nrsjnet.dbanco.balanco.service.ContaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(
        value = Paths.CONTAS,
        description = "Gerir Conta",
        consumes="application/json",
        produces="application/json"
)
@RestController
@RequestMapping(value = Paths.CONTAS,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ContaResource {

    private final ContaService contaService;

    public ContaResource(ContaService contaService) {
        this.contaService = contaService;
    }

    @ApiOperation( value = "Obter informacoes da conta corrente")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Retorna a conta salva"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção inesperada")
    })
    @GetMapping("{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public RetornoContaDTO obter(@PathVariable("cpf") String cpf){
        return this.contaService.obter(cpf);
    }

    @ApiOperation( value = "Listar contas correntes")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Retorna lista de contas salvas"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção inesperada")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RetornoContaDTO> listar(){
        return this.contaService.listar();
    }

    @ApiOperation( value = "Retorna lançamentos da conta corrente informada")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Retorna lançamentos da conta corrente informada"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção inesperada")
    })
    @GetMapping("{cpf}/extrato")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornoLancamentoDTO> extrato(@PathVariable("cpf") String cpf){
        return this.contaService.obterExtrato(cpf);
    }

}
