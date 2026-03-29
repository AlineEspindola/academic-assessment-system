# Sistema Acadêmico de Avaliações

## Análise de Melhorias — v1.1.0

> Análise crítica do código gerado na v1.0, identificando inconsistências de estilo, decisões discutíveis da IA e pontos de melhoria para a próxima versão.
> **Autora:** Aline de Abreu Espindola

---

## Índice

1. [Camada View sem interfaces](#1-camada-view-sem-interfaces)
2. [Acoplamento com estruturas de dados no domínio](#2-acoplamento-com-estruturas-de-dados-no-domínio)
3. [Nomes de variáveis e funções](#3-nomes-de-variáveis-e-funções)
4. [Comentários redundantes vs comentários úteis](#4-comentários-redundantes-vs-comentários-úteis)
5. [Inconsistência na passagem de objetos para imutabilidade](#5-inconsistência-na-passagem-de-objetos-para-imutabilidade)
6. [Validação precoce no construtor](#6-validação-precoce-no-construtor)
7. [Comportamento curioso da IA — interfaces seletivas](#7-comportamento-curioso-da-ia--interfaces-seletivas)

---

## 1. Camada View sem interfaces

### O problema

A camada `view` foi entregue sem nenhuma interface:

```
view/
├── CourseView.java
├── InputReader.java
└── Printer.java
```

Sem interfaces, qualquer evolução ou variação de comportamento na View exige alterar as classes existentes — com alto risco de quebrar regras de negócio já funcionando.

### Por que a IA fez isso?

O prompt pedia boas práticas, mas não especificava uso de interfaces na camada de View. E a IA — que não raciocina sobre escalabilidade futura, apenas sobre o que é imediatamente necessário — não viu motivo: há apenas uma `CourseView`, então para que uma interface?

Isso é tecnicamente defensável para o estado atual do código. O problema é que a IA não projeta o que o sistema pode se tornar. Ela responde ao que existe agora.

Vale o mesmo questionamento que aparece em outras partes: **a IA sabe quando dividir responsabilidades?** As abstrações vêm do mundo real — uma pessoa entende que curso, aluno e nota são conceitos distintos. A IA repete o que vê e o que o prompt induz. Sem um exemplo ou instrução explícita de separação, ela tende a centralizar.

### O que seria melhor

`CourseView` não merece interface porque é uma classe única? Não — merece porque é o ponto mais provável de variação: outra interface (web, API, mobile), outro idioma, outro formato. Uma interface aqui custa pouco e abre muito.

`Printer` e `InputReader` são utilitários de baixo nível — interfaces para eles seria engenharia desnecessária no estado atual. Mas `CourseView` sim.

Uma interface mínima poderia ser:

```java
public interface View {
    void run();
    void showInfo();
    void showMainMenu();
}
```

`run()` — ponto de entrada.
`showInfo()` — exibe as informações contextuais da tela atual, substituindo o `showCourseInfo()` específico demais.
`showMainMenu()` — todo fluxo interativo precisa de um menu de retorno.

O restante — `launchAssessment`, `registerAttendance`, `showFinalResults` — são responsabilidades do curso e podem permanecer em `CourseView`.

### Mas há mais a dividir

`CourseView` carrega muita responsabilidade: controla o fluxo, exibe informações do curso, do aluno, da avaliação, da frequência, do resultado final. Tecnicamente tudo pertence ao curso, mas na prática são contextos diferentes sendo tratados no mesmo lugar.

Um esqueleto mais adequado para versões futuras:

```
view/
├── View.java                (interface base)
├── CourseView.java          (controla fluxo principal)
├── AssessmentView.java      (lançamento e exibição de notas)
├── AttendanceView.java      (registro de presença e falta)
├── StudentView.java         (exibição de dados do aluno)
├── ResultView.java          (resultado final e recuperação)
└── MenuView.java            (menus reutilizáveis)
```

Isso não é over-engineering para o sistema atual — é o reconhecimento de que `CourseView` já está grande e vai crescer.

---

## 2. Acoplamento com estruturas de dados no domínio

### O problema

Em vários pontos do domínio, `Map<ID, Student>` aparece exposto diretamente:

```java
public class NotStartedCourse implements Course {
    private final ID id;
    private final String name;
    private final Semester semester;
    private Teacher teacher;
    private Map<ID, Student> students;
```

E no contrato da interface:

```java
Map<ID, Student> students();
```

Isso é **Data Structure Coupling** — o domínio está acoplado à estrutura de armazenamento.

### Problemas concretos

**Mudança de estrutura quebra o domínio.** Se amanhã os alunos precisarem ser armazenados em uma `List`, um `Set`, ou delegados a um `StudentRepository`, toda a camada de domínio precisa mudar junto.

**Exposição da implementação interna.** Quem recebe o `Map` sabe que o acesso é por `ID`, que o tipo é `Map`, e que pode chamar `put`, `remove`, `clear` — mesmo que o curso não devesse permitir nenhum disso.

**Violação de encapsulamento.** O domínio deveria expor *o que pode fazer*, não *como armazena*.

### Importante: isso não foi erro da IA

Esse acoplamento já estava presente na v0.1, construída manualmente. A IA apenas seguiu o padrão estabelecido. O problema é de origem — e precisa ser corrigido na v1.1 independentemente de quem o introduziu.

### Como resolver

Criar um objeto de domínio para gerenciar os alunos do curso. Algumas opções de nome que mantêm o vocabulário do domínio:

```java
StudentRegistry
CourseEnrollment
CourseRoster
```

Esse objeto seria responsável por adicionar, consultar e iterar sobre alunos — e o `Course` delegaria para ele em vez de expor o `Map` diretamente. A interface `Course` passaria a expor métodos como `enrolledStudents()` ou `studentBy(ID)`, sem revelar a estrutura interna.

O mesmo raciocínio se aplica a `Map<ID, Attendance>` em `InProgressCourse`.

---

## 3. Nomes de variáveis e funções

### Variáveis de uma letra

```java
@Override
public Attendance attendanceFor(ID studentId) {
    Attendance a = attendances.get(studentId);
    if (a == null) throw new IllegalArgumentException("...");
    return a;
}
```

A variável `a` é inofensiva em um trecho tão curto. Mas é o início de um padrão ruim. O mesmo código em um método um pouco maior se torna ilegível — e o leitor não tem como saber se `a` é `Attendance`, `Assessment` ou qualquer outra coisa que comece com A.

Nomes custam zero. `attendance` no lugar de `a` não adiciona verbosidade — adiciona clareza.

### Abreviações que forçam o leitor a voltar no código

```java
FinishedSemester fs = (FinishedSemester) course.semester();

for (Map.Entry<ID, Student> entry : course.students().entrySet()) {
    ID sid = entry.getKey();
    Student st = entry.getValue();
    Attendance att = course.attendanceFor(sid);

    if (!att.meetsMinimumRequirement()) {
        course = course.updateStudentState(sid, st.fail());
    } else if (fs.studentApprovedDirect(sid)) {
```

`fs`, `sid`, `st`, `att` — quem lê esse bloco focado apenas na lógica de aprovação precisa subir no código para lembrar o que é cada variável. É quase engraçado: preguiça de escrever `finishedSemester` e `studentAttendance` se converte em trabalho para todo leitor futuro, incluindo a própria autora três meses depois.

O código deveria ser escrito para ser lido, não para ser digitado rápido.

### Nomes de método incompletos

`attendanceFor` diz quase o que precisa, mas deixa implícito o sujeito. `attendanceForStudent` é mais claro — especialmente em um sistema que poderia ter `attendanceForCourse` ou `attendanceForBimonthly` no futuro.

A regra é simples: o nome do método deve responder à pergunta "frequência de quê?".

---

## 4. Comentários redundantes vs comentários úteis

### O problema com comentários óbvios

```java
// ── Professor ────────────────────────────────────────────────────────
Teacher teacher = new IdleTeacher(TEACHER_ID, "Prof. Marcos Henrique");
```

![Meme de comentário](https://github.com/user-attachments/assets/b448d08e-90d9-41c6-8d7c-ed03ee4fb59c)

O comentário `// Professor` antes de uma linha que já diz `Teacher teacher = new IdleTeacher(...)` é exatamente isso: instrução para abrir a caixa antes de comer a pizza. O código já é o comentário. Repeti-lo em texto é ruído.

Comentários que descrevem *o que* o código faz são geralmente desnecessários quando o código é legível. Comentários que descrevem *por que* uma decisão foi tomada são insubstituíveis.

### O que faz sentido comentar

```java
/**
 * Regra do enunciado:
 * Elegível para recuperação se: 4.0 <= M < 6.0
 * (a frequência mínima é verificada no Course/View com o objeto Attendance)
 */
@Override
public boolean studentEligibleForRecovery(ID studentId) {
    double m = semesterAverage(studentId).value();
    return m >= RECOVERY_ELIGIBLE_MIN && m < RECOVERY_ELIGIBLE_MAX;
}

/**
 * Calcula a média final com nota de recuperação.
 * R substitui a menor nota entre N1 e N2:
 * Mfinal = (max(N1, N2) + R) / 2
 */
public Score<Double> finalAverageWithRecovery(ID studentId, Score<Double> recoveryScore) {
```

Esses comentários são úteis porque explicam a **regra de negócio** por trás do código — algo que o código sozinho não consegue transmitir. Sem o comentário, um leitor poderia entender o que `4.0` e `6.0` fazem matematicamente, mas não *por que* são esses números e de onde vêm.

A distinção é: comentário que explica regra de negócio tem valor. Comentário que descreve o código tem custo.

---

## 5. Inconsistência na passagem de objetos para imutabilidade

### O padrão em `Student` — coerente com o State Pattern

```java
public StudyingStudent(Student student) {
    this.student = student;
}
```

O objeto anterior é recebido como argumento, e os dados são acessados via delegação. O estado novo envolve o estado anterior.

### O padrão em `Course` — diferente

```java
public InProgressCourse(
        ID id,
        String name,
        Teacher teacher,
        Map<ID, Student> students,
        Map<ID, Attendance> attendances,
        Semester semester
) {
    this.id = id;
    ...
}
```

Aqui os atributos são recebidos e armazenados individualmente. Não há delegação ao estado anterior.

### Qual é o problema?

Não é que um esteja errado e o outro certo — ambos são válidos no contexto do State Pattern. O problema é a **inconsistência**: o mesmo padrão aplicado de dois jeitos diferentes no mesmo projeto, sem que nenhum dos dois seja claramente superior ao outro no contexto específico.

Isso foi discutido durante o desenvolvimento e analisado com mais profundidade (ver conversa sobre `InProgressCourse(this)` no histórico do projeto). A conclusão foi que nenhuma das abordagens é universalmente melhor — e que o mais importante agora é **escolher uma e seguir ela em todo o projeto**.

Para a v1.1, será padronizado o estilo com o qual a desenvolvedora está mais familiarizada, mesmo que essa não seja a solução "mais correta" por alguma métrica externa. Consistência de estilo tem mais valor prático do que otimização isolada.

---

## 6. Validação precoce no construtor

### O código gerado

```java
public StudentAttendance(ID studentId, int totalClasses, int attendedClasses) {
    if (totalClasses < 0) {
        throw new IllegalArgumentException("Total classes cannot be negative.");
    }
    if (attendedClasses < 0 || attendedClasses > totalClasses) {
        throw new IllegalArgumentException(
                "Attended classes must be between 0 and totalClasses (" + totalClasses + ")."
        );
    }
    this.studentId = studentId;
    this.totalClasses = totalClasses;
    this.attendedClasses = attendedClasses;
}
```

### A visão pessoal sobre construtores

O construtor tem um objetivo: **construir o objeto**. Ponto. Colocar verificações de negócio dentro dele polui essa responsabilidade e abre espaço para qualquer verificação ser adicionada ali, tornando o construtor um lugar onde regras se acumulam sem critério.

A analogia mais direta:

![garrafas](https://github.com/user-attachments/assets/7cc45e1f-527e-4f00-9fed-bd43eaf480be)

Antes de analisar qualquer coisa, as duas são garrafas. Foram construídas como garrafas — esse é o trabalho do construtor. O momento de verificar o conteúdo é quando você vai usar, não quando o objeto foi criado.

Primeiro, constroem-se garrafas. A análise vem depois.

### Por que a IA fez isso mesmo sem nenhum exemplo?

Este é o ponto mais revelador desta análise inteira.

Em nenhum momento da v0.1 — construída manualmente — foram colocadas verificações no construtor. O padrão estabelecido era claro na ausência: construtores constroem, validações ficam em outro lugar.

A IA fez mesmo assim. Por quê?

Porque validação no construtor é uma das práticas mais documentadas, mais ensinadas e mais presentes na base de treinamento de qualquer modelo de linguagem. É o que a maioria dos exemplos de Java faz. É o que livros de boas práticas recomendam. É estatisticamente dominante.

Sem uma instrução explícita dizendo "não valide no construtor", o modelo previu o que é mais comum — independente do padrão estabelecido no código base.

### O que isso revela sobre como usar IA no desenvolvimento

A ausência de um padrão no código base **não é instrução implícita para a IA**. Para o modelo, ausência é apenas ausência — não sinal de intenção. Ele preenche com o que é mais frequente na sua base de dados.

Se há um estilo que deve ser respeitado mesmo quando não foi explicitamente demonstrado, ele precisa estar no prompt. A IA não infere preferência por omissão — ela infere convenção por frequência.

---

## 7. Comportamento curioso da IA — interfaces seletivas

Esta observação surgiu durante a análise e merece registro.

`CourseView`, `Printer` e `InputReader` — nenhum tem interface.

`Attendance` — tem interface (`Attendance`) com `StudentAttendance` como implementação, mesmo sendo usada por uma única classe no sistema.

Por que a IA criou interface para `Attendance` mas não para `CourseView`?

Uma hipótese: `Attendance` está dentro do pacote `domain/`, que já continha outras interfaces (`Course`, `Student`, `Teacher`, `Bimonthly`). O padrão de "todo objeto de domínio tem uma interface" estava estabelecido e visível. A IA seguiu o padrão do contexto imediato.

`CourseView` está em `view/`, um pacote novo criado na v1.0 sem nenhuma interface existente como referência. Sem padrão visível, sem instrução explícita — o modelo não inferiu que deveria haver interface.

Isso confirma uma observação consistente ao longo de todo o projeto: **a IA é boa em seguir padrões que estão presentes no contexto imediato. Onde o padrão termina, o comportamento reverte para o convencional ou desaparece**.

Não é inconsistência aleatória. É ausência de contexto.

---

## Resumo das melhorias para v1.1.0

| # | Ponto | Origem | Prioridade |
|---|---|---|---|
| 1 | Interface para `CourseView` | Ausência de instrução no prompt | Alta |
| 2 | Divisão de `CourseView` em views especializadas | Ausência de instrução no prompt | Média |
| 3 | Objeto de domínio substituindo `Map<ID, Student>` | Problema da v0.1 herdado | Alta |
| 4 | Nomes de variáveis sem abreviações (`fs`, `st`, `sid`, `att`, `a`) | Viés de digitação rápida da IA | Alta |
| 5 | `attendanceFor` → `attendanceForStudent` | Nomenclatura imprecisa | Baixa |
| 6 | Remover comentários óbvios; manter comentários de regra de negócio | Viés de documentação excessiva da IA | Média |
| 7 | Padronizar passagem de objeto vs atributos entre estados | Inconsistência v0.1 + v1.0 | Alta |
| 8 | Mover validação do construtor para camada de uso | Viés de convenção da IA ignorando padrão do projeto | Alta |

---

## Nota sobre a próxima versão

A v1.1.0 não introduz novas funcionalidades. É uma versão de alinhamento — o código se aproximando do estilo da desenvolvedora, corrigindo os desvios identificados aqui.

Um dos objetivos explícitos é medir o tempo gasto em correções e ajustes versus o que seria gasto escrevendo do zero com um prompt mais calibrado. Esse número vai informar o quanto vale investir em prompts mais específicos e em agentes de contexto especializado para as versões seguintes.
