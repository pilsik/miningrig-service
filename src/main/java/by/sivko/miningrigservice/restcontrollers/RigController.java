package by.sivko.miningrigservice.restcontrollers;

import by.sivko.miningrigservice.models.Rig;
import by.sivko.miningrigservice.services.rig.RigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController(value = "/rigs")
public class RigController {

    @Autowired
    private final RigService rigService;

    @Autowired
    public RigController(RigService rigService) {
        this.rigService = rigService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Rig> getRigs() {
        ArrayList<Rig> rigArrayList = new ArrayList<>();
        rigArrayList.add(new Rig("1", "2"));
        return rigArrayList;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public void createRig(@RequestHeader String name){
        Rig newRig = new Rig(name);
        rigService.addRig(newRig);
    }
}
