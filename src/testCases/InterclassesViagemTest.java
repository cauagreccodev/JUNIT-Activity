package testCases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import busServices.Onibus;
import busServices.Passageiro;
import busServices.Poltrona;
import busServices.TipoDePoltrona;
import busServices.Viagem;

@DisplayName("InterclassesViagemTest - Testes de Integracao Interclasses (Cenarios a-f)")
public class InterclassesViagemTest {

    private Viagem montarCenarioViagem(int numPadrao, int numLeito, double precoLeito, double precoPadrao) {
        Onibus onibus = new Onibus("BRA-2026", "Irizar i8", "Scania", numPadrao, numLeito);
        Calendar data = Calendar.getInstance();
        data.set(2026, Calendar.NOVEMBER, 30, 20, 0);
        return new Viagem("Campinas", "Curitiba", data, precoLeito, precoPadrao, onibus);
    }

    // =========================================================================
    // CENARIO a: Atribuir um Passageiro ao Onibus que esta realizando uma Viagem
    // =========================================================================

    @Test
    @DisplayName("Cenario a.i: Atribuicao bem-sucedida para ambos os tipos de Poltrona (LEITO e PADRÃO)")
    void testCenarioA_AtribuicaoBemSucedidaAmbosTipos() {
        Viagem viagem = montarCenarioViagem(2, 2, 150.0d, 90.0d);
        Onibus bus = viagem.getOnibus();
        
        Passageiro paxLeito = new Passageiro("Alice", "111.111.111-01");
        Passageiro paxPadrao = new Passageiro("Bob", "222.222.222-02");
        
        // Atribui poltrona tipo LEITO
        boolean alocadoLeito = bus.adicionarPassageiro(paxLeito, TipoDePoltrona.LEITO);
        assertTrue(alocadoLeito, "Deveria alocar passageiro com sucesso no tipo LEITO");
        
        // Atribui poltrona tipo PADRÃO
        boolean alocadoPadrao = bus.adicionarPassageiro(paxPadrao, TipoDePoltrona.PADRÃO);
        assertTrue(alocadoPadrao, "Deveria alocar passageiro com sucesso no tipo PADRÃO");
        
        // Verifica se ocupantes estao registrados nas respectivas poltronas
        Passageiro ocupante01 = bus.getPassageiroDaPoltrona("01");
        Passageiro ocupante03 = bus.getPassageiroDaPoltrona("03");
        
        assertNotNull(ocupante01);
        assertEquals("Alice", ocupante01.getNome());
        assertNotNull(ocupante03);
        assertEquals("Bob", ocupante03.getNome());
    }

    @Test
    @DisplayName("Cenario a.ii: Onibus Lotado (capacidade maxima excedida)")
    void testCenarioA_OnibusLotado() {
        // Capacidade total = 1 Leito + 1 Padrao = 2 assentos
        Viagem viagem = montarCenarioViagem(1, 1, 150.0d, 90.0d);
        Onibus bus = viagem.getOnibus();
        
        Passageiro p1 = new Passageiro("Carlos", "333");
        Passageiro p2 = new Passageiro("Daniela", "444");
        Passageiro pExcedente = new Passageiro("Eduardo", "555");
        
        assertTrue(bus.adicionarPassageiro(p1, TipoDePoltrona.LEITO));
        assertTrue(bus.adicionarPassageiro(p2, TipoDePoltrona.PADRÃO));
        assertEquals(0, bus.getLugaresLivres(), "Onibus deve estar com 0 lugares livres");
        
        // Tentativa de adicionar passageiro com onibus lotado deve retornar false
        boolean resultadoExcedente = bus.adicionarPassageiro(pExcedente, TipoDePoltrona.LEITO);
        assertFalse(resultadoExcedente, "Nao deve permitir alocacao com onibus totalmente lotado");
    }

