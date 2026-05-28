package dao;

import jakarta.persistence.EntityManager;
import model.Dependency;
import util.JPAUtil;
import java.util.List;

public class DependencyDAO {

    public void save(Dependency d) {
        EntityManager em = JPAUtil.getEntityManagerMysql();
        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
        em.close();
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerMysql();
        em.getTransaction().begin();
        Dependency d = em.find(Dependency.class, id);
        if (d != null) em.remove(d);
        em.getTransaction().commit();
        em.close();
    }

    public List<Dependency> findAll() {
        EntityManager em = JPAUtil.getEntityManagerMysql();
        List<Dependency> list = em
                .createQuery("FROM Dependency", Dependency.class)
                .getResultList();
        em.close();
        return list;
    }
}