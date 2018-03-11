package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.controllers.exceptions.AlreadyExistsException;
import by.sivko.miningrigservice.dto.MinerConfigDto;
import by.sivko.miningrigservice.dto.MinerDto;
import by.sivko.miningrigservice.models.configs.MinerConfig;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.services.configs.MinerConfigService;
import by.sivko.miningrigservice.services.miner.MinerService;
import by.sivko.miningrigservice.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/configs")
public class MinerConfigRestController {

    private final MinerConfigService minerConfigService;

    private final UserService userService;

    private final MinerService minerService;

    @Autowired
    public MinerConfigRestController(MinerConfigService minerConfigService, UserService userService, MinerService minerService) {
        this.minerConfigService = minerConfigService;
        this.userService = userService;
        this.minerService = minerService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createRig(MinerConfigDto minerConfigDto, @RequestParam(required = false) MinerDto minerDto, Principal principal, UriComponentsBuilder ucBuilder) {
        String username = principal.getName();
        User user = this.userService.findUserByUsername(username);
        if (checkExistConfigByName(user.getMinerConfigs(), minerConfigDto.getName())) {
            throw new AlreadyExistsException(String.format("A config with name [%s] already exist", minerConfigDto.getName()));
        } else {
            MinerConfig minerConfig = new MinerConfig(minerConfigDto.getName(), user);
            if (!minerDto.getName().isEmpty())
                minerConfig.setMiner(this.minerService.getMinerByName(minerDto.getName()));
            this.minerConfigService.addMinerConfig(minerConfig);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/configs").buildAndExpand().toUri());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
    }

    private boolean checkExistConfigByName(Set<MinerConfig> minerConfigs, String rigName) {
        boolean isExist = false;
        for (MinerConfig minerConfig : minerConfigs) {
            if (rigName.equalsIgnoreCase(minerConfig.getName())) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Set<MinerConfig>> getAllUserMinerConfigs(Principal principal) {
        Set<MinerConfig> rigSet = this.userService.getUserMinerConfigsByUsername(principal.getName());
        if (rigSet.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(this.userService.getUserMinerConfigsByUsername(principal.getName()), HttpStatus.OK);
    }

    @RequestMapping(value = "/config/{id}", method = RequestMethod.GET)
    public ResponseEntity<MinerConfig> getUserMinerConfigById(@PathVariable long id, Principal principal) {
        if (checkUserOwnerConfig(principal.getName(), id)) {
            MinerConfig minerConfig = this.minerConfigService.getMinerConfigById(id);
            return new ResponseEntity<>(minerConfig, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    @RequestMapping(value = "/config/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> changeMinerConfig(@PathVariable long id, MinerConfigDto minerConfigDto, @RequestParam(required = false) MinerDto minerDto, Principal principal) {
        if (checkUserOwnerConfig(principal.getName(), id)) {
            MinerConfig minerConfig = this.minerConfigService.getMinerConfigById(id);
            minerConfig.setName(minerConfigDto.getName());
            minerConfig.setCommandLine(minerConfigDto.getCommandLine());
            minerConfig.setMiner(this.minerService.getMinerByName(minerDto.getName()));
            this.minerConfigService.addMinerConfig(minerConfig);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    @RequestMapping(value = "/config/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<MinerConfig> removeUserConfig(@PathVariable long id, Principal principal) {
        if (checkUserOwnerConfig(principal.getName(), id)) {
            return new ResponseEntity<>(this.minerConfigService.removeMinerConfigById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    private boolean checkUserOwnerConfig(String username, long id) {
        boolean isUserOwnerTheRig = false;
        User user = this.userService.findUserByUsername(username);
        for (MinerConfig minerConfig : user.getMinerConfigs()) {
            if (minerConfig.getId() == id) {
                isUserOwnerTheRig = true;
                break;
            }
        }
        return isUserOwnerTheRig;
    }


}
