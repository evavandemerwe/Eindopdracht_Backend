package nl.novi.breedsoft.model.authority;

import jakarta.persistence.*;
import lombok.*;
import nl.novi.breedsoft.model.management.Person;

import java.util.List;

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

    @OneToOne(cascade = CascadeType.MERGE)
    private Person person;

    // @Singular improves readability when assigning multiple authorities to a User in a builder pattern.
    @Singular
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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

    //Getters and Setters
    public Long getId() {
        return (id != null) ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
