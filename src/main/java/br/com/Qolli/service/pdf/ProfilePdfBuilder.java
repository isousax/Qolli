package br.com.Qolli.service.pdf;

import br.com.Qolli.dto.UserProfile;
import br.com.Qolli.util.WatermarkService;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ProfilePdfBuilder {
    public byte[] build(UserProfile profile) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(output);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.setMargins(50, 40, 40, 40);

            addTitle(document);
            addLine(document);
            addUserInfoTable(document, profile);
            addFooter(document);

            WatermarkService.addImageWatermark(pdf);

            document.close();
            return output.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating profile PDF", e);
        }
    }

    private void addTitle(Document document) {
        document.add(new Paragraph("Informações do Usuário")
                .setFontSize(22)
                .setBold()
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));
    }

    private void addLine(Document document) {
        SolidLine line = new SolidLine(0.5f);
        line.setColor(ColorConstants.LIGHT_GRAY);
        document.add(new LineSeparator(line).setMarginBottom(25));
    }

    private void addUserInfoTable(Document document, UserProfile profile) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Table table = new Table(UnitValue.createPercentArray(new float[]{1.3f, 3f}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        table.addCell(createLabelCell("Nome"));
        table.addCell(createValueCell(profile.getName()));

        table.addCell(createLabelCell("E-mail"));
        table.addCell(createValueCell(profile.getEmail()));

        table.addCell(createLabelCell("UID"));
        table.addCell(createValueCell(profile.getUid()));

        table.addCell(createLabelCell("Criado em"));
        table.addCell(createValueCell(profile.getCreatedAt() != null ? formatter.format(profile.getCreatedAt()) : "N/A"));

        document.add(table);
    }

    private void addFooter(Document document) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        document.add(new Paragraph("Gerado em " + formatter.format(new Date()))
                .setFontSize(9)
                .setFontColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(30));
    }

    private Cell createLabelCell(String content) {
        return new Cell()
                .add(new Paragraph(content).setFontColor(ColorConstants.GRAY).setFontSize(11))
                .setPadding(6)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }

    private Cell createValueCell(String content) {
        return new Cell()
                .add(new Paragraph(content).setFontSize(12).setBold())
                .setPadding(6)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }
}
