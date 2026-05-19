package com.university.inventory.service.spec;

import com.university.inventory.entity.Equipment;
import com.university.inventory.entity.enums.EquipmentStatus;
import org.springframework.data.jpa.domain.Specification;

public final class EquipmentSpecifications {

    private EquipmentSpecifications() {
    }

    public static Specification<Equipment> search(String term) {
        if (term == null || term.isBlank()) return null;
        String like = "%" + term.toLowerCase() + "%";
        return (root, q, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), like),
                cb.like(cb.lower(root.get("inventoryNumber")), like));
    }

    public static Specification<Equipment> hasCategory(Long categoryId) {
        if (categoryId == null) return null;
        return (root, q, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Equipment> hasRoom(Long roomId) {
        if (roomId == null) return null;
        return (root, q, cb) -> cb.equal(root.get("room").get("id"), roomId);
    }

    public static Specification<Equipment> hasStatus(EquipmentStatus status) {
        if (status == null) return null;
        return (root, q, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Equipment> combine(Specification<Equipment>... specs) {
        Specification<Equipment> result = Specification.where(null);
        for (Specification<Equipment> s : specs) {
            if (s != null) result = result.and(s);
        }
        return result;
    }
}
