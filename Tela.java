import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Tela extends JFrame {

    private ListaTarefas listaAtivas = new ListaTarefas();
    private ListaTarefas listaConcluidas = new ListaTarefas();
    private DefaultListModel<String> modeloLista;
    private JList<String> listaTarefasUI;
    private JComboBox<String> comboFiltro;

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

        JPanel painelControles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelControles.setBackground(COR_FUNDO);

        // ComboBox para filtrar entre Pendentes e Concluídas
        String[] opcoesFiltro = {"Tarefas Pendentes", "Tarefas Concluídas"};
        comboFiltro = new JComboBox<>(opcoesFiltro);
        comboFiltro.setBackground(COR_COMBO);
        comboFiltro.setForeground(Color.WHITE);
        comboFiltro.setFocusable(false);
        comboFiltro.addActionListener(e -> atualizarTela());
        painelControles.add(comboFiltro);

        // ComboBox de Ordenação
        String[] opcoesOrdem = {"Ordenar tarefas", "Por Prioridade", "Por Data"};
        JComboBox<String> comboOrdenar = new JComboBox<>(opcoesOrdem);
        comboOrdenar.setBackground(COR_COMBO);
        comboOrdenar.setForeground(Color.WHITE);
        comboOrdenar.setFocusable(false);
        comboOrdenar.addActionListener(e -> {
            ListaTarefas listaAtual = getListaAtual();
            int index = comboOrdenar.getSelectedIndex();
            if (index == 1) listaAtual.ordenarPorPrioridade();
            if (index == 2) listaAtual.ordenarPorData();
            atualizarTela();
        });
        painelControles.add(comboOrdenar);

        painelSubTopo.add(painelControles, BorderLayout.EAST);

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
            listaAtivas.adicionar(t);
            comboFiltro.setSelectedIndex(0); // Volta para a vista de pendentes ao criar
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
    // MODAL DETALHES / EDITAR
    // =========================================================
    private void abrirModalEditar(int index) {
        // Recupera a tarefa real para mostrar as informações
        ListaTarefas listaAtual = getListaAtual();
        Tarefa tarefa = listaAtual.getTarefa(index);
        if (tarefa == null) return;

        JDialog dialog = new JDialog(this, "Informações da Tarefa", true);
        dialog.setSize(550, 400);
        dialog.setLocationRelativeTo(this);

        // Inicia exibindo apenas as informações (Modo Visualização)
        exibirModoVisualizacao(dialog, tarefa, index, listaAtual);
        dialog.setVisible(true);
    }

    private void exibirModoVisualizacao(JDialog dialog, Tarefa tarefa, int index, ListaTarefas listaDona) {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JPanel painelDialog = new JPanel(new BorderLayout(10, 20));
        painelDialog.setBackground(CINZA_CLARO);
        painelDialog.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Exibição dos dados atuais como Labels
        JPanel painelInfo = new JPanel(new GridLayout(4, 1, 0, 15));
        painelInfo.setBackground(CINZA_CLARO);
        
        painelInfo.add(criarLabelEscura("Título: " + tarefa.getTitulo()));
        painelInfo.add(criarLabelEscura("Descrição: " + tarefa.getDescricao()));
        painelInfo.add(criarLabelEscura("Prazo: " + tarefa.getDataPrazo().format(formatador)));
        painelInfo.add(criarLabelEscura("Prioridade: " + tarefa.getPrioridade()));

        painelDialog.add(painelInfo, BorderLayout.CENTER);

        // Botões de ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        painelBotoes.setBackground(CINZA_CLARO);

        JButton btnRemover = criarBotaoSecundario("Remover");
        btnRemover.addActionListener(e -> {
            listaDona.remover(index);
            atualizarTela();
            dialog.dispose();
        });

        // Só exibe o botão concluir se a tarefa estiver na lista de Ativas
        if (listaDona == listaAtivas) {
            JButton btnConcluir = criarBotaoSecundario("Concluir");
            btnConcluir.addActionListener(e -> {
                Tarefa t = listaAtivas.getTarefa(index);
                if (t != null) {
                    listaAtivas.remover(index);
                    t.setProximo(null); // Limpa o ponteiro antes de mover para outra lista
                    t.marcarComoConcluida();
                    listaConcluidas.adicionar(t);
                }
                atualizarTela();
                dialog.dispose();
            });
            painelBotoes.add(btnConcluir);
        }

        JButton btnEditar = criarBotaoPrincipal("Editar");
        btnEditar.addActionListener(e -> exibirModoEdicao(dialog, tarefa, index, listaDona));

        painelBotoes.add(btnRemover);
        painelBotoes.add(btnEditar);
        painelDialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setContentPane(painelDialog);
        dialog.revalidate();
    }

    private void exibirModoEdicao(JDialog dialog, Tarefa tarefa, int index, ListaTarefas listaDona) {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JPanel painelDialog = new JPanel(new BorderLayout(10, 20));
        painelDialog.setBackground(CINZA_CLARO);
        painelDialog.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField campoTitulo = new JTextField(tarefa.getTitulo());
        JTextField campoDescricao = new JTextField(tarefa.getDescricao());
        JTextField campoData = new JTextField(tarefa.getDataPrazo().format(formatador));
        JComboBox<String> comboPrioridade = new JComboBox<>(new String[]{"Tranquilo", "Urgente"});
        comboPrioridade.setSelectedItem(tarefa.getPrioridade());

        JPanel painelLabels = new JPanel(new GridLayout(4, 1, 0, 15));
        painelLabels.setBackground(CINZA_CLARO);
        painelLabels.add(criarLabelEscura("Novo Título:"));
        painelLabels.add(criarLabelEscura("Nova Descrição:"));
        painelLabels.add(criarLabelEscura("Novo Prazo:"));
        painelLabels.add(criarLabelEscura("Nova Prioridade:"));

        JPanel painelCampos = new JPanel(new GridLayout(4, 1, 0, 15));
        painelCampos.setBackground(CINZA_CLARO);
        painelCampos.add(campoTitulo);
        painelCampos.add(campoDescricao);
        painelCampos.add(campoData);
        painelCampos.add(comboPrioridade);

        painelDialog.add(painelLabels, BorderLayout.WEST);
        painelDialog.add(painelCampos, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setBackground(CINZA_CLARO);

        JButton btnCancelar = criarBotaoSecundario("Cancelar");
        btnCancelar.addActionListener(e -> exibirModoVisualizacao(dialog, tarefa, index, listaDona));

        JButton btnSalvar = criarBotaoPrincipal("Salvar");
        btnSalvar.addActionListener(e -> {
            if (campoTitulo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "O título da tarefa é obrigatório!");
                return;
            }

            try {
                LocalDate dataPrazo = LocalDate.parse(campoData.getText(), formatador);
                tarefa.setTitulo(campoTitulo.getText());
                tarefa.setDescricao(campoDescricao.getText()); 
                tarefa.setPrioridade((String) comboPrioridade.getSelectedItem());
                tarefa.setDataPrazo(dataPrazo);

                atualizarTela();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Formato de data inválido!");
            }
        });

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        painelDialog.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setContentPane(painelDialog);
        dialog.revalidate();
    }

    // =========================================================
    // MÉTODOS UTILITÁRIOS
    // =========================================================
    private ListaTarefas getListaAtual() {
        if (comboFiltro == null) return listaAtivas;
        return comboFiltro.getSelectedIndex() == 0 ? listaAtivas : listaConcluidas;
    }

    private void atualizarTela() {
        modeloLista.clear();
        String textoLista = getListaAtual().listar(); 
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

    private JButton criarBotaoSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(SEGUNDO_BOTAO);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        return btn;
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