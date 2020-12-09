package br.com.nrsjnet.dbanco.transacao.apresentacao;


import br.com.nrsjnet.dbanco.transacao.dominio.command.DepositarCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.SaqueCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.TransferenciaCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.NovaTransferenciaDTO;
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
        value = Paths.CONTAS_V2,
        description = "Gerir Conta",
        consumes="application/json",
        produces="application/json"
)
@RestController
@RequestMapping(value = Paths.CONTAS_V2,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ContaV2Resource {

    private final LancamentoService lancamentoService;

    public ContaV2Resource(LancamentoService lancamentoService) {
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
    public NovaTransferenciaDTO transferir(@RequestBody TransferenciaCommand dto) {
        return this.lancamentoService.transferir(dto);
    }
}
