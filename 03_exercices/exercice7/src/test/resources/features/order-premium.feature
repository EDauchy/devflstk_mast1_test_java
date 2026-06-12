Feature: Commande acceptée client PREMIUM

  Scenario: Un client PREMIUM bénéficie de 10 pourcent de remise
    Given un produit "MOUSE-01" existe avec un prix de 50.0 et un stock de 20
    And le client "premium@example.com" a le profil PREMIUM
    When le client "premium@example.com" commande 4 unités du produit "MOUSE-01"
    Then la commande est acceptée
    And le montant total est de 180.0
    And le message de confirmation est "Order confirmed for premium@example.com"
