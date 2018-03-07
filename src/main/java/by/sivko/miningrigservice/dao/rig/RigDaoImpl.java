package by.sivko.miningrigservice.dao.rig;

import by.sivko.miningrigservice.dao.GenericDaoImpl;
import by.sivko.miningrigservice.models.Rig;
import by.sivko.miningrigservice.models.user.User;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RigDaoImpl extends GenericDaoImpl<Rig,Long> implements RigDao {

    @Override
    public Rig deleteRigById(Long id) {
        Rig deletedRig = this.findOne(id);
        super.entityManager.remove(deletedRig);
        return deletedRig;
    }

    @Override
    public List<Rig> getAllRigsByUserId(User user) {
        return super.entityManager.createNamedQuery("Rig.getAllRigsByUserId", Rig.class)
                .setParameter("user_value", user)
                .getResultList();
    }
}
