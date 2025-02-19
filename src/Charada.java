import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.text.Normalizer;

public class Charada extends JFrame {
    private JPanel panelPergunta, panelRespostas;
    private JTextArea perguntaTextArea;
    private JLabel  imagemLabel;
    int pontuacao = 5;
    private JButton btnResponder;
    private JLabel perguntaLabel;
    private JTextField campoResposta;
    private int indicePergunta = 0;
    private JLabel chances;
    private JButton voltarMenu = new JButton("Voltar ao Menu");
    private int pontuacaoTotal = 0; // Variável para armazenar a pontuação total

    private String[][] perguntas = {
            {"\"Sou rápida, mas não guardo segredos por muito tempo.\n" +
                    "Ajudo o computador a pensar, mas se a luz apagar, tudo desaparece.\n" +
                    "Quem sou eu?\"", "memoria cache", "cache.jpg"},
            {"\"Sou veloz, mas esqueço tudo quando o dia acaba.\n" +
                    "Guardo informações para te ajudar, mas só enquanto estou ligada.\n" +
                    "Quem sou eu?\"", "memoria ram", "ram.jpg"},
            {"\"Trabalho sem descanso para manter tudo frio,\n" +
                    "se eu falhar, o calor vira um grande desafio.\n" +
                    "Quem sou eu?\"", "cooler", "cooler.jpg"},
            {"\"Fico grudado no\nprocessador,\n" +
                    "afastando o calor com muito\nvalor." +
                    "Sem mim, tudo pode esquentar,\n" +
                    "e o computador pode até travar.\n" +
                    "Quem sou eu?\"", "dissipador de calor", "dissipadorCalor.jpg"},
            {"\"Sem mim, nada funciona, tudo para de piscar.\n" +
                    "Entrego energia para o PC continuar a rodar.\n" +
                    "Quem sou eu?\"", "fonte de alimentação", "fonteAlimentacao.jpg"},
            {"\"Sou a base de tudo, conecto cada peça,\n" +
                    "sem mim, seu PC nem começa!\n" +
                    "Quem sou eu?\"", "placa mae", "placaMae.jpg"},
            {"\"Te levo para o mundo sem precisar andar,\n" +
                    "com fios ou sem fios, faço os dados viajar.\n" +
                    "Quem sou eu?\"", "placa de rede", "placaRede.jpg"},
            {"\"Sou o cérebro\n do computador," +
                    " faço cálculos\ne decisões com fervor." +
                    "Sem mim, nada acontece, tudo fica em pausa.\n" +
                    "Quem sou eu?\"", "processador", "processador.jpg"},
            {"\"Sou rápido como um raio e guardo tudo sem girar,\n" +
                    "seus arquivos eu faço voar, sem perder tempo de esperar.\n" +
                    "Quem sou eu?\"", "ssd", "ssd.jpg"},
            {"\"Tenho um olho eletrônico que transforma números em cores.\n" +
                    "Se quiser jogar ou criar, sem mim, não há valores.\n" +
                    "Quem sou eu?\"", "placa de video", "placaVide.jpeg"}
    };

    public Charada() {
        setTitle("Charada");
        setSize(780, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelPergunta = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imgFundo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgCharadaN1/" + perguntas[indicePergunta][2])));
                g.drawImage(imgFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelPergunta.setPreferredSize(new Dimension(800, 400));
        panelPergunta.setLayout(new BorderLayout(340, 200));
        panelPergunta.setPreferredSize(new Dimension(800, 300));

        perguntaTextArea = new JTextArea(perguntas[indicePergunta][0]);
        perguntaTextArea.setFont(new Font("Arial", Font.BOLD, 18));
        perguntaTextArea.setForeground(Color.BLACK);
        perguntaTextArea.setWrapStyleWord(true);
        perguntaTextArea.setLineWrap(true);
        perguntaTextArea.setOpaque(false);
        perguntaTextArea.setEditable(false);
        perguntaTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelPergunta.add(perguntaTextArea, BorderLayout.SOUTH);

        panelRespostas = new JPanel();
        panelRespostas.setLayout(new FlowLayout());
        panelRespostas.setBackground(Color.LIGHT_GRAY);
        panelRespostas.setPreferredSize(new Dimension(800, 100));

        campoResposta = new JTextField(20);
        campoResposta.setFont(new Font("Arial", Font.PLAIN, 16));

        btnResponder = new JButton("Responder");
        btnResponder.setFont(new Font("Arial", Font.BOLD, 16));
        btnResponder.setBackground(Color.PINK);
        btnResponder.setForeground(Color.BLACK);

        chances = new JLabel("Chances restantes: " + pontuacao);
        chances.setFont(new Font("Arial", Font.BOLD, 16));
        chances.setForeground(Color.RED);
        panelRespostas.add(chances);

        btnResponder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reproduzirSons("/sons/somBotao.wav");
                verificarResposta();
            }
        });

