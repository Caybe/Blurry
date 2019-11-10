package model;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;



import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class Invoice {


    private String dest = "C:\\Users\\cabez\\Desktop\\invoice.pdf";
    private PdfWriter writer;

    public Invoice() {
        generatePDF();
    }

    public void generatePDF(){
        try {
            // Creating a PdfWriter
            writer = new PdfWriter(dest);
            // Creating a PdfDocument
            PdfDocument pdfDoc = new PdfDocument(writer);
            // Creating a Document
            Document document = new Document(pdfDoc);

            Paragraph title = new Paragraph("BLURRY INVOICE");
            document.add(title);
            // Creating an Area Break
            AreaBreak aB = new AreaBreak();

            // Adding area break to the PDF
            document.add(aB);


            // Adding Blurry Logo
//            try {
//                String path = "src/images/logos/logov2.png";
//                ImageData data = ImageDataFactory.create(path);
//                Image image = new Image(data);
//                document.add(image);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }

            // Closing the document
            document.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
