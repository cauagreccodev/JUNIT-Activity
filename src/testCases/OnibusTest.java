package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import busServices.Onibus;
import busServices.Passageiro;
import busServices.Poltrona;
import busServices.TipoDePoltrona;

@DisplayName("OnibusTest - Testes Intraclasse (Particionamento por Estado)")
public class OnibusTest {

    @Test
    @DisplayName("Seq-ON-01: Estado VAZIO - Capacidades e lugares livres iniciais")
    void testEstadoVazio() {
        Onibus bus = new Onibus("ABC-1234", "Paradiso 1200", "Marcopolo", 4, 2);
        
        assertEquals(6, bus.getCapacidade(), "Capacidade total deve ser 6");
        assertEquals(2, bus.getCapacidade(TipoDePoltrona.LEITO), "Capacidade Leito deve ser 2");
        assertEquals(4, bus.getCapacidade(TipoDePoltrona.PADRÃO), "Capacidade Padrao deve ser 4");
        
        assertEquals(6, bus.getLugaresLivres(), "Lugares livres totais devem ser 6");
        assertEquals(2, bus.getLugaresLivres(TipoDePoltrona.LEITO), "Lugares Leito livres devem ser 2");
        assertEquals(4, bus.getLugaresLivres(TipoDePoltrona.PADRÃO), "Lugares Padrao livres devem ser 4");
        
        assertNotNull(bus.toString());
        assertTrue(bus.toString().contains("VAGA"));
    }

    @Test
    @DisplayName("Seq-ON-02: Transicao VAZIO -> PARCIALMENTE OCUPADO (Alocacao Leito)")
    void testTransicaoVazioParaParcialLeito() {
        Onibus bus = new Onibus("ABC-1234", "Paradiso 1200", "Marcopolo", 4, 2);
        Passageiro pax1 = new Passageiro("Seu Madruga", "12345678");
        
        boolean alocado = bus.adicionarPassageiro(pax1, TipoDePoltrona.LEITO);
        assertTrue(alocado, "Alocacao de Leito deve ser bem sucedida");
        
        assertEquals(5, bus.getLugaresLivres());
        assertEquals(1, bus.getLugaresLivres(TipoDePoltrona.LEITO));
        assertEquals(4, bus.getLugaresLivres(TipoDePoltrona.PADRÃO));
        
        Passageiro ocupante01 = bus.getPassageiroDaPoltrona("01");
        assertNotNull(ocupante01);
        assertEquals("Seu Madruga", ocupante01.getNome());
    }

    @Test
    @DisplayName("Seq-ON-03: Alocacao em Poltrona PADRÃO")
    void testAlocacaoPadrao() {
        Onibus bus = new Onibus("ABC-1234", "Paradiso 1200", "Marcopolo", 4, 2);
        Passageiro pax = new Passageiro("Quico", "87654321");
        
        boolean alocado = bus.adicionarPassageiro(pax, TipoDePoltrona.PADRÃO);
        assertTrue(alocado);
        
        assertEquals(5, bus.getLugaresLivres());
        assertEquals(2, bus.getLugaresLivres(TipoDePoltrona.LEITO));
        assertEquals(3, bus.getLugaresLivres(TipoDePoltrona.PADRÃO));
        
        // Poltronas leito sao 01 e 02; a primeira padrao eh 03
        Passageiro ocupante03 = bus.getPassageiroDaPoltrona("03");
        assertNotNull(ocupante03);
        assertEquals("Quico", ocupante03.getNome());
    }

    @Test
    @DisplayName("Seq-ON-04: Consulta de Poltrona por Nome quando passageiro eh localizado")
    void testConsultaPoltronaPorNome() {
        Onibus bus = new Onibus("ABC-1234", "Paradiso 1200", "Marcopolo", 1, 1);
        Passageiro pax1 = new Passageiro("Chaves", "11111111");
        bus.adicionarPassageiro(pax1, TipoDePoltrona.LEITO); // alocado na poltrona 01
        
        Poltrona p = bus.consultarPoltronaDePassageiro("Chaves");
        assertNotNull(p);
        assertEquals("01", p.getIdent());
    }

    @Test
    @DisplayName("Seq-ON-05: Exposicao da Falha F03 (NullPointerException ao consultar por nome com assento vago)")
    void testFalhaF03NullPointerConsultaNome() {
        Onibus bus = new Onibus("ABC-1234", "Paradiso 1200", "Marcopolo", 2, 2);
        // Onibus com 4 poltronas vagas. Consultar passageiro inexistente percorre poltrona vaga:
        assertThrows(NullPointerException.class, () -> {
            bus.consultarPoltronaDePassageiro("PassageiroInexistente");
        }, "Deve lancar NullPointerException por ausencia de null-check em getOcupante() (Falha F03)");
    }

