package by.sivko.miningrigservice.models.miners;

import javax.persistence.*;

@Entity
@Table(name = "miners")
public abstract class Miner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "path_to_exe_file")
    protected final String PATH_TO_EXE_FILE;

    @Column
    protected String defaultCommandLineWithParameters;

    @Column
    protected String version;

    @Column
    protected String dateRealise;

    public Miner(String PATH_TO_EXE_FILE) {
        this.PATH_TO_EXE_FILE = PATH_TO_EXE_FILE;
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
}
