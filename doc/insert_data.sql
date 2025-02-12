    -- Désactiver les vérifications de clés étrangères
    SET FOREIGN_KEY_CHECKS = 0;

    -- Vider les tables spécifiées
    TRUNCATE TABLE size_stock; 
    TRUNCATE TABLE shoes; 
    TRUNCATE TABLE clothing; 
    TRUNCATE TABLE product;
    TRUNCATE TABLE customer;
    TRUNCATE TABLE `order`;
	TRUNCATE TABLE orderdetails;
    TRUNCATE TABLE invoice;

    -- Réactiver les vérifications de clés étrangères
    SET FOREIGN_KEY_CHECKS = 1;


-- Chaussure 1 ASICS
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES ASICS FEMME GEL-CHALLENGER 14 TOUTES SURFACES',
    'La chaussure de tennis Asics Femme Gel Challenger 14 Toutes Surfaces se dévoile pour le plus grand plaisir des joueuses recherchant un modèle stable et confortable. Parfait pour les joueuses de tous les niveaux, ce modèle saura convaincre aussi bien par ses performances que par son nouveau look. Le coloris marine, vert d\'eau et lime conviendra parfaitement aux personnes souhaitant un coloris sobre, mais également à ceux souhaitant un modèle plus coloré. ',
    'chaussures', 
    'Asics', 
    82.95, 
    '/Image/Chaussures/asics1.jpg'
);

SET @product_id = LAST_INSERT_ID();

INSERT INTO shoes (product_id, surface, gender, color) VALUES (@product_id, 'TOUTES SURFACES', 'Femme', 'Marine, Vert d\'eau, Lime');

INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 5),
(@product_id, '37', 10),
(@product_id, '38', 14),
(@product_id, '39', 20);

-- Chaussure 2
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES ASICS FEMME GEL RESOLUTION 9 TOUTES SURFACES',
    'La chaussure de tennis Asics Femme Gel Resolution Toutes Surfaces surfe sur le succès de sa version 8, adoubée par Matteo Berrettini, pour continuer d\'améliorer ce modèle et passer à la version 9. Les points forts de cette chaussure, idéale pour les joueuses défendant leur terrain coûte que coûte, restent les mêmes avec une importante stabilité et beaucoup de maintien, caractéristiques encore améliorées sur la Resolution 9. Côté design, son coloris rose, bordeaux et blanc ne se démodera pas et complètera vos tenues vestimentaires.',
    'chaussures', 
    'Asics', 
    87.95, 
    '/Image/Chaussures/asics2.jpg'
);
SET @product_id = LAST_INSERT_ID();

INSERT INTO shoes (product_id, surface, gender, color) VALUES (@product_id, 'TOUTES SURFACES', 'Femme', 'Rose, Bordeaux, Blanc');

INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 5),
(@product_id, '37', 6),
(@product_id, '38', 1),
(@product_id, '39', 15);

-- Chaussure 3
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES ASICS FEMME SOLUTION SPEED FF3 PARIS TOUTES SURFACES',
    'La chaussure de tennis Asics Femme Solution Speed FF 3 Paris Toutes Surfaces est la dernière innovation de la brand japonaise. Elle s\'adresse aux joueuses polyvalentes à la recherche d\'un modèle dynamique et léger. Cette version 3 intègre des renforts supplémentaires et booste toujours plus votre vitesse. Le coloris vert d\'eau, jaune fluo et marine apportera une touche dynamique à votre tenue.',
    'chaussures', 
    'Asics', 
    107.95,  
    '/Image/Chaussures/asics3.jpg'
);

SET @product_id = LAST_INSERT_ID();

INSERT INTO shoes (product_id, surface, gender, color) VALUES (@product_id, 'TOUTES SURFACES', 'Femme', 'Vert d\'eau, Jaune fluo, Marine');

INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 5),
(@product_id, '37', 0),
(@product_id, '38', 15),
(@product_id, '39', 20);

-- Chaussure 4
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES ASICS FEMME GEL-RESOLUTION 9 PARIS TOUTES SURFACES',
    'La chaussure de tennis Asics Femme Gel Resolution Paris Toutes Surfaces surfe sur le succès de sa version 8, adoubée par Matteo Berrettini, pour continuer d\'améliorer ce modèle et passer à la version 9. Les points forts de cette chaussure, idéale pour les joueuses défendant leur terrain coûte que coûte, restent les mêmes avec une importante stabilité et beaucoup de maintien, caractéristiques encore améliorées sur la Resolution 9. Côté design, son coloris vert d\'eau, marine et lime ne se démodera pas et complètera vos tenues vestimentaires. ',
    'chaussures', 
    'Asics', 
    87.95, 
    '/Image/Chaussures/asics4.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO shoes (product_id, surface, gender, color) VALUES (@product_id, 'TOUTES SURFACES', 'Femme', 'Vert d\'eau, Marine, Lime');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 5),
(@product_id, '37', 10),
(@product_id, '38', 15),
(@product_id, '39', 20),
(@product_id, '40', 10);

