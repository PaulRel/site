package Interface;

import customer.Invoice;
import customer.Order;
import database.InvoiceDAO;
import javafx.scene.control.Alert.AlertType;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.io.IOException;

public class InvoiceView {
	
	public void genererFacture(Order order) {
		// Récupération des informations de la facture
		InvoiceDAO invoiceDAO = new InvoiceDAO();
		Invoice invoice = invoiceDAO.getInvoiceByOrder(order);

	    // Si la facture est introuvable, afficher une erreur
	    if (invoice == null) {
	        System.out.println("Facture introuvable pour la commande : " + order.getOrderId());
	        return;
	    }

	    // Chemin pour enregistrer le PDF
	    String pdfFilePath = "facture_" + invoice.getInvoiceId() + ".pdf";

	    try {
	        // Création du PDF
	        PdfWriter writer = new PdfWriter(pdfFilePath);
	        PdfDocument pdfDoc = new PdfDocument(writer);
	        Document document = new Document(pdfDoc);

	        // Titre de la facture	        
	        //document.add(new Paragraph("Facture").setBold().setFontSize(18));

	        // Ajout des détails de la facture
	        document.add(new Paragraph("Numéro de facture : " + invoice.getInvoiceId()));
	        document.add(new Paragraph("Adresse de facturation : " + invoice.getBillingAddress()));
	        document.add(new Paragraph("Adresse de livraison : " + invoice.getShippingAddress()));
	        document.add(new Paragraph("Mode de livraison : " + invoice.getShippingMethod()));
	        document.add(new Paragraph("Mode de paiement : " + invoice.getPaymentMethod()));

	        // Ajout d'une ligne de séparation
	        document.add(new Paragraph("----------------------------------------------------"));

	        // Ajout des informations de commande
	        document.add(new Paragraph("Détails de la commande"));
	        document.add(new Paragraph("Numéro de commande : " + order.getOrderId()));
	        document.add(new Paragraph("Date : " + order.getOrderDate()));
	        document.add(new Paragraph("Total : " + String.format("%.2f €", order.getTotalPrice())));

	        // Fermeture du document
	        document.close();

	        // Afficher une confirmation
	        MainView.showAlert("Succès", null, "Facture générée avec succès : " + pdfFilePath, AlertType.INFORMATION);

	        // Optionnel : ouverture automatique du PDF après sa génération
	        File pdfFile = new File(pdfFilePath);
	        if (pdfFile.exists()) {
	            java.awt.Desktop.getDesktop().open(pdfFile);
	        }
	    } catch (IOException e) {
	        MainView.showAlert("Erreur", null, "Une erreur est survenue lors de la génération de la facture : " + e.getMessage(), AlertType.ERROR);
	    }
    }
}
