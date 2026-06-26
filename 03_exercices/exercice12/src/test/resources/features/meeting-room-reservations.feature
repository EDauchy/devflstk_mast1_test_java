Feature: Gestion des reservations de salles de reunion
  En tant qu'organisateur de reunion
  Je veux reserver une salle
  Afin de planifier mes rendez-vous

  Scenario: Reservation acceptee quand la salle existe et que le creneau est libre
    Given aucune donnee n existe dans l API
    And une salle "Salle A" avec une capacite de 10 existe
    When je reserve la salle 1 pour "Alice" de "2026-06-25T10:00:00" a "2026-06-25T11:00:00"
    Then la reponse HTTP doit etre 201
    And la reponse contient le statut de reservation "CONFIRMED"

  Scenario: Reservation refusee quand la salle n existe pas
    Given aucune donnee n existe dans l API
    When je reserve la salle 99 pour "Alice" de "2026-06-25T10:00:00" a "2026-06-25T11:00:00"
    Then la reponse HTTP doit etre 404
    And la reponse contient un message d erreur

  Scenario: Reservation refusee quand le creneau chevauche une reservation existante
    Given aucune donnee n existe dans l API
    And une salle "Salle B" avec une capacite de 8 existe
    And une reservation confirmee existe pour la salle 1 de "2026-06-25T10:00:00" a "2026-06-25T11:00:00"
    When je reserve la salle 1 pour "Bob" de "2026-06-25T10:30:00" a "2026-06-25T11:30:00"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur
