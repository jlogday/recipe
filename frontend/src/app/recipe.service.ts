import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Recipe } from './recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  http: HttpClient = inject(HttpClient);
  
  constructor() {
  }

  getAllRecipes(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>('/recipes');
  }

  getRecipeById(id: number): Observable<Recipe | undefined> {
    return this.http.get<Recipe>(`/recipes/${id}`);
  }

  save(recipe: Recipe) {
    console.log(recipe);
    return this.http.post<Recipe>('/recipes', recipe);
  }
}
