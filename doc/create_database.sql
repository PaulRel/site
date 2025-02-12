CREATE DATABASE ma_base_de_donnees;
USE ma_base_de_donnees;

CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(2000),
    type ENUM('vetement', 'chaussures') NOT NULL,
    brand VARCHAR(20) NOT NULL,
    price DECIMAL(10,2) DEFAULT 0,
    image_path VARCHAR(50)
);

CREATE TABLE clothing (
    product_id INT PRIMARY KEY,
    type ENUM('Short', 'Sweat', 'Debardeur', 'Tshirt', 'Pantalon', 'Robe', 'Veste'),
    gender ENUM('Homme', 'Femme') NOT NULL,
    color VARCHAR(50) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE shoes (
    product_id INT PRIMARY KEY,
    surface ENUM('TOUTES SURFACES', 'terre battue', 'dur', 'gazon'),
    gender ENUM('Homme', 'Femme'),
    color VARCHAR(50) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE size_stock (
    product_id INT,
    size VARCHAR(10) NOT NULL,
    stock INT DEFAULT 0,
    PRIMARY KEY (product_id, size),
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);
CREATE TABLE customer (
    customerID INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    civility ENUM('M', 'Mme') NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(15) DEFAULT '',
    password VARCHAR(255) NOT NULL,
    role ENUM('Customer', 'Admin') DEFAULT 'Customer',
    address VARCHAR(255)
);

CREATE TABLE `order` (
order_id INT AUTO_INCREMENT PRIMARY KEY, -- Identifiant unique de la commande
customer_id INT NOT NULL,                -- Identifiant du client    
order_date DATETIME NOT NULL,     -- Date et heure de la commande    
status VARCHAR(20) DEFAULT 'En cours',    -- Statut de la commande    
FOREIGN KEY (customer_id) REFERENCES customer(customerID) ON DELETE CASCADE
);

CREATE TABLE OrderDetails (
detail_id INT AUTO_INCREMENT PRIMARY KEY, -- Identifiant unique pour chaque ligne de détail
order_id INT NOT NULL,                    -- Référence à la commande    
product_id INT NOT NULL,               -- Référence au produit    
quantity INT NOT NULL,                    -- Quantité commandée pour ce produit   
size VARCHAR(10) NOT NULL,
FOREIGN KEY (order_id) REFERENCES `order`(order_id) ON DELETE CASCADE,
FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE `invoice` (  
`invoice_id` INT NOT NULL AUTO_INCREMENT, -- Identifiant unique pour chaque facture  
`order_id` INT NOT NULL,                  -- Référence à la commande associée  
`billing_address` VARCHAR(255) NOT NULL,  -- Adresse de facturation  
`shipping_address` VARCHAR(255) NOT NULL, -- Adresse de livraison 
`shipping_method` VARCHAR(255) NOT NULL,  -- Methode de livraison
`shipping_price` DECIMAL(10,2),  -- Prix de livraison
`payment_method` VARCHAR(50) NOT NULL,    -- Moyen de paiement 
`invoice_date` DATETIME NOT NULL,         -- Date de création de la facture  
PRIMARY KEY (`invoice_id`),               -- Clé primaire  
KEY `order_id` (`order_id`),              -- Index pour la colonne order_id  
CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE CASCADE -- Contrainte de clé étrangère
);