-- Chaussure 5/5 Asics
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES ASICS FEMME COURT FF TOUTES SURFACES',
    'La chaussure Asics Femme Court FF 3 Toutes Surfaces a été retravaillée avec la collaboration du joueur serbe Novak Djokovic. En dehors d\'une empeigne modifiée avec un style plus dynamique, elle apportera également plus de stabilité. Les joueuses à la recherche de la polyvalence ultime, qui ne souhaitent pas choisir entre vitesse et stabilité seront séduites par cette version 3. Le coloris blanc, turquoise et lime est idéal pour compléter votre style.',
    'chaussures', 
    'Asics', 
    134.95, 
    '/Image/Chaussures/asics5.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO shoes (product_id, surface, gender, color) VALUES (@product_id, 'TOUTES SURFACES', 'Femme', 'Blanc, Turquoise, Lime');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 5),
(@product_id, '37', 10),
(@product_id, '38', 15),
(@product_id, '39', 20),
(@product_id, '40', 10);


# Chaussure 1/5 ADIDAS
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES ADIDAS FEMME GAMECOURT 2 TOUTES SURFACES',
    'Ces chaussures adidas GameCourt 2 Toutes Surfaces allient légèreté et respirabilité pour que vos pieds soient toujours au sec. De plus, elles s\'accorderont parfaitement avec toutes vos tenues grâce à leur coloris rose et blanc.',
    'chaussures', 
    'Adidas', 
    63.00, 
    '/Image/Chaussures/adidas1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO shoes (product_id, surface, gender, color) VALUES (@product_id, 'TOUTES SURFACES', 'Femme', 'Rose, Blanc');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 5),
(@product_id, '37', 10),
(@product_id, '38', 15),
(@product_id, '39', 10),
(@product_id, '40', 10);

# Chaussure 2/5 Adidas Femme
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES ADIDAS FEMME BARRICADE TOUTES SURFACES',
    'La célèbre Barricade est de retour ! Le modèle iconique créé par adidas fait son retour sur les courts pour vous apporter le contrôle nécessaire à tous vos déplacements. Maîtrisez vos déplacements pour maîtriser votre jeu et dominer votre adversaire. Le coloris blanc et argenté est très sobre et ravira les joueuses en recherche d\'élégance.',
    'chaussures',
    'Adidas', 
    81.95, 
    '/Image/Chaussures/adidas2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO shoes (product_id, surface, gender, color) VALUES (@product_id, 'TOUTES SURFACES', 'Femme', 'Blanc, Argent');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 10),
(@product_id, '37', 10),
(@product_id, '38', 10),
(@product_id, '39', 10),
(@product_id, '40', 10);


-- Chaussure 3/5 Adidas Femme
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES ADIDAS FEMME BARRICADE 13 TOUTES SURFACES',
    'Découvrez la 13ème version de la chaussure adidas Femme Barricade Toutes Surfaces ! Suivant les traces de l\'édition précédente, cette version vient améliorer la stabilité et le confort pour les joueuses défendant leur terrain coûte que coûte. Le coloris blanc et argenté est très réussi à fera fureur à vos pieds.',
    'chaussures', 
    'Adidas', 
    107.95,
    '/Image/Chaussures/adidas3.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO shoes (product_id, surface, gender, color) VALUES (@product_id, 'TOUTES SURFACES', 'Femme', 'Blanc, Argent');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 5),
(@product_id, '37', 10),
(@product_id, '38', 15),
(@product_id, '39', 20),
(@product_id, '40', 10);


-- Chaussure 4/5 Adidas Femme
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES ADIDAS FEMME BARRICADE 13 NEW YORK TOUTES SURFACES',
    'Découvrez la 13ème version de la chaussure adidas Femme Barricade New York Toutes Surfaces ! Suivant les traces de l\'édition précédente, cette version vient améliorer la stabilité et le confort pour les joueuses défendant leur terrain coûte que coûte. Le coloris blanc, violet et turquoise est très réussi et fera fureur à vos pieds.',
    'chaussures', 
    'Adidas', 
    127.95, 
    '/Image/Chaussures/adidas4.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO shoes (product_id, surface, gender, color) VALUES (@product_id, 'TOUTES SURFACES', 'Femme', 'Blanc, Violet, Turquoise');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 5),
(@product_id, '37', 10),
(@product_id, '38', 15),
(@product_id, '39', 20),
(@product_id, '40', 10);

-- Chaussure 5/5 Adidas Homme
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES ADIDAS UBERSONIC 5 TOUTES SURFACES',
    'Adidas brand une rupture dans la gamme Adizero Ubersonic avec cette version 5, aussi bien sur le plan technique que sur le plan esthétique. En effet, la vitesse sera toujours plus au coeur de ce modèle pour permettre aux joueurs de se déplacer toujours plus vite avec une chaussure toujours plus légère tout en améliorant le maintien pour sécuriser vos courses. Le design plus bas, plus dynamique et plus moderne va dans le sens des inovations et le coloris rouge et blanc mettra votre jeu de jambe en valeur.',
    'chaussures', 
    'Adidas', 
    129.90, 
    '/Image/Chaussures/adidas5.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO shoes (product_id, surface, gender, color) 
