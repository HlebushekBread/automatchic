package net.softloaf.automatchic.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username", unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "`group`")
    private String group;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @JsonIgnore
    @Column(name = "is_confirmed")
    private boolean isConfirmed;

    @JsonIgnore
    @CreatedDate
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subject> subjects;
}
