import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import javax.sound.sampled.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;

public class HardwareExterno extends JFrame{
    HashMap<String, String> situacao = new HashMap<>();
    HashMap<String, String> explicacao = new HashMap<>();
    HashMap<JButton, Cartas> mapaDeCartas = new HashMap<>();

    static class Cartas {
        String nomeCarta;
        ImageIcon imgCartaIcone;

        Cartas(String nomeCarta, ImageIcon imgCartaIcone) {
            this.nomeCarta = nomeCarta;
            this.imgCartaIcone = imgCartaIcone;
        }

        public String string() {
            return nomeCarta;
        }
    }

    String[] ListaDeCartas = {
            "Cabos", "CaixadeSom", "gabinete",
            "Headphone", "Impressora", "Monitor",
            "Pendrive", "Scanner", "Teclado", "Webcam"
    };
    int linhas = 4;
    int colunas = 5;
    int larguraCartao = 138;
    int alturaCartao = 130;

    ArrayList<Cartas> conjuntoDeCartas;
    ImageIcon versoDaCartaIcone;

    int largura = colunas * larguraCartao;
    int altura = linhas * alturaCartao;

    JFrame janela = new JFrame("Hardwares Externos do Computador");
    JLabel texto = new JLabel();
    JPanel texoPainel = new JPanel();
    JPanel quadroPainel = new JPanel();
    //JPanel reiniciarPanelJogo = new JPanel();
    JButton reiniciarBotao = new JButton();
    JPanel botaoPainel = new JPanel();
    JButton voltarMenu = new JButton("Voltar ao Menu");

    int chances = 15;
    ArrayList<JButton> quadro;
    Timer tempo;
    Timer tempoDecorar;
    boolean jogoPronto = false;
    JButton carta1Selecionada;
    JButton carta2Selecionada;