VALUES (@product_id, 'TOUTES SURFACES', 'Homme', 'Rouge, Blanc');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '39', 10),
(@product_id, '40', 15),
(@product_id, '41', 20);


-- Chaussures 1/3 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES BABOLAT FEMME JET MACH 3 TERRE BATTUE',
    'La chaussure Babolat Jet Mach Terre Battue passe à la version 3.0 en proposant une toute nouvelle coupe inspirée de la Babolat Fury. Un modèle qui joue une fois de plus sur sa grande légèreté ainsi que sur son maintien, son confort et sa résistance. Idéale pour les joueuses à la recherche d\'une réelle agilité, cette chaussure saura également vous convaincre de par son design soigné au coloris rose et blanc.',
    'chaussures', 
    'Babolat', 
    49.95, 
    '/Image/Chaussures/babolat1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO shoes (product_id, surface, gender, color) 
VALUES (@product_id, 'TERRE BATTUE', 'Femme', 'Rose, Blanc');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 6),
(@product_id, '37', 8),
(@product_id, '38', 10),
(@product_id, '39', 10),
(@product_id, '40', 10);
 
 
-- Chaussures 2/3 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES BABOLAT FEMME JET MACH 3 TERRE BATTUE',
    'La chaussure Babolat Jet Mach Terre Battue passe à la version 3.0 en proposant une toute nouvelle coupe inspirée de la Babolat Fury. Un modèle qui joue une fois de plus sur sa grande légèreté ainsi que sur son maintien, son confort et sa résistance. Idéale pour les joueuses à la recherche d\'une réelle agilité, cette chaussure saura également vous convaincre de par son design soigné au coloris bleu, blanc et rose.',
    'chaussures', 
    'Babolat', 
    63.95, 
    '/Image/Chaussures/babolat2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO shoes (product_id, surface, gender, color) 
VALUES (@product_id, 'TERRE BATTUE', 'Femme', 'Bleu, Blanc, Rose');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '36', 5),
(@product_id, '37', 10),
(@product_id, '38', 15);


-- Chaussure 3/3 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'CHAUSSURES BABOLAT JET MACH 3 TERRE BATTUE',
    'La chaussure Babolat Jet Mach Terre Battue passe à la version 3.0 en proposant une toute nouvelle coupe inspirée de la Babolat Fury. Un modèle qui joue une fois de plus sur sa grande légèreté ainsi que sur son maintien, son confort et sa résistance. Idéale pour les joueurs à la recherche d\'une réelle agilité, cette chaussure saura également vous convaincre de par son design soigné aux coloris bleu et ciel..',
    'chaussures', 
    'Babolat', 
    85.95, 
    '/Image/Chaussures/babolat3.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO shoes (product_id, surface, gender, color) 
VALUES (@product_id, 'TERRE BATTUE', 'Homme', 'Bleu, Ciel');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, '39', 12),
(@product_id, '40', 14),
(@product_id, '41', 16);

-- VETEMENTS
-- Debardeur
-- Debardeur 1 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'DEBARDEUR BABOLAT FEMME PLAY',
    'Ce débardeur Babolat au design simpliste et coloré est un incontournable des courts de tennis. Conçu pour apporter aux joueuses un bien-être en jeu optimal, il est le vêtement qu\'il vous faut pour performer avec efficacité sur le terrain. Doté de la technologie Fiber Dry, il évacue la sueur emmagasinée lors de votre effort pour vous maintenir au frais et au sec en toutes circonstances. ',
    'vetement', 
    'Babolat', 
    11.95, 
    '/Image/Vetements/Debardeur/babolat1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Debardeur', 'Femme', 'bleu');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 5),
(@product_id, 'M', 10),
(@product_id, 'L', 5);

-- Debardeur 2 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'DEBARDEUR BABOLAT FEMME PADEL',
    'Le débardeur Babolat Padel vous donnera de la souplesse et un séchage rapide pour un maximum de confort.',
    'vetement', 
    'Babolat', 
    12.50, 
    '/Image/Vetements/Debardeur/babolat2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Debardeur', 'Femme', 'blanc');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 5),
(@product_id, 'M', 8),
(@product_id, 'L', 7);


-- Débardeur 3 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'DEBARDEUR BABOLAT FEMME EXERCISE COTTON',
    'Le débardeur Babolat Exercise cotton est confectionné dans un tissu en coton mêlé à du polyester pour vous apporter autant de confort qu\'une excellente liberté de mouvement lors de votre temps de jeu. ',
    'vetement', 
    'Babolat', 
    11.95, 
    '/Image/Vetements/Debardeur/babolat3.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Debardeur', 'Femme', 'Bleu');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 0),
(@product_id, 'M', 15),
(@product_id, 'L', 10);


