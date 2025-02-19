import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static java.awt.Color.*;

public class QuizCharada extends JFrame implements ActionListener {
    JButton proximo, submeter;
    ButtonGroup grupoOpcoes;
    String perguntas[][] = new String[10][5]; // 10 perguntas, cada uma com 5 opções (0: pergunta, 1-4: opções)
    String respostas[] = new String[10]; // Respostas corretas para cada pergunta
    String respostasUser[] = new String[10]; // Respostas do usuário
    JRadioButton opt1, opt2, opt3, opt4;
    private JButton voltarMenu = new JButton("Menu");
    private JPanel painel;

    //JLabel qno;
    JTextArea questao;

    public int count = 0;
    public int pontuacao = 0;

    public QuizCharada() {

        setResizable(false);

        painel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imgFundo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/FundoQuizCharada/fundo.jpg")));
                g.drawImage(imgFundo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        painel.setLayout(null);
        setContentPane(painel);

        setTitle("Quiz Charada");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 600);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        proximo = new JButton("Próximo");
        proximo.setBounds(70, 400, 150, 50);
        proximo.setVisible(true);
        proximo.setForeground(BLACK);
        proximo.setBackground(PINK);
        proximo.addActionListener(this);
        getContentPane().add(proximo);

        submeter = new JButton("Submeter");
        submeter.setBounds(75, 470, 140,40);
        submeter.setVisible(true);
        submeter.addActionListener(this);
        submeter.setForeground(BLACK);
        submeter.setBackground(WHITE);
        getContentPane().add(submeter);
        submeter.setEnabled(false);

        voltarMenu.setFont(new Font("Arial", Font.BOLD, 16));
        voltarMenu.setBounds(620, 10, 110, 40);
        voltarMenu.setVisible(true);
        getContentPane().add(voltarMenu);
        voltarMenu.setForeground(white);
        voltarMenu.setBackground(blue);
        voltarMenu.addActionListener(e -> {
            reproduzirSons("/sons/somBotao.wav");
            dispose();
            new Menu();
        });

        questao = new JTextArea();
        questao.setFont(new Font("Arial", Font.BOLD, 16));
        questao.setBounds(70, 60, 554, 100);
        questao.setEditable(false);
        questao.setLineWrap(true);
        questao.setWrapStyleWord(true);
        questao.setVisible(true);
        getContentPane().add(questao);

        opt1 = new JRadioButton();
        opt1.setFont(new Font("Arial", Font.BOLD, 16));
        opt1.setBackground(pink);
        opt1.setBounds(70, 220, 250, 30);
        getContentPane().add(opt1);

        opt2 = new JRadioButton();
        opt2.setFont(new Font("Arial", Font.BOLD, 16));
        opt2.setBackground(pink);
        opt2.setBounds(70, 260, 250, 30);
        getContentPane().add(opt2);

        opt3 = new JRadioButton();
        opt3.setFont(new Font("Arial", Font.BOLD, 16));
        opt3.setBackground(pink);
        opt3.setBounds(70, 300, 250, 30);
        getContentPane().add(opt3);

        opt4 = new JRadioButton();
        opt4.setFont(new Font("Arial", Font.BOLD, 16));
        opt4.setBackground(pink);
        opt4.setBounds(70, 340, 250, 30);
        getContentPane().add(opt4);

        grupoOpcoes = new ButtonGroup();
        grupoOpcoes.add(opt1);
        grupoOpcoes.add(opt2);
        grupoOpcoes.add(opt3);
        grupoOpcoes.add(opt4);

        // Inicialização das perguntas e respostas
        perguntas[0][0] = "1.Me escondo à vista de todos, mas sou o alicerce do poder.\n" +
                "Sem mim, o caos reinará,pois sou a casa do que faz acontecer.\n" +
                "Não penso, não calculo, mas sem mim, nada funciona.\n" +
                "Sou frio por fora, mas guardo o fogo que a máquina entona.\n Quem sou eu?\n";
        perguntas[0][1] = "Gabinete"; // resposta
        perguntas[0][2] = "Fonte de alimentação";
        perguntas[0][3] = "Servidor";
        perguntas[0][4] = "Processador";
        respostas[0] = "Gabinete";


        perguntas[1][0] = "Sem mim, a voz não viaja, nem a luz pode correr.\n" +
                "Uno mundos sem sair do lugar, mas se me cortam, tudo pode morrer.\n" +
                "Sou visto, mas ignorado,escondido sob mesas e chão.\n" +
                "Posso levar dados ou pura energia, sempre na minha missão.\n" +
                "Quem sou eu?";
        perguntas[1][1] = "Wi-Fi";
        perguntas[1][2] = "Memória Cache";
        perguntas[1][3] = "Cabos";
        perguntas[1][4] = "SSD";

        respostas[1] = "Cabos"; // no indice coloque o valor do indice da resposta correta


        //cha3
        perguntas[2][0] = "Eu transformo pulsos em ondas, mas sem pulmões posso cantar.\n" +
                "Minha voz vem do silêncio,e sem mim, tudo será mudo.\n" +
                "Eu falo sem boca,e sou ouvido sem ouvidos.\n" +
                "Se me ferem, apenas sussurro, ou pior, sou condenado ao silêncio eterno.\n" +
                "Quem sou eu?";
        perguntas[2][1] = "Placa de Som";
        perguntas[2][2] = "Caixa de Som";//res
        perguntas[2][3] = "Fone de Ouvido";
        perguntas[2][4] = "Microfone";

        respostas[2] = "Caixa de Som";


        perguntas[3][0] = "Não tenho boca, mas canto, não tenho ouvidos, mas escuto.\n" +
                "A solidão é minha essência,pois levo o som sem que escape.\n" +
                "Eu dou vida ao silêncio,mas se me perco, o mundo escuta.\n" +
                "Afasto o ruído do mundo lá fora,e só compartilho o que permito entrar.\n" +
                "Quem sou eu";
        perguntas[3][1] = "Placa de Som";
        perguntas[3][2] = "Fone de Ouvido";
        perguntas[3][3] = "Headphone";//resp
        perguntas[3][4] = "Caixa de Som";

        respostas[3] = "Headphone";

        perguntas[4][0] = "Eu crio, mas não sou artista,Eu reproduzo, mas não sou réptil.\n" +
                "Minhas mãos são invisíveis,mas produzo sinais que você pode tocar.\n" +
                "Minha tinta é uma mentira líquida,e sem mim, o papel fica vazio.\n" +
                "Eu não sou mágica, mas faço o impossível acontecer,transformando digital em físico, sem nada mudar.\n" +
                "Quem sou eu?";
        perguntas[4][1] = "Impressora";//resp
        perguntas[4][2] = "Scanner";
        perguntas[4][3] = "Plotter";
        perguntas[4][4] = "Tela de Monitor";

        respostas[4] = "Impressora";

        perguntas[5][0] = "Eu sou um reflexo da mente,mas não tenho olhos para ver.\n" +
                "Ofereço uma janela para o mundo,mas não tenho portas a fechar.\n" +
                "Meu brilho é uma mentira luminosa,que revela o que não posso tocar.\n" +
                "Sem mim, o mundo digital é um mistério,mas sem dedos, não há acesso ao meu conteúdo.\n" +
                "Quem sou eu?";
        perguntas[5][1] = "Projetor";
        perguntas[5][2] = "Monitor";//resp
        perguntas[5][3] = "Placa de Vídeo";
        perguntas[5][4] = "Modem";

        respostas[5] = "Monitor";

        perguntas[6][0] = "Sou um pequeno mensageiro, mas sem voz,transporto pensamentos, mas não tenho mente.\n" +
                "Meu corpo é humilde, mas meu conteúdo é imenso, e sem mim, muitos dados seriam esquecidos.\n" +
                "Movo-me entre mundos digitais,e sou invisível até ser tocado.\n" +
                "Carrego o que você não vê, e deixo para trás o que não pode ser tocado.\n" +
                "Quem sou eu?";
        perguntas[6][1] = "Pendrive";//resp
        perguntas[6][2] = "Cartão de Memória";
        perguntas[6][3] = "HD Externo";
        perguntas[6][4] = "CD/DVD";

        respostas[6] = "Pendrive";

        perguntas[7][0] = "Eu olho sem olhos,toque sem mãos, revelando o invisível, sem deixar pegadas no chão.\n" +
                "Capturo o que você vê, mas não sinto o que toco, meu trabalho é fiel, mas não posso te mostrar o foco.\n" +
                "Eu sou a cópia da cópia, mas não sou um espelho.\n" +
                "Quem sou eu?";
        perguntas[7][1] = "Câmera Digital";
        perguntas[7][2] = "Fotocopiadora";
        perguntas[7][3] = "Impressora Multifuncional";
        perguntas[7][4] = "Scanner";//resp

        respostas[7] = "Scanner";

        perguntas[8][0] = "Eu sou o mensageiro silencioso, mas minha fala nunca é ouvida.\n" +
                "Comigo, você escreve sem caneta, e sem mim, a tela permanece muda.\n" +
                "Meus segredos estão nas minhas teclas,e sou mais rápido do que palavras ditas.\n" +
                "Embora eu tenha muitas opções,não sou nada sem os dedos a me guiar.\n" +
                "Quem sou eu?";
        perguntas[8][1] = "Mouse";
        perguntas[8][2] = "Teclado";//resp
        perguntas[8][3] = "Touchpad";
        perguntas[8][4] = "Ponteiro de Laser";

        respostas[8] = "Teclado";

        perguntas[9][0] = "Eu sou o olho sem pálpebras, vejo sem tocar, minha visão é digital, mas não posso te olhar no ar.\n" +
                "Gravo o que você faz, mas não sou uma câmera normal.\n" +
                "Muitos me ignoram, mas sou essencial no digital.\n" +
                "Fico escondida, mas à vista, te mostro ao mundo sem sair do lugar.\n" +
                "Quem sou eu?";
        perguntas[9][1] = "Webcam";//resp
        perguntas[9][2] = "Microfone";
        perguntas[9][3] = "Câmera de Segurança\n";
        perguntas[9][4] = "Câmera Digital\n";

        respostas[9] = "Webcam";

        enter(count);

        setVisible(true);
    }

