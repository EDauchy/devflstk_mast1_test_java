Feature: Commande refusée produit inconnu

  Scenario: La commande est refusée si le produit n'existe pas
    Given le produit "UNKNOWN-99" n'existe pas
    And le client "client@example.com" a le profil STANDARD
    When le client "client@example.com" commande 1 unité du produit "UNKNOWN-99"
    Then la commande est refusée
    And le motif de refus est "Product not found"
