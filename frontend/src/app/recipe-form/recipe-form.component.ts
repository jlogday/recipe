import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Recipe } from '../recipe';
import { RecipeService } from '../recipe.service';
import { FormArray, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, JsonPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormField } from '@angular/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-recipe-form',
  imports: [CommonModule, ReactiveFormsModule, MatButtonModule, MatIconModule, MatFormField, MatInputModule, JsonPipe],
  templateUrl: './recipe-form.component.html',
  styleUrl: './recipe-form.component.css'
})
export class RecipeFormComponent {
  route: ActivatedRoute = inject(ActivatedRoute);
  router = inject(Router);
  recipeService = inject(RecipeService);
  formBuilder = inject(FormBuilder);

  newRecipeForm = this.formBuilder.group({
    name: [''],
    description: [''],
    photo: [''],
    instructions: this.formBuilder.array([this.formBuilder.control('')]),
    ingredients: this.formBuilder.array([
      this.formBuilder.group({
        quantity: [''],
        name: [''],
        note: [''],
      })
    ]),
  })

  constructor() {
  }

  get instructions() {
    return this.newRecipeForm.get('instructions') as FormArray;
  }

  addInstruction() {
    this.instructions.push(this.formBuilder.control(''));
  }

  deleteInstruction(index: number) {
    this.instructions.removeAt(index);
  }

  get ingredients() {
    return this.newRecipeForm.get('ingredients') as FormArray;
  }

  addIngredient() {
    const ingredientForm = this.formBuilder.group({
      quantity: [''],
      name: [''],
      note: [''],
    });

    this.ingredients.push(ingredientForm);
  }

  deleteIngredient(index: number) {
    this.ingredients.removeAt(index);
  }


  submitNewRecipe() {
    console.log(this.newRecipeForm.value);
    this.recipeService.save(<Recipe>this.newRecipeForm.value).subscribe(result => this.gotoHome());
  }

  onSubmit() {
    this.recipeService.save(<Recipe>this.newRecipeForm.value).subscribe(result => this.gotoHome());
  }

  gotoHome() {
    this.router.navigate(['/']);
  }
}
