# Atividade Somativa: Teste Orientado a Objetos com Automação em JUnit 5

Este repositório contém a entrega completa da **Atividade Somativa - Teste OO com Automação em JUnit 5**, cobrindo o projeto de testes unitários **Intraclasses** e testes de integração **Interclasses** para o modelo de domínio de transporte rodoviário de passageiros (`busServices`).

---

## 1. Estrutura do Projeto

O projeto está organizado no padrão Maven / IDE, com compatibilidade direta para **Visual Studio Code**, **Eclipse**, **IntelliJ IDEA** e linha de comando via **Maven**:

```text
Atividade JUNIT/
├── pom.xml                               # Configuração Maven e dependências do JUnit 5
├── .gitignore                            # Filtro de arquivos de build e metadados de IDEs
├── README.md                             # Documentação técnica completa e relatório
├── SOMATIVA 04 - Teste OO.docx           # Documento de entrega preenchido com todas as tabelas
├── TestCase.txt                          # Código original de exemplo fornecido pelo professor
└── src/
    ├── busServices/                      # Pacote com as classes de domínio sob teste
    │   ├── TipoDePoltrona.java           # Enum { LEITO, PADRÃO }
    │   ├── Passageiro.java               # Entidade Passageiro (nome, RG)
    │   ├── Poltrona.java                 # Poltrona convencional
    │   ├── Leito.java                    # Especialização de Poltrona (ângulo de inclinação)
    │   ├── Onibus.java                   # Agregação de poltronas, capacidades e ocupação
    │   └── Viagem.java                   # Agregação de ônibus, rota, data e faturamento
    └── testCases/                        # Pacote com as classes de teste automatizadas (JUnit 5)
        ├── TestCase.java                 # Casos de teste fundamentais da Poltrona
        ├── PassageiroTest.java           # Testes Intraclasse: Particionamento por Atributos
        ├── PoltronaTest.java             # Testes Intraclasse: Particionamento por Estados
        ├── LeitoTest.java                # Testes Intraclasse: Especialização de Poltrona
        ├── OnibusTest.java               # Testes Intraclasse: Particionamento por Estados
        ├── ViagemTest.java               # Testes Intraclasse: Particionamento por Categoria
        ├── InterclassesViagemTest.java   # Testes Interclasses: Cenários de Integração (a a f)
        └── AllTests.java                 # Suíte agregadora de execução com @Suite
```

---

## 2. Entregável - Item 2: Particionamento e Sequências INTRACLASSES

A técnica de particionamento foi escolhida com base nas características estruturais de cada classe:
- **ONIBUS:** Particionamento por **Estado**, pois possui transições de estado bem definidas ao longo do ciclo de alocação de passageiros (*Vazio*, *Parcialmente Ocupado*, *Lotado por Tipo*, *Totalmente Lotado*).
- **PASSAGEIRO:** Particionamento por **Atributo**, pois é uma classe de entidade/dados sem máquina de estados ou métodos de cálculo complexos.
- **VIAGEM:** Particionamento por **Categoria (de Métodos)**, pois agrupa métodos com responsabilidades distintas (*Tarifas e Preços*, *Taxa de Ocupação*, *Faturamento Financeiro* e *Metadados de Rota*).
- **POLTRONA e LEITO:** Particionamento por **Estado** (*Livre* e *Ocupada*).

### Tabela de Particionamento e Sequências Intraclasses

