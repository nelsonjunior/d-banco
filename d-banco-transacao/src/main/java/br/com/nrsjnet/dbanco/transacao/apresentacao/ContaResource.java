package br.com.nrsjnet.dbanco.transacao.apresentacao;


import br.com.nrsjnet.dbanco.transacao.dominio.command.DepositarCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.SaqueCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.TransferenciaCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransferenciaDTO;
import br.com.nrsjnet.dbanco.transacao.service.LancamentoService;
import br.com.nrsjnet.dbanco.transacao.service.TransacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    private final TransacaoService transacaoService;

    private final LancamentoService lancamentoService;

    public ContaResource(TransacaoService transacaoService, LancamentoService lancamentoService) {
        this.transacaoService = transacaoService;
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
    public TransacaoDTO depositar(@RequestBody DepositarCommand dto) {
        return this.transacaoService.depositar(dto);
    }

    @ApiOperation( value = "Depositar valor em conta")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Retorna informacoes referente a deposito em conta"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção inesperada")
    })
    @PostMapping(path = "/depositarPorLancamneto")
    @ResponseStatus(HttpStatus.CREATED)
    public TransacaoDTO depositarPorLancamneto(@RequestBody DepositarCommand dto) {
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
    public TransacaoDTO sacar(@RequestBody SaqueCommand dto) {
        return this.transacaoService.sacar(dto);
    }

    @ApiOperation( value = "Transferir valor entre contas")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Retorna informacoes referente a transfência entre contas"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção inesperada")
    })
    @PostMapping(path = "/transferir")
    @ResponseStatus(HttpStatus.CREATED)
    public TransferenciaDTO transferir(@RequestBody TransferenciaCommand dto) {
        return this.transacaoService.transferir(dto);
    }
}
