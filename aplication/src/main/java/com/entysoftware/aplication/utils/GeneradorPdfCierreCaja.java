package com.entysoftware.aplication.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.entysoftware.aplication.model.dto.cierreCaja.ReporteCierreCaja;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

/**
 * Genera el PDF del cierre de caja: llena la plantilla templates/pdf/cierre-caja.html con Thymeleaf
 * y convierte el HTML resultante a PDF con OpenHTMLtoPDF.
 */
@Component
public class GeneradorPdfCierreCaja {

    private static final String PLANTILLA = "pdf/cierre-caja";
    private static final Locale LOCALE_COLOMBIA = Locale.forLanguageTag("es-CO");

    private final TemplateEngine templateEngine;

    public GeneradorPdfCierreCaja() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");

        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
    }

    public byte[] generar(ReporteCierreCaja reporte) {
        Context contexto = new Context(LOCALE_COLOMBIA);
        contexto.setVariable("reporte", reporte);
        contexto.setVariable("cierre", reporte.cierreDia());
        contexto.setVariable("fechaGeneracion", LocalDateTime.now());
        String html = templateEngine.process(PLANTILLA, contexto);

        try (ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(salida);
            builder.run();
            return salida.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo generar el PDF del cierre de caja", e);
        }
    }
}
