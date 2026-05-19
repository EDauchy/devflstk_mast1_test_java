
# Exercice Price Calculator

## Sujet

Créer une classe `PriceCalculator` permettant de calculer le prix final d’un produit.

La classe doit contenir les méthodes suivantes :

```java
double calculateTotalPrice(double unitPrice, int quantity)
double applyDiscount(double price, double discountRate)
double calculateVat(double price, double vatRate)
double calculatePriceWithVat(double price, double vatRate)
```

Règles métier :

```text
calculateTotalPrice(10.0, 3) retourne 30.0
applyDiscount(100.0, 0.20) retourne 80.0
calculateVat(100.0, 0.20) retourne 20.0
calculatePriceWithVat(100.0, 0.20) retourne 120.0
```

Cas d’erreur à gérer :

```text
Le prix unitaire ne doit pas être négatif.
La quantité ne doit pas être négative.
Le taux de remise ne doit pas être négatif.
Le taux de TVA ne doit pas être négatif.
```

Travail demandé :

1. créer la classe `PriceCalculator` ;
2. créer la classe `PriceCalculatorTest` ;
3. tester les cas nominaux ;
4. tester les cas d’erreur;
5. exécuter les tests ;
6. générer le rapport JaCoCo.