    public void enter(int count) {
        questao.setText(perguntas[count][0]);

        opt1.setText(perguntas[count][1]);
        opt1.setActionCommand(perguntas[count][1]);

        opt2.setText(perguntas[count][2]);
        opt2.setActionCommand(perguntas[count][2]);

        opt3.setText(perguntas[count][3]);
        opt3.setActionCommand(perguntas[count][3]);

        opt4.setText(perguntas[count][4]);
        opt4.setActionCommand(perguntas[count][4]);

        grupoOpcoes.clearSelection();

        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        reproduzirSons("/sons/somBotao.wav");
        if (e.getSource() == proximo) {
            if (grupoOpcoes.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione uma resposta antes de avançar.", "Resposta em Branco", JOptionPane.WARNING_MESSAGE);
                return;
            }
            salvarRespostaUsuario();

            if (count == 9) {
                proximo.setEnabled(false);
                submeter.setEnabled(true);
            } else {
                count++;
                enter(count);
            }

            if (count < 9) {
                proximo.setEnabled(true);
                submeter.setEnabled(false);
            }

        } else if (e.getSource() == submeter) {
            reproduzirSons("/sons/somBotao.wav");
            salvarRespostaUsuario();

            if (grupoOpcoes.getSelection() == null) {
                respostasUser[count] = "";
            } else {
                respostasUser[count] = grupoOpcoes.getSelection().getActionCommand();
            }

            pontuacao = 0;

            for (int i = 0; i < respostasUser.length; i++) {
                if (respostasUser[i].equals(respostas[i])) {
                    pontuacao += 10;
                }
            }
            if (pontuacao < 7) {
                reproduzirSons("/sons/vitoriaFinal.wav");
                JOptionPane.showMessageDialog(this, "Sua pontuação final é: " + pontuacao + " de 100");
            }
            if (pontuacao >= 7) {
                reproduzirSons("/sons/perdaFinal.wav");
                JOptionPane.showMessageDialog(this, "Sua pontuação final é: " + pontuacao + " de 100");
            }
        }
    }
    private void salvarRespostaUsuario() {
        if (opt1.isSelected()) {
            respostasUser[count] = opt1.getText();
        } else if (opt2.isSelected()) {
            respostasUser[count] = opt2.getText();
        } else if (opt3.isSelected()) {
            respostasUser[count] = opt3.getText();
        } else if (opt4.isSelected()) {
            respostasUser[count] = opt4.getText();
        }
    }

    private void carregarPergunta() {
        questao.setText(perguntas[count][0]);
        opt1.setText(perguntas[count][1]);
        opt2.setText(perguntas[count][2]);
        opt3.setText(perguntas[count][3]);
        opt4.setText(perguntas[count][4]);

        // Restaurar a opção selecionada
        grupoOpcoes.clearSelection(); // Limpa a seleção atual
        if (respostasUser[count] != null) {
            if (respostasUser[count].equals(opt1.getText())) {
                opt1.setSelected(true);
            } else if (respostasUser[count].equals(opt2.getText())) {
                opt2.setSelected(true);
            } else if (respostasUser[count].equals(opt3.getText())) {
                opt3.setSelected(true);
            } else if (respostasUser[count].equals(opt4.getText())) {
                opt4.setSelected(true);
            }
        }
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