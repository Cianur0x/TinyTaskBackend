package org.iesvdm.preproyectoapirest;

import org.iesvdm.preproyectoapirest.domain.*;
import org.iesvdm.preproyectoapirest.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class PreproyectoApiRestApplicationTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    RoleRepository roleRepository;


    @Test
    void contextLoads() {
    }

    @Test
    void crearData() {
        Role role1 = new Role();
        role1.setRoleName("Beginner");
        roleRepository.save(role1);

        Role role2 = new Role();
        role2.setRoleName("Advanced");
        roleRepository.save(role2);

        Theme theme1 = new Theme();
        theme1.setPrimaryColor("black");
        theme1.setSecondaryColor("white");
        themeRepository.save(theme1);

        Theme theme2 = new Theme();
        theme2.setPrimaryColor("pink");
        theme2.setSecondaryColor("white");
        themeRepository.save(theme2);

        User user1 = new User();
        user1.setUsername("Cianuro");
        user1.setRole(role2);
        user1.setTheme(theme1);
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("Jake");
        user2.setRole(role1);
        user2.setTheme(theme2);
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("Finn");
        user3.setRole(role1);
        user3.setTheme(theme2);
        userRepository.save(user3);

        Tag tag1 = new Tag();
        tag1.setUser(user1);
        tag1.setName("DWES");
        tagRepository.save(tag1);

        Tag tag2 = new Tag();
        tag2.setUser(user2);
        tag2.setName("DWEC");
        tagRepository.save(tag2);

        Tag tag3 = new Tag();
        tag3.setUser(user3);
        tag3.setName("EIEM");
        tagRepository.save(tag3);

        // TODO Calendar implementation, Date is deprecated
        Task task1 = new Task();
        task1.setTitle("Do postman Test");
        task1.setUser(user1);
        task1.setTaskDone(false);
        task1.setTag(tag1);
        task1.setDeadLine(new Date(2024 - 1900, 3, 07, 23, 59, 59));
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Do CRUD with Angular");
        task2.setUser(user2);
        task2.setTaskDone(false);
        task2.setTag(tag2);
        task2.setDeadLine(new Date(2024 - 1900, 3, 07, 23, 59, 59));
        taskRepository.save(task2);

        Task task3 = new Task();
        task3.setTitle("Do presentation pt.3 y pt.4");
        task3.setUser(user3);
        task3.setTaskDone(false);
        task3.setTag(tag3);
        task3.setDeadLine(new Date(2024 - 1900, 3, 07, 23, 59, 59));
        taskRepository.save(task3);

        Task task4 = new Task();
        task4.setTitle("Take notes from presentation p3");
        task4.setUser(user1);
        task4.setTaskDone(true);
        task4.setTag(tag2);
        task4.setDeadLine(new Date(2024 - 1900, 2, 29, 23, 59, 59));
        taskRepository.save(task4);

        Task task5 = new Task();
        task5.setTitle("Do presentation project");
        task5.setUser(user2);
        task5.setTaskDone(true);
        task5.setTag(tag1);
        task5.setDeadLine(new Date(2024 - 1900, 2, 29, 23, 59, 59));
        taskRepository.save(task5);

        Task task6 = new Task();
        task5.setTitle("Prepare presentation");
        task6.setUser(user3);
        task6.setTaskDone(true);
        task6.setTag(tag3);
        task6.setDeadLine(new Date(2024 - 1900, 2, 29, 23, 59, 59));
        taskRepository.save(task6);

    }

}
