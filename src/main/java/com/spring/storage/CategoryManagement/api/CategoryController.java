package com.spring.storage.CategoryManagement.api;

import com.spring.storage.CategoryManagement.services.CategoryService;
import com.spring.storage.ItemManagement.model.Item;
import com.spring.storage.ItemManagement.services.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Categories", description = "Endpoints for managing Categories")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    private final ItemService itemService;

    private final CategoryViewMapper categoryViewMapper;

    public long getEtag(HttpServletRequest request){
        final String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }
        if (ifMatchValue.startsWith("\"")) {
            return Long.parseLong(ifMatchValue.substring(1, ifMatchValue.length() - 1));
        }
        return Long.parseLong(ifMatchValue);

    }

    @Operation(summary = "Gets all Categories")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getCategories(@RequestParam(name = "query", defaultValue = "") @Parameter(description = "query string to search") final String query,
                                             @RequestParam(name = "page", defaultValue = "1") @Parameter(description = "Page number of the elements") final int page,
                                             @RequestParam(name = "sortBy", defaultValue = "catPk") @Parameter(description = "Parameter to sort by") final String sortMethod,
                                             @RequestParam(name = "ascending", defaultValue = "true") @Parameter(description = "Orientation of the sorting (Asc or Desc)") final boolean sortOrientation) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Categories", categoryViewMapper.toCategoryView(categoryService.queryByName(query,page, sortMethod, sortOrientation)));
        return response;
    }

    @Operation(summary = "Gets specific Category")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CategoryFullView> findById(@PathVariable("id") @Parameter(description = "The id of the category to find") final Long id,
                                                     @RequestParam(name = "query", defaultValue = "") @Parameter(description = "query string to search") final String query,
                                                     @RequestParam(name = "page", defaultValue = "1") @Parameter(description = "Page number of the elements") final int page,
                                                     @RequestParam(name = "sortBy", defaultValue = "itemPk") @Parameter(description = "Parameter to sort by") final String sortMethod,
                                                     @RequestParam(name = "ascending", defaultValue = "true") @Parameter(description = "Orientation of the sorting (Asc or Desc)") final boolean sortOrientation) {
        final var category = categoryService.findById(id);
        Page<Item> items = itemService.queryByNameAndCategoryId(query,id, page, sortMethod, sortOrientation);
        return ResponseEntity
                .ok()
                .eTag(Long.toString(category.getVersion()))
                .body(categoryViewMapper.toCategoryFullView(categoryService.findById(id),items));
    }

    @Operation(summary = "Creates a New Category")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoryView> createCategory(@Valid @RequestBody final CreateCategoryRequest requestBody){
        final var category = categoryService.create(requestBody);
        return ResponseEntity
                .ok()
                .eTag(Long.toString(category.getVersion()))
                .body(categoryViewMapper.toCategoryView(category));
    }

    @Operation(summary = "Updates a Category")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoryView> updateCategory(HttpServletRequest request,
                                                       @PathVariable("id")@Parameter(description = "The id of the Category to update") final Long id,
                                                       @Valid @RequestBody final EditCategoryRequest requestBody){
        final var version = getEtag(request);
        final var category = categoryService.update(version,id, requestBody);

        return ResponseEntity
                .ok()
                .eTag(Long.toString(category.getVersion()))
                .body(categoryViewMapper.toCategoryView(category));
    }
}
