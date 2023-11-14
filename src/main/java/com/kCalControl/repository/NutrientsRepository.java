package com.kCalControl.repository;

import com.kCalControl.model.Nutrients;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface NutrientsRepository extends CrudRepository<Nutrients, Integer> {

}
