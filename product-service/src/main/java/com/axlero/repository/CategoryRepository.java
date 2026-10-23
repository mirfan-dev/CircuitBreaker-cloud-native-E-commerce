package com.axlero.repository;

import com.axlero.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,String> {

    List<Category> findByParentId(String id);

    boolean existsByName(String name);
}