        add(panelPergunta, BorderLayout.NORTH);
        add(panelRespostas, BorderLayout.SOUTH);

        voltarMenu.setFont(new Font("Arial", Font.BOLD, 16));
        voltarMenu.setForeground(Color.BLACK);
        voltarMenu.setBackground(Color.PINK);
        voltarMenu.addActionListener(e -> {
            reproduzirSons("/sons/somBotao.wav");
            dispose();
            new Menu();
        });
        panelRespostas.add(campoResposta);
        panelRespostas.add(btnResponder);
        panelRespostas.add(voltarMenu);
        setVisible(true);
    }

    private void verificarResposta() {
        String respostaUsuario = removerAcentos(campoResposta.getText().trim());
        if (respostaUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira uma resposta antes de avançar.", "Resposta em Branco", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String respostaCorreta = removerAcentos(perguntas[indicePergunta][1]);

        String[][] sinonimos = {
                {"memoria cache", "cache"},
                {"memoria ram", "ram"},
                {"cooler", "ventilador"},
                {"dissipador de calor", "dissipador"},
                {"fonte de alimentacao", "fonte"},
                {"placa mae", "motherboard"},
                {"placa de rede", "placa wifi", "ethernet"},
                {"processador", "cpu"},
                {"ssd", "disco solido"},
                {"placa de video", "gpu"}
        };
        if (verificarRespostaComSinonimos(removerAcentos(respostaUsuario), sinonimos[indicePergunta])) {
            reproduzirSons("/sons/vitoria.wav");
            JOptionPane.showMessageDialog(this, "Resposta correta!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            pontuacaoTotal++;
        } else {
            reproduzirSons("/sons/perda.wav");
            JOptionPane.showMessageDialog(this, "Resposta errada!", "Erro", JOptionPane.ERROR_MESSAGE);
            pontuacao--;
            chances.setText("Chances restantes: " + pontuacao);

            if (pontuacao == 0) {
                reproduzirSons("/sons/perdaFinal.wav");
                JOptionPane.showMessageDialog(this, "Você perdeu", "Fim do Jogo", JOptionPane.INFORMATION_MESSAGE);
                reiniciarJogo();
                return;
            }
        }

        proximaPergunta();
    }

    private boolean verificarRespostaComSinonimos(String respostaUsuario, String[] sinonimos) {
        respostaUsuario = respostaUsuario.toLowerCase(); // Converte para minúsculas

        for (String sinonimo : sinonimos) {
            if (respostaUsuario.contains(sinonimo.toLowerCase())) { // Converte os sinônimos para minúsculas também
                return true;
            }
        }
        return false;
    }

    private String removerAcentos(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    private void proximaPergunta() {
        indicePergunta++;

        if (indicePergunta < perguntas.length) {
            perguntaTextArea.setText(perguntas[indicePergunta][0]);
            panelPergunta.repaint();
            campoResposta.setText("");
            repaint();
        } else {
            if (pontuacaoTotal > 7) {
                reproduzirSons("sons/vitoriaFinal.wav");
                JOptionPane.showMessageDialog(this, "Parabéns! Você completou todas as perguntas com uma pontuação de " + pontuacaoTotal + "!", "Fim do Jogo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                reproduzirSons("sons/perdaFinal.wav");
                JOptionPane.showMessageDialog(this, "Você completou todas as perguntas com uma pontuação de " + pontuacaoTotal + ". Tente novamente para melhorar sua pontuação!", "Fim do Jogo", JOptionPane.INFORMATION_MESSAGE);
            }
            reiniciarJogo();
        }
    }

    private void reiniciarJogo() {
        indicePergunta = 0;
        pontuacao = 5;
        pontuacaoTotal = 0;
        perguntaTextArea.setText(perguntas[indicePergunta][0]);
        panelPergunta.repaint();
        campoResposta.setText("");
        chances.setText("Chances restantes: " + pontuacao);
        repaint();
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