    HardwareExterno() {
        configuracoesCartao();
        embaralharCartas();
        situacao();
        configurarExplicacoes();

        janela.setLayout(new BorderLayout());
        janela.setBounds(300, 20, largura, altura);
        janela.setResizable(false);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        texto.setFont(new Font("Arial", Font.PLAIN, 20));
        texto.setHorizontalAlignment(JLabel.CENTER);
        texto.setText("Chances: " + Integer.toString(chances));

        texoPainel.setPreferredSize(new Dimension(largura, 30));
        texoPainel.add(texto);
        janela.add(texoPainel, BorderLayout.NORTH);

        janela.pack(); //adc no final
        janela.setVisible(true);//tambem

        quadro = new ArrayList<>();
        quadroPainel.setLayout(new GridLayout(linhas, colunas));

        IntStream.range(0, conjuntoDeCartas.size()).forEach(i -> {
            JButton titulo = new JButton();
            mapaDeCartas.put(titulo, conjuntoDeCartas.get(i));
            titulo.setPreferredSize(new Dimension(larguraCartao, alturaCartao));
            titulo.setOpaque(true);
            titulo.setIcon(conjuntoDeCartas.get(i).imgCartaIcone);
            titulo.setFocusable(false);
            titulo.addActionListener(e -> {
                if (!jogoPronto) return;
                if (titulo.getIcon() == versoDaCartaIcone) {
                    if (carta1Selecionada == null) {
                        carta1Selecionada = titulo;
                        int entrada = quadro.indexOf(carta1Selecionada);
                        carta1Selecionada.setIcon(conjuntoDeCartas.get(entrada).imgCartaIcone);
                        reproduzirSons("/sons/somCartas.wav");
                    } else if (carta2Selecionada == null) {
                        carta2Selecionada = titulo;
                        int entrada = quadro.indexOf(carta2Selecionada);
                        carta2Selecionada.setIcon(conjuntoDeCartas.get(entrada).imgCartaIcone);
                        reproduzirSons("/sons/somCartas.wav");

                        HardwareExterno.Cartas carta1 = mapaDeCartas.get(carta1Selecionada);
                        HardwareExterno.Cartas carta2 = mapaDeCartas.get(carta2Selecionada);

                        if (carta1.nomeCarta.equals(carta2.nomeCarta)) {
                            if (explicacao.containsKey(carta1.nomeCarta)) {
                                reproduzirSons("/sons/informacao.wav");
                                JOptionPane.showMessageDialog(janela, explicacao.get(carta1.nomeCarta), "Explicação", JOptionPane.INFORMATION_MESSAGE);
                            }
                            carta1Selecionada = null;
                            carta2Selecionada = null;
                        } else {
                            chances--;
                            texto.setText("Chances: " + Integer.toString(chances));
                            tempo.start();
                        }
                        if (chances == 0) {
                            reproduzirSons("/sons/perda.wav");
                            JOptionPane.showMessageDialog(janela, situacao.get("0"), "--", JOptionPane.INFORMATION_MESSAGE);
                            reiniciarBotao.setEnabled(true);
                        } else if (todasAsCartasReveladas()) {
                            reproduzirSons("/sons/vitoria.wav");
                            JOptionPane.showMessageDialog(janela, situacao.get("vitoria"), "--", JOptionPane.INFORMATION_MESSAGE);
                            reiniciarBotao.setEnabled(true);
                        }
                    }
                }
            });
            quadro.add(titulo);
            quadroPainel.add(titulo);
        });
        janela.add(quadroPainel);

        botaoPainel.setLayout(new GridLayout(1, 2));
        botaoPainel.setPreferredSize(new Dimension(largura, 50));

        reiniciarBotao.setFont(new Font("Arial", Font.PLAIN, 16));
        reiniciarBotao.setText("Reinciar Jogo");
        reiniciarBotao.setFocusable(false);
        reiniciarBotao.setEnabled(false);
        reiniciarBotao.addActionListener(e -> {
                 reproduzirSons("/sons/somBotao.wav");
                 if (!jogoPronto) return;
                    reiniciarBotao.setEnabled(false);
                    jogoPronto = false;
                    carta1Selecionada = null;
                    carta2Selecionada = null;
                    reproduzirSons("/sons/embaralharCartas.wav");
                    embaralharCartas();

                    for (int i = 0; i < quadro.size(); i++) {
                        quadro.get(i).setIcon(conjuntoDeCartas.get(i).imgCartaIcone);
                        mapaDeCartas.put(quadro.get(i), conjuntoDeCartas.get(i));
                    }
                    chances = 15;
                    texto.setText("Chances: " + Integer.toString(chances));
                    tempoDecorar.start();
        });
        voltarMenu.setFont(new Font("Arial", Font.BOLD, 16));
        voltarMenu.addActionListener(e -> {
            reproduzirSons("/sons/somBotao.wav");
            janela.dispose();
            new Menu();
        });

        botaoPainel.add(reiniciarBotao);
        botaoPainel.add(voltarMenu);

        janela.add(botaoPainel, BorderLayout.SOUTH);

        tempo = new Timer(1000, e -> tempo());
        tempo.setRepeats(false);

        tempoDecorar = new Timer(5000, e -> tempoDecorar());
        tempoDecorar.setRepeats(false);
        tempoDecorar.start();

        janela.pack();
        janela.setVisible(true);
        System.out.println("Janela exibida com sucesso!"); // Depuração
    }

    private void tempoDecorar() {
        IntStream.range(0, quadro.size()).forEach(i -> quadro.get(i).setIcon(versoDaCartaIcone));
        jogoPronto = true;
        reiniciarBotao.setEnabled(true);
    }

    private void tempo() {
        if (jogoPronto && carta1Selecionada != null && carta2Selecionada != null) {
            carta1Selecionada.setIcon(versoDaCartaIcone);
            carta1Selecionada = null;
            carta2Selecionada.setIcon(versoDaCartaIcone);
            carta2Selecionada = null;
        }
    }

