package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import busServices.Leito;
import busServices.Passageiro;
import busServices.TipoDePoltrona;

@DisplayName("LeitoTest - Testes Intraclasse da Subclasse Leito")
public class LeitoTest {

    @Test
    @DisplayName("Seq-LEI-01: Estado LIVRE inicial e heranca de Poltrona")
    void testEstadoLivreInicial() {
        Leito leito = new Leito("01", TipoDePoltrona.LEITO, 0.5d, 0.5d, 1.2d, 180.0d);
        
        assertNull(leito.getOcupante());
        assertEquals("01", leito.getIdent());
        assertEquals(TipoDePoltrona.LEITO, leito.getTipo());
        assertEquals(0.5d, leito.getLargura(), 0.001);
        assertEquals(0.5d, leito.getComprimento(), 0.001);
        assertEquals(1.2d, leito.getAltura(), 0.001);
    }

    @Test
    @DisplayName("Seq-LEI-02: Transicao LIVRE -> OCUPADA em Poltrona Leito")
    void testTransicaoLivreParaOcupada() {
        Leito leito = new Leito("02", TipoDePoltrona.LEITO, 0.6d, 0.8d, 1.2d, 160.0d);
        Passageiro pax = new Passageiro("Bruxa do 71", "71717171");
        
        leito.setOcupante(pax);
        
        assertNotNull(leito.getOcupante());
        assertEquals(pax, leito.getOcupante());
        assertEquals("Bruxa do 71", leito.getOcupante().getNome());
    }

    @Test
    @DisplayName("Seq-LEI-03: Avaliacao do calculo de area de Leito (Documenta Falha F05)")
    void testCalculoArea() {
        // Leito com: comprimento=0.5, largura=0.5, altura=1.2, angulo=180
        // A formula implementada em Leito eh: (comprimento * altura * largura) * anguloInclinacao
        // (0.5 * 1.2 * 0.5) * 180 = 0.3 * 180 = 54.0
        Leito leito = new Leito("03", TipoDePoltrona.LEITO, 0.5d, 0.5d, 1.2d, 180.0d);
        double valorCalculado = leito.calcularArea();
        
        // Verifica a saida real gerada pela implementacao atual
        assertEquals(54.0d, valorCalculado, 0.001, "Implementacao atual multiplica volume pelo angulo (Falha F05)");
    }
}
