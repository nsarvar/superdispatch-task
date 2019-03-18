package com.backend.tasks.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implement entity:
 * 1. Map to users
 * 2. Add equals and hashCode methods
 */
@Entity
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;
    /**
     * Map organization with users.
     * Use OneToMany association and map by organization field in User class.
     * Fetch lazy, cascade all
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "org")
    @JsonManagedReference
    private Set<User> users;

    public Organization() {
    }

    public Organization(String name) {
        this.name = name;
    }

    public Organization(String name, User... users) {
        this.name = name;
        this.users = Stream.of(users).collect(Collectors.toSet());
        this.users.forEach(x -> x.setOrg(this));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Organization))
            return false;

        Organization that = (Organization) o;

        if (id == null) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

}