    @Test
    @DisplayName("Seq-ON-06: Consulta por Objeto Passageiro e exposicao da Falha F04")
    void testFalhaF04NullPointerConsultaObjeto() {
        Onibus bus = new Onibus("ABC-1234", "Paradiso 1200", "Marcopolo", 2, 2);
        Passageiro pax = new Passageiro("Nhonho", "33333333");
        
        // Onibus com poltronas vagas lanca NullPointerException ao fazer lugar.getOcupante().equals(pax)
        assertThrows(NullPointerException.class, () -> {
            bus.consultarPoltronaDePassageiro(pax);
        }, "Deve lancar NullPointerException por ausencia de null-check antes de .equals() (Falha F04)");
    }

    @Test
    @DisplayName("Seq-ON-07: Transicao de Estado - LOTADO POR TIPO (Esgotamento de Leito)")
    void testEstadoLotadoPorTipoLeito() {
        Onibus bus = new Onibus("ABC-1234", "Paradiso 1200", "Marcopolo", 2, 1);
        Passageiro p1 = new Passageiro("Pax 1", "101");
        Passageiro p2 = new Passageiro("Pax 2", "102");
        
        assertTrue(bus.adicionarPassageiro(p1, TipoDePoltrona.LEITO));
        assertEquals(0, bus.getLugaresLivres(TipoDePoltrona.LEITO));
        
        // Tentar adicionar outro leito deve retornar falso pois o tipo esgotou
        assertFalse(bus.adicionarPassageiro(p2, TipoDePoltrona.LEITO), "Nao deve permitir alocar leito quando esgotado");
        
        // Mas ainda ha vagas no tipo PADRÃO
        assertTrue(bus.adicionarPassageiro(p2, TipoDePoltrona.PADRÃO), "Deve permitir alocar padrao quando houver vagas");
    }

    @Test
    @DisplayName("Seq-ON-08: Transicao de Estado - TOTALMENTE LOTADO")
    void testEstadoTotalmenteLotado() {
        Onibus bus = new Onibus("ABC-1234", "Paradiso 1200", "Marcopolo", 1, 1);
        Passageiro p1 = new Passageiro("Pax 1", "101");
        Passageiro p2 = new Passageiro("Pax 2", "102");
        Passageiro p3 = new Passageiro("Pax 3", "103");
        
        assertTrue(bus.adicionarPassageiro(p1, TipoDePoltrona.LEITO));
        assertTrue(bus.adicionarPassageiro(p2, TipoDePoltrona.PADRÃO));
        
        assertEquals(0, bus.getLugaresLivres());
        
        // Onibus 100% lotado: rejeita novos passageiros em qualquer tipo
        assertFalse(bus.adicionarPassageiro(p3, TipoDePoltrona.LEITO));
        assertFalse(bus.adicionarPassageiro(p3, TipoDePoltrona.PADRÃO));
    }

    @Test
    @DisplayName("Seq-ON-09: Consulta de identificador de poltrona inexistente ou livre")
    void testConsultaPoltronaInexistenteELivre() {
        Onibus bus = new Onibus("ABC-1234", "Paradiso 1200", "Marcopolo", 2, 1);
        
        // Poltrona existente mas livre
        assertNull(bus.getPassageiroDaPoltrona("01"));
        
        // Poltrona inexistente
        assertNull(bus.getPassageiroDaPoltrona("999"));
    }

    @Test
    @DisplayName("Seq-ON-10: Verificacao e atualizacao de metadados do veiculo")
    void testMetadadosVeiculo() {
        Onibus bus = new Onibus("ABC-1234", "Paradiso 1200", "Marcopolo", 4, 2);
        
        assertEquals("ABC-1234", bus.getPlaca());
        assertEquals("Paradiso 1200", bus.getModelo());
        assertEquals("Marcopolo", bus.getMarca());
        
        bus.setPlaca("XYZ-9876");
        bus.setModelo("G8 1800 DD");
        bus.setMarca("Marcopolo S.A.");
        
        assertEquals("XYZ-9876", bus.getPlaca());
        assertEquals("G8 1800 DD", bus.getModelo());
        assertEquals("Marcopolo S.A.", bus.getMarca());
    }
}