-- Débardeur 1 Asics
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'COURT DEBARDEUR TANK TOP FEMMES',
    'Le COURT TANK te permet de rester en mouvement grâce à son design flare classique. Son tissu doux et à séchage rapide permet une meilleure régulation de l\'humidité et un confort accru lors de tes matchs ou de tes entraînements. Au moins 50 % du tissu principal du vêtement est composé de matériaux recyclés afin de réduire les déchets et les émissions de CO2.',
    'vetement', 
    'Asics', 
    20.95, 
    '/Image/Vetements/Debardeur/asics1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Debardeur', 'Femme', 'Blanc, Bleu Foncé');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 8),
(@product_id, 'M', 12),
(@product_id, 'L', 10);

-- Débardeur 2 Asics
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'COURT DEBARDEUR TANK TOP FEMMES',
    'Le COURT TANK te permet de rester en mouvement grâce à son design flare classique. Son tissu doux et à séchage rapide permet une meilleure régulation de l\'humidité et un confort accru lors de tes matchs ou de tes entraînements.',
    'vetement', 
    'Asics', 
    19.95, 
    '/Image/Vetements/Debardeur/asics2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Debardeur', 'Femme', 'Rosé, Blanc');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 7),
(@product_id, 'M', 10),
(@product_id, 'L', 8);

-- Débardeur 3 Asics
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'COURT DEBARDEUR TANK TOP FEMMES',
    'Le COURT TANK te permet de rester en mouvement grâce à son design flare classique. Son tissu doux et à séchage rapide permet une meilleure régulation de l\'humidité et un confort accru lors de tes matchs ou de tes entraînements.',
    'vetement', 
    'Asics', 
    20.95, 
    '/Image/Vetements/Debardeur/asics3.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Debardeur', 'Femme', 'Bleu Foncé, Blanc');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 6),
(@product_id, 'M', 14),
(@product_id, 'L', 11);

-- Débardeur 4 Asics
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'COURT DEBARDEUR TANK TOP FEMMES',
    'Le COURT TANK te permet de rester en mouvement grâce à son design flare classique. Son tissu doux et à séchage rapide permet une meilleure régulation de l\'humidité et un confort accru lors de tes matchs ou de tes entraînements.',
    'vetement', 
    'Asics', 
    20.95, 
    '/Image/Vetements/Debardeur/asics4.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Debardeur', 'Femme', 'Rosé');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 9),
(@product_id, 'M', 13),
(@product_id, 'L', 10);


-- Debardeur 1 Adidas
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'DEBARDEUR ADIDAS FEMME PRO PARIS',
    'Performez avec style sur les courts de tennis avec ce débardeur adidas femme Pro Paris !',
    'vetement', 
    'Adidas', 
    40.95, 
    '/Image/Vetements/Debardeur/adidas1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Debardeur', 'Femme', 'jaune');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 5),
(@product_id, 'M', 10),
(@product_id, 'L', 10);


-- Débardeur 2 Adidas
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'DEBARDEUR ADIDAS FEMME CLUB',
    'Performez avec style sur les courts de tennis avec ce débardeur adidas femme Club !',
    'vetement', 
    'Adidas', 
    19.95, 
    '/Image/Vetements/Debardeur/adidas2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Debardeur', 'Femme', 'Blanc');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 12),
(@product_id, 'M', 14),
(@product_id, 'L', 10);

-- Débardeur 3 Adidas
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'DEBARDEUR ADIDAS FEMME',
    'Performez avec style sur les courts de tennis avec ce débardeur Adidas femme ! Conçu à base de polyester recyclé, ce débardeur a été pensé spécialement pour la pratique du sport tout en préservant les ressources naturelles de la planète. Son tissu léger et extensible permet des mouvements fluides pour une performance efficace sur le court. Côté design, ce débardeur de tennis adopte une coupe féminine près du corps qui mettra votre silhouette en valeur.',
    'vetement', 
    'Adidas', 
    13.95, 
    '/Image/Vetements/Debardeur/adidas3.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Debardeur', 'Femme', 'Rouge, Orange');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 15),
(@product_id, 'L', 8);


-- Sweat
-- Sweat 1 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'SWEAT BABOLAT FEMME EXERCISE HOOD',
    'Ce sweat Babolat femme Exercise Hood sera idéal pour vous couvrir lors de vos échauffements et vos retours au calme. Ce sweat garantit un confort total au porté grâce à son tissu en coton doux et agréable. Confectionné également en polyester, il assure une excellente liberté de mouvement grâce à sa légèreté. Il dispose d\'une capuche pour vous protéger en cas de pluie ainsi que d\'une poche kangourou. Côté design ce sweat arbore un style sobre avec le `name` de la brand au centre, il pourra s\'adapter à toutes vos tenues de tennis.',
    'vetement', 
    'Babolat', 
    36.95, 
    '/Image/Vetements/Sweat/babolat1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Sweat', 'Femme', 'Marine, Bleu');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 7),
(@product_id, 'M', 12),
(@product_id, 'L', 10);

