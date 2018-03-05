package by.sivko.miningrigservice.services.rig;

import by.sivko.miningrigservice.models.Rig;
import org.springframework.stereotype.Service;

public interface RigService {

    Rig addRig(Rig rig);
    Rig getRigById(Long id);
    Rig removeRigById(Long id);

}
