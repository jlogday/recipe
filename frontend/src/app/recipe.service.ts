import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Recipe } from './recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  http: HttpClient = inject(HttpClient);

  protected recipeList: Recipe[] = [
    {
      id: 0,
      name: 'Mai Tai',
      description: 'The Mai Tai cocktail is a wonderful and refreshing libation.',
      photo: '/assets/maitai.jpg',
      instructions: [
        'Add all ingredients to a cocktail shaker and shake for 10-15 seconds',
        'Strain into a chilled coupe',
        'Garnish with spent lime husk and mint sprig'
      ],
      ingredients: [
        {
          quantity: '1 oz',
          name: 'Aged Jamaican Rum',
        },
        {
          quantity: '0.5 oz',
          name: 'Jamaican Pot Still Rum',
        },
        {
          quantity: '1/2 oz',
          name: 'Martinique Rum',
        },
        {
          quantity: '1/2 oz',
          name: 'Orgeat',
        },
        {
          quantity: '1/2 oz',
          name: 'Dry Curacao',
        },
        {
          quantity: '1 oz',
          name: 'Lime Juice',
        },
        {
          quantity: '1',
          name: 'Mint Sprig',
          note: '(for garnish)',
        },
      ]
    },
    {
      id: 1,
      name: 'Manhattan',
      description: 'Manhattan is a spirit-forward classic.',
      photo: '',
      instructions: [
        'Add all ingredients to a mixing glass and stir for 10-15 seconds',
        'Strain into a rocks glass',
      ],
      ingredients: [
        {
          quantity: '2 oz',
          name: 'Rye Whiskey',
        },
        {
          quantity: '1 oz',
          name: 'Sweet Vermouth',
        },
        {
          quantity: '1 dash',
          name: 'Angostura Bitters',
        },
      ]
    },
    {
      id: 2,
      name: 'Mai Tai 2',
      description: 'The Mai Tai cocktail is a wonderful and refreshing libation.',
      photo: '/assets/maitai.jpg',
      instructions: [
        'Add all ingredients to a cocktail shaker and shake for 10-15 seconds',
        'Strain into a chilled coupe',
        'Garnish with spent lime husk and mint sprig'
      ],
      ingredients: [
        {
          quantity: '1 oz',
          name: 'Aged Jamaican Rum',
        },
        {
          quantity: '0.5 oz',
          name: 'Jamaican Pot Still Rum',
        },
        {
          quantity: '1/2 oz',
          name: 'Martinique Rum',
        },
        {
          quantity: '1/2 oz',
          name: 'Orgeat',
        },
        {
          quantity: '1/2 oz',
          name: 'Dry Curacao',
        },
        {
          quantity: '1 oz',
          name: 'Lime Juice',
        },
        {
          quantity: '1',
          name: 'Mint Sprig',
          note: '(for garnish)',
        },
      ]
    },
    {
      id: 3,
      name: 'Mai Tai 3',
      description: 'The Mai Tai cocktail is a wonderful and refreshing libation.',
      photo: '/assets/maitai.jpg',
      instructions: [
        'Add all ingredients to a cocktail shaker and shake for 10-15 seconds',
        'Strain into a chilled coupe',
        'Garnish with spent lime husk and mint sprig'
      ],
      ingredients: [
        {
          quantity: '1 oz',
          name: 'Aged Jamaican Rum',
        },
        {
          quantity: '0.5 oz',
          name: 'Jamaican Pot Still Rum',
        },
        {
          quantity: '1/2 oz',
          name: 'Martinique Rum',
        },
        {
          quantity: '1/2 oz',
          name: 'Orgeat',
        },
        {
          quantity: '1/2 oz',
          name: 'Dry Curacao',
        },
        {
          quantity: '1 oz',
          name: 'Lime Juice',
        },
        {
          quantity: '1',
          name: 'Mint Sprig',
          note: '(for garnish)',
        },
      ]
    },
  ];
  
  constructor() {
  }

  getAllRecipes(): Observable<Recipe[]> {
    return of(this.recipeList);
  }

  getRecipeById(id: number): Observable<Recipe | undefined> {
    return of(this.recipeList.find((recipe) => recipe.id === id));
  }

  submitNewRecipe(name: string, description: string) {
    console.log(`New recipe received: name: ${name}, description: ${description}`);
  }

  save(recipe: Recipe) {
    console.log(recipe);
    //return this.http.post<Recipe>('/recipes', recipe);
    const id = this.recipeList.length;
    recipe.id = id;
    this.recipeList.push(recipe);
    return of(`{id: ${id}}`);
  }
}
