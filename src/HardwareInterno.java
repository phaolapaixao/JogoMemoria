import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;

public class HardwareInterno extends JFrame {
    HashMap<String, String> situacao;
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
            "Cooler", "DissipadoresDeCalor", "FonteDeAlimentação",
            "memoriaCache", "MemoriaRam", "Placa-Mae", "PlacaDeRede",
            "PlacaDeVideo", "Processador", "ssd"
    };
    int linhas = 4;
    int colunas = 5;
    int larguraCartao = 138;
    int alturaCartao = 130;

    ArrayList<Cartas> conjuntoDeCartas;
    ImageIcon versoDaCartaIcone;

    int largura = colunas * larguraCartao;
    int altura = linhas * alturaCartao;

    JFrame janela = new JFrame("Hardwares Internos do Computador");
    JLabel texto = new JLabel();
    JPanel texoPainel = new JPanel();
    JPanel quadroPainel = new JPanel();
    JPanel botaoPainel = new JPanel();
    JButton reiniciarBotao = new JButton();
    JButton voltarMenu = new JButton("Voltar ao Menu");

    int chances = 10;
    ArrayList<JButton> quadro;
    Timer tempo;
    Timer tempoDecorar;
    boolean jogoPronto = false;
    JButton carta1Selecionada;
    JButton carta2Selecionada;

    HardwareInterno() {
        configuracoesCartao();
        embaralharCartas();
        explicacoes();
        configurarExplicacoes();

        janela.setLayout(new BorderLayout());
        janela.setSize(largura, altura);
        janela.setLocationRelativeTo(null);
        janela.setResizable(false);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setAlwaysOnTop(true);

        texto.setFont(new Font("Arial", Font.PLAIN, 20));
        texto.setHorizontalAlignment(JLabel.CENTER);
        texto.setText("Chances: " + Integer.toString(chances));

        texoPainel.setPreferredSize(new Dimension(largura, 30));
        texoPainel.add(texto);
        janela.add(texoPainel, BorderLayout.NORTH);

        quadro = new ArrayList<>();
        quadroPainel.setLayout(new GridLayout(linhas, colunas));

        IntStream.range(0, conjuntoDeCartas.size()).forEach(i -> {
            JButton titulo = new JButton();
            mapaDeCartas.put(titulo, conjuntoDeCartas.get(i));
            titulo.setPreferredSize(new Dimension(larguraCartao, alturaCartao));
            titulo.setOpaque(true);
            titulo.setIcon(conjuntoDeCartas.get(i).imgCartaIcone);
            titulo.setFocusable(false);
            titulo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!jogoPronto) {
                        return;
                    }
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

                        Cartas carta1 = mapaDeCartas.get(carta1Selecionada);
                        Cartas carta2 = mapaDeCartas.get(carta2Selecionada);

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
                }
            });
            quadro.add(titulo);
            quadroPainel.add(titulo);
        });
        janela.add(quadroPainel);

        botaoPainel.setLayout(new GridLayout(1, 2));
        botaoPainel.setPreferredSize(new Dimension(largura, 50));

        reiniciarBotao.setFont(new Font("Arial", Font.PLAIN, 16));
        reiniciarBotao.setText("Reiniciar Jogo");
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
            chances = 10;
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
            // Carrega a imagem usando o caminho do recurso
            String caminhoImagem = "/imgHardInternos/" + nomeCarta + ".png";
            java.net.URL resourceUrl = getClass().getResource(caminhoImagem);

            if (resourceUrl == null) {
                System.err.println("Imagem não encontrada: " + nomeCarta);
                continue; // Pula esta carta se a imagem não for encontrada
            }

            // Cria o ImageIcon diretamente a partir do URL do recurso
            ImageIcon imgCartaIcone = new ImageIcon(resourceUrl);
            // Redimensiona a imagem para o tamanho desejado
            Image imagemRedimensionada = imgCartaIcone.getImage().getScaledInstance(larguraCartao, alturaCartao, Image.SCALE_SMOOTH);
            imgCartaIcone = new ImageIcon(imagemRedimensionada);

            // Adiciona a carta ao conjunto de cartas (duas vezes, pois são pares)
            conjuntoDeCartas.add(new Cartas(nomeCarta, imgCartaIcone));
            conjuntoDeCartas.add(new Cartas(nomeCarta, imgCartaIcone));
        }

        // Carrega a imagem do verso da carta
        String caminhoVerso = "/imgHardInternos/bfundo.png";
        java.net.URL versoUrl = getClass().getResource(caminhoVerso);

        if (versoUrl == null) {
            throw new RuntimeException("Imagem do verso da carta não encontrada!");
        }

        // Cria o ImageIcon diretamente a partir do URL do recurso
        ImageIcon versoIcone = new ImageIcon(versoUrl);
        // Redimensiona a imagem para o tamanho desejado
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

    void explicacoes() {
        situacao = new HashMap<>();
        situacao.put("0", "Você perdeu! Tente novamente!");
        situacao.put("vitoria", "Parabéns! Você venceu!");
    }

    private boolean todasAsCartasReveladas() {
        return quadro.stream().allMatch(carta -> !carta.getIcon().equals(versoDaCartaIcone));
    }

    void configurarExplicacoes() {
        explicacao.put("Processador", "O processador, ou CPU (Unidade Central de Processamento),\né o componente responsável por executar as instruções de um computador.\n Ele interpreta e processa dados, coordenando as\n operações do sistema.");
        explicacao.put("MemoriaRAM", "A memória RAM (Random Access Memory) é responsável por\n armazenar temporariamente os dados e instruções que\n o processador precisa acessar rapidamente\n enquanto o computador está ligado.");
        explicacao.put("PlacaDeVideo", "A placa de vídeo, ou GPU (Unidade de Processamento Gráfico),\n é responsável pelo processamento e exibição de imagens, gráficos\n e vídeos na tela do computador.");
        explicacao.put("FonteDeAlimentação", "A fonte de alimentação tem a função de converter\na energia elétrica da tomada (corrente alternada - AC)\n em uma forma utilizável pelos componentes do computador\n (corrente contínua - DC).");
        explicacao.put("SSD", "O SSD (Solid State Drive) é um dispositivo de\n armazenamento que utiliza memória flash para guardar dados,\n substituindo os discos rígidos tradicionais (HDD).");
        explicacao.put("Cooler", "O cooler é um componente de resfriamento usado\n para controlar a temperatura interna dos dispositivos\n eletrônicos, como computadores, evitando que o\n excesso de calor danifique os componentes.");
        explicacao.put("DissipadoresDeCalor", "Os dissipadores de calor são componentes passivos\n utilizados para reduzir a temperatura de outros dispositivos\n eletrônicos, como processadores (CPUs), placas de vídeo (GPUs)\n e outros componentes que geram calor durante o funcionamento.");
        explicacao.put("Placa-Mae", "A placa-mãe (ou motherboard) é o principal componente\n do computador, responsável por conectar e integrar todos\n os outros componentes, permitindo a comunicação entre eles.\n Ela serve como a \"espinha dorsal\" do sistema.");
        explicacao.put("memoriaCache", "A memória cache é uma memória de acesso ultrarrápido\nque armazena dados temporários para otimizar o desempenho do computador.\n Ela fica entre o processador (CPU) e a memória RAM, ajudando a reduzir\n o tempo necessário para acessar os dados mais\n frequentemente usados.");
        explicacao.put("PlacaDeRede", "A placa de rede (também chamada de NIC - Network Interface Card)\n é um componente de hardware responsável por conectar o\n computador a uma rede, seja local (LAN), sem fio (Wi-Fi),\n ou à internet. Ela permite que o computador envie\n e receba dados para se comunicar com\n outros dispositivos.");
    }

    private void reproduzirSons(String caminho) {
        try {
            InputStream somStream = getClass().getResourceAsStream(caminho);
            if (somStream == null) {
                throw new IllegalArgumentException("Arquivo de áudio não encontrado: " + caminho);
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(somStream);

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