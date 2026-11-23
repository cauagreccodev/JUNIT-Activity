package testCases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import busServices.Passageiro;
import busServices.Poltrona;
import busServices.TipoDePoltrona;

@DisplayName("TestCase - Casos Basicos da Poltrona")
public class TestCase {

	@Test
	@DisplayName("Teste de Poltrona no Estado Ocupado")
	void test() {
		Poltrona p = new Poltrona("1A", TipoDePoltrona.PADRÃO, 1.0d, 3.0d, 6.0d);
		Passageiro pax = new Passageiro("Sr. Madruga", "12345678");
		
		p.setOcupante(pax);
		
		assertNotNull(p.getOcupante());
		assertEquals(pax, p.getOcupante());
		assertEquals(3.0d, p.calcularArea(), 0.001);
	}

	@Test
	@DisplayName("Teste de Poltrona no Estado Vazio")
	void test01() {
		Poltrona p = new Poltrona("1A", TipoDePoltrona.PADRÃO, 2.0d, 3.0d, 6.0d);
		
		assertNull(p.getOcupante());
		assertEquals(6.0d, p.calcularArea(), 0.001);
		assertEquals(2.0, p.getLargura(), 0.001);
	}
}
