import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Recipe } from '../recipe';
import { RecipeService } from '../recipe.service';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-recipe-form',
  imports: [ReactiveFormsModule],
  templateUrl: './recipe-form.component.html',
  styleUrl: './recipe-form.component.css'
})
export class RecipeFormComponent {
  route: ActivatedRoute = inject(ActivatedRoute);
  router = inject(Router);
  recipeService = inject(RecipeService);

  newRecipeForm = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
  });

  recipe!: Recipe;

  constructor() {
  }


  submitNewRecipe() {
    this.recipeService.submitNewRecipe(
      this.newRecipeForm.value.name ?? '',
      this.newRecipeForm.value.description ?? '',
    );
    this.gotoHome();
  }

  onSubmit() {
    this.recipeService.save(this.recipe).subscribe(result => this.gotoHome());
  }

  gotoHome() {
    this.router.navigate(['/']);
  }
}
