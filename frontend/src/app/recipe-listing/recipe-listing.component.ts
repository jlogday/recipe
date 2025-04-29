import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Recipe } from '../recipe';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-recipe-listing',
  imports: [CommonModule, RouterModule],
  templateUrl: './recipe-listing.component.html',
  styleUrl: './recipe-listing.component.css'
})
export class RecipeListingComponent {

  @Input() recipe!: Recipe;
}