    @Test
    @DisplayName("Cenario a.iii: Tipo de Poltrona desejada pelo Passageiro nao esta disponivel")
    void testCenarioA_TipoPoltronaNaoDisponivel() {
        // 1 Leito e 2 Padrao
        Viagem viagem = montarCenarioViagem(2, 1, 180.0d, 100.0d);
        Onibus bus = viagem.getOnibus();
        
        Passageiro p1 = new Passageiro("Fernanda", "666");
        Passageiro p2 = new Passageiro("Gabriel", "777");
        
        // Ocupa o unico Leito disponivel
        assertTrue(bus.adicionarPassageiro(p1, TipoDePoltrona.LEITO));
        assertEquals(0, bus.getLugaresLivres(TipoDePoltrona.LEITO));
        
        // Gabriel deseja LEITO, mas nao ha mais disponibilidade desse tipo
        boolean alocadoLeito = bus.adicionarPassageiro(p2, TipoDePoltrona.LEITO);
        assertFalse(alocadoLeito, "Deve recusar alocacao pois nao ha mais poltrona do tipo LEITO");
        
        // Gabriel aceita poltrona PADRÃO, que ainda possui vagas
        boolean alocadoPadrao = bus.adicionarPassageiro(p2, TipoDePoltrona.PADRÃO);
        assertTrue(alocadoPadrao, "Deve permitir alocar no tipo PADRÃO que ainda possui vagas");
    }

    // =========================================================================
    // CENARIO b: Localizar se um Passageiro esta em uma dada Viagem
    // =========================================================================

    @Test
    @DisplayName("Cenario b.i: Localizar Passageiro pelo seu Nome")
    void testCenarioB_LocalizarPassageiroPorNome() {
        Viagem viagem = montarCenarioViagem(1, 1, 150.0d, 90.0d);
        Onibus bus = viagem.getOnibus();
        
        Passageiro pax = new Passageiro("Helena Silva", "888.888.888-08");
        bus.adicionarPassageiro(pax, TipoDePoltrona.LEITO); // Alocado na poltrona '01'
        
        Poltrona poltrona = bus.consultarPoltronaDePassageiro("Helena Silva");
        assertNotNull(poltrona, "Passageiro deveria ser localizado pelo nome");
        assertEquals("01", poltrona.getIdent());
        assertEquals("Helena Silva", poltrona.getOcupante().getNome());
    }

    @Test
    @DisplayName("Cenario b.ii: Localizar Passageiro pelo seu numero de documento (RG)")
    void testCenarioB_LocalizarPassageiroPorDocumentoRG() {
        Viagem viagem = montarCenarioViagem(2, 2, 150.0d, 90.0d);
        Onibus bus = viagem.getOnibus();
        
        Passageiro p1 = new Passageiro("Igor Santos", "RG-12345");
        Passageiro p2 = new Passageiro("Julia Lima", "RG-67890");
        
        bus.adicionarPassageiro(p1, TipoDePoltrona.LEITO);
        bus.adicionarPassageiro(p2, TipoDePoltrona.PADRÃO);
        
        // Varredura para localizar passageiro pelo numero do documento (RG)
        String rgBuscado = "RG-67890";
        Passageiro paxEncontrado = null;
        String poltronaEncontrada = null;
        
        for (int i = 1; i <= bus.getCapacidade(); i++) {
            String id = (i < 10 ? "0" : "") + i;
            Passageiro p = bus.getPassageiroDaPoltrona(id);
            if (p != null && rgBuscado.equals(p.getRG())) {
                paxEncontrado = p;
                poltronaEncontrada = id;
                break;
            }
        }
        
        assertNotNull(paxEncontrado, "Passageiro com RG especificado deve ser encontrado na viagem");
        assertEquals("Julia Lima", paxEncontrado.getNome());
        assertEquals("03", poltronaEncontrada);
    }

    // =========================================================================
    // CENARIO c: Obter qual Passageiro esta ocupando uma dada Poltrona e Viagem
    // =========================================================================

    @Test
    @DisplayName("Cenario c.i: Poltrona com Passageiro - Imprimindo seu nome")
    void testCenarioC_PoltronaComPassageiro() {
        Viagem viagem = montarCenarioViagem(2, 1, 150.0d, 90.0d);
        Onibus bus = viagem.getOnibus();
        
        Passageiro pax = new Passageiro("Lucas Mendes", "999.000.111-22");
        bus.adicionarPassageiro(pax, TipoDePoltrona.LEITO);
        
        Passageiro ocupante = bus.getPassageiroDaPoltrona("01");
        assertNotNull(ocupante, "Poltrona 01 deve possuir ocupante");
        
        // Imprime o nome do passageiro conforme solicitado na especificacao
        System.out.println("[Cenario c.i] Ocupante da poltrona 01: " + ocupante.getNome());
        assertEquals("Lucas Mendes", ocupante.getNome());
    }

