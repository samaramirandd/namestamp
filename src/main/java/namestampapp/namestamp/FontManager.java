package namestampapp.namestamp;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class FontManager {
    private static final Map<String, FontConfig> fontRegistry = new HashMap<>();
    private static final String FONTS_DIR = "/fonts/";

    static {
        // Registrar fontes padrão do PDF
        registerStandardFont("Helvetica", PDType1Font.HELVETICA);
        registerStandardFont("Helvetica-Bold", PDType1Font.HELVETICA_BOLD);
        registerStandardFont("Helvetica-Oblique", PDType1Font.HELVETICA_OBLIQUE);
        registerStandardFont("Helvetica-BoldOblique", PDType1Font.HELVETICA_BOLD_OBLIQUE);
        
        registerStandardFont("Times-Roman", PDType1Font.TIMES_ROMAN);
        registerStandardFont("Times-Bold", PDType1Font.TIMES_BOLD);
        registerStandardFont("Times-Italic", PDType1Font.TIMES_ITALIC);
        registerStandardFont("Times-BoldItalic", PDType1Font.TIMES_BOLD_ITALIC);
        
        registerStandardFont("Courier", PDType1Font.COURIER);
        registerStandardFont("Courier-Bold", PDType1Font.COURIER_BOLD);
        registerStandardFont("Courier-Oblique", PDType1Font.COURIER_OBLIQUE);
        registerStandardFont("Courier-BoldOblique", PDType1Font.COURIER_BOLD_OBLIQUE);
        
        // Registrar fontes personalizadas (serão carregadas sob demanda)
        registerCustomFont("Montserrat", "Montserrat-Regular.ttf");
        registerCustomFont("Montserrat-Bold", "Montserrat-Bold.ttf");
        registerCustomFont("Montserrat-Italic", "Montserrat-Italic.ttf");
        registerCustomFont("Montserrat-BoldItalic", "Montserrat-BoldItalic.ttf");
        
        registerCustomFont("OpenSans", "OpenSans-Regular.ttf");
        registerCustomFont("OpenSans-Bold", "OpenSans-Bold.ttf");
        // Adicione mais fontes conforme necessário
    }

    private static void registerStandardFont(String name, PDFont font) {
        fontRegistry.put(name.toLowerCase(), new FontConfig(name, font, true));
    }

    private static void registerCustomFont(String name, String resourcePath) {
        fontRegistry.put(name.toLowerCase(), new FontConfig(name, resourcePath, false));
    }

    public static PDFont getFont(PDDocument document, String fontName, boolean bold, boolean italic) throws IOException {
        // Construir o nome completo da fonte com variações
        String fullFontName = fontName;
        if (bold && italic) {
            fullFontName += "-BoldItalic";
        } else if (bold) {
            fullFontName += "-Bold";
        } else if (italic) {
            fullFontName += "-Italic";
        }

        String lookupKey = fullFontName.toLowerCase();
        FontConfig config = fontRegistry.get(lookupKey);

        // Se não encontrou exata, tentar a regular
        if (config == null) {
            config = fontRegistry.get(fontName.toLowerCase());
        }

        if (config == null) {
            throw new IllegalArgumentException("Fonte não registrada: " + fontName);
        }

        if (config.isStandard) {
            return config.font;
        } else {
            // Carregar fonte personalizada sob demanda
            try (InputStream fontStream = FontManager.class.getResourceAsStream(FONTS_DIR + config.resourcePath)) {
                if (fontStream == null) {
                    throw new IOException("Arquivo da fonte não encontrado: " + config.resourcePath);
                }
                return PDType0Font.load(document, fontStream);
            }
        }
    }

    public static String[] getAvailableFonts() {
        return fontRegistry.keySet().stream()
                .filter(key -> !key.contains("-")) // Mostrar apenas nomes base
                .toArray(String[]::new);
    }

    private static class FontConfig {
        String name;
        PDFont font; // Para fontes padrão
        String resourcePath; // Para fontes personalizadas
        boolean isStandard;

        public FontConfig(String name, PDFont font, boolean isStandard) {
            this.name = name;
            this.font = font;
            this.isStandard = isStandard;
        }

        public FontConfig(String name, String resourcePath, boolean isStandard) {
            this.name = name;
            this.resourcePath = resourcePath;
            this.isStandard = isStandard;
        }
    }
}