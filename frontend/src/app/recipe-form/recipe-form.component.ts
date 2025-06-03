import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Recipe } from '../recipe';
import { RecipeService } from '../recipe.service';
import { FormArray, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { JsonPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormField } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CdkDragDrop, CdkDropList, CdkDrag } from '@angular/cdk/drag-drop';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-recipe-form',
  imports: [ReactiveFormsModule, MatButtonModule, MatIconModule, MatFormField, MatInputModule, MatDividerModule, JsonPipe, CdkDropList, CdkDrag],
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
    //this.recipeService.save(<Recipe>this.newRecipeForm.value).subscribe(result => this.gotoHome());
    let json = JSON.stringify(this.newRecipeForm.value);
    console.log(`recipe: ${json}`)
  }

  gotoHome() {
    this.router.navigate(['/']);
  }

  dropIngredient(event: CdkDragDrop<string[]>) {
    if (event.previousIndex != event.currentIndex) {
      let ingredient = this.ingredients.at(event.previousIndex);
      this.ingredients.removeAt(event.previousIndex);
      this.ingredients.insert(event.currentIndex, ingredient);
    }
  }

  dropInstruction(event: CdkDragDrop<string[]>) {
    if (event.previousIndex != event.currentIndex) {
      let instruction = this.instructions.at(event.previousIndex);
      this.instructions.removeAt(event.previousIndex);
      this.instructions.insert(event.currentIndex, instruction);
    }
  }
}
