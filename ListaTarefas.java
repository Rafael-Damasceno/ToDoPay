import java.time.LocalDate;

public class ListaTarefas {

    private Tarefa head;

    //  CONSTRUTOR
    public ListaTarefas() {
        head = null;
    }

    // =====================================================
    //  ADICIONAR (no final)
    // =====================================================
    public void adicionar(Tarefa nova) {
        if (head == null) {
            head = nova;
        } else {
            Tarefa atual = head;
            while (atual.getProximo() != null) {
                atual = atual.getProximo();
            }
            atual.setProximo(nova);
        }
    }

    // =====================================================
    //  LISTAR
    // =====================================================
    public void listar() {
        if (head == null) {
            System.out.println("Lista vazia!");
            return;
        }

        Tarefa atual = head;
        int i = 0;

        while (atual != null) {
            System.out.println(i + " -> " + atual);
            atual = atual.getProximo();
            i++;
        }
    }

    // =====================================================
    //  REMOVER
    // =====================================================
    public void remover(int indice) {
        if (head == null) {
            System.out.println("Lista vazia!");
            return;
        }

        if (indice == 0) {
            head = head.getProximo();
            return;
        }

        Tarefa atual = head;

        for (int i = 0; i < indice - 1; i++) {
            if (atual.getProximo() == null) {
                System.out.println("Índice inválido!");
                return;
            }
            atual = atual.getProximo();
        }

        if (atual.getProximo() != null) {
            atual.setProximo(atual.getProximo().getProximo());
        }
    }

    // =====================================================
    //  CONCLUIR
    // =====================================================
    public void concluir(int indice) {
        Tarefa atual = head;

        for (int i = 0; i < indice; i++) {
            if (atual == null) {
                System.out.println("Índice inválido!");
                return;
            }
            atual = atual.getProximo();
        }

        if (atual != null) {
            atual.marcarComoConcluida();
        }
    }

    // =====================================================
    //  ORDENAR POR PRIORIDADE
    // =====================================================
    public void ordenarPorPrioridade() {
        if (head == null) return;

        for (Tarefa i = head; i != null; i = i.getProximo()) {
            for (Tarefa j = i.getProximo(); j != null; j = j.getProximo()) {

                if (i.getValorPrioridade() < j.getValorPrioridade()) {
                    trocar(i, j);
                }
            }
        }
    }

    // =====================================================
    //  ORDENAR POR DATA
    // =====================================================
    public void ordenarPorData() {
        if (head == null) return;

        for (Tarefa i = head; i != null; i = i.getProximo()) {
            for (Tarefa j = i.getProximo(); j != null; j = j.getProximo()) {

                LocalDate dataI = i.getDataPrazo();
                LocalDate dataJ = j.getDataPrazo();

                if (dataI.isAfter(dataJ)) {
                    trocar(i, j);
                }
            }
        }
    }

    // =====================================================
    //  TROCAR DADOS (NÃO TROCA NÓS)
    // =====================================================
    private void trocar(Tarefa a, Tarefa b) {
        String titulo = a.getTitulo();
        String descricao = a.getDescricao();
        String prioridade = a.getPrioridade();
        LocalDate prazo = a.getDataPrazo();
        boolean concluida = a.isConcluida();

        // copia b → a
        a.copiarDadosDe(b);

        // restaura dados antigos de a → b
        b.setProximo(b.getProximo()); // mantém ponteiro
        b = restaurarDados(b, titulo, descricao, prioridade, prazo, concluida);
    }

    // método auxiliar
    private Tarefa restaurarDados(Tarefa t, String titulo, String descricao,
                                 String prioridade, LocalDate prazo, boolean concluida) {

        // criando objeto temporário para copiar
        Tarefa temp = new Tarefa(titulo, descricao, prioridade, prazo);
        if (concluida) temp.marcarComoConcluida();

        t.copiarDadosDe(temp);
        return t;
    }
}