package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.controllers.exceptions.AlreadyExistsException;
import by.sivko.miningrigservice.dto.RigDto;
import by.sivko.miningrigservice.models.rigs.Rig;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.services.rig.RigService;
import by.sivko.miningrigservice.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/rigs")
public class RigRestController {

    private final RigService rigService;

    private final UserService userService;

    @Autowired
    public RigRestController(RigService rigService, UserService userService) {
        this.rigService = rigService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Rig>> getRigsOfUser(Principal principal) {
        List<Rig> rigList = userService.getUserRigsByUsername(principal.getName());
        if (rigList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(rigList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createRig(RigDto rigDto, Principal principal, UriComponentsBuilder ucBuilder) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        if (checkExistRigByName(user.getUserRigList(), rigDto.getName())) {
            throw new AlreadyExistsException(String.format("A rig with name [%s] already exist", rigDto.getName()));
        } else {
            Rig newRig = new Rig(rigDto.getName(), rigDto.getPassword(), user);
            rigService.addRig(newRig);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/rigs").buildAndExpand().toUri());
//            headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
    }

    private boolean checkExistRigByName(List<Rig> rigList, String rigName) {
        boolean isExist = false;
        for (Rig rig : rigList) {
            if (rigName.equalsIgnoreCase(rig.getName())) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    @RequestMapping(value = "/rig/{id}", method = RequestMethod.GET)
    public ResponseEntity<Rig> getRig(@PathVariable long id, Principal principal) {
        if (checkUserOwnerRig(principal.getName(), id)) {
            Rig rig = rigService.getRigById(id);
            return new ResponseEntity<>(rig, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    @RequestMapping(value = "/rig/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Rig> removeRig(@PathVariable long id, Principal principal, UriComponentsBuilder ucBuilder) {
        if (checkUserOwnerRig(principal.getName(), id)) {
            rigService.removeRigById(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/rigs").buildAndExpand().toUri());
            return new ResponseEntity<>(headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    @RequestMapping(value = "/rig/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> changeRigPassword(@PathVariable long id, String password, Principal principal) {
        if (checkUserOwnerRig(principal.getName(), id)) {
            Rig rig = rigService.getRigById(id);
            rig.setPassword(password);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    private boolean checkUserOwnerRig(String username, long id) {
        boolean isUserOwnerTheRig = false;
        User user = userService.findUserByUsername(username);
        for (Rig rig : user.getUserRigList()) {
            if (rig.getId() == id) {
                isUserOwnerTheRig = true;
                break;
            }
        }
        return isUserOwnerTheRig;
    }
}