-- Sweat 2 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'SWEAT BABOLAT FEMME EXERCISE HOOD',
    'Optez pour ce sweat Babolat femme Exercise Hood pour plus de chaleur et de confort sur le terrain comme au quotidien. Conçu en molleton, il conservera votre chaleur corporelle afin de vous offrir un maximum de confort. Confectionné également en polyester, il vous assurera une excellente liberté de mouvement. Il dispose d\'une capuche pour vous protéger en cas de pluie ainsi que d\'une poche kangourou. Côté design ce sweat bleu pourra s\'adapter à toutes vos tenues de tennis.',
    'vetement', 
    'Babolat', 
    43.95, 
    '/Image/Vetements/Sweat/babolat2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Sweat', 'Femme', 'Bleu');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 8),
(@product_id, 'M', 10),
(@product_id, 'L', 12);

-- Sweat 3 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'SWEAT BABOLAT FEMME EXERCISE FZ A CAPUCHE',
    'Optez pour ce sweat Babolat femme Exercise FZ à capuche pour plus de chaleur et de confort sur le terrain comme au quotidien. Conçu en molleton, il conservera votre chaleur corporelle afin de vous offrir un maximum de confort. Confectionné également en polyester, il vous assurera une excellente liberté de mouvement. Il dispose d\'une capuche pour vous protéger en cas de pluie ainsi que de deux poches. Côté design, ce sweat gris pourra s\'adapter à toutes vos tenues de tennis.',
    'vetement', 
    'Babolat', 
    40.95, 
    '/Image/Vetements/Sweat/babolat3.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Sweat', 'Femme', 'Gris');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 9),
(@product_id, 'M', 13),
(@product_id, 'L', 11);


-- Sweat 1 Asics
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'SWEAT ASICS TRAINING',
    'Adoptez ce sweat Asics Training pour vos moments de détente ou pour vous échauffer avant vos matchs de tennis.',
    'vetement', 
    'Asics', 
    51.95, 
    '/Image/Vetements/Sweat/asics1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Sweat', 'Homme', 'Bleu clair');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 12),
(@product_id, 'L', 8);

-- Sweat 2 Asics
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'SWEAT A CAPUCHE HOMMES',
    'Les hoodies ne sont pas seulement super confortables, ils sont aussi polyvalents. Découvre le sweat à capuche ASICS Logo OTH pour ton entraînement, tes loisirs ou le bureau ! Ce vêtement polyvalent t\'offre non seulement un confort optimal, mais aussi une excellente fonctionnalité. Grâce aux matières respirantes et évacuant l\'humidité, tu profites à tout moment d\'un confort sec et frais et après ton sport, il te tient chaud. Les poches pratiques offrent également beaucoup de place pour tes essentiels.',
    'vetement', 
    'Asics', 
    44.95, 
    '/Image/Vetements/Sweat/asics2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Sweat', 'Homme', 'Gris, Beige');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 8),
(@product_id, 'M', 10),
(@product_id, 'L', 7);

-- Sweat 1 ADIDAS
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'SWEAT ADIDAS FEMME',
    'Ce sweat adidas Femme deviendra bientôt un indispensable pour vos entraînements de tennis ou au quotidien.',
    'vetement', 
    'Adidas', 
    51.95, 
    '/Image/Vetements/Sweat/adidas1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Sweat', 'Femme', 'vert');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 5),
(@product_id, 'M', 10),
(@product_id, 'L', 10);


-- Sweat 2 ADIDAS
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'FEELCOZY SWEAT À CAPUCHE HOMMES',
    'Le adidas Feelcozy Hoody pour homme combine un design décontracté et un confort ultime. Parfait pour les journées fraîches, pour se rendre à l\'entraînement ou pour passer des moments agréables à la maison, ce sweat à capuche offre une sensation de douceur et tient agréablement chaud. Fabriqué dans une matière Feelcozy de haute qualité, ce sweat à capuche offre une expérience de port douillette, tandis que la capuche avec cordon de serrage réglable offre une protection supplémentaire contre le vent et les intempéries. La spacieuse poche kangourou permet de ranger les petits essentiels ou de se réchauffer les mains. Le logo adidas classique imprimé sur la poitrine complète le look sportif et moderne.',
    'vetement', 
    'Adidas', 
    44.95, 
    '/Image/Vetements/Sweat/adidas2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Sweat', 'Femme', 'gris');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 5),
(@product_id, 'M', 10),
(@product_id, 'L', 10);

-- Sweat 3 ADIDAS
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'FEELCOZY SWEAT À CAPUCHE HOMMES',
    'Un temps orageux. Avec ce sweat à capuche en polaire douce, tu seras quand même toujours bien au chaud. Il ne reste plus qu\'à mettre les mains dans la poche kangourou et le facteur bien-être est parfait.',
    'vetement', 
    'Adidas', 
    36.95, 
    '/Image/Vetements/Sweat/adidas3.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Sweat', 'Femme', 'bleu clair');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 5),
(@product_id, 'M', 10),
(@product_id, 'L', 10);


-- Tshirt 1 ASICS
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'T-SHIRT ASICS MATCH NEW YORK',
    'Ce t-shirt Asics Match New York sera idéal pour vos matchs et vos entrainements intenses.',
    'vetement', 
    'Asics', 
    56.95, 
    '/Image/Vetements/Tshirt/asics1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Tshirt', 'Homme', 'Bleu marine');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 10),
