# Recipes

```mermaid
---
title: Recipes
---
erDiagram
    RECIPE }|..|{ MEASURED_INGREDIENT : has
    RECIPE ||..|{ IMAGE : has
    RECIPE ||..|{ URL : has
    RECIPE {
        int id
        string category
        string name
        string description
        string instructions
    }

    INGREDIENT {
        int id
        string name
        string description
    }

    MEASURED_INGREDIENT }|--|| INGREDIENT : has
    MEASURED_INGREDIENT {
        int id
        int INGREDIENT FK
        string quantity
    }

    IMAGE {
        int id
        int recipe_id FK
        string path
    }

    URL {
        int id
        int recipe_id FK
        string url
    }

```
