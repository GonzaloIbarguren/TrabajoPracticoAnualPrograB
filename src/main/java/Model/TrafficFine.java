package Model;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class TrafficFine implements Serializable {
    private int fineNumber;
    private int pointScoring;
    private TypeInfraction typeInfraction;
    private Automobile automobile;
    private EventLocation event;
    private BigDecimal baseAmount, finalAmount;

    public TrafficFine(int fineNumber,int pointScoring,TypeInfraction typeInfraction, Automobile automobile, EventLocation locationEvent, BigDecimal baseAmount) {
        this.fineNumber = fineNumber;
        this.event = locationEvent;
        this.finalAmount = baseAmount;
        this.pointScoring = pointScoring;
        this.automobile =automobile;
        this.typeInfraction = typeInfraction;
        try {
            generatePDF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generatePDF() throws IOException {


        // Nombre del archivo con número correlativo
        String fileName = String.format("fines/fine_%06d.pdf", fineNumber);

        // Crear documento
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream content = new PDPageContentStream(doc,page);

        content.addRect(30,780,550,-700);
        content.stroke();

        content.addRect(50, 745, 500, 30); //
        content.stroke();


        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 20);
        content.newLineAtOffset(150, 753); // posición (x=200, y=750)
        content.showText("TRAFFIC INFRINGEMENT NOTICE");
        content.endText();

        content.beginText();
        content.newLineAtOffset(420, 730);
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.showText("Infringement Nro:");
        content.newLineAtOffset(100, 0);
        content.setFont(PDType1Font.HELVETICA, 12);
        content.showText(""+ fineNumber);
        content.endText();


        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 16);
        content.newLineAtOffset(60, 700);
        content.showText("OFFENCE DETAILS:");
        content.endText();

        content.beginText();
        content.newLineAtOffset(60, 670);
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.showText("Vehicle Plate:");
        content.newLineAtOffset(100, 0);
        content.setFont(PDType1Font.HELVETICA, 12);
        content.showText(automobile.getLicensePlate());
        content.newLineAtOffset(100, 0);
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.showText("Owner:");
        content.newLineAtOffset(50, 0);
        content.setFont(PDType1Font.HELVETICA, 12);
        content.showText(automobile.getOwner());
        content.endText();

        content.beginText();
        content.newLineAtOffset(60, 655);
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.showText("Offence Date:");
        content.newLineAtOffset(100, 0);
        content.setFont(PDType1Font.HELVETICA, 12);
        content.showText(event.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        content.endText();

        content.beginText();
        content.newLineAtOffset(60, 640);
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.showText("Offence Time:");
        content.newLineAtOffset(100, 0);
        content.setFont(PDType1Font.HELVETICA, 12);
        content.showText(event.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        content.endText();

        content.beginText();
        content.newLineAtOffset(60, 625);
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.showText("Offence Location:");
        content.newLineAtOffset(120, 0);
        content.setFont(PDType1Font.HELVETICA, 12);
        content.showText(event.getAddress());
        content.endText();


        content.beginText();
        content.newLineAtOffset(60, 595);
        content.setFont(PDType1Font.HELVETICA_BOLD, 16);
        content.showText("Description:");
        content.endText();

        content.addRect(65, 580, 500, -75); //
        content.stroke();


        content.beginText();
        content.newLineAtOffset(70, 565);
        content.setFont(PDType1Font.HELVETICA, 12);
        content.showText(typeInfraction.getDescription());
        content.endText();

        content.beginText();
        content.newLineAtOffset(60, 475);
        content.setFont(PDType1Font.HELVETICA_BOLD, 16);
        content.showText("Images:");
        content.endText();

        PDImageXObject image = PDImageXObject.createFromByteArray(doc, getClass().getResourceAsStream("/Cruce_rojo.jpg").readAllBytes(),"red");
        content.drawImage(image,70,250,200,200);


        content.beginText();
        content.newLineAtOffset(60,220 );
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.showText("Points Deducted:");
        content.newLineAtOffset(100, 0);
        content.setFont(PDType1Font.HELVETICA, 12);
        content.showText(""+pointScoring);
        content.endText();

        content.beginText();
        content.newLineAtOffset(60,190);
        content.setFont(PDType1Font.HELVETICA_BOLD, 14);
        content.showText("Infringement Penalty: ");
        content.newLineAtOffset(150, 0);
        content.setFont(PDType1Font.HELVETICA, 14);
        content.showText("$"+finalAmount);
        content.endText();



        String barcodeString = buildBarcodeString(fineNumber,finalAmount);
        BufferedImage barcodeImg = generateBarcodeImage(barcodeString,400,60);

        ByteArrayOutputStream baps = new ByteArrayOutputStream();
        ImageIO.write(barcodeImg,"png",baps);
        PDImageXObject barImg = PDImageXObject.createFromByteArray(doc,baps.toByteArray(),"barcode");
        content.drawImage(barImg,60,115,300,60);

        content.close();


        // Guardar en carpeta
        doc.save(fileName);
        doc.close();

        System.out.println("Fine saved in: " + fileName);
    }

    public String buildBarcodeString(int fineNumber,BigDecimal finalAmount){

        String A = String.format("%06d",fineNumber);

        BigDecimal centsBd = finalAmount.multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
        long cents = centsBd.longValueExact();
        String B = String.format("%012d",cents);
        System.out.println(A+B);

        return A + B;
    }

    public static BufferedImage generateBarcodeImage(String barcodeText,int height,int width){
        try {
            Code128Writer writer = new Code128Writer();
            BitMatrix bitMatrix = writer.encode(barcodeText, BarcodeFormat.CODE_128, width, height);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            throw new RuntimeException("Error generating barcode", e);
        }
    }








    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public EventLocation getEvent() {
        return event;
    }

    public void setEvent(EventLocation event) {
        this.event = event;
    }

    public void setAutomobile(Automobile automobile) {
        this.automobile = automobile;
    }

    public Automobile getAutomobile() {
        return automobile;
    }

    public TypeInfraction getTypeInfraction() {
        return typeInfraction;
    }

    public void setTypeInfraction(TypeInfraction typeInfraction) {
        this.typeInfraction = typeInfraction;
    }

    public int getPointScoring() {
        return pointScoring;
    }

    public void setPointScoring(int pointScoring) {
        this.pointScoring = pointScoring;
    }

    public int getFineNumer() {
        return fineNumber;
    }


    public int getFineNumber() {
        return fineNumber;
    }

    public void setFineNumber(int fineNumber) {
        this.fineNumber = fineNumber;
    }


    public void setFineNumer(int fineNumer) {
        this.fineNumber = fineNumer;
    }
}