(@product_id, 'L', 10);

-- Tshirt 2 ASICS
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'COURT T-SHIRT FEMMES',
    'Le COURT SHORT SLEEVE TOP est parfait pour un usage quotidien. Le color-blocking minimaliste lui confère une touche de simplicité lors des matchs et des entraînements. De plus, il dispose de propriétés de régulation de l\'humidité améliorées pour que tu restes au sec sur le terrain. ',
    'vetement', 
    'Asics', 
    20.95, 
    '/Image/Vetements/Tshirt/asics2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Tshirt', 'Femme', 'Blanc, Bleu foncé');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 10),
(@product_id, 'L', 10);

-- Tshirt 3 ASICS
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'GAME T-SHIRT FEMMES',
    'Un design élégant et une fonctionnalité maximale pour le court de tennis. Découvre le Game Tee d\'ASICS pour femmes, le t-shirt parfait pour tout amateur de tennis. Avec ses lignes épurées et son design minimaliste, il allie style et confort pour soutenir idéalement tes performances sur le court. Que ce soit à l\'entraînement ou en tournoi, le tee ASICS Game te fournit le soutien nécessaire pour que tu puisses te concentrer pleinement sur ton jeu. Les coutures douces minimisent les irritations de la peau et le tissu léger te permet de te sentir parfaitement à l\'aise. Il allie fonctionnalité, confort et design élégant. Parfait pour tous ceux qui veulent faire bonne figure sur et en dehors du terrain. Procure-toi dès maintenant ton maillot ASICS et fais la différence !',
    'vetement', 
    'Asics', 
    23.95, 
    '/Image/Vetements/Tshirt/asics3.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Tshirt', 'Femme', 'corail, rouge');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 10),
(@product_id, 'L', 10);

-- Tshirt 1 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'T-SHIRT BABOLAT FEMME PLAY',
    'Ce t-shirt Babolat au design simpliste et coloré est un incontournable des courts de tennis. Conçu pour apporter aux joueuses un bien-être en jeu optimal, il est le vêtement qu\'il vous faut pour performer avec efficacité sur le terrain. Doté de la technologie Fiber Dry, il évacue la sueur emmagasinée lors de votre effort pour vous maintenir au frais et au sec en toutes circonstances',
    'vetement', 
    'Babolat', 
    9.95, 
    '/Image/Vetements/Tshirt/babolat1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Tshirt', 'Femme', 'Bleu');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 10),
(@product_id, 'L', 10);

-- Tshirt 2 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'T-SHIRT BABOLAT EXERCISE BIG FLAG',
    'Ce t-shirt Babolat Exercise Big Flag est à adopter au plus vite pour vos futurs entraînements de tennis et au quotidien ! Simple et efficace, il vous apportera un confort optimal grâce à la douceur du coton tout en vous assurant une excellente liberté de mouvement du fait de sa légèreté. Côté design, le logo Babolat coloré présent au centre dynamisera votre tenue.',
    'vetement', 
    'Babolat', 
    15.95, 
    '/Image/Vetements/Tshirt/babolat2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Tshirt', 'Homme', 'Vert');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 10),
(@product_id, 'L', 10);


-- Tshirt 3 Babolat
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'T-SHIRT BABOLAT EXERCISE',
    'Ce t-shirt Babolat Exercise est à adopter au plus vite pour vos futurs entraînements de tennis et au quotidien ! Simple et efficace, il vous apportera un confort optimal grâce à la douceur du coton tout en vous assurant une excellente liberté de mouvement du fait de sa légèreté. Côté design, ce t-shirt s\'associera facilement avec vos tenues pour un style décontracté et sportif.',
    'vetement', 
    'Babolat', 
    18.95, 
    '/Image/Vetements/Tshirt/babolat3.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Tshirt', 'Homme', 'Bleu');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 10),
(@product_id, 'L', 10);

-- Tshirt 1 Adidas
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'FREELIFT T-SHIRT HOMMES',
    'Découvre le tee-shirt Freelift d\'adidas pour hommes, le tee-shirt parfait pour tout amateur de tennis. Avec ses lignes épurées et son design minimaliste, il allie style et confort pour soutenir idéalement tes performances sur le court. Que ce soit à l\'entraînement ou en tournoi, le tee adidas Freelift t\'offre le soutien nécessaire pour que tu puisses te concentrer pleinement sur ton jeu. Les coutures douces minimisent les irritations de la peau et le matériau léger te permet de te sentir parfaitement à l\'aise. Il allie fonctionnalité, confort et design élégant. Parfait pour tous ceux qui veulent faire bonne figure sur et en dehors du terrain. Procure-toi dès maintenant ton tee-shirt adidas et fais la différence!',
    'vetement', 
    'Adidas', 
    43.95, 
    '/Image/Vetements/Tshirt/adidas1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Tshirt', 'Homme', 'Violet, Berry');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 10),
(@product_id, 'L', 10);

