package com.okuylu_back;

import com.okuylu_back.security.entity.Role;
import com.okuylu_back.model.User;
import com.okuylu_back.repository.RoleRepository;
import com.okuylu_back.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        Role userRole = roleRepository.findByRoleName("user");
        User user = new User();
        user.setEmail("testkmlknkl@example.com");
        user.setPasswordHash("hashedpassword");
        user.setUsername("testPerson");
        user.setRole(userRole);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));


        System.out.println(123);
        user.setGender(true);
        user.setIsBlocked(false);
        System.out.println(123456);

        System.out.println(user);
        userRepository.save(user);
        System.out.println(12388888);

    }
}