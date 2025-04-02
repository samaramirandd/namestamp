package namestampapp.namestamp;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFSigner {
    static void processPDFs(String templatePath, String csvPath, String outputFolder,
                            int x, int y, String fontFamily, int fontSize,
                            Color textColor, boolean bold, boolean italic) {
        // Criar a pasta de saída se não existir
        File outputDir = new File(outputFolder);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        try {
            // Ler os nomes do CSV
            List<String> names = readNamesFromCSV(csvPath);

            System.out.println("\nIniciando processamento de " + names.size() + " nomes...");

            // Processar cada nome
            for (String name : names) {
                System.out.println("Processando: " + name);
                signPDF(templatePath, outputFolder + File.separator + name + ".pdf", name, x, y, fontFamily, fontSize, textColor, bold, italic);
            }

            System.out.println("\nProcessamento concluído! " + names.size() + " PDFs gerados em " + outputFolder);

        } catch (IOException | CsvValidationException e) {
            System.err.println("Erro ao processar os arquivos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<String> readNamesFromCSV(String csvPath) throws IOException, CsvValidationException {
        List<String> names = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvPath)).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length > 0 && !line[0].isEmpty()) {
                    names.add(line[0].trim());
                }
            }
        }

        return names;
    }

    public static void signPDF(String templatePath, String outputPath, String name,
                               int x, int y, String fontFamily, int fontSize, Color color,
                               boolean bold, boolean italic) throws IOException {

        // Carregar o PDF template
        PDDocument document = PDDocument.load(new File(templatePath));

        // Selecionar a fonte apropriada
        PDFont font;
        if (fontFamily.equalsIgnoreCase("Helvetica")) {
            if (bold && italic) {
                font = PDType1Font.HELVETICA_BOLD_OBLIQUE;
            } else if (bold) {
                font = PDType1Font.HELVETICA_BOLD;
            } else if (italic) {
                font = PDType1Font.HELVETICA_OBLIQUE;
            } else {
                font = PDType1Font.HELVETICA;
            }
        } else if (fontFamily.equalsIgnoreCase("Times")) {
            if (bold && italic) {
                font = PDType1Font.TIMES_BOLD_ITALIC;
            } else if (bold) {
                font = PDType1Font.TIMES_BOLD;
            } else if (italic) {
                font = PDType1Font.TIMES_ITALIC;
            } else {
                font = PDType1Font.TIMES_ROMAN;
            }
        } else if (fontFamily.equalsIgnoreCase("Courier")) {
            if (bold && italic) {
                font = PDType1Font.COURIER_BOLD_OBLIQUE;
            } else if (bold) {
                font = PDType1Font.COURIER_BOLD;
            } else if (italic) {
                font = PDType1Font.COURIER_OBLIQUE;
            } else {
                font = PDType1Font.COURIER;
            }
        } else {
            // Fallback para Helvetica se a fonte especificada não for suportada
            System.out.println("[AVISO] Fonte '" + fontFamily + "' não disponível, usando Helvetica");
            font = bold ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA;
        }

        // Obter a primeira página (ou alterar para a página desejada)
        PDPage page = document.getPage(0);

        // Criar o stream de conteúdo para adicionar texto
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true, true)) {
            // Configurar a fonte
            contentStream.setFont(font, fontSize);

            // Configurar a cor
            contentStream.setNonStrokingColor(color);

            // Posicionar e escrever o texto
            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(name);
            contentStream.endText();
        }

        // Salvar o documento
        document.save(outputPath);
        document.close();
    }
}