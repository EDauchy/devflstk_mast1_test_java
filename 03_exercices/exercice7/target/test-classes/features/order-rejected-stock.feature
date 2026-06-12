Feature: Commande refusée stock insuffisant

  Scenario: La commande est refusée si le stock est insuffisant
    Given un produit "CABLE-01" existe avec un prix de 15.0 et un stock de 3
    And le client "client@example.com" a le profil STANDARD
    When le client "client@example.com" commande 5 unités du produit "CABLE-01"
    Then la commande est refusée
    And le motif de refus est "Insufficient stock"
