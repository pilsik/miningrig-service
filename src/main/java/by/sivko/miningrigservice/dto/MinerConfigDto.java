package by.sivko.miningrigservice.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class MinerConfigDto {

    @Length(min = 3, message = "*Your configName must have at least 3 characters")
    @NotEmpty(message = "*Please provide your name")
    private String name;

    @NotEmpty(message = "*Please provide your commandLine")
    private String commandLine;

    public String getName() {
        return name;
    }

    public String getCommandLine() {
        return commandLine;
    }
}
