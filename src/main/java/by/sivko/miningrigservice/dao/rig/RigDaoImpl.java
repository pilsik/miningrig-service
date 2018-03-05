package by.sivko.miningrigservice.dao.rig;

import by.sivko.miningrigservice.dao.GenericDaoImpl;
import by.sivko.miningrigservice.models.Rig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RigDaoImpl extends GenericDaoImpl<Rig,Long> implements RigDao {

    @Override
    public List<Rig> getAllRigsByUser(String user) {
        return null;
    }

    @Override
    public Rig deleteRigById(Long id) {
        Rig deletedRig = this.findOne(id);
        super.entityManager.remove(deletedRig);
        return deletedRig;
    }
}
