package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.controllers.exceptions.AlreadyExistsException;
import by.sivko.miningrigservice.dto.RigDto;
import by.sivko.miningrigservice.models.Rig;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.services.rig.RigService;
import by.sivko.miningrigservice.services.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping(value = "/rigs")
public class RigRestController {

    private static Logger logger = Logger.getLogger(RigRestController.class);

    @Autowired
    private RigService rigService;

    @Autowired
    private UserService userService;

    /**
     * Retrieve all rigs of user
     *
     * @param principal
     * @return rigs
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Set<Rig>> getRigsOfUser(Principal principal) {
        logger.info(String.format("User with name [%s] view rigs", principal.getName()));
        Set<Rig> rigSet = userService.getUserRigsByUsername(principal.getName());
        if (rigSet.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userService.getUserRigsByUsername(principal.getName()), HttpStatus.OK);
    }

    /**
     * Create new rig
     *
     * @param rigDto
     * @param principal
     * @param ucBuilder
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createRig(RigDto rigDto, Principal principal, UriComponentsBuilder ucBuilder) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        if (checkExistRigByName(user.getUserRigSet(), rigDto.getName())) {
            throw new AlreadyExistsException(String.format("A rig with name [%s] already exist", rigDto.getName()));
        } else {
            Rig newRig = new Rig(user, rigDto.getName(), rigDto.getPassword());
            rigService.addRig(newRig);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/rigs").buildAndExpand().toUri());
//            headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
    }

    private boolean checkExistRigByName(Set<Rig> rigSet, String rigName) {
        boolean isExist = false;
        for (Rig rig : rigSet) {
            if (rigName.equalsIgnoreCase(rig.getName())) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    /**
     * Retrieve rig
     *
     * @param id
     * @param principal
     * @return
     */
    @RequestMapping(value = "/rig/{id}", method = RequestMethod.GET)
    public ResponseEntity<Rig> getRig(@PathVariable long id, Principal principal) {
        if (checkUserOwnerRig(principal.getName(), id)) {
            Rig rig = rigService.getRigById(id);
            return new ResponseEntity<>(rig, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    /**
     * Delete rig
     *
     * @param id
     * @param principal
     * @param ucBuilder
     * @return
     */
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
        for (Rig rig : user.getUserRigSet()) {
            if (rig.getId() == id) {
                isUserOwnerTheRig = true;
                break;
            }
        }
        return isUserOwnerTheRig;
    }
}
