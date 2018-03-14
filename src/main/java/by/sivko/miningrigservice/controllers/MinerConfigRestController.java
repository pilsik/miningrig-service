package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.controllers.exceptions.AlreadyExistsException;
import by.sivko.miningrigservice.controllers.exceptions.NotExistException;
import by.sivko.miningrigservice.dto.MinerConfigDto;
import by.sivko.miningrigservice.dto.MinerDto;
import by.sivko.miningrigservice.models.configs.MinerConfig;
import by.sivko.miningrigservice.models.miners.Miner;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.services.configs.MinerConfigService;
import by.sivko.miningrigservice.services.miner.MinerService;
import by.sivko.miningrigservice.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/configs")
public class MinerConfigRestController {

    private MinerConfigService minerConfigService;

    private UserService userService;

    private MinerService minerService;

    @Autowired
    public MinerConfigRestController(MinerConfigService minerConfigService, UserService userService, MinerService minerService) {
        this.minerConfigService = minerConfigService;
        this.userService = userService;
        this.minerService = minerService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createRig(@Valid MinerConfigDto minerConfigDto, @RequestParam(required = false) Long minerId, Principal principal) {
        String username = principal.getName();
        User user = this.userService.findUserByUsername(username);
        if (checkExistConfigByName(user.getMinerConfigs(), minerConfigDto.getName())) {
            throw new AlreadyExistsException(String.format("A config with name [%s] already exist", minerConfigDto.getName()));
        } else {
            MinerConfig minerConfig = new MinerConfig(minerConfigDto.getName(), user);
            if (minerId != null) {
                Miner miner = this.minerService.getMinerById(minerId);
                if (miner != null)
                    minerConfig.setMiner(miner);
                else throw new NotExistException(String.format("A miner with id [%s] already exist", minerId));
            }
            this.minerConfigService.addMinerConfig(minerConfig);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    private boolean checkExistConfigByName(List<MinerConfig> minerConfigs, String rigName) {
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
    public ResponseEntity<List<MinerConfig>> getAllMinerConfigs(Principal principal) {
        List<MinerConfig> rigSet = this.userService.getUserMinerConfigsByUsername(principal.getName());
        if (rigSet.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(rigSet, HttpStatus.OK);
    }

    @RequestMapping(value = "/config/{id}", method = RequestMethod.GET)
    public ResponseEntity<MinerConfig> getMinerConfigById(@PathVariable long id, Principal principal) {
        if (checkOwnerConfig(principal.getName(), id)) {
            MinerConfig minerConfig = this.minerConfigService.getMinerConfigById(id);
            return new ResponseEntity<>(minerConfig, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    @RequestMapping(value = "/config/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> changeMinerConfig(@PathVariable long id, MinerConfigDto minerConfigDto, @RequestParam(required = false) MinerDto minerDto, Principal principal) {
        if (checkOwnerConfig(principal.getName(), id)) {
            MinerConfig minerConfig = this.minerConfigService.getMinerConfigById(id);
            minerConfig.setName(minerConfigDto.getName());
            minerConfig.setCommandLine(minerConfigDto.getCommandLine());
            minerConfig.setMiner(this.minerService.getMinerById(minerDto.getId()));
            this.minerConfigService.addMinerConfig(minerConfig);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    @RequestMapping(value = "/config/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<MinerConfig> removeUserConfig(@PathVariable long id, Principal principal) {
        if (checkOwnerConfig(principal.getName(), id)) {
            return new ResponseEntity<>(this.minerConfigService.removeMinerConfigById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    private boolean checkOwnerConfig(String username, long id) {
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
