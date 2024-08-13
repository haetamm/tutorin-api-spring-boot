package tutorin.com.model;

import jakarta.persistence.*;
import lombok.*;
import tutorin.com.constant.TableName;
import tutorin.com.constant.UserRoleEnum;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = TableName.T_ROLE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
}

