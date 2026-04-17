import java.time.LocalDate;

public class Tarefa {

    // 🔹 ATRIBUTOS (dados do nó)
    private String titulo;
    private String descricao;
    private String prioridade; // "URGENTE" ou "TRANQUILO"
    private LocalDate dataCriacao;
    private LocalDate dataPrazo;
    private boolean concluida;

    private Tarefa proximo; // ponteiro para o próximo nó

    // 🔹 CONSTRUTOR
    public Tarefa(String titulo, String descricao, String prioridade, LocalDate dataPrazo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.dataPrazo = dataPrazo;
        this.dataCriacao = LocalDate.now();
        this.concluida = false;
        this.proximo = null;
    }

    // 🔹 MÉTODOS
    public void marcarComoConcluida() {
        this.concluida = true;
    }

    public int getValorPrioridade() {
        if (prioridade.equals("URGENTE")) return 1;
        return 2;
    }

    public String toString() {
        return "[" + prioridade + "] " + titulo +
               " | Prazo: " + dataPrazo +
               (concluida ? " V" : " X");
    }

    // getters e setters básicos (exemplo)
    public Tarefa getProximo() { return proximo; }
    public void setProximo(Tarefa proximo) { this.proximo = proximo; }
}