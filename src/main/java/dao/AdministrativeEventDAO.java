package dao;

import jakarta.persistence.EntityManager;
import model.AdministrativeEvent;
import util.JPAUtil;
import java.util.List;

public class AdministrativeEventDAO {

    public void save(AdministrativeEvent e) {
        EntityManager em = JPAUtil.getEntityManagerMysql();
        try {
            em.getTransaction().begin();
            em.merge(e);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerMysql();
        try {
            em.getTransaction().begin();
            AdministrativeEvent e = em.find(AdministrativeEvent.class, id);
            if (e != null) em.remove(e);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<AdministrativeEvent> findAll() {
        EntityManager em = JPAUtil.getEntityManagerMysql();
        List<AdministrativeEvent> list = em
                .createQuery("FROM AdministrativeEvent", AdministrativeEvent.class)
                .getResultList();
        em.close();
        return list;
    }

    public List<AdministrativeEvent> findByIdNumber(String idNumber) {
        EntityManager em = JPAUtil.getEntityManagerMysql();
        List<AdministrativeEvent> list = em
                .createQuery("FROM AdministrativeEvent e WHERE e.idNumber LIKE :idNumber", AdministrativeEvent.class)
                .setParameter("idNumber", "%" + idNumber + "%")
                .getResultList();
        em.close();
        return list;
    }
}