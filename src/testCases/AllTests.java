package testCases;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    TestCase.class,
    PassageiroTest.class,
    PoltronaTest.class,
    LeitoTest.class,
    OnibusTest.class,
    ViagemTest.class,
    InterclassesViagemTest.class
})
public class AllTests {
    // Suite executavel que agrega todos os testes intraclasse e interclasse do projeto
}
