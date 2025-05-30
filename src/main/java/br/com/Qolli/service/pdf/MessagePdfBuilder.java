package br.com.Qolli.service.pdf;

import br.com.Qolli.dto.Message;
import br.com.Qolli.util.WatermarkService;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class MessagePdfBuilder {
    public byte[] build(List<Message> messages) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            ByteArrayOutputStream tempOutput = new ByteArrayOutputStream();
            PdfWriter tempWriter = new PdfWriter(tempOutput);
            PdfDocument tempPdf = new PdfDocument(tempWriter);
            Document tempDocument = new Document(tempPdf);

            addTitle(tempDocument);
            for (Message msg : messages) {
                addMessage(tempDocument, msg);
            }
            addFooter(tempDocument);
            tempDocument.close();

            PdfReader reader = new PdfReader(new ByteArrayInputStream(tempOutput.toByteArray()));
            PdfWriter finalWriter = new PdfWriter(output);
            PdfDocument finalPdf = new PdfDocument(reader, finalWriter);

            WatermarkService.addImageWatermark(finalPdf);
            finalPdf.close();

            return output.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error building message PDF", e);
        }
    }


    private void addTitle(Document document) {
        document.add(new Paragraph("Histórico da Conversa")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));
    }

    private void addMessage(Document document, Message msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        document.add(new Paragraph("De: " + msg.getFrom())
                .setBold().setFontSize(12).setMarginBottom(2));
        document.add(new Paragraph("Para: " + msg.getTo())
                .setItalic().setFontSize(12).setMarginBottom(4));
        document.add(new Paragraph("Mensagem: " + msg.getText())
                .setFontSize(12).setMarginBottom(4));
        document.add(new Paragraph("Enviado em: " + (msg.getTimestamp() != null ? sdf.format(msg.getTimestamp()) : "N/A"))
                .setFontSize(10).setFontColor(ColorConstants.GRAY).setMarginBottom(10));
        document.add(new LineSeparator(new SolidLine()).setMarginBottom(10));
    }

    private void addFooter(Document document) {
        String now = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        document.add(new Paragraph("Gerado em " + now)
                .setFontSize(9)
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.RIGHT));
    }
}