    @Test
    @DisplayName("Cenario c.ii: Poltrona livre (sem ocupante)")
    void testCenarioC_PoltronaLivre() {
        Viagem viagem = montarCenarioViagem(2, 1, 150.0d, 90.0d);
        Onibus bus = viagem.getOnibus();
        
        // Nao adiciona passageiro na poltrona 02
        Passageiro ocupante = bus.getPassageiroDaPoltrona("02");
        assertNull(ocupante, "Poltrona livre deve retornar null para ocupante");
    }

    @Test
    @DisplayName("Cenario c.iii: Poltrona nao existente")
    void testCenarioC_PoltronaNaoExistente() {
        Viagem viagem = montarCenarioViagem(2, 1, 150.0d, 90.0d);
        Onibus bus = viagem.getOnibus();
        
        // Consulta identificador inexistente
        Passageiro ocupante = bus.getPassageiroDaPoltrona("999");
        assertNull(ocupante, "Poltrona inexistente deve retornar null");
    }

    // =========================================================================
    // CENARIO d: Obter a quantidade de Poltronas disponiveis em uma Viagem
    // =========================================================================

    @Test
    @DisplayName("Cenario d.i: Quantidade total disponivel")
    void testCenarioD_QuantidadeTotalDisponivel() {
        Viagem viagem = montarCenarioViagem(3, 2, 200.0d, 120.0d);
        Onibus bus = viagem.getOnibus();
        
        // Total inicial = 5 vagas
        assertEquals(5, bus.getLugaresLivres());
        
        bus.adicionarPassageiro(new Passageiro("Pax 1", "01"), TipoDePoltrona.LEITO);
        bus.adicionarPassageiro(new Passageiro("Pax 2", "02"), TipoDePoltrona.PADRÃO);
        
        // Apos 2 alocacoes: 3 vagas restantes
        assertEquals(3, bus.getLugaresLivres());
    }

    @Test
    @DisplayName("Cenario d.ii: Quantidade disponivel por tipo de Poltrona definido na enum")
    void testCenarioD_QuantidadeDisponivelPorTipo() {
        Viagem viagem = montarCenarioViagem(3, 2, 200.0d, 120.0d);
        Onibus bus = viagem.getOnibus();
        
        assertEquals(2, bus.getLugaresLivres(TipoDePoltrona.LEITO));
        assertEquals(3, bus.getLugaresLivres(TipoDePoltrona.PADRÃO));
        
        bus.adicionarPassageiro(new Passageiro("Pax 1", "01"), TipoDePoltrona.LEITO);
        
        assertEquals(1, bus.getLugaresLivres(TipoDePoltrona.LEITO), "Deveria restar 1 Leito livre");
        assertEquals(3, bus.getLugaresLivres(TipoDePoltrona.PADRÃO), "Padrao deve permanecer inalterado com 3 livres");
    }

    // =========================================================================
    // CENARIO e: Obter a taxa de ocupacao de uma Viagem
    // =========================================================================

    @Test
    @DisplayName("Cenario e.i: Taxa de ocupacao geral da Viagem")
    void testCenarioE_TaxaOcupacaoGeral() {
        Viagem viagem = montarCenarioViagem(2, 2, 100.0d, 50.0d);
        Onibus bus = viagem.getOnibus();
        
        // Vazio: 0%
        assertEquals(0.0d, viagem.getTaxaOcupação(), 0.001);
        
        // 1 ocupante de 4: 25% (0.25)
        bus.adicionarPassageiro(new Passageiro("P1", "1"), TipoDePoltrona.LEITO);
        assertEquals(0.25d, viagem.getTaxaOcupação(), 0.001);
        
        // 2 ocupantes de 4: 50% (0.50)
        bus.adicionarPassageiro(new Passageiro("P2", "2"), TipoDePoltrona.PADRÃO);
        assertEquals(0.50d, viagem.getTaxaOcupação(), 0.001);
        
        // 4 ocupantes de 4: 100% (1.00)
        bus.adicionarPassageiro(new Passageiro("P3", "3"), TipoDePoltrona.LEITO);
        bus.adicionarPassageiro(new Passageiro("P4", "4"), TipoDePoltrona.PADRÃO);
        assertEquals(1.00d, viagem.getTaxaOcupação(), 0.001);
    }

