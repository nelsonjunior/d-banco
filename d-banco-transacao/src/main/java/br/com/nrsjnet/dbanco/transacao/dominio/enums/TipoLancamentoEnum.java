package br.com.nrsjnet.dbanco.transacao.dominio.enums;

import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Lancamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

public enum TipoLancamentoEnum {

    DEPOSITO{
        @Override
        public BigDecimal valorBonus(BigDecimal valor) {
            return valor.multiply(new BigDecimal("0.005")).setScale(2, RoundingMode.HALF_DOWN);
        }

        @Override
        public BigDecimal valorTaxas(BigDecimal valor) {
            return BigDecimal.ZERO;
        }

        @Override
        public String mensagem() {
            return "Deposito de {0} com bônus de {1}";
        }
    },
    SAQUE{
        @Override
        public BigDecimal valorBonus(BigDecimal valor) {
            return BigDecimal.ZERO;
        }

        @Override
        public BigDecimal valorTaxas(BigDecimal valor) {
            return valor.multiply(new BigDecimal("0.01")).setScale(2, RoundingMode.HALF_DOWN);
        }

        @Override
        public String mensagem() {
            return "Saque de {0} com taxa de {1}";
        }
    },
    CREDITO_TRANSFERENCIA{
        @Override
        public BigDecimal valorBonus(BigDecimal valor) {
            return BigDecimal.ZERO;
        }

        @Override
        public BigDecimal valorTaxas(BigDecimal valor) {
            return BigDecimal.ZERO;
        }

        @Override
        public String mensagem() {
            return "Credito de {0} realizado por {1}";
        }
    },
    DEBITO_TRANSFERENCIA{
        @Override
        public BigDecimal valorBonus(BigDecimal valor) {
            return BigDecimal.ZERO;
        }

        @Override
        public BigDecimal valorTaxas(BigDecimal valor) {
            return BigDecimal.ZERO;
        }

        @Override
        public String mensagem() {
            return "Debito de {0} realizado para {1}";
        }
    };

    public Optional<Lancamento> obterLancamento(List<Lancamento> lancamentos){
        return lancamentos.stream().filter(l -> l.getTipoLancamento().equals(this)).findFirst();
    }

    public String obterMensagem(Object ...parametros){
        return MessageFormat.format(this.mensagem(), parametros);
    }

    public abstract String mensagem();

    public abstract BigDecimal valorBonus(BigDecimal valor);

    public abstract BigDecimal valorTaxas(BigDecimal valor);


}
