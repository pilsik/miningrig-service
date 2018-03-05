package by.sivko.miningrigservice.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T,PK> {

    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    {
        ParameterizedType genericSuperClass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperClass.getActualTypeArguments()[0];
    }

    @Override
    public T save(T t) {
        this.entityManager.persist(t);
        return t;
    }

    @Override
    public T findOne(PK id) {
        return this.entityManager.find(entityClass,id);
    }
}
