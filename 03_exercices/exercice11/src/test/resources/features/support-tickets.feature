Feature: Gestion des tickets de support
  En tant qu'utilisateur du support technique
  Je veux gerer des tickets
  Afin de suivre les demandes d'assistance

  Scenario: Creation d un ticket valide
    Given aucun ticket n existe dans l API
    When je cree un ticket avec le titre "Probleme de connexion" et la priorite "HIGH"
    Then la reponse HTTP doit etre 201
    And la reponse contient le titre "Probleme de connexion"
    And la reponse contient le statut "OPEN"

  Scenario: Resolution d un ticket
    Given aucun ticket n existe dans l API
    And un ticket existe avec le titre "Bug critique" et la priorite "HIGH"
    When je resous le ticket cree
    Then la reponse HTTP doit etre 200
    And la reponse contient le statut "RESOLVED"

  Scenario: Refus de modification d un ticket deja resolu
    Given aucun ticket n existe dans l API
    And un ticket resolu existe avec le titre "Ticket clos"
    When je tente de passer le ticket 1 au statut "IN_PROGRESS"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur

  Scenario: Consultation d un ticket inexistant
    Given aucun ticket n existe dans l API
    When je demande le ticket avec l identifiant 99
    Then la reponse HTTP doit etre 404
    And la reponse contient un message d erreur
