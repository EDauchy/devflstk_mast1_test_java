Feature: Gestion des comptes bancaires
  En tant que client de la banque
  Je veux gerer mes comptes
  Afin de realiser des operations financieres

  Scenario: Creation d'un nouveau compte
    Given aucun compte n existe dans l API
    When je cree un compte avec le numero "FR001" pour le titulaire "Alice Martin"
    Then la reponse HTTP doit etre 201
    And la reponse contient le solde 0 pour le compte "FR001"

  Scenario: Depot d'argent sur un compte
    Given aucun compte n existe dans l API
    And un compte "FR001" existe pour "Alice Martin"
    When je depose 200 euros sur le compte "FR001"
    Then la reponse HTTP doit etre 200
    And la reponse contient le solde 200 pour le compte "FR001"

  Scenario: Retrait avec fonds suffisants
    Given aucun compte n existe dans l API
    And un compte "FR001" existe pour "Alice Martin" avec un solde de 500 euros
    When je retire 150 euros du compte "FR001"
    Then la reponse HTTP doit etre 200
    And la reponse contient le solde 350 pour le compte "FR001"

  Scenario: Retrait avec fonds insuffisants
    Given aucun compte n existe dans l API
    And un compte "FR001" existe pour "Alice Martin" avec un solde de 50 euros
    When je retire 100 euros du compte "FR001"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur

  Scenario: Virement entre deux comptes
    Given aucun compte n existe dans l API
    And un compte "FR001" existe pour "Alice Martin" avec un solde de 300 euros
    And un compte "FR002" existe pour "Bob Dupont" avec un solde de 100 euros
    When je vire 120 euros du compte "FR001" vers le compte "FR002"
    Then la reponse HTTP doit etre 200
    And le compte "FR001" a un solde de 180 euros
    And le compte "FR002" a un solde de 220 euros

  Scenario: Virement refuse pour solde insuffisant
    Given aucun compte n existe dans l API
    And un compte "FR001" existe pour "Alice Martin" avec un solde de 40 euros
    And un compte "FR002" existe pour "Bob Dupont" avec un solde de 100 euros
    When je vire 80 euros du compte "FR001" vers le compte "FR002"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur
