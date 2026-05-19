package com.university.inventory.config;

import com.university.inventory.entity.Category;
import com.university.inventory.entity.Equipment;
import com.university.inventory.entity.Room;
import com.university.inventory.entity.User;
import com.university.inventory.entity.enums.EquipmentCondition;
import com.university.inventory.entity.enums.EquipmentStatus;
import com.university.inventory.entity.enums.Role;
import com.university.inventory.repository.CategoryRepository;
import com.university.inventory.repository.EquipmentRepository;
import com.university.inventory.repository.RoomRepository;
import com.university.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RoomRepository roomRepository;
    private final EquipmentRepository equipmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Seed skipped: data already present");
            return;
        }
        log.info("Seeding initial data...");

        user("Admin User", "admin@test.com", "admin123", Role.ADMIN, "Administrator", "IT Department");
        user("IT Specialist", "it@test.com", "it123", Role.IT_SPECIALIST, "Engineer", "IT Department");
        User teacher = user("Teacher User", "teacher@test.com", "teacher123",
                Role.TEACHER, "Lecturer", "Computer Science");
        user("Manager User", "manager@test.com", "manager123", Role.MANAGER, "Head of Dept", "Administration");

        List<Category> categories = List.of(
                category("Ноутбуки", "Portable computers"),
                category("Проекторы", "Projectors"),
                category("Компьютеры", "Desktop computers"),
                category("Принтеры", "Printers"),
                category("Сетевое оборудование", "Network devices"),
                category("Интерактивные доски", "Interactive whiteboards")
        );

        List<Room> rooms = List.of(
                room("Кабинет 305", "Главный корпус", 3, "Аудитория"),
                room("Компьютерный класс 210", "Главный корпус", 2, "Компьютерный класс"),
                room("Лаборатория робототехники", "Корпус Б", 1, "Лаборатория"),
                room("Серверная", "Корпус Б", 0, "Серверная"),
                room("Библиотека", "Главный корпус", 1, "Библиотека")
        );

        String[] names = {"Dell Latitude", "HP ProBook", "Epson EB-X05", "Acer Aspire",
                "Canon LBP", "Cisco Switch", "SMART Board", "Lenovo ThinkPad",
                "BenQ Projector", "TP-Link Router", "iMac 24", "Brother HL",
                "ASUS VivoBook", "MikroTik hAP", "Promethean Board"};
        EquipmentStatus[] statuses = EquipmentStatus.values();
        EquipmentCondition[] conditions = EquipmentCondition.values();
        Random rnd = new Random(42);
        List<Equipment> equipment = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Equipment e = new Equipment();
            e.setName(names[i] + " #" + (i + 1));
            e.setInventoryNumber("INV-" + String.format("%04d", 1000 + i));
            e.setSerialNumber("SN" + (100000 + rnd.nextInt(899999)));
            e.setCategory(categories.get(rnd.nextInt(categories.size())));
            e.setRoom(rooms.get(rnd.nextInt(rooms.size())));
            e.setStatus(statuses[rnd.nextInt(statuses.length)]);
            e.setCondition(conditions[rnd.nextInt(conditions.length)]);
            e.setResponsibleUser(i % 4 == 0 ? teacher : null);
            e.setPurchaseDate(LocalDate.now().minusDays(rnd.nextInt(1000)));
            e.setPrice(BigDecimal.valueOf(50000 + rnd.nextInt(450000)));
            e.setDescription("Seed equipment item " + (i + 1));
            equipment.add(e);
        }
        equipmentRepository.saveAll(equipment);

        log.info("Seeding done: {} users, {} categories, {} rooms, {} equipment",
                userRepository.count(), categoryRepository.count(),
                roomRepository.count(), equipmentRepository.count());
    }

    private User user(String name, String email, String rawPass, Role role, String position, String dept) {
        return userRepository.save(User.builder()
                .fullName(name)
                .email(email)
                .password(passwordEncoder.encode(rawPass))
                .role(role)
                .position(position)
                .department(dept)
                .enabled(true)
                .build());
    }

    private Category category(String name, String desc) {
        return categoryRepository.save(Category.builder().name(name).description(desc).build());
    }

    private Room room(String name, String building, int floor, String type) {
        return roomRepository.save(Room.builder()
                .name(name).building(building).floor(floor).type(type)
                .description(name).build());
    }
}