| Classe | Particionamento Adotado | Sequências INTRACLASSES (1 por linha) |
| :--- | :--- | :--- |
| **ONIBUS** | **Por Estados**<br>*(VAZIO, PARCIALMENTE OCUPADO, LOTADO POR TIPO, TOTALMENTE LOTADO)* | 01 - `Onibus-getCapacidade()-getLugaresLivres()-getLugaresLivres(LEITO)-getLugaresLivres(PADRÃO)-toString()`<br>02 - `Onibus-adicionarPassageiro(pax, LEITO)-getLugaresLivres(LEITO)-getPassageiroDaPoltrona("01")`<br>03 - `Onibus-adicionarPassageiro(pax, PADRÃO)-getLugaresLivres(PADRÃO)-getPassageiroDaPoltrona("03")`<br>04 - `Onibus-adicionarPassageiro(pax, LEITO)-consultarPoltronaDePassageiro(pax.getNome())-getIdent()`<br>05 - `Onibus-adicionarPassageiro(pax, LEITO)-consultarPoltronaDePassageiro(pax)-getIdent()`<br>06 - `Onibus-consultarPoltronaDePassageiro("Inexistente") [exibe Falha F03 com assento vago]`<br>07 - `Onibus-[adicionarPassageiro(pax, LEITO)]nLeitos-adicionarPassageiro(paxExcedente, LEITO)[false]`<br>08 - `Onibus-[adicionarPassageiro()]nLotacaoMax-getLugaresLivres()[0]-adicionarPassageiro()[false]`<br>09 - `Onibus-getPassageiroDaPoltrona("999")[null]`<br>10 - `Onibus-setPlaca()-getPlaca()-setModelo()-getModelo()-setMarca()-getMarca()` |
| **PASSAGEIRO** | **Por Atributos**<br>*(Classe de dados / DTO: Nome e RG)* | 01 - `Passageiro-getNome()-setNome()-getNome()`<br>02 - `Passageiro-getRG()-setRG()-getRG()`<br>03 - `Passageiro-getNome()-getRG()-setNome()-setRG()` |
| **VIAGEM** | **Por Categorias**<br>*(Tarifas, Taxa de Ocupação, Faturamento e Metadados)* | 01 - `Viagem-getPrecoPassagem(LEITO)-setPrecoPassagem()-getPrecoPassagem(LEITO)`<br>02 - `Viagem-getPrecoPassagem(PADRÃO)-setPrecoPassagem()-getPrecoPassagem(PADRÃO)`<br>03 - `Viagem-getTaxaOcupação() [Ônibus Vazio, 50% e 100% Lotado]`<br>04 - `Viagem-getTaxaOcupação(LEITO)-getTaxaOcupação(PADRÃO) [exibe Falha F02]`<br>05 - `Viagem-calcularTotalFaturado() [exibe Falha F01 de precedência e tarifa]`<br>06 - `Viagem-getOrigem()-setOrigem()-getDestino()-setDestino()-getDataViagem()-setDataViagem()-getOnibus()-setOnibus()` |
| **POLTRONA** | **Por Estado**<br>*(LIVRE ou OCUPADA)* | 01 - `Poltrona-getOcupante()[null]-calcularArea()-getLargura()-getComprimento()-getAltura()`<br>02 - `Poltrona-setOcupante(pax)-getOcupante()[not null]-calcularArea()`<br>03 - `Poltrona-setOcupante(pax)-setOcupante(null)-getOcupante()[null]`<br>04 - `Poltrona-setLargura()-setComprimento()-setAltura()-calcularArea()`<br>05 - `Poltrona-setIdent()-getIdent()-setTipo()-getTipo()` |
| **LEITO** | **Por Estado / Especialização**<br>*(LIVRE ou OCUPADA, herança de Poltrona)* | 01 - `Leito-getOcupante()[null]-calcularArea()-getTipo()[LEITO]`<br>02 - `Leito-setOcupante(pax)-getOcupante()[not null]-calcularArea()`<br>03 - `Leito-calcularArea() [exibe Falha F05 da fórmula]` |

---

## 3. Entregável - Item 3: Automação dos Testes INTRACLASSES (JUnit 5)

Os testes foram organizados nas seguintes classes de teste no pacote `testCases`:

- **`testCases.PassageiroTest`**: Exercita de forma independente os atributos `nome` e `RG`, verificando valores iniciais e mutabilidade via setters.
- **`testCases.PoltronaTest`**: Exercita as transições entre os estados `LIVRE` e `OCUPADA`, checagem de referências de ocupantes, mutabilidade geométrica e cálculo de área útil.
- **`testCases.LeitoTest`**: Exercita a especialização `Leito`, verificando a inicialização com ângulo de inclinação e registrando a fórmula implementada.
- **`testCases.OnibusTest`**: Exercita o ciclo de ocupação do veículo, controle de assentos disponíveis por categoria (`LEITO` e `PADRÃO`), rejeição de alocação após esgotamento de assentos e expõe o comportamento de pesquisa por ocupante.
- **`testCases.ViagemTest`**: Exercita o gerenciamento de rotas, tarifas de bilhetes, cálculo global de ocupação e expõe o cálculo das métricas financeiras.

