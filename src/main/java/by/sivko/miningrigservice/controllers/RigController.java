package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.models.Rig;
import by.sivko.miningrigservice.services.rig.RigService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class RigController {

    private static Logger logger  = Logger.getLogger(RigController.class);

    @Autowired
    private final RigService rigService;

    @Autowired
    public RigController(RigService rigService) {
        this.rigService = rigService;
    }

    @RequestMapping(value = "/rigs", method = RequestMethod.GET, produces = "application/json")
    public List<Rig> getRigs(Principal principal)
    {
        logger.info(String.format("User: %s showing rigs",principal.getName()));
        return rigService.getRigsById(principal.getName());
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public void createRig(@RequestHeader String name, @RequestHeader String password){
        Rig newRig = new Rig(name, password);
        rigService.addRig(newRig);
    }
}