    @Test
    @DisplayName("Cenario e.ii: Taxa de ocupacao por tipo de Poltrona (Exposicao da Falha F02)")
    void testCenarioE_TaxaOcupacaoPorTipo() {
        Viagem viagem = montarCenarioViagem(2, 2, 100.0d, 50.0d);
        Onibus bus = viagem.getOnibus();
        
        // Metodo implementado: onib.getCapacidade(tp) - onib.getLugaresLivres(tp) / onib.getCapacidade(tp)
        // Com 2 leitos vazios: 2.0 - (2 / 2) = 1.0 (devido a falta de parenteses - Falha F02)
        double taxaLeito = viagem.getTaxaOcupação(TipoDePoltrona.LEITO);
        assertEquals(1.0d, taxaLeito, 0.001, "Demonstra calculo incorreto devido a Falha F02");
        
        // Calculo matematico correto esperado pela integracao:
        double taxaCorretaLeito = (double)(bus.getCapacidade(TipoDePoltrona.LEITO) - bus.getLugaresLivres(TipoDePoltrona.LEITO)) / bus.getCapacidade(TipoDePoltrona.LEITO);
        assertEquals(0.0d, taxaCorretaLeito, 0.001, "Taxa real esperada sem bugs deve ser 0.0");
    }

    // =========================================================================
    // CENARIO f: Obter o faturamento de uma Viagem
    // =========================================================================

    @Test
    @DisplayName("Cenario f.i: Faturamento geral de uma Viagem (Exposicao da Falha F01)")
    void testCenarioF_FaturamentoGeral() {
        Viagem viagem = montarCenarioViagem(2, 2, 100.0d, 50.0d);
        
        // Executa o metodo original e atesta o comportamento com a Falha F01
        double faturamentoRetornado = viagem.calcularTotalFaturado();
        // Leito: 2 - (2 * 100) = -198; Padrao: 2 - (2 * 100) = -198; Total = -396.0
        assertEquals(-396.0d, faturamentoRetornado, 0.001, "Demonstra resultado incorreto pela Falha F01 de precedencia");
        
        // Calculo matematico correto esperado pela regra de negocio:
        Onibus bus = viagem.getOnibus();
        double faturamentoEsperado = ((bus.getCapacidade(TipoDePoltrona.LEITO) - bus.getLugaresLivres(TipoDePoltrona.LEITO)) * viagem.getPrecoPassagem(TipoDePoltrona.LEITO))
                                   + ((bus.getCapacidade(TipoDePoltrona.PADRÃO) - bus.getLugaresLivres(TipoDePoltrona.PADRÃO)) * viagem.getPrecoPassagem(TipoDePoltrona.PADRÃO));
        assertEquals(0.0d, faturamentoEsperado, 0.001, "Com onibus vazio faturamento real deve ser 0.0");
    }

    @Test
    @DisplayName("Cenario f.ii: Faturamento de uma Viagem por tipo de Poltrona definido na enum")
    void testCenarioF_FaturamentoPorTipo() {
        Viagem viagem = montarCenarioViagem(4, 2, 120.0d, 80.0d);
        Onibus bus = viagem.getOnibus();
        
        // Aloca 2 leitos e 1 padrao
        bus.adicionarPassageiro(new Passageiro("P1", "1"), TipoDePoltrona.LEITO);
        bus.adicionarPassageiro(new Passageiro("P2", "2"), TipoDePoltrona.LEITO);
        bus.adicionarPassageiro(new Passageiro("P3", "3"), TipoDePoltrona.PADRÃO);
        
        // Faturamento por tipo LEITO: 2 ocupados * R$ 120 = R$ 240
        int ocupadosLeito = bus.getCapacidade(TipoDePoltrona.LEITO) - bus.getLugaresLivres(TipoDePoltrona.LEITO);
        double faturamentoLeito = ocupadosLeito * viagem.getPrecoPassagem(TipoDePoltrona.LEITO);
        assertEquals(240.0d, faturamentoLeito, 0.001);
        
        // Faturamento por tipo PADRÃO: 1 ocupado * R$ 80 = R$ 80
        int ocupadosPadrao = bus.getCapacidade(TipoDePoltrona.PADRÃO) - bus.getLugaresLivres(TipoDePoltrona.PADRÃO);
        double faturamentoPadrao = ocupadosPadrao * viagem.getPrecoPassagem(TipoDePoltrona.PADRÃO);
        assertEquals(80.0d, faturamentoPadrao, 0.001);
        
        // Faturamento total combinado: R$ 320
        assertEquals(320.0d, faturamentoLeito + faturamentoPadrao, 0.001);
    }
}
