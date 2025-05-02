import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Recipe } from '../recipe';
import { RecipeService } from '../recipe.service';
import { FormArray, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'app-recipe-form',
  imports: [ReactiveFormsModule, JsonPipe],
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
    ingredients: this.formBuilder.array([this.formBuilder.control('')]),
  })

  constructor() {
  }

  get instructions() {
    return this.newRecipeForm.get('instructions') as FormArray;
  }

  addInstruction() {
    this.instructions.push(this.formBuilder.control(''));
  }

  get ingredients() {
    return this.newRecipeForm.get('ingredients') as FormArray;
  }

  addIngredient() {
    this.instructions.push(this.formBuilder.control(''));
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
