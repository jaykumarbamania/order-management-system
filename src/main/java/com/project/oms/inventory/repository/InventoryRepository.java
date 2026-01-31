package com.project.oms.inventory.repository;

import com.project.oms.inventory.domain.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryItem, UUID> {

    Optional<InventoryItem> findByProductId(UUID productId);
    Optional<InventoryItem> findByAvailableQuantity(int availableQuantity);
}
