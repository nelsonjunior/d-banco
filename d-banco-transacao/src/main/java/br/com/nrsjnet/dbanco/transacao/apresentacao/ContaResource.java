package br.com.nrsjnet.dbanco.transacao.apresentacao;


import br.com.nrsjnet.dbanco.transacao.dominio.command.DepositarCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.SaqueCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.TransferenciaCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransferenciaDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.service.LancamentoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    private final LancamentoService lancamentoService;

    public ContaResource(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @ApiOperation( value = "Depositar valor em conta")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Retorna informacoes referente a deposito em conta"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção inesperada")
    })
    @PostMapping(path = "/depositar")
    @ResponseStatus(HttpStatus.CREATED)
    public TransacaoDTO depositar(@Valid @RequestBody DepositarCommand dto) {
        return this.lancamentoService.depositar(dto);
    }

    @ApiOperation( value = "Sacar valor em conta")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Retorna informacoes referente a saque em conta"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção inesperada")
    })
    @PostMapping(path = "/sacar")
    @ResponseStatus(HttpStatus.CREATED)
    public TransacaoDTO sacar(@Valid @RequestBody SaqueCommand dto) {
        return this.lancamentoService.sacar(dto);
    }

    @ApiOperation( value = "Transferir valor entre contas")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Retorna informacoes referente a transfência entre contas"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção inesperada")
    })
    @PostMapping(path = "/transferir")
    @ResponseStatus(HttpStatus.CREATED)
    public TransferenciaDTO transferir(@Valid @RequestBody TransferenciaCommand dto) {
        return this.lancamentoService.transferir(dto);
    }
}
