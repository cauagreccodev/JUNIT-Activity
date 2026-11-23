package testCases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import busServices.Onibus;
import busServices.Passageiro;
import busServices.TipoDePoltrona;
import busServices.Viagem;

@DisplayName("ViagemTest - Testes Intraclasse (Particionamento por Categoria de Metodos)")
public class ViagemTest {

    private Viagem criarViagemPadrao() {
        Onibus onibus = new Onibus("XYZ-5678", "Paradiso G7", "Marcopolo", 2, 2);
        Calendar data = Calendar.getInstance();
        data.set(2026, Calendar.DECEMBER, 15, 14, 30);
        return new Viagem("Sao Paulo", "Rio de Janeiro", data, 120.0d, 80.0d, onibus);
    }

    // ==========================================
    // Categoria 1: Tarifas e Precos de Passagem
    // ==========================================

    @Test
    @DisplayName("Seq-VG-01: Consulta e alteracao do preco de passagem LEITO")
    void testPrecoPassagemLeito() {
        Viagem viagem = criarViagemPadrao();
        assertEquals(120.0d, viagem.getPrecoPassagem(TipoDePoltrona.LEITO), 0.001);
        assertEquals(120.0d, viagem.getPrecoLeito(), 0.001);
        
        viagem.setPrecoPassagem(150.0d, TipoDePoltrona.LEITO);
        assertEquals(150.0d, viagem.getPrecoPassagem(TipoDePoltrona.LEITO), 0.001);
    }

    @Test
    @DisplayName("Seq-VG-02: Consulta e alteracao do preco de passagem PADRÃO")
    void testPrecoPassagemPadrao() {
        Viagem viagem = criarViagemPadrao();
        assertEquals(80.0d, viagem.getPrecoPassagem(TipoDePoltrona.PADRÃO), 0.001);
        
        viagem.setPrecoPassagem(95.0d, TipoDePoltrona.PADRÃO);
        assertEquals(95.0d, viagem.getPrecoPassagem(TipoDePoltrona.PADRÃO), 0.001);
    }

    // ==========================================
    // Categoria 2: Metricas de Taxa de Ocupacao
    // ==========================================

    @Test
    @DisplayName("Seq-VG-03: Calculo da Taxa de Ocupacao Global")
    void testTaxaOcupacaoGlobal() {
        Viagem viagem = criarViagemPadrao();
        Onibus bus = viagem.getOnibus();
        
        // Onibus vazio: 0 ocupados de 4 lugares
        assertEquals(0.0d, viagem.getTaxaOcupação(), 0.001, "Taxa inicial deve ser 0%");
        
        // 2 passageiros alocados: 2 ocupados de 4 lugares (50%)
        bus.adicionarPassageiro(new Passageiro("Pax 1", "101"), TipoDePoltrona.LEITO);
        bus.adicionarPassageiro(new Passageiro("Pax 2", "102"), TipoDePoltrona.PADRÃO);
        assertEquals(0.5d, viagem.getTaxaOcupação(), 0.001, "Taxa com metade dos assentos deve ser 50%");
        
        // 4 passageiros alocados: 4 ocupados de 4 lugares (100%)
        bus.adicionarPassageiro(new Passageiro("Pax 3", "103"), TipoDePoltrona.LEITO);
        bus.adicionarPassageiro(new Passageiro("Pax 4", "104"), TipoDePoltrona.PADRÃO);
        assertEquals(1.0d, viagem.getTaxaOcupação(), 0.001, "Taxa com onibus lotado deve ser 100%");
    }

    @Test
    @DisplayName("Seq-VG-04: Avaliacao da Taxa de Ocupacao por Tipo (Documenta Falha F02)")
    void testTaxaOcupacaoPorTipoFalhaF02() {
        Viagem viagem = criarViagemPadrao();
        // A implementacao possui a falha F02:
        // (double) onib.getCapacidade(tp) - onib.getLugaresLivres(tp) / onib.getCapacidade(tp);
        // Divisao eh executada antes da subtracao por precedencia de operadores.
        // Com capacidade=2 e livres=2: 2.0 - (2 / 2) = 1.0 (mesmo com 0 passageiros!)
        double taxaLeitoReal = viagem.getTaxaOcupação(TipoDePoltrona.LEITO);
        assertEquals(1.0d, taxaLeitoReal, 0.001, "Demonstra retorno distorcido pela falha F02 de precedencia");
    }

    // ==========================================
    // Categoria 3: Calculos Financeiros (Faturamento)
    // ==========================================

    @Test
    @DisplayName("Seq-VG-05: Avaliacao do Calculo de Faturamento (Documenta Falha F01)")
    void testCalculoFaturamentoFalhaF01() {
        Viagem viagem = criarViagemPadrao();
        // A implementacao contem a Falha F01:
        // faturaLeito = capacidade - livres * precoLeito; (calcula capacidade - (livres * preco))
        // faturaComum = capacidade - livres * precoLeito; (usa precoLeito em vez de precoPadrao!)
        // Com onibus vazio (leito: cap=2, livres=2, preco=120; padrao: cap=2, livres=2):
        // faturaLeito = 2 - (2 * 120) = -238
        // faturaComum = 2 - (2 * 120) = -238
        // total = -476.0
        double faturamento = viagem.calcularTotalFaturado();
        assertEquals(-476.0d, faturamento, 0.001, "Demonstra calculo incorreto devido a Falha F01 de precedencia e tipo de preco");
    }

    // ==========================================
    // Categoria 4: Metadados e Rotas da Viagem
    // ==========================================

    @Test
    @DisplayName("Seq-VG-06: Verificacao e atualizacao dos dados da viagem")
    void testMetadadosViagem() {
        Viagem viagem = criarViagemPadrao();
        
        assertEquals("Sao Paulo", viagem.getOrigem());
        assertEquals("Rio de Janeiro", viagem.getDestino());
        assertNotNull(viagem.getDataViagem());
        assertNotNull(viagem.getOnibus());
        
        viagem.setOrigem("Campinas");
        viagem.setDestino("Belo Horizonte");
        assertEquals("Campinas", viagem.getOrigem());
        assertEquals("Belo Horizonte", viagem.getDestino());
        
        Calendar novaData = Calendar.getInstance();
        novaData.set(2027, Calendar.JANUARY, 1);
        viagem.setDataViagem(novaData);
        assertEquals(novaData, viagem.getDataViagem());
        
        Onibus novoOnibus = new Onibus("NEW-9999", "G8", "Marcopolo", 10, 5);
        viagem.setOnibus(novoOnibus);
        assertEquals(novoOnibus, viagem.getOnibus());
    }
}
