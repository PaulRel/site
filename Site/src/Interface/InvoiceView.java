package Interface;

import customer.CartItem;
import customer.Invoice;
import customer.Order;
import database.InvoiceDAO;
import javafx.scene.control.Alert.AlertType;
import products.Product;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InvoiceView {
	
	Invoice invoice;
	public void generateInvoice(Order order) {
		// Récupération des informations de la facture
		InvoiceDAO invoiceDAO = new InvoiceDAO();
		invoice = invoiceDAO.getInvoiceById(order);

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
	        
	        try {// Ajoute l'icone du site  
	            ImageData imageData = ImageDataFactory.create(getClass().getResource("/Image/logo.jpg").toExternalForm());
	            Image logo = new Image(imageData);
	            logo.setWidth(50); // Ajuste la taille de l'image
	            logo.setHeight(50);
	            document.add(logo);
	        } catch (Exception e) {
	            // Si l'image ne peut pas être chargée, laisse vide ou ajoute un texte
	            document.add(new Paragraph("Image non disponible"));
	        }
	        
	        // Titre de la facture	        
	        document.add(new Paragraph("Facture").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
	              
	        // Ajout de la table des adresses au document
            document.add(getInfoTable(order));
            
            // Ajout des informations de commande
	        document.add(new Paragraph("\nDétails de la commande").setBold());
	        document.add(new Paragraph("Référence de la commande : " + order.getOrderId()));
	        document.add(new Paragraph("Date : " + order.getOrderDate()+"\n"));
            document.add(getProductsTable(order));
            
            // Ajout des totaux
            document.add(getTotalTable(order));

	        document.close();
	        MainView.showAlert("Succès", null, "Facture générée avec succès : " + pdfFilePath, AlertType.INFORMATION);

	        // Ouverture automatique du PDF après sa génération
	        File pdfFile = new File(pdfFilePath);
	        if (pdfFile.exists()) {
	            java.awt.Desktop.getDesktop().open(pdfFile);
	        }
	    } catch (IOException e) {
	        MainView.showAlert("Erreur", null, "Une erreur est survenue lors de la génération de la facture : " + e.getMessage(), AlertType.ERROR);
	    }
    }
	
	private Table getInfoTable(Order order) {
		// Ajout des adresses en haut de la facture
		String customerLastName = order.getCustomer().getLastName();
		String customerFirstName = order.getCustomer().getFirstName();
		String customerPhoneNumber = order.getCustomer().getPhoneNumber();
		
        float[] columnWidths = {1, 1}; // 2 colonnes de taille égale
        Table addressesTable = new Table(columnWidths);
        addressesTable.setBorder(Border.NO_BORDER);

        // Colonne gauche : Adresse de facturation
        Cell billingAddressCell = new Cell();
        billingAddressCell.setBorder(Border.NO_BORDER); billingAddressCell.setPadding(10); billingAddressCell.setMargin(10);
        billingAddressCell.add(new Paragraph("Adresse de facturation").setBold());
        billingAddressCell.add(new Paragraph(customerFirstName + customerLastName));
        billingAddressCell.add(new Paragraph(invoice.getBillingAddress()));
        billingAddressCell.add(new Paragraph(customerPhoneNumber));
        addressesTable.addCell(billingAddressCell);

        // Colonne droite : Adresse de livraison
        Cell shippingAddressCell = new Cell();
        shippingAddressCell.setBorder(Border.NO_BORDER); shippingAddressCell.setPadding(10); shippingAddressCell.setMargin(10);
        shippingAddressCell.add(new Paragraph("Adresse de livraison").setBold());
        shippingAddressCell.add(new Paragraph(customerFirstName + customerLastName));
        shippingAddressCell.add(new Paragraph(invoice.getShippingAddress()));
        shippingAddressCell.add(new Paragraph(customerPhoneNumber));
        addressesTable.addCell(shippingAddressCell);
        
        addressesTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        return addressesTable;
	}
	
	
	private Table getProductsTable(Order order) {
		// Définit un tableau avec 5 colonnes
		float[] columnWidths = {1, 3, 2, 1, 2}; // Largeurs relatives
        Table table = new Table(columnWidths);

        // Ajoute les en-têtes de colonne
        table.addHeaderCell(new Cell().add(new Paragraph("Référence").setTextAlignment(TextAlignment.CENTER)));
        table.addHeaderCell(new Cell().add(new Paragraph("Produit").setTextAlignment(TextAlignment.CENTER)));
        table.addHeaderCell(new Cell().add(new Paragraph("Prix unitaire (HT)").setTextAlignment(TextAlignment.CENTER)));
        table.addHeaderCell(new Cell().add(new Paragraph("Qté").setTextAlignment(TextAlignment.CENTER)));
        table.addHeaderCell(new Cell().add(new Paragraph("Total (TTC)").setTextAlignment(TextAlignment.CENTER)));

        // Récupère la liste des produits
        List<CartItem> products = order.getProducts();

        // Parcourt les produits pour remplir la table
        for (CartItem item : products) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            String size = item.getSize();

            // Colonne Référence
            table.addCell(new Cell().add(new Paragraph(String.valueOf(product.getId())).setTextAlignment(TextAlignment.CENTER)));

            // Colonne Produit (image et nom)
            Cell productCell = new Cell();
            try {
                // Ajoute l'image du produit
                Image productImage = new Image(ImageDataFactory.create(getClass().getResource(product.getImagePath()).toExternalForm()));
                productImage.setWidth(50); // Ajuste la taille de l'image
                productImage.setHeight(50);
                productCell.add(productImage);
            } catch (Exception e) {
                // Si l'image ne peut pas être chargée, laisse vide ou ajoute un texte
                productCell.add(new Paragraph("Image non disponible"));
            }
            // Ajoute le nom du produit
            productCell.add(new Paragraph(product.getName() + " Taille: "+ size));
            table.addCell(productCell);

            // Colonne Prix unitaire (HT) 	Supposons un taux de TVA de 20 %
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f €", product.getPrice()/1.2)).setTextAlignment(TextAlignment.RIGHT))); // Prix TTC / 1.2 (TVA)

            // Colonne Qté
            table.addCell(new Cell().add(new Paragraph(String.valueOf(quantity)).setTextAlignment(TextAlignment.CENTER)));

            // Colonne Total (TTC)
            double totalTTC = product.getPrice() * quantity; // Prix TTC × quantité
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f €", totalTTC))));
        }
        return table;
	}
	
	
	private Table getTotalTable(Order order) {
        float[] columnWidths = {2, 1}; // 2 colonnes de taille égale
        Table totalTable = new Table(columnWidths);
        totalTable.setWidth(UnitValue.createPercentValue(100));
        totalTable.setBorder(Border.NO_BORDER);
        
        // Ajout des détails de la facture
        Cell detailsCell = new Cell();
        detailsCell.setPaddingTop(30);
        detailsCell.setBorder(Border.NO_BORDER);
        detailsCell.add(new Paragraph());
        detailsCell.add(new Paragraph("Mode de livraison : " + invoice.getShippingMethod()));
        detailsCell.add(new Paragraph("Mode de paiement : " + invoice.getPaymentMethod()));
        totalTable.addCell(detailsCell);
        
        // Ajout des totaux
        Cell totalCell = new Cell();
        totalCell.setPaddingTop(30);
        totalCell.setBorder(Border.NO_BORDER);
        totalCell.setTextAlignment(TextAlignment.RIGHT);
        totalCell.add(new Paragraph("Total produits HT            " + String.format("%.2f €", order.getTotalPrice()/1.2)));
        totalCell.add(new Paragraph("Total produits TTC           " + String.format("%.2f €", order.getTotalPrice())));
        totalCell.add(new Paragraph("Total frais de port           "+ String.format("%.2f €", invoice.getShippingPrice())));
        totalCell.add(new Paragraph("Total HT           " + String.format("%.2f €", order.getTotalPrice()/1.2)));
        totalCell.add(new Paragraph("TVA (20%)             " + String.format("%.2f €", order.getTotalPrice()*0.2)));
        totalCell.add(new Paragraph("Total TTC           " + String.format("%.2f €", order.getTotalPrice()+invoice.getShippingPrice())).setBold().setFontSize(16));
        totalTable.addCell(totalCell);
        return totalTable;	
	}
	
	public void showInvoice(Invoice invoice) {
	    // Chemin du fichier PDF de la facture
	    String pdfFilePath = "facture_" + invoice.getInvoiceId() + ".pdf";

	    // Vérification si le fichier PDF existe
	    File pdfFile = new File(pdfFilePath);
	    if (pdfFile.exists()) {
	        try {
	            // Ouvrir le PDF avec l'application par défaut
	            java.awt.Desktop.getDesktop().open(pdfFile);
	        } catch (IOException e) {
	            // Gestion d'erreur si le PDF ne peut pas être ouvert
	            System.out.println("Erreur lors de l'ouverture du fichier PDF: " + e.getMessage());
	        }
	    } else {
	        System.out.println("Le fichier PDF de la facture n'existe pas.");
	    }
	}

}
