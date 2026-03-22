# Academic Assessment System — v1.0
## Documentação Técnica Completa

> Padrão de Projeto: **State Pattern + Imutabilidade**
> Arquitetura orientada a objetos seguindo Clean Code, GoF e SOLID

---

## Sumário

1. [Visão Geral do Sistema](#1-visão-geral-do-sistema)
2. [Arquitetura e Padrões de Projeto](#2-arquitetura-e-padrões-de-projeto)
3. [Documentação por Classe](#3-documentação-por-classe)
4. [Fluxo Completo de Dados](#4-fluxo-completo-de-dados)
5. [Dados Mockados](#5-dados-mockados)
6. [Camada View — Interface Terminal](#6-camada-view--interface-terminal)
7. [Como Compilar e Executar](#7-como-compilar-e-executar)

---

## 1. Visão Geral do Sistema

O **Academic Assessment System** automatiza o processo de avaliação acadêmica semestral. Controla o ciclo completo de um curso: cadastro de notas bimestrais, cálculo de médias, gestão de presença e processamento de recuperação.

### 1.1 Objetivo acadêmico

| Conceito | Definição |
|---|---|
| **N1** | Média das avaliações do 1º Bimestre — ex: `(Prova1 + Trabalho1) / 2` |
| **N2** | Média das avaliações do 2º Bimestre — ex: `(Prova2 + Trabalho2) / 2` |
| **M** | Média Semestral = `(N1 + N2) / 2` |
| **R** | Nota de Recuperação — substitui a menor entre N1 e N2 |
| **Mfinal** | `(max(N1, N2) + R) / 2` |

### 1.2 Regras de aprovação

| Situação | Condição |
|---|---|
| ✅ Aprovado Direto | `M >= 6.0` **E** frequência `>= 75%` |
| ⚠️ Recuperação | `4.0 <= M < 6.0` **E** frequência `>= 75%` |
| ❌ Reprovado por Falta | frequência `< 75%` — independente da nota |
| ❌ Reprovado por Nota | `M < 4.0` |
| ✅ Aprovado após Rec. | `Mfinal >= 6.0` |
| ❌ Reprovado após Rec. | `Mfinal < 6.0` |

---

## 2. Arquitetura e Padrões de Projeto

### 2.1 Padrão State (GoF)

Cada entidade do domínio segue o **State Pattern** do GoF: o comportamento de um objeto muda conforme seu estado interno, e cada estado é representado por uma classe concreta separada. Isso elimina condicionais (`if/switch` por status) e torna as transições explícitas e auditáveis.

> **Princípio aplicado:** cada classe de estado somente implementa as operações válidas naquele estado. Operações inválidas lançam exceções descritivas, funcionando como guardiãs de contratos.

### 2.2 Imutabilidade

Todas as transições de estado **retornam um novo objeto** em vez de modificar o existente.

- Atributos são `final`
- Coleções são copiadas em profundidade antes de modificações (`deepCopy` no `InProgressBimonthly`)
- Garante segurança em multi-thread e rastreabilidade de histórico

### 2.3 Fluxo geral de estados

```
Course      :  NOT_STARTED → IN_PROGRESS → FINISHED

Semester    :  NOT_STARTED → FIRST_BIMONTHLY_IN_PROGRESS
                           → SECOND_BIMONTHLY_IN_PROGRESS → FINISHED

Bimonthly   :  NOT_STARTED → IN_PROGRESS → FINISHED

Assessment  :  NOT_STARTED → IN_PROGRESS → EVALUATED

Student     :  NOT_STARTED → STUDYING → APPROVED
                                      → FAILED
                                      → IN_RECOVERY → APPROVED | FAILED

Teacher     :  IDLE → TEACHING
```

### 2.4 Estrutura de pacotes

| Pacote | Responsabilidade |
|---|---|
| `domain.primitive` | Tipos base: `ID`, `Score<T>`, `DefaultID`, `DefaultScore`, `ValidatedScore` |
| `domain.assessment` | Avaliação individual: `NotStarted → InProgress → Evaluated` |
| `domain.attendance` | Presença como objeto de domínio imutável vinculado ao aluno no curso |
| `domain.student` | Ciclo de vida: `NotStarted → Studying → Approved / Failed / InRecovery` |
| `domain.teacher` | `Idle` (não leciona) ou `Teaching` (pode avaliar) |
| `domain.bimonthly` | Bimestre com avaliações por aluno e cálculo de média |
| `domain.semester` | 4 estados; lógica de recuperação no `FinishedSemester` |
| `domain.course` | Orquestrador: professor, alunos, presenças e semestre |
| `domain.studentAssessmentRecord` | Registro imutável de submissão (data, hora, aluno, assessment) |
| `mock` | `DataMock`: dados fixos de professor, 2 alunos e curso |
| `view` | `CourseView` (menu), `Printer` (terminal), `InputReader` (Scanner + validações) |

---

## 3. Documentação por Classe

---

### `Score<T>` — `domain.primitive` — Interface

Tipo base genérico para qualquer nota. Contrato mínimo.

| Método | Retorno | Descrição |
|---|---|---|
| `value()` | `T` | Retorna o valor da nota |

---

### `ID` — `domain.primitive` — Interface

Identificador único de qualquer entidade do domínio.

| Método | Retorno | Descrição |
|---|---|---|
| `value()` | `String` | Retorna o valor textual do ID |
| `same(ID)` | `boolean` | Compara igualdade de dois IDs |

---

### `DefaultID` — `domain.primitive` — Classe

Implementação padrão de `ID` baseada em `String`. Implementa `equals`/`hashCode` para uso como chave de `HashMap`.

| Método | Retorno | Descrição |
|---|---|---|
| `value()` | `String` | Retorna a String do ID |
| `same(ID)` | `boolean` | Compara por valor de String |
| `equals` / `hashCode` | — | Implementados para uso como chave de `HashMap` |

---

### `DefaultScore` — `domain.primitive` — Classe

`Score<Double>` básico sem validação de range. Usado internamente para médias calculadas.

| Método | Retorno | Descrição |
|---|---|---|
| `value()` | `Double` | Retorna o valor Double |

---

### `ValidatedScore` — `domain.primitive` — Classe

`Score<Double>` com validação de range `[0.0, 10.0]`. Lança `IllegalArgumentException` se fora do intervalo.

| Método | Retorno | Descrição |
|---|---|---|
| `ValidatedScore(double)` | — | Construtor: valida `0.0 <= value <= 10.0` |
| `value()` | `Double` | Retorna o valor validado |

---

### `Assessment` — `domain.assessment` — Interface

Contrato de uma avaliação. Define o ciclo de vida: criação → início → pontuação.

| Método | Retorno | Descrição |
|---|---|---|
| `id()` | `ID` | Identificador da avaliação |
| `name()` | `String` | Nome legível (ex: `"Prova 1"`) |
| `start()` | `Assessment` | Transição para `InProgressAssessment` |
| `generate_score(Score<Double>)` | `Assessment` | Atribui nota; retorna `EvaluatedAssessment` |
| `score()` | `Score<Double>` | Nota atual (`0.0` se ainda não avaliada) |
| `status()` | `String` | `NOT_STARTED` \| `IN_PROGRESS` \| `EVALUATED` |

#### Estados de Assessment

| Classe | Permite `start()` | Permite `generate_score()` | Observação |
|---|---|---|---|
| `NotStartedAssessment` | ✅ → `InProgress` | ❌ `IllegalStateException` | Estado inicial |
| `InProgressAssessment` | ❌ `IllegalStateException` | ✅ → `Evaluated` | Aceita nota |
| `EvaluatedAssessment` | ❌ `UnsupportedOperationException` | ❌ `UnsupportedOperationException` | Terminal |
| `DefaultAssessment` | ✅ (retorna `this`) | ❌ `UnsupportedOperationException` | Dados base |

---

### `Attendance` — `domain.attendance` — Interface

Presença como objeto de domínio. Registra aulas totais e aulas assistidas. **Imutável**: cada operação retorna nova instância.

| Método | Retorno | Descrição |
|---|---|---|
| `studentId()` | `ID` | ID do aluno vinculado |
| `totalClasses()` | `int` | Total de aulas registradas |
| `attendedClasses()` | `int` | Aulas que o aluno compareceu |
| `attendanceRate()` | `double` | Percentual de frequência (`0.0` a `1.0`) |
| `meetsMinimumRequirement()` | `boolean` | `true` se frequência `>= 75%` |
| `registerPresence()` | `Attendance` | Nova instância com `+1` total e `+1` presença |
| `registerAbsence()` | `Attendance` | Nova instância com `+1` total e `+0` presença |

#### `StudentAttendance` — Implementação concreta

Construtor valida: `totalClasses >= 0`, `attendedClasses` entre `0` e `totalClasses`.

```java
new StudentAttendance(ID studentId)               // inicia 0/0
new StudentAttendance(ID, int total, int attended) // valores pré-definidos
```

---

### `Student` — `domain.student` — Interface

Ciclo de vida do aluno. Cada transição retorna nova instância do estado correspondente.

| Método | Retorno | Descrição |
|---|---|---|
| `id()` / `name()` / `registration()` | — | Dados cadastrais |
| `status()` | `String` | `NOT_STARTED` \| `STUDYING` \| `IN_RECOVERY` \| `APPROVED` \| `FAILED` |
| `start()` | `Student` | → `StudyingStudent` |
| `approve()` | `Student` | → `ApprovedStudent` |
| `fail()` | `Student` | → `FailedStudent` |
| `sendToRecovery()` | `Student` | → `RecoveryStudent` |

#### Transições válidas por estado

| Estado | `start()` | `approve()` | `fail()` | `sendToRecovery()` |
|---|---|---|---|---|
| `NotStartedStudent` | ✅ | ❌ | ❌ | ❌ |
| `StudyingStudent` | ❌ | ✅ | ✅ | ✅ |
| `RecoveryStudent` | ❌ | ✅ | ✅ | ❌ |
| `ApprovedStudent` | ❌ | ❌ | ❌ | ❌ |
| `FailedStudent` | ❌ | ❌ | ❌ | ❌ |

---

### `Teacher` — `domain.teacher` — Interface

Professor com ciclo de vida simples.

| Método | Retorno | Descrição |
|---|---|---|
| `id()` / `name()` | — | Dados cadastrais |
| `evaluateAssessment(Assessment, Score<Double>)` | `Assessment` | Executa `start()` + `generate_score()` em sequência |
| `status()` | `String` | `IDLE` \| `TEACHING` |

| Estado | `evaluateAssessment()` |
|---|---|
| `IdleTeacher` | ❌ `IllegalStateException` |
| `TeachingTeacher` | ✅ — executa `assessment.start().generate_score(score)` |

---

### `Bimonthly` — `domain.bimonthly` — Interface

Bimestre com avaliações mapeadas por aluno. Calcula média individual.

| Método | Retorno | Descrição |
|---|---|---|
| `id()` / `order()` | — | Identificação e ordem (`1` ou `2`) |
| `start()` | `Bimonthly` | → `InProgressBimonthly` |
| `addAssessment(ID, Assessment)` | `Bimonthly` | Adiciona ao aluno; retorna nova instância (imutável) |
| `finish()` | `Bimonthly` | → `FinishedBimonthly` |
| `averageFor(ID)` | `Score<Double>` | Média das notas do aluno neste bimestre |
| `assessmentsFor(ID)` | `List<Assessment>` | Lista das avaliações do aluno |
| `status()` | `String` | `NOT_STARTED` \| `IN_PROGRESS` \| `FINISHED` |

> **Nota sobre `averageFor`:** se o aluno não tem avaliações, retorna `DefaultScore(0.0)`.

---

### `Semester` — `domain.semester` — Interface

Semestre com **4 estados** de implementação. Gerencia os dois bimestres e os cálculos de média.

| Método | Retorno | Descrição |
|---|---|---|
| `start()` | `Semester` | → `InProgressSemester` (inicia 1º bimestre) |
| `finishFirstBimonthly()` | `Semester` | → `SecondBimonthlyInProgressSemester` |
| `finishSecondBimonthly()` | `Semester` | → `FinishedSemester` |
| `addAssessment(int, ID, Assessment)` | `Semester` | Adiciona ao bimestre ativo; retorna nova instância |
| `semesterAverage(ID)` | `Score<Double>` | `(N1 + N2) / 2` |
| `studentEligibleForRecovery(ID)` | `boolean` | `4.0 <= M < 6.0` — apenas em `FinishedSemester` |

#### Estados do Semester e operações permitidas

| Estado | `addAssessment(1,...)` | `addAssessment(2,...)` | `finishFirst` | `finishSecond` |
|---|---|---|---|---|
| `NotStartedSemester` | ❌ | ❌ | ❌ | ❌ |
| `InProgressSemester` | ✅ | ❌ | ✅ | ❌ |
| `SecondBimonthlyInProgressSemester` | ❌ | ✅ | ❌ | ✅ |
| `FinishedSemester` | ❌ | ❌ | ❌ | ❌ |

#### `FinishedSemester` — métodos extras

| Método | Retorno | Descrição |
|---|---|---|
| `studentApprovedDirect(ID)` | `boolean` | `M >= 6.0` |
| `finalAverageWithRecovery(ID, Score<Double>)` | `Score<Double>` | `(max(N1, N2) + R) / 2` |
| `studentApprovedAfterRecovery(ID, Score<Double>)` | `boolean` | `Mfinal >= 6.0` |

---

### `Course` — `domain.course` — Interface

Orquestrador central. Agrega professor, alunos, presenças e semestre. Delega ao `Semester` as operações de avaliação.

| Método | Retorno | Descrição |
|---|---|---|
| `registerTeacher(Teacher)` | `Course` | Registra professor antes do início |
| `registerStudents(Map<ID,Student>)` | `Course` | Registra alunos antes do início |
| `start()` | `Course` | Valida pré-condições; → `InProgressCourse` |
| `addAssessment(int, ID, Assessment)` | `Course` | Delega ao Semester; retorna nova instância |
| `finishFirstBimonthly()` | `Course` | Encerra 1º e inicia 2º bimestre |
| `finishSecondBimonthly()` | `Course` | Encerra 2º bimestre |
| `finish()` | `Course` | → `FinishedCourse` |
| `updateStudentState(ID, Student)` | `Course` | Atualiza estado do aluno; retorna nova instância |
| `updateAttendance(ID, Attendance)` | `Course` | Atualiza presença; retorna nova instância |
| `attendanceFor(ID)` | `Attendance` | Retorna presença atual do aluno |

> **`start()` valida:** `teacher != null` e `students` não vazio. Converte todos os alunos para `StudyingStudent`, o professor para `TeachingTeacher`, cria `StudentAttendance(0/0)` para cada aluno e chama `semester.start()`.

---

## 4. Fluxo Completo de Dados

### 4.1 Inicialização (`DataMock → Course.start()`)

```
DataMock.buildCourse()
  └── NotStartedCourse (professor: IdleTeacher, alunos: NotStartedStudent)
        └── semester: NotStartedSemester
              ├── firstBimonthly:  NotStartedBimonthly (order=1)
              └── secondBimonthly: NotStartedBimonthly (order=2)

course.start()
  ├── alunos → StudyingStudent (para cada aluno)
  ├── professor → TeachingTeacher
  ├── attendances → StudentAttendance(0/0) (para cada aluno)
  └── semester.start()
        └── firstBimonthly.start() → InProgressBimonthly
        → InProgressSemester
  → InProgressCourse
```

### 4.2 Ciclo de lançamento de nota

```
CourseView.launchAssessment(bimesterOrder)
  1. NotStartedAssessment(id, "Prova 1")
  2. .start()          → InProgressAssessment
  3. .generate_score() → EvaluatedAssessment (nota gravada)
  4. course.addAssessment(bimestre, studentId, evaluated)
       └── semester.addAssessment(bimestre, studentId, evaluated)
             └── bimonthly.addAssessment(studentId, evaluated)
                   └── deepCopy do mapa + nova entrada
                   → InProgressBimonthly (nova instância)
             → InProgressSemester (nova instância)
       → InProgressCourse (nova instância)
```

### 4.3 Encerramento do 1º Bimestre

```
course.finishFirstBimonthly()
  └── semester.finishFirstBimonthly()
        ├── firstBimonthly.finish()   → FinishedBimonthly
        └── secondBimonthly.start()  → InProgressBimonthly
        → SecondBimonthlyInProgressSemester
  → InProgressCourse (nova instância)
```

### 4.4 Encerramento do 2º Bimestre e decisão final

```
course.finishSecondBimonthly()
  └── semester.finishSecondBimonthly()
        └── secondBimonthly.finish() → FinishedBimonthly
        → FinishedSemester

course.finish() → FinishedCourse

CourseView.showFinalResults()
  Para cada aluno:
  ├── att.meetsMinimumRequirement() → false → REPROVADO POR FALTA
  ├── fs.studentApprovedDirect(id)  → true  → APROVADO
  ├── fs.studentEligibleForRecovery(id) → true → IN_RECOVERY
  └── else → REPROVADO (M < 4.0)
```

### 4.5 Fluxo de Recuperação

```
CourseView.launchRecovery()
  ├── Filtra: students com status == "IN_RECOVERY"
  ├── Exibe N1, N2, M do aluno
  ├── Lê nota R do usuário
  ├── fs.finalAverageWithRecovery(id, R) = (max(N1, N2) + R) / 2
  ├── fs.studentApprovedAfterRecovery(id, R)
  │     ├── true  → course.updateStudentState(id, student.approve())
  │     └── false → course.updateStudentState(id, student.fail())
```

---

## 5. Dados Mockados

Os dados abaixo são pré-definidos e carregados automaticamente ao iniciar o sistema (`DataMock.buildCourse()`). Somente **notas**, **presenças** e o **fluxo de avaliação** são interativos.

| Campo | Valor mockado |
|---|---|
| **Curso** | Matemática — ID: `CRS-001` |
| **Professor** | Prof. Marcos Henrique — ID: `T-001` |
| **Aluno 1** | Ana Souza Silva — Matrícula `202601` — ID: `S-001` |
| **Aluno 2** | João da Cunha — Matrícula `202602` — ID: `S-002` |
| **Semestre** | 2026/1 — `01/03/2026` → `30/06/2026` — ID: `SEM-2026-1` |
| **1º Bimestre** | `01/03/2026` → `30/04/2026` — ID: `BIM-1` |
| **2º Bimestre** | `01/05/2026` → `30/06/2026` — ID: `BIM-2` |

---

## 6. Camada View — Interface Terminal

### 6.1 Componentes

| Classe | Função |
|---|---|
| `CourseView` | Orquestra o menu principal. Mantém referência ao `Course` e reatribui a cada operação imutável. |
| `Printer` | Utilitário estático: `header()`, `section()`, `info()`, `grade()`, `success()`, `warn()`, `error()`, `menu()`. |
| `InputReader` | Wrapper do `Scanner`: `readDouble()` rejeita valores fora de `[0, 10]` e aceita vírgula como separador decimal. |

### 6.2 Menus por estado do curso

| Status do curso | Opções exibidas |
|---|---|
| `NOT_STARTED` | `[1]` Iniciar o curso |
| `IN_PROGRESS` + 1º Bim | `[1]` Lançar nota `[2]` Presença/Falta `[3]` Encerrar 1º Bim `[4]` Ver situação `[5]` Sair |
| `IN_PROGRESS` + 2º Bim | `[1]` Lançar nota `[2]` Presença/Falta `[3]` Encerrar 2º Bim `[4]` Ver situação `[5]` Sair |
| `FINISHED` | `[1]` Ver resultado final `[2]` Lançar nota de recuperação `[3]` Sair |

### 6.3 Comportamento adaptativo

O método `showMainMenu()` lê `course.status()` e `semester.status()` em tempo real e exibe somente as opções válidas ao estado atual. Isso impede ações inválidas antes mesmo que o domínio lance exceções.

---

## 7. Como Compilar e Executar

### 7.1 Pré-requisitos

- JDK 17 ou superior
- Nenhuma dependência externa — apenas biblioteca padrão Java

### 7.2 Terminal

```bash
# 1. Entrar no diretório src
cd academic-assessment_system-v1/src

# 2. Compilar todos os arquivos .java
javac -d ../out $(find . -name "*.java")

# 3. Executar
cd ../out && java Main
```

### 7.3 IntelliJ IDEA

1. **File → Open** → selecionar a pasta raiz do projeto
2. Clicar com botão direito em `src/` → **Mark Directory as → Sources Root**
3. **File → Project Structure → SDK** → selecionar JDK 17+
4. Executar a classe `Main` diretamente (`Shift + F10`)
