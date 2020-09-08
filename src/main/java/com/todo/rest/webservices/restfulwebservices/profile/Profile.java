package com.todo.rest.webservices.restfulwebservices.profile;
import com.todo.rest.webservices.restfulwebservices.user.DAOUser;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Profile {

    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;

    @Size(max = 4000)
    private String bio;
    //1mb of size
    @Column(length=1048576)
    private String picture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DAOUser user;

    protected Profile(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id) &&
                Objects.equals(firstname, profile.firstname) &&
                Objects.equals(lastname, profile.lastname) &&
                Objects.equals(bio, profile.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, bio);
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public DAOUser getUser() {
        return user;
    }

    public void setUser(DAOUser user) {
        this.user = user;
    }
}
