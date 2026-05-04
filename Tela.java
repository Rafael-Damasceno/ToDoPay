import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Tela extends JFrame {

    private ListaTarefas lista = new ListaTarefas();
    private DefaultListModel<String> modeloLista;
    private JList<String> listaTarefasUI;

    // Paleta de Cores
    private final Color COR_FUNDO = new Color(43, 3, 59);
    private final Color PRINCIPAL_BOTAO = new Color(255, 200, 92);
    private final Color SEGUNDO_BOTAO = new Color(156, 213, 255);
    private final Color COR_COMBO = new Color(143, 1, 119);
    private final Color CINZA_CLARO = new Color(217, 217, 217);
    private final Color TEXTO_ESCURO = new Color(30, 20, 60);

    public Tela() {
        setTitle("To-Do List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel Principal 
        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(20, 30, 20, 30));
        setContentPane(painelPrincipal);

        // Cabeçalho
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(COR_FUNDO);
        painelTopo.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel tituloApp = new JLabel("For U To-Do");
        tituloApp.setFont(new Font("SansSerif", Font.BOLD, 40));
        tituloApp.setForeground(Color.WHITE);
        painelTopo.add(tituloApp, BorderLayout.NORTH);
        // Linha decorativa abaixo do título
        tituloApp.setBorder(new EmptyBorder(0, 0, 5, 0)); 
        JPanel linhaDecorativa = new JPanel();
        linhaDecorativa.setBackground(COR_COMBO);
        linhaDecorativa.setPreferredSize(new Dimension(0, 5)); 
        painelTopo.add(linhaDecorativa, BorderLayout.CENTER);

        // Sub-cabeçalho (Minhas Tarefas + Ordenar)
        JPanel painelSubTopo = new JPanel(new BorderLayout());
        painelSubTopo.setBackground(COR_FUNDO);
        painelSubTopo.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel lblMinhasTarefas = new JLabel("Minhas tarefas");
        lblMinhasTarefas.setFont(new Font("SansSerif", Font.PLAIN, 20));
        lblMinhasTarefas.setForeground(Color.WHITE);
        painelSubTopo.add(lblMinhasTarefas, BorderLayout.WEST);

        // ComboBox de Ordenação
        String[] opcoesOrdem = {"Ordenar tarefas", "Por Prioridade", "Por Data"};
        JComboBox<String> comboOrdenar = new JComboBox<>(opcoesOrdem);
        comboOrdenar.setBackground(COR_COMBO);
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

        // Quadro principal de listagem de tarefas
        modeloLista = new DefaultListModel<>();
        listaTarefasUI = new JList<>(modeloLista);
        listaTarefasUI.setBackground(CINZA_CLARO);
        listaTarefasUI.setFont(new Font("SansSerif", Font.BOLD, 16));
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
        scroll.setBorder(BorderFactory.createEmptyBorder()); // Remove a borda padrão
        painelPrincipal.add(scroll, BorderLayout.CENTER);

        // Parte inferior com o botão de adicionar
        JPanel painelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelBottom.setBackground(COR_FUNDO);

        JButton btnAdicionar = criarBotaoPrincipal("Adicionar");
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
        // Data formatada e previamente preenchida com a data atual
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        campoData.setText(LocalDate.now().format(formatador));

        JComboBox<String> comboPrioridade = new JComboBox<>(new String[]{"Tranquilo", "Urgente"});

        JPanel painelLabels = new JPanel(new GridLayout(4, 1, 0, 15));
        painelLabels.setBackground(CINZA_CLARO);
        painelLabels.add(criarLabelEscura("Título:"));
        painelLabels.add(criarLabelEscura("Descrição:"));
        painelLabels.add(criarLabelEscura("Prazo:"));
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
        btnCancelar.setBackground(SEGUNDO_BOTAO);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnCancelar.addActionListener(e -> dialog.dispose());

        // Salvar a nova tarefa 
        JButton btnOk = criarBotaoPrincipal("Criar");
        btnOk.addActionListener(e -> {
            // Verifica se o título foi preenchido
            String titulo = campoTitulo.getText().trim();
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "O título da tarefa é obrigatório!");
                return;
            }

            // Verifica se a data está no formato correto
            try {
            // Formata a data de entrada do usuário
            DateTimeFormatter formatador2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataPrazo = LocalDate.parse(campoData.getText(), formatador2);
            
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
            JOptionPane.showMessageDialog(dialog, "Formato de data inválido! Use DD/MM/AAAA");
        }
        });

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnOk);
        painelDialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setContentPane(painelDialog);
        dialog.setVisible(true);
    }

    // =========================================================
    // MODAL EXCLUIR / DETALHES
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

        JPanel painelBotoes = new JPanel(new BorderLayout());
        painelBotoes.setBackground(CINZA_CLARO);

        // Removendo a tarefa
        JButton btnRemover = new JButton("Remover");
        btnRemover.setBackground(SEGUNDO_BOTAO);
        btnRemover.setForeground(Color.BLACK);
        btnRemover.setFocusPainted(false);
        btnRemover.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnRemover.addActionListener(e -> {
            lista.remover(index);
            atualizarTela();
            dialog.dispose();
        });

        JButton btnConcluir = criarBotaoPrincipal("Concluir");
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
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(TEXTO_ESCURO);
        return label;
    }

    // Método para criar botões com estilo definido
    private JButton criarBotaoPrincipal(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(PRINCIPAL_BOTAO);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // Padding interno
        return btn;
    }
}