import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarefa {

    private String titulo;
    private String descricao;
    private String prioridade;
    private LocalDate dataCriacao;
    private LocalDate dataPrazo;
    private boolean concluida;

    private Tarefa proximo;

    public Tarefa(String titulo, String descricao, String prioridade, LocalDate dataPrazo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.dataPrazo = dataPrazo;
        this.dataCriacao = LocalDate.now();
        this.concluida = false;
        this.proximo = null;
    }

    public void marcarComoConcluida() {
        this.concluida = true;
    }

    public int getValorPrioridade() {
        if (prioridade.equals("Urgente")) return 2;
        return 1;
    }

    public void copiarDadosDe(Tarefa outra) {
        this.titulo = outra.titulo;
        this.descricao = outra.descricao;
        this.prioridade = outra.prioridade;
        this.dataPrazo = outra.dataPrazo;
        this.concluida = outra.concluida;
    }

    public String toString() {
        // Formatador para a data de saída
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = dataPrazo.format(formatador);

        return  titulo + " | Prazo: " + dataFormatada +
               " | Prioridade: " + prioridade +
               " | Status: " + (concluida ? "Concluída" : "Pendente");
    }

    // getters
    public Tarefa getProximo() { return proximo; }
    public void setProximo(Tarefa proximo) { this.proximo = proximo; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getPrioridade() { return prioridade; }
    public LocalDate getDataPrazo() { return dataPrazo; }
    public boolean isConcluida() { return concluida; }
}