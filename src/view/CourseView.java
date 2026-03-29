package view;

import domain.assessment.Assessment;
import domain.assessment.NotStartedAssessment;
import domain.attendance.Attendance;
import domain.course.Course;
import domain.primitive.DefaultID;
import domain.primitive.DefaultScore;
import domain.primitive.ID;
import domain.primitive.Score;
import domain.semester.FinishedSemester;
import domain.semester.Semester;
import domain.student.Student;
import mock.DataMock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CourseView implements View {

    private Course course;
    private final InputReader input;

    // Contador para IDs de avaliações geradas em tempo de execução
    private final AtomicInteger assessmentCounter = new AtomicInteger(1);

    public CourseView() {
        this.input = new InputReader();
        this.course = DataMock.buildCourse();
    }

    public void run() {
        Printer.header("SISTEMA DE AVALIAÇÃO ACADÊMICA  v1.0");
        showInfo();

        boolean running = true;
        while (running) {
            running = showMainMenu();
        }

        input.close();
        Printer.header("Sistema encerrado. Até logo!");
    }

    public void showInfo() {
        Printer.section("Dados do Curso (Mockados)");
        Printer.info("Curso", course.name());
        Printer.info("Professor", course.teacher().name());
        Printer.info("Status do curso", course.status());
        Printer.blank();
        Printer.info("Alunos matriculados", "");
        for (Map.Entry<ID, Student> e : course.students().entrySet()) {
            System.out.printf("    • %-22s  Matrícula: %d%n",
                    e.getValue().name(), e.getValue().registration());
        }
    }

    public boolean showMainMenu() {
        Printer.section("MENU PRINCIPAL  [Status: " + course.status() + "]");

        String statusCurso = course.status();

        switch (statusCurso) {

            case "NOT_STARTED" -> {
                Printer.menu("Iniciar o curso");
                int option = input.readMenuOption(1);
                if (option == 1) startCourse();
            }

            case "IN_PROGRESS" -> {
                String semStatus = course.semester().status();

                if ("FIRST_BIMONTHLY_IN_PROGRESS".equals(semStatus)) {
                    Printer.menu(
                            "Lançar nota (1º Bimestre)",
                            "Registrar presença / falta",
                            "Encerrar 1º Bimestre",
                            "Ver situação dos alunos",
                            "Sair"
                    );
                    int option = input.readMenuOption(5);
                    switch (option) {
                        case 1 -> launchAssessment(1);
                        case 2 -> registerAttendance();
                        case 3 -> finishFirstBimonthly();
                        case 4 -> showStudentSummary();
                        case 5 -> { return false; }
                    }

                } else if ("SECOND_BIMONTHLY_IN_PROGRESS".equals(semStatus)) {
                    Printer.menu(
                            "Lançar nota (2º Bimestre)",
                            "Registrar presença / falta",
                            "Encerrar 2º Bimestre",
                            "Ver situação dos alunos",
                            "Sair"
                    );
                    int option = input.readMenuOption(5);
                    switch (option) {
                        case 1 -> launchAssessment(2);
                        case 2 -> registerAttendance();
                        case 3 -> finishSecondBimonthly();
                        case 4 -> showStudentSummary();
                        case 5 -> { return false; }
                    }
                }
            }

            case "FINISHED" -> {
                Printer.menu(
                        "Ver resultado final dos alunos",
                        "Lançar nota de recuperação",
                        "Sair"
                );
                int option = input.readMenuOption(3);
                switch (option) {
                    case 1 -> showFinalResults();
                    case 2 -> launchRecovery();
                    case 3 -> { return false; }
                }
            }

            default -> {
                Printer.error("Estado inesperado: " + statusCurso);
                return false;
            }
        }

        return true;
    }

    // ── Ações Privadas do Curso ────────────────────────────────────────────────────────────────

    private void startCourse() {
        try {
            course = course.start();
            Printer.success("Curso iniciado! 1º Bimestre em andamento.");
        } catch (Exception e) {
            Printer.error(e.getMessage());
        }
    }

    private void launchAssessment(int bimesterOrder) {
        Printer.section("LANÇAR NOTA — " + bimesterOrder + "º Bimestre");

        ID studentId = selectStudent();
        if (studentId == null) return;

        String assessmentName = input.readLine("  Nome da avaliação (ex: Prova 1): ");
        double scoreValue = input.readDouble("  Nota (0.0 a 10.0): ");

        Score<Double> score = new DefaultScore(scoreValue);
        String assessId = "ASS-" + assessmentCounter.getAndIncrement();

        Assessment assessment = new NotStartedAssessment(new DefaultID(assessId), assessmentName);
        Assessment inProgress = assessment.start();
        Assessment evaluated  = inProgress.generate_score(score);

        course = course.addAssessment(bimesterOrder, studentId, evaluated);

        Printer.success("Nota " + String.format("%.2f", scoreValue)
                + " lançada para " + course.student(studentId).name()
                + " [" + assessmentName + "]");
    }

    private void registerAttendance() {
        Printer.section("REGISTRAR PRESENÇA / FALTA");

        ID studentId = selectStudent();
        if (studentId == null) return;

        Attendance current = course.attendanceFor(studentId);
        Printer.info("Frequência atual",
                String.format("%.0f%%  (%d/%d aulas)",
                        current.attendanceRate() * 100,
                        current.attendedClasses(),
                        current.totalClasses()));

        Printer.menu("Presença", "Falta");
        int op = input.readMenuOption(2);

        Attendance updated = (op == 1) ? current.registerPresence() : current.registerAbsence();
        course = course.updateAttendance(studentId, updated);

        String tipo = (op == 1) ? "Presença" : "Falta";
        Printer.success(tipo + " registrada. Frequência: "
                + String.format("%.0f%%", updated.attendanceRate() * 100)
                + " (" + updated.attendedClasses() + "/" + updated.totalClasses() + ")");
    }

    private void finishFirstBimonthly() {
        try {
            course = course.finishFirstBimonthly();
            Printer.success("1º Bimestre encerrado. 2º Bimestre iniciado!");
            showBimonthlyAverages(1);
        } catch (Exception e) {
            Printer.error(e.getMessage());
        }
    }

    private void finishSecondBimonthly() {
        try {
            // 1. Fecha o 2º bimestre — semestre vira FinishedSemester, curso ainda é InProgressCourse
            course = course.finishSecondBimonthly();

            FinishedSemester fs = (FinishedSemester) course.semester();

            // 2. Transita estado de cada aluno enquanto o curso ainda aceita mutação
            for (Map.Entry<ID, Student> entry : course.students().entrySet()) {
                ID sid = entry.getKey();
                Student st = entry.getValue();
                Attendance att = course.attendanceFor(sid);

                if (!att.meetsMinimumRequirement()) {
                    course = course.updateStudentState(sid, st.fail());
                } else if (fs.studentApprovedDirect(sid)) {
                    course = course.updateStudentState(sid, st.approve());
                } else if (fs.studentEligibleForRecovery(sid)) {
                    course = course.updateStudentState(sid, st.sendToRecovery());
                } else {
                    course = course.updateStudentState(sid, st.fail());
                }
            }

            // 3. Lacra o curso — FinishedCourse bloqueia toda mutação a partir daqui
            course = course.finish();

            Printer.success("2º Bimestre encerrado. Semestre finalizado!");

            // 4. Exibe — apenas leitura
            showFinalResults();

        } catch (Exception e) {
            Printer.error(e.getMessage());
        }
    }

    private void showStudentSummary() {
        Printer.section("SITUAÇÃO DOS ALUNOS");
        Semester sem = course.semester();

        for (Map.Entry<ID, Student> entry : course.students().entrySet()) {
            ID sid = entry.getKey();
            Student st = entry.getValue();
            Attendance att = course.attendanceFor(sid);

            System.out.printf("%n  ► %s  (Matrícula %d)%n", st.name(), st.registration());

            // Médias disponíveis por bimestre
            String semStatus = sem.status();
            if ("FIRST_BIMONTHLY_IN_PROGRESS".equals(semStatus)) {
                List<Assessment> list = sem.firstBimonthly().assessmentsFor(sid);
                printAssessments(list);
                if (!list.isEmpty()) {
                    double avg = sem.firstBimonthly().averageFor(sid).value();
                    Printer.grade("    Média parcial 1º Bim", avg);
                }
            } else {
                double n1 = sem.firstBimonthly().averageFor(sid).value();
                Printer.grade("    Nota 1º Bimestre (N1)", n1);
                if ("SECOND_BIMONTHLY_IN_PROGRESS".equals(semStatus)) {
                    List<Assessment> list = sem.secondBimonthly().assessmentsFor(sid);
                    printAssessments(list);
                    if (!list.isEmpty()) {
                        double avg = sem.secondBimonthly().averageFor(sid).value();
                        Printer.grade("    Média parcial 2º Bim", avg);
                    }
                }
            }

            Printer.info("    Frequência",
                    String.format("%.0f%%  (%d/%d aulas)",
                            att.attendanceRate() * 100,
                            att.attendedClasses(),
                            att.totalClasses()));
        }
    }

    private void showBimonthlyAverages(int order) {
        Printer.section("MÉDIAS DO " + order + "º BIMESTRE");
        Semester sem = course.semester();

        for (Map.Entry<ID, Student> entry : course.students().entrySet()) {
            ID sid = entry.getKey();
            double avg = (order == 1)
                    ? sem.firstBimonthly().averageFor(sid).value()
                    : sem.secondBimonthly().averageFor(sid).value();
            Printer.grade("  " + entry.getValue().name(), avg);
        }
    }

    private void showFinalResults() {
        Printer.section("RESULTADO FINAL DO SEMESTRE");

        if (!(course.semester() instanceof FinishedSemester fs)) {
            Printer.warn("O semestre ainda não foi encerrado.");
            return;
        }

        for (Map.Entry<ID, Student> entry : course.students().entrySet()) {
            ID sid = entry.getKey();
            Student st = entry.getValue();
            Attendance att = course.attendanceFor(sid);

            double n1   = fs.firstBimonthly().averageFor(sid).value();
            double n2   = fs.secondBimonthly().averageFor(sid).value();
            double m    = fs.semesterAverage(sid).value();
            double freq = att.attendanceRate() * 100;

            System.out.printf("%n  ► %s  (Matrícula %d)%n", st.name(), st.registration());
            Printer.grade("    N1 (1º Bimestre)", n1);
            Printer.grade("    N2 (2º Bimestre)", n2);
            Printer.grade("    Média Semestral (M)", m);
            Printer.info("    Frequência", String.format("%.0f%%", freq));

            // Decisão de aprovação / recuperação / reprovação
            switch (st.status()) {
                case "APPROVED"    -> Printer.success("    APROVADO DIRETAMENTE");
                case "IN_RECOVERY" -> Printer.warn("    ENCAMINHADO PARA RECUPERAÇÃO (M entre 4.0 e 5.99)");
                case "FAILED"      -> {
                    if (!att.meetsMinimumRequirement()) {
                        Printer.error("    REPROVADO POR FALTA (frequência abaixo de 75%)");
                    } else {
                        Printer.error("    REPROVADO (M abaixo de 4.0)");
                    }
                }
            }
        }
    }

    private void launchRecovery() {
        if (!(course.semester() instanceof FinishedSemester fs)) {
            Printer.warn("O semestre ainda não foi encerrado.");
            return;
        }

        // Apenas alunos em recuperação
        List<Map.Entry<ID, Student>> eligible = course.students().entrySet().stream()
                .filter(e -> "IN_RECOVERY".equals(e.getValue().status()))
                .toList();

        if (eligible.isEmpty()) {
            Printer.warn("Nenhum aluno em situação de recuperação.");
            return;
        }

        Printer.section("LANÇAR NOTA DE RECUPERAÇÃO");

        for (Map.Entry<ID, Student> entry : eligible) {
            ID sid = entry.getKey();
            Student st = entry.getValue();

            double n1 = fs.firstBimonthly().averageFor(sid).value();
            double n2 = fs.secondBimonthly().averageFor(sid).value();
            double m  = fs.semesterAverage(sid).value();

            System.out.printf("%n  Aluno: %s%n", st.name());
            Printer.grade("  N1", n1);
            Printer.grade("  N2", n2);
            Printer.grade("  Média Semestral (M)", m);

            double r = input.readDouble("  Nota de recuperação (0.0 a 10.0): ");
            Score<Double> recovery = new DefaultScore(r);

            double mFinal = fs.finalAverageWithRecovery(sid, recovery).value();
            Printer.grade("  Média Final (Nmaior + R) / 2", mFinal);

            if (fs.studentApprovedAfterRecovery(sid, recovery)) {
                Printer.success("  APROVADO após recuperação.");
            } else {
                Printer.error("  REPROVADO após recuperação (Mfinal < 6.0).");
            }
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private ID selectStudent() {
        Printer.section("Selecionar Aluno");
        List<Map.Entry<ID, Student>> list = new ArrayList<>(course.students().entrySet());
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("  [%d] %s  (Matrícula %d)%n",
                    i + 1,
                    list.get(i).getValue().name(),
                    list.get(i).getValue().registration());
        }
        System.out.print("\n  Opção: ");
        int op = input.readMenuOption(list.size());
        return list.get(op - 1).getKey();
    }

    private void printAssessments(List<Assessment> assessments) {
        for (Assessment a : assessments) {
            System.out.printf("      - %-20s  %.2f%n", a.name(), a.score().value());
        }
    }
}
