import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecipeListingComponent } from '../recipe-listing/recipe-listing.component';
import { Recipe } from '../recipe';
import { RecipeService } from '../recipe.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RecipeListingComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  recipeList: Recipe[] = [];
  recipeService: RecipeService = inject(RecipeService);

  constructor() {
    this.recipeList = this.recipeService.getAllRecipes();
  }
}
