import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        ListaTarefas lista = new ListaTarefas();

        // Criando tarefas
        lista.adicionar(new Tarefa("Estudar ED", "Lista encadeada", "URGENTE", LocalDate.of(2026, 5, 10)));
        lista.adicionar(new Tarefa("Academia", "Treino A", "TRANQUILO", LocalDate.of(2026, 5, 20)));
        lista.adicionar(new Tarefa("Prova", "Cálculo 3", "URGENTE", LocalDate.of(2026, 5, 5)));
        lista.adicionar(new Tarefa("Projeto", "POO", "TRANQUILO", LocalDate.of(2026, 5, 15)));

        System.out.println("=== LISTA ORIGINAL ===");
        lista.listar();

        // 🔴 ORDENAR POR PRIORIDADE
        System.out.println("\n=== ORDENAR POR PRIORIDADE ===");
        lista.ordenarPorPrioridade();
        lista.listar();

        // 🔴 ORDENAR POR DATA
        System.out.println("\n=== ORDENAR POR DATA ===");
        lista.ordenarPorData();
        lista.listar();

        // ❌ REMOVER
        System.out.println("\n=== REMOVENDO índice 1 ===");
        lista.remover(1);
        lista.listar();

        // ✔️ CONCLUIR
        System.out.println("\n=== CONCLUINDO índice 0 ===");
        lista.concluir(0);
        lista.listar();
    }
}