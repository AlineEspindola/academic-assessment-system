# Academic Assessment System — v1.0
## Plano de Testes — Casos de Teste Completos

> Fluxos Felizes · Fluxos Alternativos · Fluxos de Erro · Regras de Negócio

---

## Índice de Casos de Teste

| ID | Título | Categoria |
|---|---|---|
| [CT-01](#ct-01--iniciar-curso-com-dados-válidos) | Iniciar curso com dados válidos | Fluxo Feliz |
| [CT-02](#ct-02--lançar-nota-válida-no-1º-bimestre) | Lançar nota válida no 1º Bimestre | Fluxo Feliz |
| [CT-03](#ct-03--múltiplas-avaliações-e-cálculo-de-n1) | Múltiplas avaliações e cálculo de N1 | Regra de Negócio |
| [CT-04](#ct-04--registrar-presença-e-falta) | Registrar presença e falta | Fluxo Feliz |
| [CT-05](#ct-05--encerrar-1º-bimestre) | Encerrar 1º Bimestre — verificar N1 | Fluxo Feliz |
| [CT-06](#ct-06--lançar-nota-no-2º-bimestre) | Lançar nota no 2º Bimestre | Fluxo Feliz |
| [CT-07](#ct-07--encerrar-2º-bimestre-e-finalizar-semestre) | Encerrar 2º Bimestre e finalizar semestre | Fluxo Feliz |
| [CT-08](#ct-08--aprovação-direta) | Aprovação direta — `M >= 6.0` + frequência `>= 75%` | Regra de Negócio |
| [CT-09](#ct-09--encaminhamento-para-recuperação) | Encaminhamento para recuperação — `4.0 <= M < 6.0` | Regra de Negócio |
| [CT-10](#ct-10--reprovação-por-nota) | Reprovação por nota — `M < 4.0` | Regra de Negócio |
| [CT-11](#ct-11--reprovação-por-falta) | Reprovação por falta — frequência `< 75%` | Regra de Negócio |
| [CT-12](#ct-12--aprovação-após-recuperação) | Aprovação após recuperação — `Mfinal >= 6.0` | Fluxo Alternativo |
| [CT-13](#ct-13--reprovação-após-recuperação) | Reprovação após recuperação — `Mfinal < 6.0` | Fluxo Alternativo |
| [CT-14](#ct-14--nota-mínima-de-recuperação) | Nota mínima de recuperação para aprovação | Regra de Negócio |
| [CT-15](#ct-15--iniciar-curso-já-iniciado) | Tentar iniciar curso já iniciado | Fluxo de Erro |
| [CT-16](#ct-16--lançar-nota-antes-de-iniciar-o-curso) | Lançar nota antes de iniciar o curso | Fluxo de Erro |
| [CT-17](#ct-17--lançar-nota-no-bimestre-errado) | Lançar nota no bimestre errado | Fluxo de Erro |
| [CT-18](#ct-18--encerrar-2º-bimestre-antes-do-1º) | Encerrar 2º Bimestre antes do 1º | Fluxo de Erro |
| [CT-19](#ct-19--nota-fora-do-intervalo-0-10) | Nota fora do intervalo `[0, 10]` | Fluxo de Erro |
| [CT-20](#ct-20--aluno-sem-nenhuma-nota) | Aluno sem nenhuma nota — média zero | Fluxo Alternativo |
| [CT-21](#ct-21--recuperação-para-aluno-aprovado-direto) | Lançar recuperação para aluno aprovado diretamente | Fluxo de Erro |
| [CT-22](#ct-22--generate_score-em-evaluatedassessment) | `generate_score` em `EvaluatedAssessment` | Fluxo de Erro |
| [CT-23](#ct-23--verificar-imutabilidade) | Verificar imutabilidade — `addAssessment` retorna nova instância | Regra de Negócio |
| [CT-24](#ct-24--frequência-exatamente-75) | Frequência exatamente 75% — elegível para recuperação | Regra de Negócio |
| [CT-25](#ct-25--média-exatamente-60) | Média exatamente 6.0 — aprovação direta | Regra de Negócio |
| [CT-26](#ct-26--média-exatamente-40) | Média exatamente 4.0 — elegível para recuperação | Regra de Negócio |
| [CT-27](#ct-27--média-599--limite-exclusivo) | Média 5.99 — limite exclusivo de 6.0 | Regra de Negócio |
| [CT-28](#ct-28--n110-n210--aprovação-direta-sem-recuperação) | N1=10, N2=10 — aprovação direta, sem recuperação | Regra de Negócio |

---

## Fluxo Feliz

---

### CT-01 — Iniciar curso com dados válidos

**Categoria:** Fluxo Feliz
**Objetivo:** Verificar que o curso transita de `NOT_STARTED` para `IN_PROGRESS` e o semestre inicia corretamente.

**Pré-condições:**
- `DataMock` carregado: curso Matemática com Prof. Marcos Henrique e 2 alunos registrados
- `course.status()` = `NOT_STARTED`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Executar o sistema (`java Main`) | — |
| 2 | Verificar informações exibidas na tela inicial | — |
| 3 | Selecionar opção `[1] Iniciar o curso` | `1` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `course.status()` | `IN_PROGRESS` | ✅ |
| `course.semester().status()` | `FIRST_BIMONTHLY_IN_PROGRESS` | ✅ |
| `course.teacher().status()` | `TEACHING` | ✅ |
| `course.student(S-001).status()` | `STUDYING` | ✅ |
| `course.student(S-002).status()` | `STUDYING` | ✅ |
| `course.attendanceFor(S-001).totalClasses()` | `0` — nenhuma aula registrada | ✅ |
| Menu exibido | Opções do 1º Bimestre visíveis | ✅ |

---

### CT-02 — Lançar nota válida no 1º Bimestre

**Categoria:** Fluxo Feliz
**Objetivo:** Verificar que uma avaliação é registrada corretamente com nota e nome.

**Pré-condições:**
- Curso em `IN_PROGRESS`, semestre em `FIRST_BIMONTHLY_IN_PROGRESS`
- Nenhuma avaliação lançada ainda para Ana

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[1] Lançar nota (1º Bimestre)` | `1` |
| 2 | Selecionar aluno Ana Souza Silva | `1` |
| 3 | Informar nome da avaliação | `Prova 1` |
| 4 | Informar nota | `8.5` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| Avaliação registrada | `nome="Prova 1"`, `score=8.50` | ✅ |
| `assessmentsFor(S-001).size()` no 1º Bim | `1` | ✅ |
| `averageFor(S-001)` no 1º Bim | `8.50` | ✅ |
| Mensagem exibida | `✔ Nota 8.50 lançada para Ana Souza Silva [Prova 1]` | ✅ |

---

### CT-03 — Múltiplas avaliações e cálculo de N1

**Categoria:** Regra de Negócio
**Objetivo:** Verificar cálculo de `N1 = (Prova1 + Trabalho1) / 2`.

**Pré-condições:**
- Curso em `IN_PROGRESS`, 1º Bimestre em `IN_PROGRESS`
- Nenhuma avaliação registrada ainda para Ana

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Lançar nota para Ana — Prova 1 | `7.0` |
| 2 | Lançar nota para Ana — Trabalho 1 | `9.0` |
| 3 | Selecionar `[4] Ver situação dos alunos` | `4` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `assessmentsFor(S-001).size()` | `2` | ✅ |
| `averageFor(S-001)` = `(7.0 + 9.0) / 2` | `8.00` | ✅ |
| Exibição das avaliações | `Prova 1: 7.00`  \|  `Trabalho 1: 9.00` | ✅ |

---

### CT-04 — Registrar presença e falta

**Categoria:** Fluxo Feliz
**Objetivo:** Verificar que o objeto `Attendance` é atualizado corretamente a cada registro.

**Pré-condições:**
- Curso em `IN_PROGRESS`
- Attendance de Ana: `0/0` aulas

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[2] Registrar presença / falta` | `2` |
| 2 | Selecionar aluno Ana | `1` |
| 3 | Selecionar `[1] Presença` | `1` |
| 4 | Repetir para uma Falta | `2` |
| 5 | Repetir para mais 2 Presenças | — |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `totalClasses()` | `4` | ✅ |
| `attendedClasses()` | `3` | ✅ |
| `attendanceRate()` | `0.75` (75%) | ✅ |
| `meetsMinimumRequirement()` | `true` | ✅ |

---

### CT-05 — Encerrar 1º Bimestre

**Categoria:** Fluxo Feliz
**Objetivo:** Verificar transição de estado e exibição de médias ao encerrar o 1º Bimestre.

**Pré-condições:**
- Ana: Prova 1 = `8.0`, Trabalho 1 = `6.0` → N1 = `7.0`
- João: Prova 1 = `5.0`, Trabalho 1 = `5.0` → N1 = `5.0`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[3] Encerrar 1º Bimestre` | `3` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `semester.status()` | `SECOND_BIMONTHLY_IN_PROGRESS` | ✅ |
| `firstBimonthly().status()` | `FINISHED` | ✅ |
| `secondBimonthly().status()` | `IN_PROGRESS` | ✅ |
| N1 Ana exibida | `7.00` | ✅ |
| N1 João exibida | `5.00` | ✅ |
| Menu atualizado | Opções do 2º Bimestre visíveis | ✅ |

---

### CT-06 — Lançar nota no 2º Bimestre

**Categoria:** Fluxo Feliz
**Objetivo:** Verificar que avaliações do 2º Bimestre são aceitas após encerrar o 1º.

**Pré-condições:**
- Semestre em `SECOND_BIMONTHLY_IN_PROGRESS`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[1] Lançar nota (2º Bimestre)` | `1` |
| 2 | Selecionar Ana | `1` |
| 3 | Informar nome | `Prova 2` |
| 4 | Informar nota | `9.0` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `secondBimonthly().assessmentsFor(S-001).size()` | `1` | ✅ |
| `averageFor(S-001)` no 2º Bim | `9.00` | ✅ |

---

### CT-07 — Encerrar 2º Bimestre e finalizar semestre

**Categoria:** Fluxo Feliz
**Objetivo:** Verificar cálculo de `M = (N1 + N2) / 2` e transição para `FINISHED`.

**Pré-condições:**
- Ana: N1=`7.0`, N2=`9.0` → M=`8.0`
- João: N1=`5.0`, N2=`5.0` → M=`5.0`
- Frequência de ambos `>= 75%`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[3] Encerrar 2º Bimestre` | `3` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `course.status()` | `FINISHED` | ✅ |
| `semester.status()` | `FINISHED` | ✅ |
| `semesterAverage(S-001)` — Ana | `8.00` | ✅ |
| `semesterAverage(S-002)` — João | `5.00` | ✅ |
| Ana → `studentApprovedDirect` | `true` (M=8.0 >= 6.0) | ✅ APROVADO DIRETO |
| João → `studentEligibleForRecovery` | `true` (5.0 ∈ [4.0, 6.0)) | ⚠️ RECUPERAÇÃO |

---

## Regras de Negócio

---

### CT-08 — Aprovação direta

**Categoria:** Regra de Negócio
**Objetivo:** Verificar a regra de aprovação direta completa.

**Pré-condições:**
- Semestre finalizado
- Aluno com N1=`6.0`, N2=`6.0` → M=`6.0`
- Frequência: 4 presenças em 4 aulas = `100%`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[1] Ver resultado final dos alunos` | `1` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| M calculada | `6.00` — `(6.0 + 6.0) / 2` | ✅ |
| `studentApprovedDirect()` | `true` | ✅ |
| `meetsMinimumRequirement()` | `true` | ✅ |
| `student.status()` | `APPROVED` | ✅ APROVADO DIRETO |
| Mensagem exibida | `✔ APROVADO DIRETAMENTE` | ✅ |

---

### CT-09 — Encaminhamento para recuperação

**Categoria:** Regra de Negócio
**Objetivo:** Verificar regra de elegibilidade para recuperação.

**Pré-condições:**
- Aluno com N1=`4.0`, N2=`6.0` → M=`5.0`
- Frequência `>= 75%`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[1] Ver resultado final` | `1` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| M calculada | `5.00` | ✅ |
| `studentEligibleForRecovery()` | `true` — `4.0 <= 5.0 < 6.0` | ✅ |
| `meetsMinimumRequirement()` | `true` | ✅ |
| `student.status()` | `IN_RECOVERY` | ⚠️ RECUPERAÇÃO |
| Mensagem exibida | `⚠ ENCAMINHADO PARA RECUPERAÇÃO` | ✅ |

---

### CT-10 — Reprovação por nota

**Categoria:** Regra de Negócio
**Objetivo:** Verificar que aluno com média abaixo de 4.0 é reprovado diretamente.

**Pré-condições:**
- Aluno com N1=`2.0`, N2=`4.0` → M=`3.0`
- Frequência `>= 75%`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[1] Ver resultado final` | `1` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| M calculada | `3.00` | ✅ |
| `studentApprovedDirect()` | `false` | ✅ |
| `studentEligibleForRecovery()` | `false` — M < 4.0 | ✅ |
| `student.status()` | `FAILED` | ❌ REPROVADO POR NOTA |
| Mensagem exibida | `✖ REPROVADO (M abaixo de 4.0)` | ✅ |

---

### CT-11 — Reprovação por falta

**Categoria:** Regra de Negócio
**Objetivo:** Verificar que frequência insuficiente reprova **independente da nota**.

**Pré-condições:**
- Aluno com M=`8.0` (seria aprovado pela nota)
- Frequência: 3 presenças em 5 aulas = `60%`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Registrar 3 presenças e 2 faltas para o aluno | — |
| 2 | Finalizar semestre e ver resultado | — |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `attendanceRate()` | `0.60` (60%) | ✅ |
| `meetsMinimumRequirement()` | `false` | ✅ |
| `student.status()` | `FAILED` | ❌ REPROVADO POR FALTA |
| Mensagem exibida | `✖ REPROVADO POR FALTA (frequência abaixo de 75%)` | ✅ |

> ⚠️ **Observação:** A verificação de frequência tem **prioridade** sobre a nota. Aluno com M=8.0 mas 60% de frequência é reprovado por falta.

---

### CT-24 — Frequência exatamente 75%

**Categoria:** Regra de Negócio
**Objetivo:** Verificar que o limite de 75% é **inclusivo**.

**Pré-condições:**
- Aluno com M=`5.0` (elegível por nota)
- 3 presenças em 4 aulas = `75%`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Registrar 3 presenças e 1 falta | — |
| 2 | Ver resultado final | — |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `attendanceRate()` | `0.75` (75%) | ✅ |
| `meetsMinimumRequirement()` | `true` — `>= 0.75` | ✅ |
| `student.status()` | `IN_RECOVERY` | ⚠️ RECUPERAÇÃO |

---

### CT-25 — Média exatamente 6.0

**Categoria:** Regra de Negócio
**Objetivo:** Verificar que `M = 6.0` resulta em aprovação direta (limite inclusivo).

**Pré-condições:**
- N1=`6.0`, N2=`6.0` → M=`6.0`
- Frequência `>= 75%`

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| M | `6.00` | ✅ |
| `studentApprovedDirect()` | `true` — `>= 6.0` | ✅ |
| `student.status()` | `APPROVED` | ✅ APROVADO DIRETO |

---

### CT-26 — Média exatamente 4.0

**Categoria:** Regra de Negócio
**Objetivo:** Verificar que `M = 4.0` é elegível para recuperação (limite inclusivo).

**Pré-condições:**
- N1=`4.0`, N2=`4.0` → M=`4.0`
- Frequência `>= 75%`

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| M | `4.00` | ✅ |
| `studentEligibleForRecovery()` | `true` — `>= 4.0 e < 6.0` | ✅ |
| `student.status()` | `IN_RECOVERY` | ⚠️ RECUPERAÇÃO |

---

### CT-27 — Média 5.99 — limite exclusivo

**Categoria:** Regra de Negócio
**Objetivo:** Verificar que `M = 5.99` **não** é aprovação direta.

**Pré-condições:**
- N1=`5.98`, N2=`6.0` → M=`5.99`
- Frequência `>= 75%`

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| M | `5.99` | ✅ |
| `studentApprovedDirect()` | `false` — `< 6.0` | ✅ |
| `studentEligibleForRecovery()` | `true` | ⚠️ RECUPERAÇÃO |

> ⚠️ **Observação:** O limite de aprovação direta é **exclusivo** para valores abaixo de 6.0. `M` deve ser `>= 6.0` para aprovação direta.

---

## Recuperação

---

### CT-12 — Aprovação após recuperação

**Categoria:** Fluxo Alternativo
**Objetivo:** Verificar fórmula `Mfinal = (max(N1, N2) + R) / 2` com aprovação.

**Pré-condições:**
- Aluno com status `IN_RECOVERY`
- N1=`5.0`, N2=`4.0` → M=`4.5`; `max(N1, N2) = 5.0`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[2] Lançar nota de recuperação` | `2` |
| 2 | Informar nota de recuperação | `7.0` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `max(N1, N2)` | `5.0` | ✅ |
| `Mfinal = (5.0 + 7.0) / 2` | `6.00` | ✅ |
| `studentApprovedAfterRecovery()` | `true` — `>= 6.0` | ✅ |
| `student.status()` | `APPROVED` | ✅ APROVADO APÓS REC. |
| Mensagem | `✔ APROVADO após recuperação.` | ✅ |

---

### CT-13 — Reprovação após recuperação

**Categoria:** Fluxo Alternativo
**Objetivo:** Verificar que nota de recuperação insuficiente resulta em `FAILED`.

**Pré-condições:**
- N1=`5.0`, N2=`4.0` → M=`4.5`; `max = 5.0`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[2] Lançar nota de recuperação` | `2` |
| 2 | Informar nota de recuperação | `6.0` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `max(N1, N2)` | `5.0` | ✅ |
| `Mfinal = (5.0 + 6.0) / 2` | `5.50` — `< 6.0` | ✅ |
| `studentApprovedAfterRecovery()` | `false` | ✅ |
| `student.status()` | `FAILED` | ❌ REPROVADO APÓS REC. |
| Mensagem | `✖ REPROVADO após recuperação` | ✅ |

---

### CT-14 — Nota mínima de recuperação

**Categoria:** Regra de Negócio
**Objetivo:** Verificar a nota mínima `R` que resulta em `Mfinal = 6.0`.

**Pré-condições:**
- N1=`4.0`, N2=`5.0` → `max = 5.0`
- Para `Mfinal = 6.0`: `R = 2 × 6.0 − 5.0 = 7.0`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Lançar nota de recuperação | `7.0` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `Mfinal = (5.0 + 7.0) / 2` | `6.00` | ✅ |
| `studentApprovedAfterRecovery()` | `true` | ✅ APROVADO APÓS REC. |

> ⚠️ **Fórmula geral:** para qualquer `max(N1, N2) = X`, a nota mínima de aprovação é `R = 12.0 − X`.

---

### CT-28 — N1=10, N2=10 — aprovação direta sem recuperação

**Categoria:** Regra de Negócio
**Objetivo:** Verificar que aluno com M=10.0 é aprovado diretamente e não vai para recuperação.

**Pré-condições:**
- N1=`10.0`, N2=`10.0` → M=`10.0`
- Frequência `>= 75%`

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| M | `10.00` | ✅ |
| `studentApprovedDirect()` | `true` | ✅ APROVADO DIRETO |
| Recuperação necessária | Não — já aprovado diretamente | ✅ |

---

## Fluxos de Erro

---

### CT-15 — Iniciar curso já iniciado

**Categoria:** Fluxo de Erro
**Objetivo:** Verificar que `InProgressCourse` lança `IllegalStateException` ao chamar `start()`.

**Pré-condições:**
- Curso já em `IN_PROGRESS`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Chamar `course.start()` programaticamente | — |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| Exceção lançada | `IllegalStateException: Course is already in progress.` | ❌ EXCEPTION |
| Estado do curso | Permanece `IN_PROGRESS` | ✅ |

---

### CT-16 — Lançar nota antes de iniciar o curso

**Categoria:** Fluxo de Erro
**Objetivo:** Verificar que `NotStartedCourse` rejeita `addAssessment`.

**Pré-condições:**
- Curso em `NOT_STARTED`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Chamar `course.addAssessment(1, studentId, assessment)` | — |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| Exceção lançada | `IllegalStateException: Cannot add assessments — course has not started.` | ❌ EXCEPTION |

---

### CT-17 — Lançar nota no bimestre errado

**Categoria:** Fluxo de Erro
**Objetivo:** Verificar que `InProgressSemester` rejeita `addAssessment` no bimestre 2 quando o 1º está ativo.

**Pré-condições:**
- Semestre em `FIRST_BIMONTHLY_IN_PROGRESS`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Chamar `semester.addAssessment(2, studentId, assessment)` | — |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| Exceção lançada | `IllegalStateException: Second bimonthly has not started yet.` | ❌ EXCEPTION |

---

### CT-18 — Encerrar 2º Bimestre antes do 1º

**Categoria:** Fluxo de Erro
**Objetivo:** Verificar que `InProgressSemester` bloqueia `finishSecondBimonthly`.

**Pré-condições:**
- Semestre em `FIRST_BIMONTHLY_IN_PROGRESS`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Chamar `semester.finishSecondBimonthly()` | — |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| Exceção lançada | `IllegalStateException: First bimonthly must be finished before the second can end.` | ❌ EXCEPTION |

---

### CT-19 — Nota fora do intervalo [0, 10]

**Categoria:** Fluxo de Erro
**Objetivo:** Verificar que `InputReader` rejeita nota inválida e solicita nova entrada.

**Pré-condições:**
- Menu de lançamento de nota aberto

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Informar nota negativa | `-1` |
| 2 | Informar nota acima do máximo | `11` |
| 3 | Informar texto | `abc` |
| 4 | Informar nota válida | `7.5` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| Ao digitar `-1` | `✖ Nota deve estar entre 0.0 e 10.0.` | ❌ EXCEPTION |
| Ao digitar `11` | `✖ Nota deve estar entre 0.0 e 10.0.` | ❌ EXCEPTION |
| Ao digitar `abc` | `✖ Entrada inválida. Digite um número (ex: 7.5).` | ❌ EXCEPTION |
| Ao digitar `7.5` | Nota aceita e lançada | ✅ |

---

### CT-20 — Aluno sem nenhuma nota

**Categoria:** Fluxo Alternativo
**Objetivo:** Verificar comportamento quando aluno não tem avaliações no bimestre.

**Pré-condições:**
- Bimestre em `IN_PROGRESS`, nenhuma avaliação lançada para João

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Encerrar 1º Bimestre sem lançar nota para João | — |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| `assessmentsFor(S-002)` | Lista vazia `[]` | ✅ |
| `averageFor(S-002)` retorna | `DefaultScore(0.0)` | ✅ |
| N1 de João exibida | `0.00` | ✅ |

> ⚠️ **Observação:** O sistema não impede encerrar bimestre sem notas. É responsabilidade do operador garantir que todas as notas foram lançadas.

---

### CT-21 — Recuperação para aluno aprovado direto

**Categoria:** Fluxo de Erro
**Objetivo:** Verificar que `CourseView` filtra apenas alunos com status `IN_RECOVERY`.

**Pré-condições:**
- Semestre finalizado
- Ana com M=`8.0` → status `APPROVED`
- João com M=`5.0` → status `IN_RECOVERY`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Selecionar `[2] Lançar nota de recuperação` | `2` |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| Alunos listados | Apenas João da Cunha | ✅ |
| Ana não aparece | Status `APPROVED` — filtrada pela View | ✅ |

---

### CT-22 — `generate_score` em `EvaluatedAssessment`

**Categoria:** Fluxo de Erro
**Objetivo:** Verificar que `EvaluatedAssessment` rejeita `generate_score` com exceção.

**Pré-condições:**
- Assessment já avaliada com status `EVALUATED`

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Chamar `evaluatedAssessment.generate_score(novaScore)` | — |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| Exceção lançada | `UnsupportedOperationException: Assessment has already been evaluated.` | ❌ EXCEPTION |
| Score da assessment | Permanece inalterada | ✅ |

---

### CT-23 — Verificar imutabilidade

**Categoria:** Regra de Negócio
**Objetivo:** Verificar que o objeto original não é modificado após `addAssessment`.

**Pré-condições:**
- Curso em `IN_PROGRESS`, 1º bimestre ativo

**Passos:**

| # | Ação | Entrada |
|---|---|---|
| 1 | Guardar referência ao `course` original | — |
| 2 | Chamar `course.addAssessment(1, studentId, assessment)` | — |
| 3 | Comparar as referências | — |

**Resultado esperado:**

| Campo verificado | Valor esperado | Status |
|---|---|---|
| Referência original | Permanece sem a nova assessment | ✅ |
| Nova instância retornada | Contém a nova assessment | ✅ |
| `course == courseNovo` | `false` — objetos distintos | ✅ |

> ⚠️ **Observação:** Este é o princípio fundamental do padrão imutável. `addAssessment` faz `deepCopy` do mapa interno antes de qualquer modificação.

---

## Resumo dos Limites de Fronteira

| Limite | Valor | Comportamento |
|---|---|---|
| Aprovação direta — mínimo | `M = 6.0` | ✅ Aprovado (inclusivo) |
| Recuperação — máximo | `M = 5.99...` | ⚠️ Recuperação (exclusivo em 6.0) |
| Recuperação — mínimo | `M = 4.0` | ⚠️ Recuperação (inclusivo) |
| Reprovação — máximo | `M = 3.99...` | ❌ Reprovado (exclusivo em 4.0) |
| Frequência mínima | `75%` | ✅ Elegível (inclusivo) |
| Nota de avaliação — mínimo | `0.0` | ✅ Aceita (inclusivo) |
| Nota de avaliação — máximo | `10.0` | ✅ Aceita (inclusivo) |
| Nota de recuperação mínima para M=5 | `R = 7.0` | `(5.0 + 7.0) / 2 = 6.0` ✅ |
| Nota de recuperação mínima para M=4 | `R = 8.0` | `(4.0 + 8.0) / 2 = 6.0` ✅ |
