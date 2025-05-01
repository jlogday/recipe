import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecipeListComponent } from '../recipe-list/recipe-list.component';
import { Recipe } from '../recipe';
import { RecipeService } from '../recipe.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RecipeListComponent, ReactiveFormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  recipeList: Recipe[] = [];
  recipeService: RecipeService = inject(RecipeService);
  newRecipeForm = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
  });

  constructor() {
    //this.recipeList = this.recipeService.getAllRecipes();
  }

  ngOnInit() {
    console.log('onInit')
    this.recipeService.getAllRecipes().subscribe(data => {
      this.recipeList = data;
    });
  }

  submitNewRecipe() {
    this.recipeService.submitNewRecipe(
      this.newRecipeForm.value.name ?? '',
      this.newRecipeForm.value.description ?? '',
    );
  }
}
