package com.kCalControl.service.impl;

import com.kCalControl.dto.SearchParamsDTO;
import com.kCalControl.exceptions.NetworkException;
import com.kCalControl.model.*;
import com.kCalControl.repository.IngredientRepository;
import com.kCalControl.service.IngredientService;
import com.kCalControl.service.WhoIAm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    WhoIAm whoIAm;

    @Override
    public void addTypeToIngredient(String type, Ingredient ingredient) {
        ingredient.setType(type);
    }

    @Override
    public Ingredient convertIngredientOld2Ingredient(IngredientsOld ingredientsOld) {
        LocalDateTime date = LocalDateTime.now();

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientsOld.getId());
        ingredient.setCategory(ingredientsOld.getCategory());
        ingredient.setDescription(ingredientsOld.getDescription());

        Assets assets = new Assets();
        assets.setCreationDate(date);
        assets.setModificationDate(date);
        UserDB godUser = whoIAm.currentUser().orElseThrow(()-> new NetworkException("User who performed the conversion not found", HttpStatus.NOT_FOUND));
        assets.setCreationPerson(godUser);
        assets.setModificationPerson(godUser);

        Nutrients nutrients = new Nutrients();

        //GOLD
        try {
            Class<?> oldIngredientClass = ingredientsOld.getClass();
            Class<?> nutrientsClass = nutrients.getClass();
            Field[] fields = oldIngredientClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();

                Object value = field.get(ingredientsOld);

                try {
                    Field nutrientsField = nutrientsClass.getDeclaredField(fieldName);
                    nutrientsField.setAccessible(true);
                    nutrientsField.set(nutrients, value);
                } catch (NoSuchFieldException e) {
                    // Field does not exist in Nutrients class
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        ingredient.setNutrients(nutrients);
        return ingredient;
    }

    @Override
    public Page<Ingredient> getIngredient(int page, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "type");
        PageRequest pageRequest = PageRequest.of(page, pageSize, sort);
        return ingredientRepository.findAll(pageRequest);
    }

    @Override
    public Page<Ingredient> getIngredientsFromSearch(SearchParamsDTO dto) {
        Sort sorted = null;
        sorted = switch (dto.getSort()) {
            case "az" -> Sort.by(Sort.Direction.ASC, "type", "category");
            case "za" -> Sort.by(Sort.Direction.DESC, "type", "category");
            case "newer" -> Sort.by(Sort.Direction.DESC, "creationDate");
            case "older" -> Sort.by(Sort.Direction.ASC, "creationDate");
            case "newerM" -> Sort.by(Sort.Direction.DESC, "modificationDate");
            case "olderM"-> Sort.by(Sort.Direction.ASC, "modificationDate");
            default -> Sort.unsorted();
        };
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getPageSize(), sorted);
        return ingredientRepository.findByTypeIgnoreCaseOrCategoryIgnoreCaseOrDescriptionLikeIgnoreCase
                (dto.getQuery(), dto.getQuery(), dto.getQuery(),pageRequest);
    }

}
