package by.sivko.miningrigservice.models;

import by.sivko.miningrigservice.models.miner.UserMinerConfig;
import by.sivko.miningrigservice.models.miner.VideoCard;
import by.sivko.miningrigservice.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * class Rig
 */
@Entity
@Table(name = "rigs")
public class Rig implements Serializable {

    private static final long serialVersionUID = 1227933851518935604L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rig_gen")
    @SequenceGenerator(name = "rig_gen", sequenceName = "rig_seq")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "rig", cascade = CascadeType.ALL)
    private Status status;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn
    private UserMinerConfig userMinerConfig;

    //constructors
    public Rig() {
    }

    public Rig(User user, String name, String password) {
        this.user = user;
        this.name = name;
        this.password = password;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Status getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserMinerConfig getUserMinerConfig() {
        return userMinerConfig;
    }

    public void setUserMinerConfig(UserMinerConfig userMinerConfig) {
        this.userMinerConfig = userMinerConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rig rig = (Rig) o;

        if (!name.equals(rig.name)) return false;
        if (!password.equals(rig.password)) return false;
        return user.equals(rig.user);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}
