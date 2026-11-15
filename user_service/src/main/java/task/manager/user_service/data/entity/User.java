package task.manager.user_service.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import task.manager.user_service.data.enums.UserRole;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;
    private LocalDate birthDate;
    @Column(updatable = false)
    private Instant createdAt;
    private Instant updatedAt;

}