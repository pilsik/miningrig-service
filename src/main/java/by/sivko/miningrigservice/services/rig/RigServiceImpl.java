package by.sivko.miningrigservice.services.rig;

import by.sivko.miningrigservice.dao.rig.RigDao;
import by.sivko.miningrigservice.models.Rig;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RigServiceImpl implements RigService {

    @Autowired
    RigDao rigDao;

    @Autowired
    UserService userService;

    @Override
    public Rig addRig(Rig rig) {
        return rigDao.save(rig);
    }

    @Override
    public Rig getRigById(Long id) {
        return rigDao.findOne(id);
    }

    @Override
    public Rig removeRigById(Long id) {
        return rigDao.deleteRigById(id);
    }

}
