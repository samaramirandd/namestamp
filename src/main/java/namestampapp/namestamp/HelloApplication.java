package namestampapp.namestamp;

import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class HelloApplication {
    /*
    public class HelloApplication extends Application {

        @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        }


        public static void main(String[] args) {
            launch();
        }*/
// Configurações padrão
    private static final int DEFAULT_FONT_SIZE = 12;
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final String DEFAULT_FONT_FAMILY = "Montserrat";
    private static final boolean DEFAULT_BOLD = true;
    private static final boolean DEFAULT_ITALIC = false;
    private static final int DEFAULT_X_POSITION = 100;
    private static final int DEFAULT_Y_POSITION = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean useMenu = true;
        String templatePath = "";
        String csvPath = "";
        String outputFolder = "";
        int x = DEFAULT_X_POSITION;
        int y = DEFAULT_Y_POSITION;
        String fontFamily = DEFAULT_FONT_FAMILY;
        int fontSize = DEFAULT_FONT_SIZE;
        Color textColor = DEFAULT_COLOR;
        boolean bold = DEFAULT_BOLD;
        boolean italic = DEFAULT_ITALIC;

        // Verificar se os argumentos foram fornecidos pela linha de comando
        if (args.length >= 3) {
            useMenu = false;
            templatePath = args[0];
            csvPath = args[1];
            outputFolder = args[2];

            // Parsear opções adicionais da linha de comando
            for (int i = 3; i < args.length; i++) {
                if (args[i].equals("-x") && i + 1 < args.length) {
                    x = Integer.parseInt(args[++i]);
                } else if (args[i].equals("-y") && i + 1 < args.length) {
                    y = Integer.parseInt(args[++i]);
                } else if (args[i].equals("-font") && i + 1 < args.length) {
                    fontFamily = args[++i];
                } else if (args[i].equals("-size") && i + 1 < args.length) {
                    fontSize = Integer.parseInt(args[++i]);
                } else if (args[i].equals("-color") && i + 1 < args.length) {
                    String[] rgb = args[++i].split(",");
                    if (rgb.length == 3) {
                        textColor = new Color(
                                Integer.parseInt(rgb[0]),
                                Integer.parseInt(rgb[1]),
                                Integer.parseInt(rgb[2])
                        );
                    }
                } else if (args[i].equals("-bold")) {
                    bold = true;
                } else if (args[i].equals("-italic")) {
                    italic = true;
                }
            }
        }

        // Modo interativo - solicitar informações ou mostrar menu
        if (useMenu) {
            boolean running = true;

            while (running) {
                System.out.println("\n===== NAMESTAMP - MENU PRINCIPAL =====");
                System.out.println("1. Configurar Caminho do PDF Template");
                System.out.println("2. Configurar Caminho do CSV com Nomes");
                System.out.println("3. Configurar Pasta de Saída");
                System.out.println("4. Configurar Posição da Assinatura");
                System.out.println("5. Configurar Fonte e Estilo");
                System.out.println("6. Configurar Cor do Texto");
                System.out.println("7. Mostrar Configurações Atuais");
                System.out.println("8. Processar PDFs");
                System.out.println("9. Sair");
                System.out.print("\nEscolha uma opção: ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida. Por favor, digite um número.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        System.out.print("Digite o caminho do arquivo PDF template: ");
                        templatePath = scanner.nextLine().trim();
                        if (!new File(templatePath).exists()) {
                            System.out.println("[AVISO] Arquivo não encontrado: " + templatePath);
                        } else {
                            System.out.println("Template configurado: " + templatePath);
                        }
                        break;

                    case 2:
                        System.out.print("Digite o caminho do arquivo CSV com os nomes: ");
                        csvPath = scanner.nextLine().trim();
                        if (!new File(csvPath).exists()) {
                            System.out.println("[AVISO] Arquivo não encontrado: " + csvPath);
                        } else {
                            System.out.println("CSV configurado: " + csvPath);
                        }
                        break;

                    case 3:
                        System.out.print("Digite o caminho da pasta de saída: ");
                        outputFolder = scanner.nextLine().trim();
                        System.out.println("Pasta de saída: " + outputFolder);
                        break;

                    case 4:
                        System.out.print("Posição X (atual: " + x + "): ");
                        try {
                            x = Integer.parseInt(scanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Valor inválido, mantendo posição X = " + x);
                        }

                        System.out.print("Posição Y (atual: " + y + "): ");
                        try {
                            y = Integer.parseInt(scanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Valor inválido, mantendo posição Y = " + y);
                        }
                        System.out.println("Posição configurada: X=" + x + ", Y=" + y);
                        break;

                    case 5:
                        System.out.println("Fontes disponíveis: Helvetica, Times, Courier");
                        System.out.print("Nome da fonte (atual: " + fontFamily + "): ");
                        String newFont = scanner.nextLine().trim();
                        if (!newFont.isEmpty()) {
                            fontFamily = newFont;
                        }

                        System.out.print("Tamanho da fonte (atual: " + fontSize + "): ");
                        try {
                            fontSize = Integer.parseInt(scanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Valor inválido, mantendo tamanho = " + fontSize);
                        }

                        System.out.print("Negrito (S/N) (atual: " + (bold ? "S" : "N") + "): ");
                        String boldChoice = scanner.nextLine().trim().toUpperCase();
                        if (boldChoice.equals("S") || boldChoice.equals("SIM")) {
                            bold = true;
                        } else if (boldChoice.equals("N") || boldChoice.equals("NÃO") || boldChoice.equals("NAO")) {
                            bold = false;
                        }

                        System.out.print("Itálico (S/N) (atual: " + (italic ? "S" : "N") + "): ");
                        String italicChoice = scanner.nextLine().trim().toUpperCase();
                        if (italicChoice.equals("S") || italicChoice.equals("SIM")) {
                            italic = true;
                        } else if (italicChoice.equals("N") || italicChoice.equals("NÃO") || italicChoice.equals("NAO")) {
                            italic = false;
                        }

                        System.out.println("Fonte configurada: " + fontFamily + ", tamanho " + fontSize +
                                (bold ? ", negrito" : "") +
                                (italic ? ", itálico" : ""));
                        break;

                    case 6:
                        int r = textColor.getRed();
                        int g = textColor.getGreen();
                        int b = textColor.getBlue();

                        System.out.println("Cor atual: R=" + r + ", G=" + g + ", B=" + b);
                        System.out.print("Vermelho (0-255): ");
                        try {
                            r = Integer.parseInt(scanner.nextLine().trim());
                            r = Math.max(0, Math.min(255, r));
                        } catch (NumberFormatException e) {
                            System.out.println("Valor inválido, mantendo R = " + r);
                        }

                        System.out.print("Verde (0-255): ");
                        try {
                            g = Integer.parseInt(scanner.nextLine().trim());
                            g = Math.max(0, Math.min(255, g));
                        } catch (NumberFormatException e) {
                            System.out.println("Valor inválido, mantendo G = " + g);
                        }

                        System.out.print("Azul (0-255): ");
                        try {
                            b = Integer.parseInt(scanner.nextLine().trim());
                            b = Math.max(0, Math.min(255, b));
                        } catch (NumberFormatException e) {
                            System.out.println("Valor inválido, mantendo B = " + b);
                        }

                        textColor = new Color(r, g, b);
                        System.out.println("Cor configurada: R=" + r + ", G=" + g + ", B=" + b);
                        break;

                    case 7:
                        System.out.println("\n===== CONFIGURAÇÕES ATUAIS =====");
                        System.out.println("PDF Template: " + (templatePath.isEmpty() ? "Não configurado" : templatePath));
                        System.out.println("Arquivo CSV: " + (csvPath.isEmpty() ? "Não configurado" : csvPath));
                        System.out.println("Pasta de Saída: " + (outputFolder.isEmpty() ? "Não configurado" : outputFolder));
                        System.out.println("Posição: X=" + x + ", Y=" + y);
                        System.out.println("Fonte: " + fontFamily + ", tamanho " + fontSize +
                                (bold ? ", negrito" : "") +
                                (italic ? ", itálico" : ""));
                        System.out.println("Cor: R=" + textColor.getRed() +
                                ", G=" + textColor.getGreen() +
                                ", B=" + textColor.getBlue());
                        break;

                    case 8:
                        if (templatePath.isEmpty() || csvPath.isEmpty() || outputFolder.isEmpty()) {
                            System.out.println("[ERRO] Por favor, configure todos os caminhos necessários primeiro.");
                            break;
                        }

                        if (!new File(templatePath).exists()) {
                            System.out.println("[ERRO] Arquivo de template não encontrado: " + templatePath);
                            break;
                        }

                        if (!new File(csvPath).exists()) {
                            System.out.println("[ERRO] Arquivo CSV não encontrado: " + csvPath);
                            break;
                        }

                        // Processar os PDFs
                        PDFSigner.processPDFs(templatePath, csvPath, outputFolder, x, y, fontFamily, fontSize, textColor, bold, italic);
                        break;

                    case 9:
                        System.out.println("Saindo do programa...");
                        running = false;
                        break;

                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            }
        } else {
            // Modo linha de comando - processar diretamente
            PDFSigner.processPDFs(templatePath, csvPath, outputFolder, x, y, fontFamily, fontSize, textColor, bold, italic);
        }

        scanner.close();
    }
}