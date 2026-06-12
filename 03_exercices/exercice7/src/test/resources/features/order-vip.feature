Feature: Commande acceptée client VIP

  Scenario: Un client VIP bénéficie de 20 pourcent de remise
    Given un produit "KEYBOARD-01" existe avec un prix de 100.0 et un stock de 5
    And le client "vip@example.com" a le profil VIP
    When le client "vip@example.com" commande 2 unités du produit "KEYBOARD-01"
    Then la commande est acceptée
    And le montant total est de 160.0
    And le message de confirmation est "Order confirmed for vip@example.com"
