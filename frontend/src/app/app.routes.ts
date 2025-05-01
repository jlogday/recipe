import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { DetailsComponent } from './details/details.component';
import { RecipeFormComponent } from './recipe-form/recipe-form.component';

const routeConfig: Routes = [
    {
        path: '',
        component: HomeComponent,
        title: 'Home Page',
    },
    {
        path: 'recipes/:id',
        component: DetailsComponent,
        title: 'Recipe Details',
    },
    {
        path: 'addrecipe',
        component: RecipeFormComponent,
        title: 'Add Recipe'
    },
];

export default routeConfig;