import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecipeListComponent } from '../recipe-list/recipe-list.component';
import { Recipe } from '../recipe';
import { RecipeService } from '../recipe.service';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDivider } from '@angular/material/divider';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RecipeListComponent, RouterModule, MatButtonModule, MatIconModule, MatDivider],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  recipeList: Recipe[] = [];
  recipeService: RecipeService = inject(RecipeService);

  constructor() {
  }

  ngOnInit() {
    this.recipeService.getAllRecipes().subscribe(data => {
      this.recipeList = data;
    });
  }
}
