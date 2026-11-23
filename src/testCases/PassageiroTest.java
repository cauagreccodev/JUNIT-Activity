package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import busServices.Passageiro;

@DisplayName("PassageiroTest - Testes Intraclasse (Particionamento por Atributos)")
public class PassageiroTest {

    @Test
    @DisplayName("Seq-PAX-01: Validacao e modificacao do atributo Nome")
    void testAtributoNome() {
        Passageiro pax = new Passageiro("Chaves", "11111111");
        assertEquals("Chaves", pax.getNome());
        
        pax.setNome("Chaves del Ocho");
        assertEquals("Chaves del Ocho", pax.getNome());
    }

    @Test
    @DisplayName("Seq-PAX-02: Validacao e modificacao do atributo RG")
    void testAtributoRG() {
        Passageiro pax = new Passageiro("Kiko", "22222222");
        assertEquals("22222222", pax.getRG());
        
        pax.setRG("33333333");
        assertEquals("33333333", pax.getRG());
    }

    @Test
    @DisplayName("Seq-PAX-03: Consistencia completa dos atributos da instancia")
    void testConsistenciaAtributos() {
        Passageiro pax = new Passageiro("Dona Florinda", "44444444");
        assertNotNull(pax.getNome());
        assertNotNull(pax.getRG());
        
        pax.setNome("Florinda Corcuera");
        pax.setRG("55555555");
        
        assertEquals("Florinda Corcuera", pax.getNome());
        assertEquals("55555555", pax.getRG());
    }
}
