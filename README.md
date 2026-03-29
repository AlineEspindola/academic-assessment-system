# Sistema Acadêmico de Avaliações

<img width="484" height="400" alt="image" src="https://github.com/user-attachments/assets/1dc4c4cd-704c-4888-bfcf-687214235f95" />

<img width="484" height="400" alt="image" src="https://github.com/user-attachments/assets/dec9abd0-136d-4102-bc43-2726bd30e096" />

> Sistema de avaliação acadêmica semestral — projeto de estudo em design patterns orientados a objetos e benchmark de modelos de IA.

**Autora:** Aline de Abreu Espindola

**Stack:** Java 17+ — sem dependências externas

---

## Índice

- [O que é este projeto](#o-que-é-este-projeto)
- [O problema que o sistema resolve](#o-problema-que-o-sistema-resolve)
- [Como executar](#como-executar)
- [Histórico de versões](#histórico-de-versões)
    - [v0.1 — Fundação arquitetural](#v01--fundação-arquitetural)
    - [v1.0 — Primeira versão executável](#v10--primeira-versão-executável)
- [Design patterns utilizados](#design-patterns-utilizados)
- [Estrutura de pacotes — v1.0](#estrutura-de-pacotes--v10)
- [IA como ferramenta de desenvolvimento — análise real](#ia-como-ferramenta-de-desenvolvimento--análise-real)
- [Sobre o estilo pessoal de desenvolvimento](#sobre-o-estilo-pessoal-de-desenvolvimento)
- [O que vem a seguir — v1.2](#o-que-vem-a-seguir--v12)
- [Referências](#referências)

---

## O que é este projeto

Este repositório tem três camadas de propósito simultâneas:

**1. Exercício de domínio** — implementar um sistema real de avaliação acadêmica com regras de negócio concretas: bimestres, médias, recuperação, frequência.

**2. Laboratório de design patterns** — estudar como padrões clássicos (State, Decorator, Value Object) se manifestam em código de domínio real, não em exemplos didáticos abstratos.

**3. Benchmark de IA** — usar o projeto como experimento controlado para observar como diferentes modelos de linguagem (Claude, Gemini, GPT) interpretam, completam e tomam decisões de design diante de um código base intencionalmente incompleto.

A v0.1 foi construída manualmente pela desenvolvedora com arquitetura definida e lacunas deliberadas. A v1.0 foi completada com auxílio do Claude Sonnet. As diferenças entre o que foi feito e o que seria feito pela desenvolvedora são documentadas explicitamente — esse delta é parte do estudo.

---

## O problema que o sistema resolve

O processo ensino-aprendizagem é avaliado por duas notas bimestrais calculadas a partir das avaliações de cada bimestre.

```
N1 = média das avaliações do 1º bimestre   ex: (Prova1 + Trabalho1) / 2
N2 = média das avaliações do 2º bimestre   ex: (Prova2 + Trabalho2) / 2
M  = (N1 + N2) / 2
```

**Aprovação direta:** `M >= 6.0` e frequência `>= 75%`

**Recuperação:** elegível se `4.0 <= M < 6.0` e frequência `>= 75%`

```
Mfinal = (max(N1, N2) + R) / 2
```

A recuperação substitui a menor nota, mantendo a maior. Aprovado após recuperação se `Mfinal >= 6.0`.

**Reprovação por falta:** frequência `< 75%` tem prioridade sobre qualquer nota.

---

## Como executar

```bash
# Compilar
cd academic-assessment_system/src
javac -d ../out $(find . -name "*.java")

# Executar
cd ../out && java Main
```

Requer JDK 17+. Sem dependências externas.

O sistema inicia com dados mockados (curso de Matemática, Prof. Marcos Henrique, alunos Ana e João). Notas, presenças e recuperação são cadastradas interativamente via terminal.

---

## Histórico de versões

### v0.1 — Fundação arquitetural

**Full Changelog:** https://github.com/AlineEspindola/academic-assessment-system/compare/v0.1.0...v0.1.0

Construída integralmente à mão. Não é executável — sua finalidade é estabelecer a arquitetura e deixar lacunas deliberadas como exercício e como base para benchmark de IA.

**O que está presente:**
- Ciclo de vida completo de `Assessment`: `NotStarted → InProgress → Evaluated`
- Estrutura de `Course` com estados `NotStarted` e `InProgress`
- Entidades `Student` (com `ApprovedStudent`, `FailedStudent`, `StudyingStudent`), `Teacher` (`Idle`, `Teaching`)
- Primitivos de domínio: `ID`, `Score<T>`, `DefaultID`
- Esqueleto de `Bimonthly` e `Semester`
- `StudentAssessmentRecord` como registro de submissão

**O que está deliberadamente incompleto:**

| Componente | Estado deliberado |
|---|---|
| `Bimonthly` | `start()`, `finish()`, `final_average()` retornam `null` |
| `Semester` | métodos `void` vazios — sem retorno de estado |
| `Score` | `DefaultScore` é `Score<String>` — inconsistência intencional com comentário `// Criar novos tipos realmente validados` |
| `Student` | sem métodos de transição (`approve()`, `fail()`) no contrato da interface |
| `TeachingTeacher` | `evaluate_assessment()` retorna `null` |

Cada lacuna é uma pergunta implícita para quem — humano ou IA — for completar o código.

---

### v1.0 — Primeira versão executável

**Full Changelog:** https://github.com/AlineEspindola/academic-assessment-system/compare/v0.1.0...v1.0.0

Completada com auxílio do **Claude Sonnet** (Anthropic). Primeira versão interativa e funcional.

**O que foi adicionado:**

- **`Attendance`** — presença como objeto de domínio imutável. Vinculada ao aluno no contexto do curso, não dentro do `Student`. A regra de 75% é encapsulada dentro de `StudentAttendance`.

- **`Bimonthly` completo** — 3 estados (`NotStarted → InProgress → Finished`). `addAssessment` é imutável via `deepCopy`. Médias calculadas por aluno.

- **`Semester` com 4 estados imutáveis** — contrato corrigido de `void` para retorno de estado. `FinishedSemester` contém toda a lógica de aprovação, recuperação e cálculo de `Mfinal`.

- **`Student` com transições no contrato** — `approve()`, `fail()`, `sendToRecovery()` adicionados à interface. `RecoveryStudent` criado.

- **`Score<Double>`** — `DefaultScore` corrigido de `Score<String>`. `ValidatedScore` adicionado com range `[0.0, 10.0]`.

- **Camada `view`** — `CourseView` (menu adaptativo por estado), `Printer` (formatação), `InputReader` (validação de entrada). Menu adapta opções ao `course.status()` e `semester.status()` em tempo real.

- **`DataMock`** — dados fixos de curso, professor e alunos. Notas e presenças são interativas.

**Comparação rápida:**

| Componente | v0.1 | v1.0 |
|---|---|---|
| `Assessment` | sem `name()`, sem `status()` | completo |
| `Score` | `Score<String>` + raw types | `Score<Double>` consistente |
| `Bimonthly` | esqueleto null | 3 estados + imutabilidade |
| `Semester` | void em tudo | 4 estados + lógica de aprovação |
| `Student` | sem transições no contrato | contrato completo + `RecoveryStudent` |
| `Course` | sem `Attendance` | agrega `Attendance`, delega ao `Semester` |
| Executável | não | sim — ciclo completo via terminal |

---

## Design patterns utilizados

### State Pattern (GoF — Behavioral)

Padrão central do projeto. Cada entidade com ciclo de vida é representada por múltiplas classes concretas — uma por estado. Operações inválidas lançam exceções explícitas.

```
Assessment:  NotStarted → InProgress → Evaluated
Course:      NotStarted → InProgress → Finished
Semester:    NotStarted → InProgress → SecondBimonthlyInProgress → Finished
Bimonthly:   NotStarted → InProgress → Finished
Student:     NotStarted → Studying → Approved | Failed | InRecovery
Teacher:     Idle → Teaching
```

A motivação é eliminar condicionais (`if status == "X"`) distribuídos pelo código. Cada estado conhece o que pode e o que não pode fazer.

### Decorator Pattern (GoF — Structural)

Presente na estrutura de `TeachingTeacher`, `StudyingStudent` e demais wrappers de estado. Cada estado recebe a instância anterior no construtor e delega chamadas de leitura, adicionando apenas o comportamento do novo estado.

```java
public TeachingTeacher(Teacher teacher) {
    this.teacher = teacher;          // preserva o objeto original
}
public String name() {
    return teacher.name();           // delega — não copia
}
```

### Value Object / Primitive Obsession Avoidance (DDD)

`ID` não é `String`. `Score<T>` não é `double`. Tipos de domínio com semântica própria, substituíveis sem quebrar contratos.

### Imutabilidade como princípio

Transições de estado retornam novas instâncias. Coleções são copiadas antes de modificações. `FinishedCourse` bloqueia toda mutação — um curso encerrado não muda.

---

## Estrutura de pacotes — v1.0

```
src/
├── Main.java
├── mock/
│   └── DataMock.java
├── view/
│   ├── CourseView.java
│   ├── InputReader.java
│   └── Printer.java
└── domain/
    ├── assessment/
    │   ├── Assessment.java
    │   ├── DefaultAssessment.java
    │   ├── NotStartedAssessment.java
    │   ├── InProgressAssessment.java
    │   └── EvaluatedAssessment.java
    ├── attendance/
    │   ├── Attendance.java
    │   └── StudentAttendance.java
    ├── bimonthly/
    │   ├── Bimonthly.java
    │   ├── NotStartedBimonthly.java
    │   ├── InProgressBimonthly.java
    │   └── FinishedBimonthly.java
    ├── course/
    │   ├── Course.java
    │   ├── NotStartedCourse.java
    │   ├── InProgressCourse.java
    │   └── FinishedCourse.java
    ├── primitive/
    │   ├── Score.java
    │   ├── ID.java
    │   ├── DefaultID.java
    │   ├── DefaultScore.java
    │   └── ValidatedScore.java
    ├── semester/
    │   ├── Semester.java
    │   ├── NotStartedSemester.java
    │   ├── InProgressSemester.java
    │   ├── SecondBimonthlyInProgressSemester.java
    │   └── FinishedSemester.java
    ├── student/
    │   ├── Student.java
    │   ├── NotStartedStudent.java
    │   ├── StudyingStudent.java
    │   ├── RecoveryStudent.java
    │   ├── ApprovedStudent.java
    │   └── FailedStudent.java
    ├── studentAssessmentRecord/
    │   ├── StudentAssessmentRecord.java
    │   └── DefaultStudentAssessmentRecord.java
    └── teacher/
        ├── Teacher.java
        ├── IdleTeacher.java
        └── TeachingTeacher.java
```

---

## IA como ferramenta de desenvolvimento — análise real

Esta seção é a mais importante do projeto. O que foi observado aqui não é teoria sobre IA — é o resultado direto do processo de construção da v1.0.

### O prompt original não é suficiente

O prompt que gerou a v1.0 foi extenso: descrevia o domínio completo, as regras acadêmicas, os cinco itens de tarefa, as decisões respondidas sobre arquitetura (imutabilidade, State Pattern, presença como objeto), e ainda assim o resultado teve inconsistências identificáveis em relação ao estilo da desenvolvedora.

Isso não é falha do modelo. É a natureza do problema: um prompt, por mais detalhado que seja, não substitui o acúmulo de decisões implícitas que um desenvolvedor carrega sobre como prefere escrever código. Nenhum texto consegue capturar completamente esse nível de especificidade — e quanto mais longo o prompt fica para tentar cobrir isso, mais chances de alucinação ele induz.

### O que a IA faz bem

Quando o modelo recebe uma base arquitetural com padrão claro e consistente, ele consegue estendê-lo de forma coerente. O State Pattern em `Assessment` foi bem reconhecido e replicado em `Bimonthly`, `Semester` e `Course`. A imutabilidade foi mantida. O fluxo de delegação entre `Course → Semester → Bimonthly` foi respeitado.

Em outras palavras: quando a estrutura já está definida e o padrão é explícito, a IA é boa em seguir. O problema começa onde o padrão termina — nas decisões que a base não cobria.

### Onde a IA desvia

**Viés de convenção amplamente usada sobre preferência pessoal**

A validação em `ValidatedScore` é um exemplo concreto:

```java
public ValidatedScore(double value) {
    if (value < MIN || value > MAX) {
        throw new IllegalArgumentException(
                "Score must be between " + MIN + " and " + MAX + ". Received: " + value
        );
    }
    this.value = value;
}
```

Isso é validação precoce — a exceção é lançada no construtor antes de qualquer uso. É uma abordagem amplamente documentada, presente em incontáveis exemplos na internet e em livros. A IA prevê essa solução porque ela é estatisticamente dominante na base de treinamento.

Isso não significa que ela seja errada. Significa que a IA não tem como saber que esta desenvolvedora pode preferir validação em outro momento do ciclo de vida do objeto, ou que pode preferir deixar a validação de range na borda da aplicação (o `InputReader`) sem duplicar no domínio.

A IA não sabe qual código é melhor — ela prevê qual código aparece com mais frequência associado àquele contexto. Isso produz código que parece certo porque segue convenções amplamente aceitas, mas que pode não refletir o estilo específico de quem vai usar.

**Passagem de atributos em vez de objetos**

A v0.1 estabelecia um padrão claro: construir o objeto `Course` com todos os seus atributos encapsulados e passar o objeto inteiro. A IA, em vários momentos, passou atributos separados em vez de compor e passar o objeto. Exemplo no `DataMock`:

```java
// o que seria esperado: construir e passar o objeto Course completo
Course course = new NotStartedCourse(COURSE_ID, "Matemática", semester);

// o que a IA fez em alguns momentos: passou atributos avulsos em outras partes
```

Isso é uma inconsistência de estilo que um desenvolvedor humano familiarizado com o projeto não cometeria — porque viu o padrão repetido diversas vezes. A IA não acumula essa familiaridade da mesma forma: cada geração é uma nova previsão sobre o que vem a seguir, sem o peso de "já vi esse padrão ser usado 10 vezes neste arquivo".

**Constantes hardcoded onde poderia ser configurável**

```java
private static final double MINIMUM_RATE = 0.75;
```

A taxa de presença mínima está encapsulada dentro de `StudentAttendance` como constante privada. Em termos de Clean Code e de encapsulamento, isso é defensável — a regra está em um único lugar. Mas do ponto de vista do domínio real, quem define 75% não é o programador: é o gestor, o regulamento, o usuário do sistema. Um requisito que pertence ao gestor não deveria ser uma constante compilada no código; deveria ser um input configurável.

A IA escolheu a solução mais comum para o problema de "evitar magic number" — definir como constante nomeada. É uma solução correta para o problema de legibilidade, mas não necessariamente para o problema de modelagem de domínio.

**Padrão de nomes técnicos sobre nomes de domínio**

Não ocorreu neste projeto de forma grave, mas é um risco recorrente: a IA tende a introduzir nomes como `Controller`, `Decorator`, `IAttendance` (com prefixo de interface) quando não instruída explicitamente a evitá-los. Esses nomes fazem sentido dentro da linguagem de design patterns, mas saem do vocabulário do domínio real — um professor não sabe o que é um `AttendanceController`.

O código da v0.1 usava nomes de domínio puros (`IdleTeacher`, `StudyingStudent`, `NotStartedCourse`) e a IA manteve esse padrão quando existia. O problema é que sem essa âncora explícita, o default tende ao vocabulário técnico.

### O desenvolvedor como tomador de decisões — o que isso significa na prática

A conclusão mais honesta desta experiência é que a divisão "IA executa, desenvolvedor decide" é real, mas não é simples. O desenvolvedor precisa:

**1. Conhecer o domínio profundamente** — para identificar quando a IA fez uma escolha tecnicamente válida mas semanticamente errada. O caso de `MINIMUM_RATE = 0.75` só é percebido como inadequado por quem entende que esse número pertence ao domínio do negócio, não ao código.

**2. Reconhecer seu próprio estilo** — o que parece preferência pessoal muitas vezes é uma decisão arquitetural que afeta manutenibilidade. Saber articular "prefiro X a Y porque Z" é a diferença entre corrigir o código da IA e deixar passando porque "parece certo".

**3. Saber que o prompt define o teto** — a IA não vai além do que o prompt permite. Um prompt vago produz código genérico. Um prompt específico produz código próximo ao esperado. Mas existe um limite: nenhum prompt consegue capturar o estilo acumulado de anos de decisões individuais de um desenvolvedor específico.

**4. Tratar o output como PR, não como entrega** — o código gerado por IA deve ser revisado com o mesmo rigor de um pull request de um desenvolvedor júnior muito rápido: tecnicamente funcional, convencionalmente correto, mas potencialmente desalinhado com as decisões de arquitetura do projeto.

### O papel do MCP e de agentes especializados

Uma consequência direta do que foi observado: prompts únicos e longos tentando cobrir todas as especificidades de um estilo de desenvolvimento tendem a produzir dois problemas simultaneamente — ficam grandes demais (induzindo alucinação) e ainda assim incompletos.

A direção mais promissora é a de agentes especializados por contexto: um agente que conhece as regras de domínio, outro que conhece as decisões arquiteturais, outro que conhece o estilo de nomenclatura. Isso é o que o conceito de MCP (Model Context Protocol) viabiliza — contexto modular em vez de contexto monolítico. Cada agente carrega apenas o que precisa para sua função, sem o ruído de informações irrelevantes que degradam a qualidade das previsões.

Para este projeto especificamente, isso significaria: um agente com o contexto das regras acadêmicas do domínio, um com os padrões arquiteturais escolhidos, um com exemplos do estilo da desenvolvedora (como ela nomeia, como ela compõe objetos, onde ela valida). Separados, eles são mais precisos do que um único prompt tentando fazer tudo.

---

## Sobre o estilo pessoal de desenvolvimento

Esta seção existe porque um README honesto de um projeto de estudo pessoal deve documentar não só o que foi feito, mas como a autora pensa sobre o que foi feito.

### Design patterns são ferramenta, não dogma

Os padrões do GoF — State, Decorator, Factory e os outros — são úteis quando emergem naturalmente do problema. O que não funciona é aplicá-los como checklist: "precisa ter um Factory aqui porque é boa prática".

O critério pessoal para usar um padrão é: ele torna o código mais próximo de como o domínio real funciona, ou mais distante? Se um professor avalia uma prova, o código deveria ter algo que se lê como `professor.avaliar(prova, nota)`. Se para chegar nisso é necessário introduzir um `AssessmentEvaluationStrategyFactory`, o padrão está servindo ao padrão, não ao domínio.

Isso não é rejeitar design patterns. É ter clareza sobre quando eles servem e quando criam uma abstração que só faz sentido para quem leu os mesmos livros.

### Nomes de domínio sobre nomes técnicos

Prefixos de interface (`IAttendance`), sufixos de padrão (`AttendanceDecorator`, `CourseController`) e nomes de camada (`CourseRepository`) têm seu lugar em projetos maiores com convenções de equipe. Em projetos de domínio puro, eles criam uma segunda linguagem — a linguagem técnica — que compete com a linguagem do negócio.

`IdleTeacher` é mais claro que `TeacherWithoutCourseState`. `StudyingStudent` é mais claro que `ActiveEnrollmentStudentDecorator`. O nome deve ser a primeira documentação.

### Validação e o lugar certo das regras

A questão de onde colocar `0.75` é representativa de uma pergunta maior: quem é o dono de cada regra?

- O programador é dono das regras técnicas: como iterar, como estruturar, como garantir thread-safety.
- O domínio é dono das regras de negócio que raramente mudam: uma avaliação tem nota entre 0 e 10.
- O gestor/usuário é dono das regras configuráveis: quantos por cento de frequência é o mínimo, qual é a nota de corte para recuperação.

Quando uma regra do gestor vira uma constante no código, ela sai do lugar certo. Não é um erro grave em um sistema pequeno — mas em um sistema que vai a produção, isso significa abrir o código cada vez que o regulamento muda. `MINIMUM_RATE = 0.75` parece inofensivo; `PASSING_GRADE = 6.0` também. Mas ambos são decisões do regulamento acadêmico, não do código.

### O código deve ser lido como o negócio é entendido

O teste mais direto para qualquer decisão de design é: se alguém que entende o negócio mas não sabe programar ler os nomes das classes e métodos em voz alta, faz sentido? Se sim, o código está no lugar certo. Se não, há abstração técnica demais ou vocabulário errado.

---

## O que vem a seguir — v1.1

A v1.0 é funcional mas tem desvios em relação ao estilo da desenvolvedora que merecem correção antes de evoluir o sistema. A v1.1 será uma análise linha a linha do código gerado, identificando:

- Onde a IA tomou decisões convencionais que não são as preferidas
- Onde a imutabilidade poderia ser mais ou menos rigorosa
- Onde a nomenclatura saiu do vocabulário de domínio
- Onde validações estão no lugar errado (muito cedo, muito tarde, duplicadas)

Um objetivo explícito da v1.1 é medir quanto tempo é gasto em correções e ajustes do output da IA versus quanto seria gasto escrevendo do zero, dado o nível de especificidade do prompt atual. Esse número é relevante para decidir quanto esforço vale a pena investir em prompts mais sofisticados versus construir uma base melhor de contexto para agentes especializados.

A hipótese é que prompts mais curtos com agentes mais especializados produzirão resultados mais alinhados com o estilo da desenvolvedora do que um prompt único longo — e que o tempo de revisão cairá proporcionalmente.

---