---

## 4. Entregável - Item 4: Relatório de Falhas Localizadas nos Testes Intraclasses

Durante a execução sistemática dos testes automatizados, foram identificadas e documentadas as seguintes anomalias e falhas na base de código original:

| Falha | Descrição | Possível Causa |
| :--- | :--- | :--- |
| **F01 - Erro de precedência e tarifa em `calcularTotalFaturado`** | `Viagem.calcularTotalFaturado()` calcula valores incorretos e negativos. Na expressão `capacidade - livres * preco`, a multiplicação é calculada antes da subtração. Além disso, `faturaComum` utiliza indevidamente o preço de `LEITO` em vez de `PADRÃO`. | Falta de parênteses para agrupar a subtração antes do produto: `((capacidade - livres) * preco)`. Cópia indevida do tipo `LEITO` na linha de cálculo da fatura das poltronas comuns. |
| **F02 - Erro de precedência na Taxa de Ocupação por Tipo** | `Viagem.getTaxaOcupação(TipoDePoltrona tp)` retorna valores distorcidos (ex: 1.0 para ônibus 100% vazio). Na expressão `(double) onib.getCapacidade(tp) - onib.getLugaresLivres(tp) / onib.getCapacidade(tp)`, a divisão ocorre antes da subtração. | Ausência de parênteses no agrupamento do numerador: a expressão correta deve ser `(double)(capacidade - livres) / capacidade`. |
| **F03 - `NullPointerException` em `consultarPoltronaDePassageiro(String)`** | `Onibus.consultarPoltronaDePassageiro(String nome)` lança `java.lang.NullPointerException` caso exista qualquer poltrona vaga no veículo durante a pesquisa. | Falta de validação defensiva contra nulo: o código executa `lugar.getOcupante().getNome()` diretamente sem checar se `lugar.getOcupante() != null`. |
| **F04 - `NullPointerException` em `consultarPoltronaDePassageiro(Passageiro)`** | `Onibus.consultarPoltronaDePassageiro(Passageiro pax)` lança `java.lang.NullPointerException` ao percorrer qualquer poltrona vaga. | Ausência de teste contra nulo antes de invocar `lugar.getOcupante().equals(pax)`. |
| **F05 - Cálculo de área distorcido em `Leito.calcularArea()`** | `Leito.calcularArea()` calcula o volume tridimensional multiplicado pelo ângulo de inclinação: `(comprimento * altura * largura) * anguloInclinacao`, gerando valores de grandeza desproporcional (ex: 54.0 m² para assento de 0.5x0.5). | Sobrescrita indevida da fórmula geométrica de área bidimensional (`comprimento * largura`) herdada de `Poltrona`. |
| **F06 - Falta de `equals` e `hashCode` na classe `Passageiro`** | A comparação por objeto em `consultarPoltronaDePassageiro(Passageiro pax)` depende de `equals()`. Sem sobrescrita em `Passageiro`, duas instâncias com mesmo nome e RG são tratadas como diferentes (comparação de ponteiro de memória). | Ausência de implementação dos métodos fundamentais `equals(Object)` e `hashCode()` na entidade `Passageiro`. |
| **F07 - Incompatibilidade de encoding e caracteres especiais em identificadores** | Identificadores como `getTaxaOcupação()` e a constante `TipoDePoltrona.PADRÃO` utilizam caracteres acentuados não-ASCII (`ç`, `ã`). Ambientes compilando com UTF-8 geram erro se o encoding não for explicitado como ISO-8859-1. | Uso de caracteres acentuados em nomes de métodos e literais da linguagem Java sem padronização de codificação de arquivos. |

---

## 5. Entregável - Item 5: Cenários e Sequências INTERCLASSES

Os testes interclasses exercitam a integração colaborativa entre múltiplas classes (`Viagem`, `Onibus`, `Poltrona`, `Leito`, `Passageiro` e `TipoDePoltrona`).