-- Tshirt 2 Adidas
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'FREELIFT FR PRO T-SHIRT',
    'Découvrez le t-shirt Adidas Freelift FR Pro, le t-shirt idéal pour les sportifs exigeants. Ce t-shirt a été spécialement conçu pour vous offrir une liberté de mouvement maximale sans glisser vers le haut. Le matériau respirant et évacuant l\'humidité vous garde au sec et confortable, même lors d\'un entraînement intensif. Grâce à sa coupe ergo`name`ique et à son design moderne, le t-shirt Freelift FR Pro est parfait pour le fitness, la course à pied et d\'autres activités sportives. Misez sur Adidas pour des performances optimales et un confort élégant à chaque séance d\'entraînement.',
    'vetement', 
    'Adidas', 
    49.95, 
    '/Image/Vetements/Tshirt/adidas2.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Tshirt', 'Homme', 'Blanc , Bleu, Noire, Multicolor');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 10),
(@product_id, 'L', 10);


-- Tshirt 3 Adidas
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'TRAINING ESSENTIAL MIN T-SHIRT',
    'Ce t-shirt d\'entraînement adidas allie style et performance. Grâce à la technologie AEROREADY qui absorbe l\'humidité, il procure une sensation agréablement sèche. Sa coupe spéciale FreeLift te garantit une totale liberté de mouvement sans glisser. Ce product fait partie de notre engagement contre les déchets plastiques et contient au moins 70% de matières recyclées',
    'vetement', 
    'Adidas', 
    22.95, 
    '/Image/Vetements/Tshirt/adidas3.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Tshirt', 'Femme', 'Vert');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 10),
(@product_id, 'M', 10),
(@product_id, 'L', 10);



-- Short Asics
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'SHORT ASICS COURT 7IN',
    'Ce short Asics Court 7 In vous accompagnera dans tous vos déplacements sur les courts de tennis.',
    'vetement', 
    'Asics', 
    40.50, 
    '/Image/Vetements/Short/asics1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Short', 'Homme', 'blanc');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'S', 5),
(@product_id, 'M', 8),
(@product_id, 'L', 7);

-- Robe 1
-- Robe 1/1 ADIDAS
INSERT INTO product (`name`, description, type, brand, price, image_path)
VALUES (
    'ROBE ADIDAS FEMME',
    'Performez avec style sur les courts de tennis avec cette robe adidas.',
    'vetement', 
    'Adidas', 
    79.95, 
    '/Image/Vetements/Robe/adidas1.jpg'
);
SET @product_id = LAST_INSERT_ID();
INSERT INTO clothing (product_id, type, gender, color) VALUES (@product_id, 'Robe', 'Femme', 'bleu');
INSERT INTO size_stock (product_id, size, stock) VALUES
(@product_id, 'XS', 5),
(@product_id, 'S', 5),
(@product_id, 'M', 5);



-- Customer
INSERT INTO `customer` (`First_name`, `Last_name`, `Civility`, `Email`, `phone_number`, `Password`, `Role`, `Address`)
VALUES 
('Roger', 'Federer', 'M', 'roger.federer@example.com', '0123456789', 'Password123*', 'Customer', '1 Swiss Lane, Basel, Switzerland'),
('Serena', 'Williams', 'Mme', 'serena.williams@example.com', '0312551234', 'securePassword456!', 'Customer', '12 Champion Drive, Palm Beach, FL, USA'),
('Rafael', 'Nadal', 'M', 'rafael.nadal@example.com', '0911223344', 'Topspin789', 'Customer', 'Mallorca Avenue, Manacor, Spain'),
('Pauline', 'Fourel', 'Mme', 'fourel.pauline@gmail.comvueproduits', '0611223344', 'Tennis16!', 'Customer', 'Rue du chateau, Paris, France'),
('Pre`name`', '`name`', 'M', 'admin', '0911223344', 'mdp', 'Admin', 'Mallorca Avenue, Manacor, Spain'),
('Richard', 'Gasquet', 'M', 'richard.gasquet@free.fr', '0645215794', 'BonjourTous3!', 'Customer', '11 rue Clovis, Neuchâtel, Suisse'),
('Noémie', 'DUPONT', 'Mme', 'no.dupont@free.fr', '456734', 'Lala8843!', 'Customer', '11 rue Martin, Paris, France'),
('Jean', 'MARTIN', 'M', 'jean.martin@example.com', '0123456789', 'Mdp1234*', 'Customer', '5 rue de la Paix, Lyon, France'),
('Claire', 'LECLERC', 'Mme', 'claire.leclerc@example.com', '9876543210', 'Motdepasse123*', 'Customer', '24 rue des Lilas, Toulouse, France'),
('Pierre', 'DURAND', 'M', 'pierre.durand@example.com', '0112233445', 'securePassword*', 'Customer', '32 avenue de la République, Marseille, France'),
('Lucie', 'BENOIT', 'Mme', 'lucie.benoit@example.com', '0223344556', 'passwordSecure4*', 'Customer', '14 boulevard des Champs-Élysées, Paris, France');


