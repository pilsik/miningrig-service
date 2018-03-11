package by.sivko.miningrigservice.services.rig;

import by.sivko.miningrigservice.dao.rig.RigDao;
import by.sivko.miningrigservice.models.rigs.Rig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RigServiceImpl implements RigService {

    private final RigDao rigDao;

    @Autowired
    public RigServiceImpl(RigDao rigDao) {
        this.rigDao = rigDao;
    }

    @Override
    public void addRig(Rig rig) {
        rigDao.save(rig);
    }

    @Override
    public Rig getRigById(Long id) {
        return rigDao.findOne(id);
    }

    @Override
    public void removeRigById(Long id) {
        rigDao.deleteRigById(id);
    }

}
