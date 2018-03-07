package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.dto.NewRigDto;
import by.sivko.miningrigservice.models.Rig;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.services.rig.RigService;
import by.sivko.miningrigservice.services.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping(value = "/rigs")
public class RigController {

    private static Logger logger = Logger.getLogger(RigController.class);

    @Autowired
    private RigService rigService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getRigs(Principal principal, @RequestParam(value = "successMessage" , required = false) String successMessage) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rigs", userService.getUserRigsByUsername(principal.getName()));
        modelAndView.addObject("successMessage", successMessage);
        modelAndView.setViewName("rigs");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView registerNewRig(Principal principal) {
        NewRigDto rig = new NewRigDto();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rig", rig);
        modelAndView.addObject("username", principal.getName());
        modelAndView.setViewName("createRig");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createNewRig(HttpServletResponse response, NewRigDto newRigDto, Principal principal, BindingResult bindingResult) throws IOException {
        ModelAndView modelAndView = new ModelAndView("redirect:/rigs");
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        if (checkExistRigByName(user.getUserRigSet(), username)) {
            bindingResult
                    .rejectValue("name", "error.rig",
                            "There is already a user registered with the login provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("createRig");
        } else {
            Rig newRig = new Rig(user, newRigDto.getName(), newRigDto.getPassword());
            rigService.addRig(newRig);
            modelAndView.addObject("successMessage", "New rig has been create successfully");
        }
        return modelAndView;
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

    @RequestMapping(value = "/rig/{id}/delete", method = RequestMethod.GET)
    public ModelAndView removeRig(@PathVariable long id, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("redirect:/rigs");
        if (checkUserOwnerRig(principal.getName(), id)) {
            rigService.removeRigById(id);
            modelAndView.addObject("successMessage", "Rig was success remove");
        } else {
            modelAndView.addObject("errorMessage", "Can't delete not your rig");
        }
        return modelAndView;
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
