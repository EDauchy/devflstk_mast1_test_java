Feature: Réservation refusée conflit de créneau

  Scenario: La réservation est refusée si la salle est déjà occupée
    Given la salle "E5" existe avec une capacité maximale de 10
    And une réservation existante pour la salle "E5" de "2026-06-12T10:00" à "2026-06-12T12:00"
    When l'utilisateur "frank@example.com" réserve la salle "E5" pour 5 participants de "2026-06-12T11:00" à "2026-06-12T13:00"
    Then la réservation est refusée
    And le motif de refus est "Room already booked"