### Tabela de Cenários, Variações e Sequências Interclasses

| Cenário | Variação | Sequência INTERCLASSES para testar Cenário/Variação |
| :--- | :--- | :--- |
| **a) Atribuir um Passageiro ao Onibus que está realizando uma Viagem** | **i) Atribuição bem-sucedida para ambos os tipos de Poltrona** | `Viagem(on) -> on.adicionarPassageiro(pax1, LEITO)[true] -> on.getPassageiroDaPoltrona("01") -> on.adicionarPassageiro(pax2, PADRÃO)[true] -> on.getPassageiroDaPoltrona("03")` |
| **a) Atribuir um Passageiro ao Onibus que está realizando uma Viagem** | **ii) Onibus Lotado** | `Viagem(on) -> [on.adicionarPassageiro(pax_i, tipo)]nLotacao -> on.adicionarPassageiro(paxExcedente, LEITO)[false]` |
| **a) Atribuir um Passageiro ao Onibus que está realizando uma Viagem** | **iii) Tipo de Poltrona desejada pelo Passageiro não está disponível** | `Viagem(on) -> [on.adicionarPassageiro(pax, LEITO)]nLeitos -> on.adicionarPassageiro(paxNovo, LEITO)[false] -> on.adicionarPassageiro(paxNovo, PADRÃO)[true]` |
| **b) Localizar se um Passageiro está em uma dada Viagem** | **i) Pelo seu nome** | `Viagem(on) -> on.adicionarPassageiro(pax, LEITO) -> on.consultarPoltronaDePassageiro("Helena Silva") -> poltrona.getIdent() -> poltrona.getOcupante().getNome()` |
| **b) Localizar se um Passageiro está em uma dada Viagem** | **ii) Pelo seu número de documento (RG)** | `Viagem(on) -> on.adicionarPassageiro(pax, PADRÃO) -> varredura de poltronas de 1 a n -> getPassageiroDaPoltrona(id).getRG().equals("RG-Alvo") -> identifica id da poltrona` |
| **c) Obter qual Passageiro está ocupando uma dada Poltrona e Viagem** | **i) Poltrona com Passageiro – imprimindo seu nome** | `Viagem(on) -> on.adicionarPassageiro(pax, LEITO) -> on.getPassageiroDaPoltrona("01") -> print(ocupante.getNome()) -> assertNotNull & assertEquals` |
| **c) Obter qual Passageiro está ocupando uma dada Poltrona e Viagem** | **ii) Poltrona livre** | `Viagem(on) -> on.getPassageiroDaPoltrona("02")[null]` |
| **c) Obter qual Passageiro está ocupando uma dada Poltrona e Viagem** | **iii) Poltrona não existente** | `Viagem(on) -> on.getPassageiroDaPoltrona("999")[null]` |
| **d) Obter a quantidade de Poltronas disponíveis em uma Viagem** | **i) Quantidade total disponível** | `Viagem(on) -> on.getLugaresLivres() -> on.adicionarPassageiro(pax, LEITO) -> on.getLugaresLivres() [decrementado]` |
| **d) Obter a quantidade de Poltronas disponíveis em uma Viagem** | **ii) Quantidade disponível por tipo definido na enum** | `Viagem(on) -> on.getLugaresLivres(LEITO) -> on.getLugaresLivres(PADRÃO) -> on.adicionarPassageiro(pax, LEITO) -> on.getLugaresLivres(LEITO)[decrementado]` |
| **e) Obter a taxa de ocupação de uma Viagem** | **i) Taxa de ocupação geral** | `Viagem(on) -> viagem.getTaxaOcupação()[0.0] -> [on.adicionarPassageiro()] -> viagem.getTaxaOcupação()[calculado (cap - livres)/cap]` |
| **e) Obter a taxa de ocupação de uma Viagem** | **ii) Taxa de ocupação por tipo definido na enum** | `Viagem(on) -> viagem.getTaxaOcupação(LEITO) -> viagem.getTaxaOcupação(PADRÃO) [demonstra Falha F02]` |
| **f) Obter o faturamento de uma Viagem** | **i) Faturamento geral de uma Viagem** | `Viagem(on, precos) -> [on.adicionarPassageiro()] -> viagem.calcularTotalFaturado() [demonstra Falha F01]` |
| **f) Obter o faturamento de uma Viagem** | **ii) Faturamento por tipo de Poltrona definido na enum** | `Viagem(on) -> (on.getCapacidade(tp) - on.getLugaresLivres(tp)) * viagem.getPrecoPassagem(tp)` |

