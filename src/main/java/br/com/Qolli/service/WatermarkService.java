package br.com.Qolli.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import org.springframework.stereotype.Service;

@Service
public class WatermarkService {
    public static void addImageWatermark(PdfDocument pdf) {
        String imagePath = "src/main/resources/static/images/Qolli.png";

        try {
            ImageData imageData = ImageDataFactory.create(imagePath);

            for (int i = 1; i <= pdf.getNumberOfPages(); i++) {
                PdfPage page = pdf.getPage(i);
                Rectangle pageSize = page.getPageSize();

                PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
                Canvas canvas = new Canvas(pdfCanvas, pageSize);

                Image img = new Image(imageData)
                        .scaleAbsolute(pageSize.getWidth(), pageSize.getHeight())
                        .setOpacity(0.05f)
                        .setFixedPosition(0, 0);

                canvas.add(img);
                canvas.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar marca d'água", e);
        }
    }
}
