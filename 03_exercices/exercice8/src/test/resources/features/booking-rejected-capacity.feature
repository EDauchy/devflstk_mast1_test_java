Feature: Réservation refusée capacité insuffisante

  Scenario: La réservation est refusée si le nombre de participants dépasse la capacité
    Given la salle "C3" existe avec une capacité maximale de 6
    And aucune réservation n'existe pour la salle "C3"
    When l'utilisateur "dave@example.com" réserve la salle "C3" pour 10 participants de "2026-06-12T14:00" à "2026-06-12T16:00"
    Then la réservation est refusée
    And le motif de refus est "Capacity exceeded"
