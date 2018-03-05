package by.sivko.miningrigservice.services.rig;

import by.sivko.miningrigservice.dao.rig.RigDao;
import by.sivko.miningrigservice.models.Rig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RigServiceImpl implements RigService {

    @Autowired
    RigDao rigDao;

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

    @Override
    public List<Rig> getRigsById(String user) {
        return rigDao.getAllRigsByUser(user);
    }
}
