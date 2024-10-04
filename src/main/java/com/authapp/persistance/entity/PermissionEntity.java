package com.authapp.persistance.entity;

import com.authapp.persistance.entity.enums.PermissionEnum;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "permissions")
public class PermissionEntity extends BaseEntity {

    @Column(name = "permission_name")
    @Enumerated(EnumType.STRING)
    private PermissionEnum permissionEnum;

}
