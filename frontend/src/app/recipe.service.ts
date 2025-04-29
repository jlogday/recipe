import { Injectable } from '@angular/core';
import { Recipe } from './recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  protected recipeList: Recipe[] = [
    {
      id: 0,
      name: 'Mai Tai',
      description: 'The Mai Tai cocktail is a wonderful and refreshing libation.',
      photo: '/assets/maitai.jpg',
      instructions: 'Add all ingredients to a cocktail shaker and shake for 10-15 seconds. Strain into a chilled coupe. Garnish with spent lime husk and mint sprig.',
      ingredientList: [
        '1 oz Aged Jamaican Rum',
        '0.5 oz Jamaican Pot Still Rum',
        '1/2 oz Martinique Rum',
        '1/2 oz Orgeat',
        '1/2 oz Dry Curacao',
        '1 oz Lime Juice',
        '1 Mint Sprig (for garnish)'
      ]
    },
    {
      id: 1,
      name: 'Recipe 2',
      description: 'Recipe 2 is a spirit-forward concoction.',
      photo: '',
      instructions: 'Add all ingredients to a mixing glass and stir for 10-15 seconds. Strain into a rocks glass.',
      ingredientList: [
        '2 oz Rye Whiskey',
        '1 oz Sweet Vermouth',
        '1 dash Angostura Bitters',
      ]
    }
  ];
  
  constructor() { }

  getAllRecipes(): Recipe[] {
    return this.recipeList;
  }

  getRecipeById(id: number): Recipe | undefined {
    return this.recipeList.find((recipe) => recipe.id === id);
  }
}
