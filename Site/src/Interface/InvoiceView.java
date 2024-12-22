package Interface;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class InvoiceView {
	public static void genererFacture(Invoice invoice) {
        try {
            // Initialiser le document PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(cheminFichier));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Titre de la facture
            document.add(new Paragraph("Facture").setBold().setFontSize(20));

            // Infos client
            document.add(new Paragraph("Nom : " + nomClient));
            document.add(new Paragraph("Adresse : " + adresseClient));
            document.add(new Paragraph("\n"));

            // Tableau pour les produits/services
            Table table = new Table(new float[]{3, 1, 1});
            table.addCell("Description");
            table.addCell("Quantité");
            table.addCell("Prix");

            for (String[] produit : produits) {
                table.addCell(produit[0]); // Description
                table.addCell(produit[1]); // Quantité
                table.addCell(produit[2]); // Prix
            }

            document.add(table);

            // Total
            document.add(new Paragraph("\nTotal : " + total + " €").setBold());

            // Fermer le document
            document.close();

            System.out.println("Facture générée : " + cheminFichier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

}
