package com.kCalControl.controller.impl;

import com.kCalControl.controller.IngredientController;
import com.kCalControl.dto.ingredient.CategorizeIngredientsDTO;
import com.kCalControl.dto.SearchParamsDTO;
import com.kCalControl.model.Ingredient;
import com.kCalControl.model.IngredientsOld;
import com.kCalControl.repository.IngredientRepository;
import com.kCalControl.repository.IngredientsOldRepository;
import com.kCalControl.repository.NutrientsRepository;
import com.kCalControl.service.IngredientService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientControllerImpl implements IngredientController {

    private final static Logger logger = LoggerFactory.getLogger(IngredientControllerImpl.class);
    @Autowired
    IngredientsOldRepository ingredientsOldRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    NutrientsRepository nutrientsRepository;
    @Autowired
    IngredientService ingredientService;

    @Override
    public ResponseEntity<Void> categorizeIngredients(CategorizeIngredientsDTO dto) {
        boolean ingredientExistingList = ingredientRepository.existsByCategoryLike(dto.getCategory());
        if(ingredientExistingList){
            return ResponseEntity.badRequest().build();
        }else {
            List<IngredientsOld> ingredientsOldList = ingredientsOldRepository.findByCategoryLike(dto.getCategory());
            List<Ingredient> ingredientList = ingredientsOldList.stream()
                    .map(i -> ingredientService.convertIngredientOld2Ingredient(i))
                    .collect(Collectors.toList());
            ingredientList.forEach(i -> i.setType(dto.getType()));
            ingredientList.forEach(i -> nutrientsRepository.save(i.getNutrients()));
            ingredientRepository.saveAll(ingredientList);
            ingredientsOldRepository.deleteAll(ingredientsOldList);
            return ResponseEntity.ok().build();        }
    }
    @Override
    public String listIngredients(Model model, int page, int pageSize) {
        Page<Ingredient> ingredientsList = ingredientService.getIngredient(page, pageSize);
        model.addAttribute("ingredient", ingredientsList.getContent());
        model.addAttribute("last", ingredientsList.isLast());
        model.addAttribute("params",new SearchParamsDTO());
        return "auth/admin/listIngredient";
    }

    @Override
    public String searchIngredients(SearchParamsDTO dto) {
        Page<Ingredient> ingredientsSearchList = ingredientService.getIngredientsFromSearch(dto);
        return "/auth/admin/listIngredient";
    }

}
