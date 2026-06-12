Feature: Commande acceptée client STANDARD

  Scenario: Un client STANDARD passe une commande sans remise
    Given un produit "LAPTOP-01" existe avec un prix de 1000.0 et un stock de 10
    And le client "std@example.com" a le profil STANDARD
    When le client "std@example.com" commande 2 unités du produit "LAPTOP-01"
    Then la commande est acceptée
    And le montant total est de 2000.0
    And le message de confirmation est "Order confirmed for std@example.com"
