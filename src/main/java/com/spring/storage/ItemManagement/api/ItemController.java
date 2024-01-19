package com.spring.storage.ItemManagement.api;


import com.spring.storage.FileManagement.FileStorageService;
import com.spring.storage.ItemManagement.model.Item;
import com.spring.storage.ItemManagement.services.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

@Tag(name = "Items", description = "Endpoints for managing Items")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final FileStorageService fileStorageService;

    private final ItemService itemService;

    private final ItemViewMapper itemViewMapper;

    public Item doUploadFile(final Long itemId, final MultipartFile file, final Long version) {

        Item item = itemService.checkVersion(version, itemId);

        final String fileName = fileStorageService.storeFile(String.valueOf(itemId), file);

        String fileDownloadUri = "/api/item/" + itemId + "/photo/" + fileName;

        item = itemService.fileSave(item, fileDownloadUri, version);
        return item;
    }

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

    @Operation(summary = "Gets specific Items")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ItemDetailedView> findById(@PathVariable("id") @Parameter(description = "The id of the item to find") final Long id) {
        final var item = itemService.findById(id);
        return ResponseEntity
                .ok()
                .eTag(Long.toString(item.getVersion()))
                .body(itemViewMapper.toItemDetailedView(item));
    }

    @Operation(summary = "Creates a New Item")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDetailedView> createItem(@RequestPart("json") final CreateItemRequest requestBody,
                                                       @RequestParam(value = "file", required = false) MultipartFile file){
        final var item = itemService.create(requestBody);
        if(file!=null) {
            doUploadFile(item.getItemPk(), file, item.getVersion());
        }
        return ResponseEntity
                .ok()
                .eTag(Long.toString(item.getVersion()))
                .body(itemViewMapper.toItemDetailedView(item));
    }

    @Operation(summary = "Updates an Items details")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDetailedView> updateItemDetails(HttpServletRequest request,
                                                              @PathVariable("id")@Parameter(description = "The id of the Item to update") final Long id,
                                                              @RequestPart("json") EditItemRequest requestBody,
                                                              @RequestParam(value = "file", required = false) MultipartFile file)
    {
        final var version = getEtag(request);
        final var item = itemService.updateItem(version+1, id, requestBody);
        if(file!=null) {
            doUploadFile(item.getItemPk(), file, item.getVersion());
        }


        return ResponseEntity
                .ok()
                .eTag(Long.toString(item.getVersion()))
                .body(itemViewMapper.toItemDetailedView(item));
    }

    @Operation(summary = "Uploads an image for an item")
    @PostMapping("/{itemId}/photo")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDetailedView> uploadFile(final HttpServletRequest request,
                                                       @PathVariable("itemId") @Parameter(description = "The id of the item to get a new image") final Long itemId,
                                                       @RequestParam("file") final MultipartFile file) throws URISyntaxException {

        final var version = getEtag(request);
        final var item = doUploadFile(itemId, file, version);
        return ResponseEntity
                .ok()
                .eTag(Long.toString(item.getVersion()))
                .body(itemViewMapper.toItemDetailedView(item));

    }

    @Operation(summary = "Downloads the photo of the item")
    @GetMapping("/{itemId}/photo/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable final String fileName,
                                                 final HttpServletRequest request) {
        // Load file as Resource
        final Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (final IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Operation(summary = "Updates an Items Amount")
    @PatchMapping("/amount")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDetailedView> updateItemAmount(HttpServletRequest request,
                                                             @Valid @RequestBody final EditItemAmountRequest requestBody){
        final var version = getEtag(request);
        final var item = itemService.updateAmount(version,requestBody);

        return ResponseEntity
                .ok()
                .eTag(Long.toString(item.getVersion()))
                .body(itemViewMapper.toItemDetailedView(item));
    }

    @Operation(summary = "adds a value to an Items Amount")
    @PatchMapping("/addAmount")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDetailedView> addItemAmount(HttpServletRequest request,
                                                          @Valid @RequestBody final EditItemAmountRequest requestBody){
        final var version = getEtag(request);
        final var item = itemService.addAmount(version,requestBody);

        return ResponseEntity
                .ok()
                .eTag(Long.toString(item.getVersion()))
                .body(itemViewMapper.toItemDetailedView(item));
    }

    @Operation(summary = "Toggles if an item is favorite or no")
    @PatchMapping("/favorite")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDetailedView> toggleItemFavorite(HttpServletRequest request,
                                                               @Valid @RequestBody final ItemIdRequest requestBody){

        final var version = getEtag(request);
        final var item = itemService.favoriteItem(version,requestBody.getId());

        return ResponseEntity
                .ok()
                .eTag(Long.toString(item.getVersion()))
                .body(itemViewMapper.toItemDetailedView(item));
    }

    @Operation(summary = "Toggles if an item is in the shopping List or no")
    @PatchMapping("/shopList")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDetailedView> toggleShoppingListItem(HttpServletRequest request,
                                                                   @Valid @RequestBody final ItemIdRequest requestBody){

        final var version = getEtag(request);
        final var item = itemService.toggleNeedsShopping(version,requestBody.getId());

        return ResponseEntity
                .ok()
                .eTag(Long.toString(item.getVersion()))
                .body(itemViewMapper.toItemDetailedView(item));
    }

    @Operation(summary = "Refreshes the 'LastModifiedDate' to the day of the request, in case the amount hasn't changed in a while")
    @PatchMapping("/refreshCheckupDate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDetailedView> refreshCheckupDate(HttpServletRequest request,
                                                               @Valid @RequestBody final ItemIdRequest requestBody){

        final var version = getEtag(request);
        final var item = itemService.refreshLastModified(version,requestBody.getId());

        return ResponseEntity
                .ok()
                .eTag(Long.toString(item.getVersion()))
                .body(itemViewMapper.toItemDetailedView(item));
    }

    @Operation(summary = "Gets all Items or do a query via the query parameter")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getItems(@RequestParam(name = "query", defaultValue = "") @Parameter(description = "query string to search") final String query,
                                        @RequestParam(name = "page", defaultValue = "1") @Parameter(description = "Page number of the elements") final int page,
                                        @RequestParam(name = "sortBy", defaultValue = "itemPk") @Parameter(description = "Parameter to sort by") final String sortMethod,
                                        @RequestParam(name = "ascending", defaultValue = "true") @Parameter(description = "Orientation of the sorting (Asc or Desc)") final boolean sortOrientation) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Items", itemViewMapper.toItemView(itemService.queryByName(query,page,sortMethod,sortOrientation)));
        return response;
    }

    @Operation(summary = "Query all favorited items")
    @GetMapping("/favorite")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> queryAllFavorites(@RequestParam(name = "query", defaultValue = "") @Parameter(description = "query string to search") final String query,
                                                 @RequestParam(name = "page", defaultValue = "1") @Parameter(description = "Page number of the elements") final int page,
                                                 @RequestParam(name = "sortBy", defaultValue = "itemPk") @Parameter(description = "Parameter to sort by") final String sortMethod,
                                                 @RequestParam(name = "ascending", defaultValue = "true") @Parameter(description = "Orientation of the sorting (Asc or Desc)") final boolean sortOrientation) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Items", itemViewMapper.toItemView(itemService.queryByNameAndFavoriteTrue(query,page,sortMethod,sortOrientation)));
        return response;
    }

    @Operation(summary = "Get a list of ALL items without stock")
    @GetMapping("/noStock")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> queryOutOfStock(@RequestParam(name = "query", defaultValue = "") @Parameter(description = "query string to search") final String query,
                                               @RequestParam(name = "page", defaultValue = "1") @Parameter(description = "Page number of the elements") final int page,
                                               @RequestParam(name = "sortBy", defaultValue = "itemPk") @Parameter(description = "Parameter to sort by") final String sortMethod,
                                               @RequestParam(name = "ascending", defaultValue = "true") @Parameter(description = "Orientation of the sorting (Asc or Desc)") final boolean sortOrientation) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Items", itemViewMapper.toItemView(itemService.queryOutOfStock(query,page,sortMethod,sortOrientation)));
        return response;
    }

    @Operation(summary = "Get a list of favorited items with no stock(shopping list)")
    @GetMapping("/shopList")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> queryShoppingList(@RequestParam(name = "query", defaultValue = "") @Parameter(description = "query string to search") final String query,
                                                 @RequestParam(name = "page", defaultValue = "1") @Parameter(description = "Page number of the elements") final int page,
                                                 @RequestParam(name = "sortBy", defaultValue = "itemPk") @Parameter(description = "Parameter to sort by") final String sortMethod,
                                                 @RequestParam(name = "ascending", defaultValue = "true") @Parameter(description = "Orientation of the sorting (Asc or Desc)") final boolean sortOrientation) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Items", itemViewMapper.toItemView(itemService.queryShoppingList(query,page,sortMethod,sortOrientation)));
        return response;
    }

    @Operation(summary = "Get a list of ALL items that need a checkup")
    @GetMapping("/checkupList")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> queryCheckupList(@RequestParam(name = "query", defaultValue = "") @Parameter(description = "query string to search") final String query,
                                                @RequestParam(name = "page", defaultValue = "1") @Parameter(description = "Page number of the elements") final int page,
                                                @RequestParam(name = "sortBy", defaultValue = "itemPk") @Parameter(description = "Parameter to sort by") final String sortMethod,
                                                @RequestParam(name = "ascending", defaultValue = "true") @Parameter(description = "Orientation of the sorting (Asc or Desc)") final boolean sortOrientation) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Items", itemViewMapper.toItemView(itemService.queryNeedCheckup(query,page,sortMethod,sortOrientation)));
        return response;
    }


}