---

## 6. Entregável - Item 6: Automação dos Testes INTERCLASSES (JUnit 5)

A classe **`testCases.InterclassesViagemTest`** implementa todas as variações descritas no Item 5:

- **`testCenarioA_AtribuicaoBemSucedidaAmbosTipos`**: Valida a alocação de passageiros nos tipos `LEITO` e `PADRÃO`.
- **`testCenarioA_OnibusLotado`**: Valida o retorno `false` ao tentar alocar passageiro com capacidade máxima esgotada.
- **`testCenarioA_TipoPoltronaNaoDisponivel`**: Valida a recusa para tipo esgotado (`LEITO`) e o redirecionamento bem-sucedido para o tipo ainda disponível (`PADRÃO`).
- **`testCenarioB_LocalizarPassageiroPorNome`**: Consulta o assento pelo nome do passageiro.
- **`testCenarioB_LocalizarPassageiroPorDocumentoRG`**: Localiza o passageiro pelo RG varrendo as poltronas da viagem.
- **`testCenarioC_PoltronaComPassageiro`**: Imprime no console o nome do passageiro da poltrona conforme especificado e valida os dados.
- **`testCenarioC_PoltronaLivre`**: Assegura retorno `null` para assento vago.
- **`testCenarioC_PoltronaNaoExistente`**: Assegura retorno `null` para identificador inexistente.
- **`testCenarioD_QuantidadeTotalDisponivel`**: Verifica o decremento dinâmico de `getLugaresLivres()`.
- **`testCenarioD_QuantidadeDisponivelPorTipo`**: Verifica o isolamento de disponibilidade entre `LEITO` e `PADRÃO`.
- **`testCenarioE_TaxaOcupacaoGeral`**: Testa o cálculo de taxa geral (0%, 25%, 50%, 100%).
- **`testCenarioE_TaxaOcupacaoPorTipo`**: Testa a taxa por tipo e atesta o comportamento da Falha F02.
- **`testCenarioF_FaturamentoGeral`**: Testa a chamada de `calcularTotalFaturado()` e expõe a Falha F01.
- **`testCenarioF_FaturamentoPorTipo`**: Realiza o cálculo isolado de faturamento por tipo de poltrona (`LEITO` e `PADRÃO`).

---

## 7. Como Executar os Testes

### Opção A: Linha de Comando (Maven)

Certifique-se de ter o JDK 17+ instalado:

```bash
# Executa toda a suite de testes automatizados
mvn test

# Executa uma classe de teste especifica
mvn test -Dtest=InterclassesViagemTest
```

### Opção B: Visual Studio Code

1. Abra a pasta raiz do projeto no VS Code (`File` -> `Open Folder`).
2. Com a extensão **Extension Pack for Java** habilitada, acerte o ícone de **Testing** (tubo de ensaio na barra lateral).
3. Clique em **Run Tests** ou execute individualmente clicando sobre o botão verde `▶` ao lado dos métodos `@Test`.

### Opção C: Eclipse IDE

1. Abra o Eclipse e selecione `File` -> `Import...` -> `Maven` -> `Existing Maven Projects`.
2. Aponte para a pasta raiz do projeto e clique em `Finish`.
3. Clique com o botão direito sobre a pasta `src/testCases` (ou sobre `AllTests.java`).
4. Selecione `Run As` -> `JUnit Test`.

### Opção D: IntelliJ IDEA

1. Abra o IntelliJ e selecione `File` -> `Open...`, escolhendo a pasta raiz do projeto.
2. Aguarde a sincronização automática do Maven.
3. Clique com o botão direito sobre o pacote `testCases` ou sobre `AllTests.java` e selecione `Run 'AllTests'`.
