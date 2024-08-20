import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Item {
    String nome;
    double preco;

    public Item(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    @Override
    public String toString() {
        return nome + " - R$ " + preco;
    }
}

class Pedido {
    private final DefaultListModel<Item> itens;
    private String observacao;

    public Pedido() {
        this.itens = new DefaultListModel<>();
        this.observacao = "";
    }

    public void adicionarItem(Item item) {
        itens.addElement(item);
    }

    public void adicionarObservacao(String observacao) {
        this.observacao = observacao;
    }

    public double calcularTotal() {
        double total = 0;
        for (int i = 0; i < itens.size(); i++) {
            total += itens.getElementAt(i).preco;
        }
        return total;
    }

    public DefaultListModel<Item> getItens() {
        return itens;
    }

    public String getObservacao() {
        return observacao;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < itens.size(); i++) {
            sb.append(itens.getElementAt(i)).append("\n");
        }
        sb.append("Total: R$ ").append(calcularTotal()).append("\n");
        if (!observacao.isEmpty()) {
            sb.append("Observação: ").append(observacao).append("\n");
        }
        return sb.toString();
    }
}

public class SistemaLancheGUI {
    private final Pedido pedido;
    private final JFrame frame;
    private final JComboBox<String> itemComboBox;
    private JList<Item> sacolaList = new JList<Item>();
    private JLabel totalLabel = new JLabel();

    public SistemaLancheGUI() {
        pedido = new Pedido();
        frame = new JFrame("Sistema de Lanches");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        inputPanel.add(new JLabel("Escolha o Item:"));
        String[] itens = {"Pizza - R$ 20.00", "Hambúrguer - R$ 15.00", "Coxinha - R$ 5.00", 
                          "Crepe - R$ 12.00", "Lasanha - R$ 18.00", "Vatapá - R$ 25.00"};
        itemComboBox = new JComboBox<>(itens);
        inputPanel.add(itemComboBox);

        JButton addItemButton = new JButton("Adicionar Item");
        addItemButton.addActionListener(new AddItemListener());
        inputPanel.add(addItemButton);

        JButton addBebidaButton = new JButton("Adicionar Bebida");
        addBebidaButton.addActionListener(e -> {
            String[] bebidas = {"Coca-Cola - R$ 5.00", "Suco de Laranja - R$ 4.50", "Água Mineral - R$ 3.00"};
            String bebidaEscolhida = (String) JOptionPane.showInputDialog(frame, 
                "Escolha uma bebida:", "Bebidas", JOptionPane.QUESTION_MESSAGE, 
                null, bebidas, bebidas[0]);
            if (bebidaEscolhida != null) {
                pedido.adicionarItem(new Item(bebidaEscolhida.split(" - ")[0], 
                Double.parseDouble(bebidaEscolhida.split(" - ")[1].substring(3))));
                sacolaList.updateUI();
                totalLabel.setText("Total: R$ " + pedido.calcularTotal());
            }
        });
        inputPanel.add(addBebidaButton);

        JButton addSobremesaButton = new JButton("Adicionar Sobremesa");
        addSobremesaButton.addActionListener(e -> {
            String[] sobremesas = {"Brownie - R$ 7.00", "Sorvete - R$ 6.50", "Pudim - R$ 5.50"};
            String sobremesaEscolhida = (String) JOptionPane.showInputDialog(frame, 
                "Escolha uma sobremesa:", "Sobremesas", JOptionPane.QUESTION_MESSAGE, 
                null, sobremesas, sobremesas[0]);
            if (sobremesaEscolhida != null) {
                pedido.adicionarItem(new Item(sobremesaEscolhida.split(" - ")[0], 
                Double.parseDouble(sobremesaEscolhida.split(" - ")[1].substring(3))));
                sacolaList.updateUI();
                totalLabel.setText("Total: R$ " + pedido.calcularTotal());
            }
        });
        inputPanel.add(addSobremesaButton);

        JButton addObservationButton = new JButton("Adicionar Observação");
        addObservationButton.addActionListener(e -> {
            String observacao = JOptionPane.showInputDialog(frame, "Digite a observação:");
            pedido.adicionarObservacao(observacao);
        });
        inputPanel.add(addObservationButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        sacolaList = new JList<>(pedido.getItens());
        frame.add(new JScrollPane(sacolaList), BorderLayout.CENTER);
        new JTextArea();

        totalLabel = new JLabel("Total: R$ 0.00");
        frame.add(totalLabel, BorderLayout.SOUTH);

        JButton fecharPedidoButton = new JButton("Fechar Pedido");
        fecharPedidoButton.addActionListener(new FecharPedidoListener());
        frame.add(fecharPedidoButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private class AddItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String itemEscolhido = (String) itemComboBox.getSelectedItem();
            if (itemEscolhido != null) {
                pedido.adicionarItem(new Item(itemEscolhido.split(" - ")[0], 
                Double.parseDouble(itemEscolhido.split(" - ")[1].substring(3))));
                sacolaList.updateUI();
                totalLabel.setText("Total: R$ " + pedido.calcularTotal());
            }
        }
    }

    private class FecharPedidoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Exibir os itens do pedido
            JOptionPane.showMessageDialog(frame, pedido.toString(), "Pedido Fechado", JOptionPane.INFORMATION_MESSAGE);

            // Calcular e exibir o tempo estimado de preparo em uma nova janela
            int tempoPreparo = (int) (Math.random() * 20 + 10); // Tempo entre 10 e 30 minutos
            String mensagemTempo = "Tempo estimado de preparo: " + tempoPreparo + " minutos.";
            JOptionPane.showMessageDialog(frame, mensagemTempo, "Tempo de Preparo", JOptionPane.INFORMATION_MESSAGE);

            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SistemaLancheGUI::new);
    }
}
