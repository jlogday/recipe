import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecipeListingComponent } from '../recipe-listing/recipe-listing.component';
import { Recipe } from '../recipe';
import { RecipeService } from '../recipe.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RecipeListingComponent, ReactiveFormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  recipeList: Recipe[] = [];
  recipeService: RecipeService = inject(RecipeService);
  newRecipeForm = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
  });

  constructor() {
    this.recipeList = this.recipeService.getAllRecipes();
  }

  submitNewRecipe() {
    this.recipeService.submitNewRecipe(
      this.newRecipeForm.value.name ?? '',
      this.newRecipeForm.value.description ?? '',
    );
  }
}
