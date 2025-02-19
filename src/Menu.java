import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Menu extends JFrame {

    private static Clip backgroundClip; // Som de fundo

    public Menu() {
        setTitle("Menu Principal");
        setSize(780, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        restaurarVolume();

        // Iniciar o som de fundo
        if (backgroundClip == null || !backgroundClip.isRunning()) {
            carregarSomFundo();
        }

        JPanel fundoPainel = new JPanel() {
            private Image backgroundImage = new ImageIcon(getClass().getResource("/imgMenu/fundo.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        fundoPainel.setLayout(new GridBagLayout());

        JLabel titulo = new JLabel("Aprenda Função dos Hardwares do Computador com o Jogo da Memória", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.BLACK);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton hadwInternos = criarBotao("Hardwares Internos", new Color(240, 128, 128));
        JButton hadwExternos = criarBotao("Hardwares Externos", new Color(135, 206, 235));
        JButton ajuda = criarBotao("Ajuda", new Color(140, 16, 120));
        JButton sair = criarBotao("Sair", new Color(186, 85, 211));
        JButton charadaN1 = criarBotao("Charada", new Color(14, 126, 20));
        JButton charadaN2 = criarBotao("Quiz Charada", new Color(135, 206, 235));

        // Painel dos botões (transparente para exibir fundo)
        JPanel botoesPainel = new JPanel(new GridBagLayout());
        botoesPainel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        botoesPainel.add(hadwInternos, gbc);

        gbc.gridx = 1;
        botoesPainel.add(hadwExternos, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        botoesPainel.add(ajuda, gbc);

        gbc.gridx = 1;
        botoesPainel.add(sair, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        botoesPainel.add(charadaN1, gbc);

        gbc.gridx = 1;
        botoesPainel.add(charadaN2, gbc);

        hadwInternos.addActionListener(e -> {
            reproduzirSons("/sons/somBotaomenu.wav"); // Som do botão
            ajustarVolume(-10.0f);
            dispose();
            new HardwareInterno();
        });

        hadwExternos.addActionListener(e -> {
            reproduzirSons("/sons/somBotaomenu.wav"); // Som do botão
            ajustarVolume(-10.0f);
            dispose();
            new HardwareExterno();
        });

        charadaN1.addActionListener(e -> {
            reproduzirSons("/sons/somBotaomenu.wav"); // Som do botão
            ajustarVolume(-10.0f);
            dispose();
            new Charada();
        });

        charadaN2.addActionListener(e -> {
            reproduzirSons("/sons/somBotaomenu.wav"); // Som do botão
            ajustarVolume(-10.0f);
            dispose();
            new QuizCharada();
        });

        ajuda.addActionListener(e -> {
            reproduzirSons("/sons/somBotaomenu.wav"); // Som do botão
            JOptionPane.showMessageDialog(null,
                    "Bem-vindo ao jogo da memória!\n\n" +
                            "Selecione 'Hardwares Externos' nível 1 ou 'Hardwares Internos' nível 2 para começar.\n" +
                            "Você terá 5 min para decorar as cartas, quando terminar tente as charadas para testar seus conhecimentos.", "Ajuda", JOptionPane.INFORMATION_MESSAGE);
        });

        sair.addActionListener(e -> {
            reproduzirSons("/sons/somBotaomenu.wav"); // Som do botão
            System.exit(0);
        });

        fundoPainel.add(botoesPainel);
        add(titulo, BorderLayout.NORTH);
        add(fundoPainel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setBackground(cor);
        botao.setForeground(Color.BLACK);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createRaisedBevelBorder());
        botao.setPreferredSize(new Dimension(200, 50));
        return botao;
    }

    private void reproduzirSons(String caminho) {
        try {
            // Carrega o arquivo de áudio
            InputStream somStream = getClass().getResourceAsStream(caminho);
            if (somStream == null) {
                throw new IllegalArgumentException("Arquivo de áudio não encontrado: " + caminho);
            }

            // Usa BufferedInputStream para suportar mark/reset
            BufferedInputStream bufferedStream = new BufferedInputStream(somStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);

            // Obtém um Clip para reproduzir o som
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Reproduz o som uma vez
            clip.start();

            // Fecha o Clip após a reprodução
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

    private void carregarSomFundo() {
        try {
            InputStream backgroundThemeStream = getClass().getResourceAsStream("/sons/somFundo.wav");
            if (backgroundThemeStream == null) {
                throw new IllegalArgumentException("Arquivo de áudio não encontrado!");
            }

            // Usa BufferedInputStream para suportar mark/reset
            BufferedInputStream bufferedStream = new BufferedInputStream(backgroundThemeStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);

            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioInputStream);

            // Configura o som para tocar em loop contínuo
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);

            // Inicia a reprodução do som
            backgroundClip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar o som de fundo!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajustarVolume(float volume) {
        if (backgroundClip != null && backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
        }
    }

    private void restaurarVolume() {
        if (backgroundClip != null && backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(0.0f); // Volume padrão
        }
    }
}