# Jogo da Memória de Hardwares

Este projeto é um jogo da memória desenvolvido em Java utilizando Programação Orientada a Objetos (POO). O objetivo é ajudar os jogadores a aprenderem sobre os hardwares internos e externos de um computador de forma interativa e divertida. O jogo inclui quatro classes principais, cada uma responsável por uma funcionalidade específica.

## Classes do Projeto

O projeto é composto pelas seguintes classes:

### Menu
A classe principal que exibe o menu inicial do jogo, com opções para escolher entre hardwares internos, hardwares externos, charadas e quiz.

### Charada
Uma classe que apresenta charadas relacionadas aos hardwares internos do computador. O jogador deve adivinhar a resposta correta para avançar.

### QuizCharada
Um quiz com perguntas de múltipla escolha sobre hardwares externos. O jogador ganha pontos ao responder corretamente.

### HardwareInterno
Um jogo da memória onde o jogador deve combinar cartas que representam hardwares internos do computador. Cada combinação correta exibe uma explicação sobre o hardware.

### HardwareExterno
Um jogo da memória onde o jogador deve combinar cartas que representam hardwares externos do computador. Cada combinação correta exibe uma explicação sobre o hardware.

## Como Usar

Para executar o projeto, siga os passos abaixo:

1. Clone o repositório:
    ```bash
    git clone https://github.com/seu-usuario/seu-projeto.git
    ```

2. Compile e execute o projeto:
    ```bash
    javac *.java
    java Menu
    ```

3. No menu principal, escolha uma das opções:
    - **Hardwares Internos**: Jogue o jogo da memória para aprender sobre hardwares internos.
    - **Hardwares Externos**: Jogue o jogo da memória para aprender sobre hardwares externos.
    - **Hardwares Externos**: Responda ao quiz sobre hardwares externos.
    - **Charada**: Resolva charadas relacionadas aos hardwares.
    - **Quiz Charada**: Teste seus conhecimentos com perguntas de múltipla escolha.

## Requisitos

- **Java Development Kit (JDK)**: Versão 8 ou superior.
- **Bibliotecas**: O projeto utiliza as bibliotecas padrão do Java (javax.sound, javax.swing, java.awt).

## Como Contribuir

1. Faça um fork do projeto.
2. Crie uma branch para sua feature: `git checkout -b feature/nova-feature`.
3. Commit suas mudanças: `git commit -m 'Adiciona nova feature'`.
4. Faça um push para a branch: `git push origin feature/nova-feature`.
5. Abra um Pull Request.


## Explicação das Funcionalidades

### Menu
- Exibe o menu principal com botões para acessar diferentes partes do jogo.
- Reproduz sons de fundo e efeitos sonoros ao interagir com os botões.

### Charada
- Apresenta charadas relacionadas aos hardwares internos.
- O jogador tem 5 chances para acertar as respostas.
- Exibe explicações sobre os hardwares ao acertar.

### QuizCharada
- Um quiz com 10 perguntas de múltipla escolha sobre hardwares externos.
- O jogador ganha pontos ao responder corretamente e recebe feedback ao final.

### HardwareInterno
- Um jogo da memória com cartas que representam hardwares internos.
- O jogador deve combinar as cartas corretamente para ganhar.
- Cada combinação correta exibe uma explicação sobre o hardware.

## Melhorias Futuras

- Adicionar mais níveis de dificuldade.
- Incluir mais hardwares e perguntas.
- Implementar um sistema de pontuação global.