    void configuracoesCartao() {
        conjuntoDeCartas = new ArrayList<>();
        for (String nomeCarta : ListaDeCartas) {
            String caminhoImagem = "/imgHardExternos/" + nomeCarta + ".png";
            java.net.URL resourceUrl = getClass().getResource(caminhoImagem);

            if (resourceUrl == null) {
                JOptionPane.showMessageDialog(null, "Imagem não encontrada: " + nomeCarta, "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1); // Encerra o programa se uma imagem não for encontrada
            }

            ImageIcon imgCartaIcone = new ImageIcon(resourceUrl);
            Image imagemRedimensionada = imgCartaIcone.getImage().getScaledInstance(larguraCartao, alturaCartao, Image.SCALE_SMOOTH);
            imgCartaIcone = new ImageIcon(imagemRedimensionada);

            conjuntoDeCartas.add(new Cartas(nomeCarta, imgCartaIcone));
            conjuntoDeCartas.add(new Cartas(nomeCarta, imgCartaIcone));
        }

        String caminhoVerso = "/imgHardExternos/fundo.jpg";
        java.net.URL versoUrl = getClass().getResource(caminhoVerso);

        if (versoUrl == null) {
            JOptionPane.showMessageDialog(null, "Imagem do verso da carta não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Encerra o programa se a imagem do verso não for encontrada
        }

        ImageIcon versoIcone = new ImageIcon(versoUrl);
        Image versoRedimensionado = versoIcone.getImage().getScaledInstance(larguraCartao, alturaCartao, Image.SCALE_SMOOTH);
        versoDaCartaIcone = new ImageIcon(versoRedimensionado);
    }

    void embaralharCartas() {
        for (int i = 0; i < conjuntoDeCartas.size(); i++) {
            int j = (int) (Math.random() * conjuntoDeCartas.size());
            Cartas temp = conjuntoDeCartas.get(i);
            conjuntoDeCartas.set(i, conjuntoDeCartas.get(j));
            conjuntoDeCartas.set(j, temp);
        }
    }

    void situacao() {
        situacao = new HashMap<>();
        situacao.put("0", "Você perdeu! Tente novamente!");
        situacao.put("vitoria", "Parabéns! Você venceu!");
    }

    private boolean todasAsCartasReveladas() {
        return quadro.stream().allMatch(carta -> !carta.getIcon().equals(versoDaCartaIcone));
    }

    void configurarExplicacoes() {
        explicacao = new HashMap<>();
        explicacao.put("Cabos", "Os cabos em um computador têm a função de conectar\ne transmitir energia, dados e sinais entre os componentes internos\ne externos. Eles podem ser classificados em três categorias principais:\nCabos de energia – Fornecem eletricidade para o funcionamento do computador e seus periféricos.\nExemplo:\nCabo de força da fonte de alimentação.\nCabos de dados – Transmitem informações entre os dispositivos internos e externos.\nExemplo:\nSATA (para conectar HDs e SSDs à placa-mãe).\nCabos de rede – Permitem a conexão do computador à internet ou\na outras máquinas em uma rede.\nExemplo:\nCabo de rede Ethernet (RJ-45)");
        explicacao.put("CaixadeSom", "A caixa de som tem a função de converter sinais\nelétricos em ondas sonoras, permitindo que o usuário ouça áudio\nreproduzido pelo computador. ");
        explicacao.put("gabinete", "O gabinete é a estrutura que abriga e protege os principais\ncomponentes de um computador, como a placa-mãe, processador, memória RAM,\nfonte de alimentação, HD/SSD e placas adicionais.");
        explicacao.put("Headphone", "O headphone é um dispositivo de áudio utilizado para reprodução\nde som diretamente nos ouvidos do usuário. Ele pode ser com fio (P2, USB)\n ou sem fio (Bluetooth) e pode incluir um microfone embutido.");
        explicacao.put("Impressora", "A impressora é um periférico de saída que tem a função de transferir\ninformações digitais do computador para o papel ou outros materiais físicos.");
        explicacao.put("Monitor", "O monitor é um periférico de saída que tem a função de exibir informações\nvisuais geradas pelo computador, como imagens, vídeos e textos.");
        explicacao.put("Pendrive", "O pendrive é um dispositivo de armazenamento portátil que utiliza\nmemória flash para salvar e transferir arquivos entre computadores\ne outros dispositivos.");
        explicacao.put("Scanner", "O scanner é um periférico de entrada que tem a função de converter\ndocumentos físicos, imagens e fotos em arquivos digitais, permitindo que sejam armazenados,\neditados ou compartilhados no computador.");
        explicacao.put("Teclado", "O teclado é um periférico de entrada que permite ao usuário digitar textos,\n comandos e controlar o computador através de teclas específicas.");
        explicacao.put("Webcam", "A webcam é um periférico de entrada que captura e transmite imagens\ne vídeos em tempo real para o computador.");
    }

    private void reproduzirSons(String caminho) {
        try {
            InputStream somStream = getClass().getResourceAsStream(caminho);
            if (somStream == null) {
                JOptionPane.showMessageDialog(this, "Arquivo de áudio não encontrado: " + caminho, "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Usa BufferedInputStream para suportar mark/reset
            BufferedInputStream bufferedStream = new BufferedInputStream(somStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao reproduzir o som do botão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}