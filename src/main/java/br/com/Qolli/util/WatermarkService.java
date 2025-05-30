package br.com.Qolli.util;

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

        if (pdf == null || pdf.getNumberOfPages() == 0) {
            throw new IllegalStateException("PDF does not contain pages to apply watermark.");
        }

        String imagePath = "src/main/resources/static/images/Qolli.png";

        try {
            ImageData imageData = ImageDataFactory.create(imagePath);

            for (int i = 1; i <= pdf.getNumberOfPages(); i++) {
                PdfPage page = pdf.getPage(i);

                if (page == null) {
                    System.err.println("Page " + i + " is null. Jumping...");
                    continue;
                }

                Rectangle pageSize = null;
                try {
                    pageSize = page.getPageSize();
                } catch (Exception e) {
                    System.err.println("Error getting pageSize of page " + i + ": " + e.getMessage());
                    continue;
                }

                if (pageSize == null) {
                    System.err.println("pagePage Size " + i + " is null. Jumping...");
                    continue;
                }

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
            throw new RuntimeException("Error adding watermark", e);
        }
    }
}
