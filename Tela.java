import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Tela extends JFrame {

    private ListaTarefas lista = new ListaTarefas();

    private JTextField campoTitulo;
    private JTextField campoDescricao;
    private JTextField campoData;
    private JComboBox<String> comboPrioridade;
    private JTextArea areaTarefas;

    public Tela() {
        setTitle("To-Do List");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel Principal com margens
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(painelPrincipal);

        // --- PAINEL DE ENTRADA (Topo) ---
        JPanel painelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        painelInput.setBorder(BorderFactory.createTitledBorder("Nova Tarefa"));

        painelInput.add(new JLabel("Título:"));
        campoTitulo = new JTextField();
        painelInput.add(campoTitulo);

        painelInput.add(new JLabel("Descrição:"));
        campoDescricao = new JTextField();
        painelInput.add(campoDescricao);

        painelInput.add(new JLabel("Prazo (AAAA-MM-DD):"));
        campoData = new JTextField();
        painelInput.add(campoData);

        painelInput.add(new JLabel("Prioridade:"));
        comboPrioridade = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
        painelInput.add(comboPrioridade);

        painelPrincipal.add(painelInput, BorderLayout.NORTH);

        // --- ÁREA DE LISTAGEM (Centro) ---
        areaTarefas = new JTextArea();
        areaTarefas.setEditable(false);
        areaTarefas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaTarefas);
        scroll.setBorder(BorderFactory.createTitledBorder("Minhas Tarefas"));
        painelPrincipal.add(scroll, BorderLayout.CENTER);

        // --- PAINEL DE BOTÕES (Lado Direito) ---
        JPanel painelBotoes = new JPanel(new GridLayout(6, 1, 5, 5));
        
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnRemover = new JButton("Remover");
        JButton btnConcluir = new JButton("Concluir");
        JButton btnSortPrio = new JButton("Ord. Prioridade");
        JButton btnSortData = new JButton("Ord. Data");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnConcluir);
        painelBotoes.add(btnRemover);
        painelBotoes.add(new JSeparator());
        painelBotoes.add(btnSortPrio);
        painelBotoes.add(btnSortData);

        painelPrincipal.add(painelBotoes, BorderLayout.EAST);

        // AÇÕES
        btnAdicionar.addActionListener(e -> adicionarTarefa());
        btnRemover.addActionListener(e -> removerTarefa());
        btnConcluir.addActionListener(e -> concluirTarefa());
        btnSortPrio.addActionListener(e -> { lista.ordenarPorPrioridade(); atualizarTela(); });
        btnSortData.addActionListener(e -> { lista.ordenarPorData(); atualizarTela(); });
    }

    private void adicionarTarefa() {
        String titulo = campoTitulo.getText();
        String descricao = campoDescricao.getText();
        String prioridade = (String) comboPrioridade.getSelectedItem();

        LocalDate dataPrazo;
        try {
            dataPrazo = LocalDate.parse(campoData.getText()); // Formato esperado: YYYY-MM-DD
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido! Use AAAA-MM-DD");
            return;
        }

        Tarefa t = new Tarefa(titulo, descricao, prioridade, dataPrazo);
        lista.adicionar(t);

        // Limpar campos após adicionar
        campoTitulo.setText("");
        campoDescricao.setText("");
        campoData.setText("");
        atualizarTela();
    }

    private void removerTarefa() {
    try {
        String input = JOptionPane.showInputDialog("Índice da tarefa:");
        int indice = Integer.parseInt(input);

        lista.remover(indice);
        atualizarTela();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Digite um número válido!");
    }
}

    private void concluirTarefa() {
    try {
        String input = JOptionPane.showInputDialog("Índice da tarefa:");
        int indice = Integer.parseInt(input);

        lista.concluir(indice);
        atualizarTela();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Digite um número válido!");
    }
}

    private void atualizarTela() {
        areaTarefas.setText(lista.listar());
    }
}