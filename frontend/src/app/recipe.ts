import { Ingredient } from "./ingredient";

export interface Recipe {
    id?: number;
    name: string;
    description: string;
    photo?: string;
    instructions: string[];
    ingredients: Ingredient[];
}
