Feature: Réservation acceptée

  Scenario: Réserver une salle existante avec une capacité suffisante
    Given la salle "A1" existe avec une capacité maximale de 10
    And aucune réservation n'existe pour la salle "A1"
    When l'utilisateur "alice@example.com" réserve la salle "A1" pour 5 participants de "2026-06-12T10:00" à "2026-06-12T12:00"
    Then la réservation est acceptée

  Scenario: Réserver une salle à capacité maximale
    Given la salle "B2" existe avec une capacité maximale de 8
    And aucune réservation n'existe pour la salle "B2"
    When l'utilisateur "bob@example.com" réserve la salle "B2" pour 8 participants de "2026-06-13T09:00" à "2026-06-13T11:00"
    Then la réservation est acceptée
