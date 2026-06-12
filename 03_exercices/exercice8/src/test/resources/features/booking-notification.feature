Feature: Notification de réservation

  Scenario: Une notification est envoyée en cas de succès
    Given la salle "G7" existe avec une capacité maximale de 10
    And aucune réservation n'existe pour la salle "G7"
    When l'utilisateur "henry@example.com" réserve la salle "G7" pour 3 participants de "2026-06-12T10:00" à "2026-06-12T11:00"
    Then la réservation est acceptée
    And une notification de confirmation est envoyée à "henry@example.com"

  Scenario: Aucune notification n'est envoyée en cas d'échec
    Given la salle "H8" n'existe pas
    When l'utilisateur "iris@example.com" réserve la salle "H8" pour 2 participants de "2026-06-12T10:00" à "2026-06-12T11:00"
    Then la réservation est refusée
    And aucune notification n'est envoyée à "iris@example.com"
