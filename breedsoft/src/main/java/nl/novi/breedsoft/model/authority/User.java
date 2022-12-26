package nl.novi.breedsoft.model.authority;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    // @Singular improves readability when assigning multiple authorities to a User in a builder pattern.
    @Singular
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_authorities",
            joinColumns = {
                @JoinColumn(name = "USERS_ID", referencedColumnName = "ID")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "AUTHORITIES_ID", referencedColumnName = "ID")
            }
        )
    private List<Authority> authorities;

    public Long getId() {
        return (id != null) ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
