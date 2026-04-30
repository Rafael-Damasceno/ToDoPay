import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Tela extends JFrame {

    private ListaTarefas lista = new ListaTarefas();
    private DefaultListModel<String> modeloLista;
    private JList<String> listaTarefasUI;

    // Paleta de Cores
    private final Color AZUL_FUNDO = new Color(0, 51, 77);
    private final Color LARANJA_BOTAO = new Color(255, 102, 68);
    private final Color ROXO_COMBO = new Color(85, 45, 98);
    private final Color CINZA_CLARO = new Color(217, 217, 217);
    private final Color TEXTO_ESCURO = new Color(30, 20, 60);

    public Tela() {
        setTitle("To-Do List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel Principal com a cor de fundo Azul Escuro
        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));
        painelPrincipal.setBackground(AZUL_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(20, 30, 20, 30));
        setContentPane(painelPrincipal);

        // --- CABEÇALHO (Top) ---
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(AZUL_FUNDO);
        painelTopo.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel tituloApp = new JLabel("For U To-Do");
        tituloApp.setFont(new Font("SansSerif", Font.BOLD, 32));
        tituloApp.setForeground(Color.WHITE);
        painelTopo.add(tituloApp, BorderLayout.NORTH);

        // Sub-cabeçalho (Minhas Tarefas + Ordenar)
        JPanel painelSubTopo = new JPanel(new BorderLayout());
        painelSubTopo.setBackground(AZUL_FUNDO);
        painelSubTopo.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel lblMinhasTarefas = new JLabel("Minhas tarefas");
        lblMinhasTarefas.setFont(new Font("SansSerif", Font.PLAIN, 20));
        lblMinhasTarefas.setForeground(Color.WHITE);
        painelSubTopo.add(lblMinhasTarefas, BorderLayout.WEST);

        // ComboBox de Ordenação
        String[] opcoesOrdem = {"Ordenar tarefas", "Por Prioridade", "Por Data"};
        JComboBox<String> comboOrdenar = new JComboBox<>(opcoesOrdem);
        comboOrdenar.setBackground(ROXO_COMBO);
        comboOrdenar.setForeground(Color.WHITE);
        comboOrdenar.setFocusable(false);
        comboOrdenar.addActionListener(e -> {
            int index = comboOrdenar.getSelectedIndex();
            if (index == 1) lista.ordenarPorPrioridade();
            if (index == 2) lista.ordenarPorData();
            atualizarTela();
        });
        painelSubTopo.add(comboOrdenar, BorderLayout.EAST);

        painelTopo.add(painelSubTopo, BorderLayout.SOUTH);
        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        // --- ÁREA DE LISTAGEM (Center) ---
        modeloLista = new DefaultListModel<>();
        listaTarefasUI = new JList<>(modeloLista);
        listaTarefasUI.setBackground(CINZA_CLARO);
        listaTarefasUI.setFont(new Font("SansSerif", Font.BOLD, 14));
        listaTarefasUI.setForeground(Color.BLACK);
        
        // Espaçamento interno dos itens da lista
        listaTarefasUI.setFixedCellHeight(35); 
        
        // Detecção de clique na lista para abrir o Modal de Edição
        listaTarefasUI.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    int index = listaTarefasUI.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        abrirModalEditar(index);
                        listaTarefasUI.clearSelection(); // Tira a marcação azul de seleção
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(listaTarefasUI);
        scroll.setBorder(BorderFactory.createEmptyBorder()); // Remove a borda padrão feia
        painelPrincipal.add(scroll, BorderLayout.CENTER);

        // --- PAINEL INFERIOR (Bottom) ---
        JPanel painelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelBottom.setBackground(AZUL_FUNDO);

        JButton btnAdicionar = criarBotaoLaranja("Adicionar");
        btnAdicionar.addActionListener(e -> abrirModalAdicionar());
        painelBottom.add(btnAdicionar);

        painelPrincipal.add(painelBottom, BorderLayout.SOUTH);
        
        atualizarTela();
    }

    // =========================================================
    // MODAL ADICIONAR
    // =========================================================
    private void abrirModalAdicionar() {
        JDialog dialog = new JDialog(this, "Modal adicionar", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);

        JPanel painelDialog = new JPanel(new BorderLayout(10, 20));
        painelDialog.setBackground(CINZA_CLARO);
        painelDialog.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Campos
        JTextField campoTitulo = new JTextField();
        JTextField campoDescricao = new JTextField();
        JTextField campoData = new JTextField();
        JComboBox<String> comboPrioridade = new JComboBox<>(new String[]{"Tranquilo", "Urgente"});

        JPanel painelLabels = new JPanel(new GridLayout(4, 1, 0, 15));
        painelLabels.setBackground(CINZA_CLARO);
        painelLabels.add(criarLabelEscura("Título:"));
        painelLabels.add(criarLabelEscura("Descrição:"));
        painelLabels.add(criarLabelEscura("Prazo (AAAA-MM-DD):"));
        painelLabels.add(criarLabelEscura("Prioridade:"));

        JPanel painelCampos = new JPanel(new GridLayout(4, 1, 0, 15));
        painelCampos.setBackground(CINZA_CLARO);
        painelCampos.add(campoTitulo);
        painelCampos.add(campoDescricao);
        painelCampos.add(campoData);
        painelCampos.add(comboPrioridade);

        painelDialog.add(painelLabels, BorderLayout.WEST);
        painelDialog.add(painelCampos, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setBackground(CINZA_CLARO);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnOk = criarBotaoLaranja("OK");
        btnOk.addActionListener(e -> {
            try {
                LocalDate dataPrazo = LocalDate.parse(campoData.getText());
                Tarefa t = new Tarefa(
                    campoTitulo.getText(), 
                    campoDescricao.getText(), 
                    (String) comboPrioridade.getSelectedItem(), 
                    dataPrazo
                );
                lista.adicionar(t);
                atualizarTela();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Formato de data inválido! Use AAAA-MM-DD");
            }
        });

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnOk);
        painelDialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setContentPane(painelDialog);
        dialog.setVisible(true);
    }

    // =========================================================
    // MODAL EDITAR / DETALHES
    // =========================================================
    private void abrirModalEditar(int index) {
        // Recupera a tarefa real para mostrar as informações
        Tarefa tarefa = lista.getTarefa(index);

        JDialog dialog = new JDialog(this, "Modal editar", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);

        JPanel painelDialog = new JPanel(new BorderLayout(10, 20));
        painelDialog.setBackground(CINZA_CLARO);
        painelDialog.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Informações da Tarefa
        JPanel painelInfo = new JPanel(new GridLayout(4, 1, 0, 10));
        painelInfo.setBackground(CINZA_CLARO);
        
        painelInfo.add(criarLabelEscura("Título: " + tarefa.getTitulo()));
        painelInfo.add(criarLabelEscura("Descrição: " + tarefa.getDescricao()));
        painelInfo.add(criarLabelEscura("Prazo: " + tarefa.getDataPrazo()));
        painelInfo.add(criarLabelEscura("Prioridade: " + tarefa.getPrioridade()));

        painelDialog.add(painelInfo, BorderLayout.CENTER);

        // Botões (Remover na esquerda, Concluir na direita)
        JPanel painelBotoes = new JPanel(new BorderLayout());
        painelBotoes.setBackground(CINZA_CLARO);

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBackground(Color.WHITE);
        btnRemover.setForeground(Color.BLACK);
        btnRemover.setFocusPainted(false);
        btnRemover.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnRemover.addActionListener(e -> {
            lista.remover(index);
            atualizarTela();
            dialog.dispose();
        });

        JButton btnConcluir = criarBotaoLaranja("Concluir");
        btnConcluir.addActionListener(e -> {
            lista.concluir(index);
            atualizarTela();
            dialog.dispose();
        });

        painelBotoes.add(btnRemover, BorderLayout.WEST);
        painelBotoes.add(btnConcluir, BorderLayout.EAST);
        painelDialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setContentPane(painelDialog);
        dialog.setVisible(true);
    }

    // =========================================================
    // MÉTODOS UTILITÁRIOS
    // =========================================================
    private void atualizarTela() {
        modeloLista.clear();
        // Assume que listar() retorna todas as tarefas formatadas separadas por quebra de linha (\n)
        String textoLista = lista.listar(); 
        if (textoLista != null && !textoLista.isEmpty()) {
            String[] linhas = textoLista.split("\n");
            for (String linha : linhas) {
                if (!linha.trim().isEmpty()) {
                    modeloLista.addElement("  " + linha); // Adiciona um pequeno espaço na frente para não colar na borda
                }
            }
        }
    }

    private JLabel criarLabelEscura(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(TEXTO_ESCURO);
        return label;
    }

    private JButton criarBotaoLaranja(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(LARANJA_BOTAO);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // Padding interno
        return btn;
    }
}