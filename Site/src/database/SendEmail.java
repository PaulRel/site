package database;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import javafx.scene.control.Alert.AlertType;

import java.util.Properties;

import Interface.MainView;
import customer.Customer;

public class SendEmail {

    // Méthode pour configurer la session SMTP
    public static Session setupSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        
        String username = "fourel.pauline@gmail.com";
        String password = "orpbyhtmezbttrkq";

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    // Méthode pour envoyer un e-mail
    public static void sendEmail(Session session, String to, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("fourel.pauline@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Envoi de l'e-mail
            Transport.send(message);
            MainView.showAlert("Succès", null, "Email envoyé avec succès ", AlertType.INFORMATION);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour envoyer un e-mail spécifique de livraison
    public static void sendOrderDeliveryEmail(String email) {
        // Configuration de la session SMTP     
        Session session = setupSession();
        String to = email;

        // Sujet et corps du message
        String subject = "Votre commande est en cours de livraison";
        String body = "Bonjour,\n\nVotre commande est en cours de livraison et devrait arriver sous peu.\n\nMerci pour votre achat !\n\nCordialement,\nL'équipe.";

        // Envoi de l'e-mail
        sendEmail(session, to, subject, body);
    }
    
 // Méthode pour envoyer une newsletter à des clients
    public static void sendNewsletterEmail(String to) {
        // Configuration de la session SMTP
        Session session = setupSession();

        // Sujet et corps du message pour la newsletter
        String subject = "Découvrez nos nouvelles collections de vêtements et chaussures de tennis !";
        String body = "Bonjour,\n\n" +
                      "Nous avons le plaisir de vous annoncer l'arrivée de notre nouvelle collection de vêtements et chaussures de tennis.\n\n" +
                      "Voici quelques-unes des nouveautés disponibles sur notre site :\n" +
                      "- **Chaussures de Tennis Nike Zoom** - Confort et performance exceptionnels pour vos matchs.\n" +
                      "- **T-shirts Under Armour** - Légers et respirants pour vous accompagner pendant vos entraînements.\n" +
                      "- **Shorts Adidas Tennis** - Une coupe parfaite pour vous aider à rester agile sur le court.\n\n" +
                      "De plus, profitez de **20% de réduction** sur toute la collection avec le code **TENNIS20** !\n\n" +
                      "Visitez notre site pour découvrir toutes nos offres et trouver le produit parfait pour vous : [www.votresite.com](http://www.votresite.com)\n\n" +
                      "N'attendez plus, faites passer votre jeu au niveau supérieur avec notre nouvelle gamme !\n\n" +
                      "Cordialement,\nL'équipe TennisShop";

        // Envoi de l'e-mail
        sendEmail(session, to, subject, body);
    }
    
 // Méthode pour envoyer le mot de passe à un utilisateur (Customer)
    public static void sendPasswordToCustomer(String email) {
        // Configuration de la session SMTP
        Session session = setupSession();

        // Récupérer l'email et le mot de passe du Customer
        String to = email; // Email du client
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getCustomerByEmail(email);
        
        String customerPassword = customer.getPassword();

        // Sujet et corps du message
        String subject = "Votre mot de passe demandé";
        String body = "Bonjour " + customer.getLastName() + ",\n\n" +  // Ajout du nom du client
                      "Voici votre mot de passe :\n\n" +
                      customerPassword + "\n\n" +
                      "Si vous avez des questions, n'hésitez pas à nous contacter.\n\n" +
                      "Cordialement,\nL'équipe TennisShop";

        // Envoi de l'e-mail
        sendEmail(session, to, subject, body);
    }

}