package by.sivko.miningrigservice.models.miner;

import by.sivko.miningrigservice.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_miner_configs")
public class UserMinerConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @JsonIgnore
    @JoinColumn(nullable = false)
    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Miner miner;

    @Column
    private String commandLine;

    public UserMinerConfig() {
    }

    public UserMinerConfig(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public UserMinerConfig(String name, User user, Miner miner) {
        this.name = name;
        this.user = user;
        this.miner = miner;
        this.commandLine = miner.getDefaultCommandLineWithParameters();
    }

    public void setMiner(Miner miner) {
        this.miner = miner;
        this.commandLine = miner.getDefaultCommandLineWithParameters();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Miner getMiner() {
        return miner;
    }

}
