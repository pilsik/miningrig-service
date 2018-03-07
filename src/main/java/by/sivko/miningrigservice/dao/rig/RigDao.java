package by.sivko.miningrigservice.dao.rig;

import by.sivko.miningrigservice.dao.GenericDao;
import by.sivko.miningrigservice.models.Rig;
import by.sivko.miningrigservice.models.user.User;

import java.util.List;

public interface RigDao extends GenericDao<Rig,Long> {

    Rig deleteRigById(Long id);

}
