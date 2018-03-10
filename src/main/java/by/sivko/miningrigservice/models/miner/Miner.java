package by.sivko.miningrigservice.models.miner;

import javax.persistence.*;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "miners")
public abstract class Miner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "path_to_exe_file")
    protected final String PATH_TO_EXE_FILE;

    @Column
    protected String defaultCommandLineWithParameters;

    @Column
    protected String version;

    @Column
    protected String dateRealise;

    @ElementCollection(targetClass=String.class)
    @MapKeyColumn(name="key_of_param")
    @Column(name="value_of_param")
    protected Map<String, String> paramNames;

    public Miner(String PATH_TO_EXE_FILE) {
        this.PATH_TO_EXE_FILE = PATH_TO_EXE_FILE;
        this.paramNames = new LinkedHashMap<>(0);
    }

    public String getPATH_TO_EXE_FILE() {
        return PATH_TO_EXE_FILE;
    }

    public String getDefaultCommandLineWithParameters() {
        return defaultCommandLineWithParameters;
    }

    public String getVersion() {
        return version;
    }

    public String getDateRealise() {
        return dateRealise;
    }

    public Map<String, String> getParamNames() {
        return paramNames;
    }
}
