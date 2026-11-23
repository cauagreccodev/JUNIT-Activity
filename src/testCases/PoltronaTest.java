package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import busServices.Passageiro;
import busServices.Poltrona;
import busServices.TipoDePoltrona;

@DisplayName("PoltronaTest - Testes Intraclasse (Particionamento por Estado)")
public class PoltronaTest {

    @Test
    @DisplayName("Seq-POL-01: Estado LIVRE inicial e propriedades geometricas")
    void testEstadoLivreInicial() {
        Poltrona poltrona = new Poltrona("01", TipoDePoltrona.PADRÃO, 1.5d, 2.0d, 1.2d);
        
        assertNull(poltrona.getOcupante(), "Poltrona recem-criada deve estar livre");
        assertEquals("01", poltrona.getIdent());
        assertEquals(TipoDePoltrona.PADRÃO, poltrona.getTipo());
        assertEquals(1.5d, poltrona.getLargura(), 0.001);
        assertEquals(2.0d, poltrona.getComprimento(), 0.001);
        assertEquals(1.2d, poltrona.getAltura(), 0.001);
        assertEquals(3.0d, poltrona.calcularArea(), 0.001, "Area deve ser comprimento * largura");
    }

    @Test
    @DisplayName("Seq-POL-02: Transicao de Estado LIVRE -> OCUPADA")
    void testTransicaoLivreParaOcupada() {
        Poltrona poltrona = new Poltrona("02", TipoDePoltrona.PADRÃO, 1.0d, 2.0d, 1.0d);
        Passageiro pax = new Passageiro("Professor Girafales", "88888888");
        
        poltrona.setOcupante(pax);
        
        assertNotNull(poltrona.getOcupante(), "Poltrona deve estar ocupada apos setOcupante");
        assertEquals(pax, poltrona.getOcupante());
        assertEquals("Professor Girafales", poltrona.getOcupante().getNome());
    }

    @Test
    @DisplayName("Seq-POL-03: Transicao de Estado OCUPADA -> LIVRE (Desocupacao)")
    void testTransicaoOcupadaParaLivre() {
        Poltrona poltrona = new Poltrona("03", TipoDePoltrona.LEITO, 1.2d, 2.5d, 1.0d);
        Passageiro pax = new Passageiro("Senhor Barriga", "99999999");
        
        poltrona.setOcupante(pax);
        assertNotNull(poltrona.getOcupante());
        
        poltrona.setOcupante(null);
        assertNull(poltrona.getOcupante(), "Poltrona deve retornar ao estado livre apos setOcupante(null)");
    }

    @Test
    @DisplayName("Seq-POL-04: Modificacao de dimensoes e recalculo da area")
    void testAlteracaoDimensoesECalculoArea() {
        Poltrona poltrona = new Poltrona("04", TipoDePoltrona.PADRÃO, 1.0d, 1.0d, 1.0d);
        assertEquals(1.0d, poltrona.calcularArea(), 0.001);
        
        poltrona.setLargura(2.0d);
        poltrona.setComprimento(3.0d);
        poltrona.setAltura(1.5d);
        
        assertEquals(2.0d, poltrona.getLargura(), 0.001);
        assertEquals(3.0d, poltrona.getComprimento(), 0.001);
        assertEquals(1.5d, poltrona.getAltura(), 0.001);
        assertEquals(6.0d, poltrona.calcularArea(), 0.001);
    }

    @Test
    @DisplayName("Seq-POL-05: Modificacao de Identificador e Tipo")
    void testAlteracaoIdentETipo() {
        Poltrona poltrona = new Poltrona("05", TipoDePoltrona.PADRÃO, 1.0d, 2.0d, 1.0d);
        
        poltrona.setIdent("05-VIP");
        poltrona.setTipo(TipoDePoltrona.LEITO);
        
        assertEquals("05-VIP", poltrona.getIdent());
        assertEquals(TipoDePoltrona.LEITO, poltrona.getTipo());
    }
}
