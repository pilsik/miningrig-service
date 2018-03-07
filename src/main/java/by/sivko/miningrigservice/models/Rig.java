package by.sivko.miningrigservice.models;

import by.sivko.miningrigservice.models.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * class Rig
 */
@Entity
@Table(name = "rigs")
@NamedQueries({
        @NamedQuery(name = "Rig.getAllRigsByUserId", query = "SELECT r FROM Rig r WHERE r.user=:user_value")
})
public class Rig implements Serializable{

    private static final long serialVersionUID = 1227933851518935604L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rig_gen")
    @SequenceGenerator(name = "rig_gen", sequenceName = "rig_seq")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String miner;

    @Transient
    private String status;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Transient
    private List<VideoCard> videoCardList;

    //constructors
    public Rig() {
    }

    public Rig(User user, String name, String password){
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

    public String getMiner() {
        return miner;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public List<VideoCard> getVideoCardList() {
        return videoCardList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVideoCardList(List<VideoCard> videoCardList) {
        this.videoCardList = videoCardList;
    }
}
