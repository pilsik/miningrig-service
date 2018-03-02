package by.sivko.miningrigservice.restcontrollers;

import by.sivko.miningrigservice.models.Rig;
import by.sivko.miningrigservice.services.RigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RigController {

    @Autowired
    RigService rigService;

    @RequestMapping(value = "/rigs", method = RequestMethod.GET, produces = "application/json")
    public List<Rig> getRigs() {
        ArrayList<Rig> rigArrayList = new ArrayList<>();
        rigArrayList.add(new Rig("1","2"));
        return rigArrayList;
    }
}
