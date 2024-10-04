package com.authapp;

import com.authapp.persistance.entity.PermissionEntity;
import com.authapp.persistance.entity.RoleEntity;
import com.authapp.persistance.entity.UserEntity;
import com.authapp.persistance.entity.enums.PermissionEnum;
import com.authapp.persistance.entity.enums.RoleEnum;
import com.authapp.persistance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class AuthAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthAppApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            PermissionEntity createPermission = PermissionEntity.builder()
                    .permissionEnum(PermissionEnum.CREATE)
                    .build();

            PermissionEntity readPermission = PermissionEntity.builder()
                    .permissionEnum(PermissionEnum.READ)
                    .build();

            PermissionEntity updatePermission = PermissionEntity.builder()
                    .permissionEnum(PermissionEnum.UPDATE)
                    .build();

            PermissionEntity deletePermission = PermissionEntity.builder()
                    .permissionEnum(PermissionEnum.DELETE)
                    .build();

            RoleEntity adminRole = RoleEntity.builder()
                    .roleEnum(RoleEnum.ADMIN)
                    .permissions(Set.of(createPermission, readPermission, updatePermission, deletePermission))
                    .build();

            RoleEntity employeeRole = RoleEntity.builder()
                    .roleEnum(RoleEnum.EMPLOYEE)
                    .permissions(Set.of(readPermission, updatePermission))
                    .build();

            RoleEntity userRole = RoleEntity.builder()
                    .roleEnum(RoleEnum.USER)
                    .permissions(Set.of(readPermission))
                    .build();

            UserEntity userAdmin = UserEntity.builder()
                    .fullNames("Claudia Patricia")
                    .email("claudia@mail.com")
                    .password(passwordEncoder.encode("1234"))
                    .isEnable(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(adminRole))
                    .build();

            UserEntity userEmployee = UserEntity.builder()
                    .fullNames("Edgar Daniel")
                    .email("edgar@mail.com")
                    .password(passwordEncoder.encode("1234"))
                    .isEnable(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(employeeRole))
                    .build();

            UserEntity user = UserEntity.builder()
                    .fullNames("Juan Camilo")
                    .email("bernieta@mail.com")
                    .password(passwordEncoder.encode("1234"))
                    .isEnable(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(userRole))
                    .build();

            userRepository.saveAll(List.of(userAdmin, userEmployee, user));
        };
    }
}
