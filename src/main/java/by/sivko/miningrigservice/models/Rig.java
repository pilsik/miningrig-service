package by.sivko.miningrigservice.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * class Rig
 */
@Entity
@Table(name = "rigs")
public class Rig implements Serializable{

    private static final long serialVersionUID = 1227933851518935604L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rig_gen")
    @SequenceGenerator(name = "rig_gen", sequenceName = "rig_seq")
    private long id;

    @Column(name = "name")
    private String name;

    @Transient
    private String miner;

    @Transient
    private String status;

    @Column(name = "user")
    private String user;

    @Transient
    private List<VideoCard> videoCardList;

    //constructors
    public Rig() {
    }

    public Rig(String name) {
        this.name = name;
    }

    public Rig(String name, String user) {
        this.name = name;
        this.user = user;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public String getMiner() {
        return miner;
    }

    public String getStatus() {
        return status;
    }

    public String getUser() {
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
