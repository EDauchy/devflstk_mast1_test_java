Feature: Réservation acceptée après une réservation existante

  Scenario: La réservation est acceptée si le créneau commence après une réservation existante
    Given la salle "F6" existe avec une capacité maximale de 10
    And une réservation existante pour la salle "F6" de "2026-06-12T10:00" à "2026-06-12T12:00"
    When l'utilisateur "grace@example.com" réserve la salle "F6" pour 6 participants de "2026-06-12T12:00" à "2026-06-12T14:00"
    Then la réservation est acceptée
