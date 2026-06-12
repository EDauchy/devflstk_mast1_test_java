Feature: Réservation refusée période invalide

  Scenario: La réservation est refusée si la date de fin est avant la date de début
    Given la salle "D4" existe avec une capacité maximale de 12
    And aucune réservation n'existe pour la salle "D4"
    When l'utilisateur "eve@example.com" réserve la salle "D4" pour 4 participants de "2026-06-12T16:00" à "2026-06-12T14:00"
    Then la réservation est refusée
    And le motif de refus est "Invalid period"
