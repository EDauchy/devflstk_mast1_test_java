Feature: Réservation refusée salle inconnue

  Scenario: La réservation est refusée si la salle n'existe pas
    Given la salle "Z9" n'existe pas
    When l'utilisateur "carol@example.com" réserve la salle "Z9" pour 3 participants de "2026-06-12T10:00" à "2026-06-12T11:00"
    Then la réservation est refusée
    And le motif de refus est "Room not found"