-- Insertion des commandes dans la table "order"
INSERT INTO `order` (customer_id, order_date, status) VALUES
(1, '2024-10-25 00:00:00', 'Annulée'),
(1, '2024-10-27 00:00:00', 'Validée'),
(4, '2024-11-5 00:00:00', 'Annulée'),
(2, '2024-11-15 00:00:00', 'Annulée'),
(4, '2024-12-02 00:00:00', 'Annulée'),
(3, '2024-12-07 00:00:00', 'Annulée'),
(5, '2024-12-07 00:00:00', 'Annulée'),
(4, '2025-01-03 00:00:00', 'Livrée'),
(4, '2025-01-05 00:00:00', 'Validée'),
(2, '2025-01-05 00:00:00', 'Annulée'),
(1, '2025-01-08 00:00:00', 'Validée'),
(5, '2025-01-14 00:00:00', 'Annulée'),
(6, '2025-01-14 00:00:00', 'Annulée'),
(4, '2025-01-14 00:00:00', 'Annulée'),
(4, '2025-01-20 00:00:00', 'En cours'),
(1, '2025-01-21 00:00:00', 'Annulée'),
(1, '2025-01-25 00:00:00', 'Validée'),
(6, '2024-01-26 00:00:00', 'Livrée'),
(2, '2025-01-26 00:00:00', 'Annulée'),
(4, '2025-02-12 00:00:00', 'En cours'),
(3, '2025-01-26 00:00:00', 'Annulée'),
(10, '2025-01-26 00:00:00', 'Annulée'),
(9, '2025-01-26 00:00:00', 'Validée'),
(8, '2025-01-26 00:00:00', 'Livrée'),
(7, '2025-01-26 00:00:00', 'Annulée'),
(6, '2025-01-27 00:00:00', 'Annulée'),
(10, '2025-01-17 00:00:00', 'Annulée');

-- Insertion des détails de commande dans la table "orderdetails"
INSERT INTO `orderdetails` (order_id, product_id, quantity, size) VALUES
(2, 2, 1, '37'),
(2, 6, 2, '37'),
(2, 14, 1, 'M'),
(3, 2, 1, '37'),
(4, 3, 1, '39'),
(5, 7, 1, '39'),
(5, 10, 2, 'L'),
(7, 2, 3, '37'),
(8, 2, 1, '37'),
(8, 2, 2, '37'),
(9, 2, 1, '38'),
(9, 39, 2, 'L'),
(11, 7, 3, '38'),
(11, 37, 1, 'L'),
(11, 35, 1, 'L'),
(15, 26, 2, 'L'),
(15, 9, 1, '36'),
(17, 15, 1, 'L'),
(17, 25, 2, 'M'),
(18, 1, 1, '39'),
(18, 28, 1, 'S'),
(20, 40, 1, 'L'),
(20, 7, 1, '38'),
(20, 8, 1, '37'),
(23, 25, 2, 'L'),
(23, 22, 1, 'M'),
(24, 1, 1, '38');

-- Insertion des factures dans la table "invoice"
INSERT INTO `invoice` (order_id, billing_address, shipping_address, shipping_method, shipping_price, payment_method, invoice_date) VALUES
(2, '1 Swiss Lane, Basel, Switzerland', '1 Swiss Lane, Basel, Switzerland', 'Chronopost 15,00 €', 15, 'Paiement par Paypal', '2024-10-27 00:00:00'),
(8, 'Mallorca Avenue, Manacor, Spain', 'Mallorca Avenue, Manacor, Spain', 'Chronopost 15,00 €', 15, 'Paiement par Paypal', '2025-01-03 00:00:00'),
(9, 'Mallorca Avenue, Manacor, Spain', 'Mallorca Avenue, Manacor, Spain', 'Chronopost 15,00 €', 15, 'Paiement par Paypal', '2025-01-05 00:00:00'),
(11, '1 Swiss Lane, Basel, Switzerland', '1 Swiss Lane, Basel, Switzerland', 'Colissimo mon domicile 4,00 €', 4, 'Paiement sécurisé par carte bancaire', '2025-01-08 00:00:00'),
(17, '1 Swiss Lane, Basel, Switzerland', '1 Swiss Lane, Basel, Switzerland', 'Colissimo mon domicile 4,00 €', 4, 'Paiement sécurisé par carte bancaire', '2025-01-25 00:00:00'),
(18, '11 rue Clovis, Neuchâtel, Suisse', '11 rue Clovis, Neuchâtel, Suisse', 'Colissimo mon domicile 4,00 €', 4, 'Paiement sécurisé par carte bancaire', '2025-01-26 00:00:00'),
(23, '24 rue des Lilas, Toulouse, France', '24 rue des Lilas, Toulouse, France', 'Colissimo mon domicile 4,00 €', 4, 'Paiement sécurisé par carte bancaire', '2025-01-26 00:00:00'),
(24, '5 rue de la Paix, Lyon, France', '5 rue de la Paix, Lyon, France', 'Colissimo mon domicile 4,00 €', 4, 'Paiement sécurisé par carte bancaire', '2025-01-26 00:00:00');
