
package com.campus.lost.found;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ItemRepository itemRepository;

    public AdminController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // View all reported items
    @GetMapping("/items")
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Mark an item as resolved
    @PutMapping("/items/{id}/resolve")
    public ResponseEntity<?> resolveItem(@PathVariable Long id) {

        Optional<Item> existingItem = itemRepository.findById(id);

        if (existingItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Item item = existingItem.get();
        item.setStatus("Resolved");

        Item savedItem = itemRepository.save(item);

        return ResponseEntity.ok(savedItem);
    }

    // Delete a reported item
    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {

        if (!itemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        itemRepository.deleteById(id);

        return ResponseEntity.ok(
            Map.of("message", "Item deleted successfully")
        );
    }
}
