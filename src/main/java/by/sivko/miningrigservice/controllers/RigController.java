package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.dto.NewRigDto;
import by.sivko.miningrigservice.models.Rig;
import by.sivko.miningrigservice.services.rig.RigService;
import by.sivko.miningrigservice.services.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class RigController {

    private static Logger logger = Logger.getLogger(RigController.class);

    private final RigService rigService;

    private final UserService userService;

    @Autowired
    public RigController(RigService rigService, UserService userService) {
        this.rigService = rigService;
        this.userService = userService;
    }

    @RequestMapping(value = "/user/{username}/rig/all", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Rig> getRigs(Principal principal) {
        logger.info(String.format("User: %s showing rigs", principal.getName()));
        return rigService.getRigsByUsername(principal.getName());
    }

    @RequestMapping(value = "/user/{username}/rig/create", method = RequestMethod.POST)
    public void createNewRig(@PathVariable String username, HttpServletResponse response, NewRigDto newRigDto, Principal principal) throws IOException {
        Rig newRig = new Rig(userService.findUserByUsername(principal.getName()), newRigDto.getName(), newRigDto.getPassword());
        rigService.addRig(newRig);
        logger.info(String.format("User: %s add rig", principal.getName()));
        response.sendRedirect("/user/"+username+"/rig/all");
    }

    @RequestMapping(value = "/user/{username}/rig/create", method = RequestMethod.GET)
    public ModelAndView registerNewRig(Principal principal) {
        NewRigDto rig = new NewRigDto();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rig", rig);
        modelAndView.addObject("username", principal.getName());
        modelAndView.setViewName("createRig");
        return modelAndView;
    }
}
