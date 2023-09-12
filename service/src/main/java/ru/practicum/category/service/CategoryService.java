package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.ModelNotFoundException;
import ru.practicum.category.dtos.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto save(CategoryDto dto) {
        Category savedCategory = categoryRepository.save(CategoryMapper.dtoToCategory(dto));
        log.info("Создана категория с названием - {} с id - {}!", savedCategory.getName(), savedCategory.getId());
        return CategoryMapper.categoryToDto(savedCategory);
    }

    @Transactional
    public CategoryDto update(Long id, CategoryDto dto) {
        categoryRepository.findById(id).orElseThrow(() -> new ModelNotFoundException("Категория не найдена!"));
        dto.setId(id);
        Category savedCategory = categoryRepository.save(CategoryMapper.dtoToCategory(dto));
        log.info("Обновлена категория с названием - {} с id - {}!", savedCategory.getName(), savedCategory.getId());
        return CategoryMapper.categoryToDto(savedCategory);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new ModelNotFoundException("Категория не найдена!"));
        categoryRepository.deleteById(id);
        log.info("Категория с id - {} удалена!", id);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll(Integer from, Integer size) {
        PageRequest request = PageRequest.of(from / size, size);
        return CategoryMapper.listToDto(categoryRepository.findAll(request).toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ModelNotFoundException("Категория не найдена!"));
        return CategoryMapper.categoryToDto(category);
    }
}
