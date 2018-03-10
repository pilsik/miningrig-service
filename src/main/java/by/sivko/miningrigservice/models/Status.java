package by.sivko.miningrigservice.models;

import by.sivko.miningrigservice.models.miner.Miner;
import by.sivko.miningrigservice.models.miner.VideoCard;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "status")
public class Status implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @JoinColumn(nullable = false)
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Rig rig;

    @Column
    private boolean online = false;

    @Column
    private boolean needReboot = false;

    @ElementCollection(targetClass = String.class)
    @MapKeyColumn(name = "key_of_param")
    @Column(name = "value_of_param")
    private Map<String, String> realParamNames;

    @JsonIgnore
    @JoinColumn(nullable = false)
    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Miner miner;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "status", cascade = CascadeType.ALL)
    private List<VideoCard> cardList;

    public Status() {
    }

    public Rig getRig() {
        return rig;
    }

    public void setRig(Rig rig) {
        this.rig = rig;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isNeedReboot() {
        return needReboot;
    }

    public void setNeedReboot(boolean needReboot) {
        this.needReboot = needReboot;
    }

    public Miner getMiner() {
        return miner;
    }

    public void setMiner(Miner miner) {
        this.miner = miner;
        //   this.realParamNames = new HashMap<>(miner.getParamNames());
    }

    public Map<String, String> getRealParamNames() {
        return realParamNames;
    }

    public void setRealParamNames(Map<String, String> realParamNames) {
        this.realParamNames = realParamNames;
    }

    public List<VideoCard> getCardList() {
        return cardList;
    }

    public void setCardList(List<VideoCard> cardList) {
        this.cardList = cardList;
    }

}
