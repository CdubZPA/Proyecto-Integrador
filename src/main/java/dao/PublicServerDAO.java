package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.PublicServer;
import util.JPAUtil;

import java.util.List;

public class PublicServerDAO {

    public void save(PublicServer server) {
        EntityManager em = null;

        try {
            em = JPAUtil.getEntityManagerMysql();
            em.getTransaction().begin();

            if (server.getId() == null) {
                em.persist(server);
            } else {
                em.merge(server);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void delete(Long id) {
        EntityManager em = null;

        try {
            em = JPAUtil.getEntityManagerMysql();
            em.getTransaction().begin();

            PublicServer server = em.find(PublicServer.class, id);

            if (server != null) {
                em.remove(server);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PublicServer> findAll() {
        EntityManager em = null;

        try {
            em = JPAUtil.getEntityManagerMysql();

            TypedQuery<PublicServer> query = em.createQuery(
                    "SELECT s FROM PublicServer s ORDER BY s.id DESC",
                    PublicServer.class
            );

            return query.getResultList();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PublicServer> findByIdNumber(String idNumber) {
        EntityManager em = null;

        try {
            em = JPAUtil.getEntityManagerMysql();

            TypedQuery<PublicServer> query = em.createQuery(
                    "SELECT s FROM PublicServer s WHERE s.idNumber LIKE :idNumber ORDER BY s.id DESC",
                    PublicServer.class
            );

            query.setParameter("idNumber", "%" + idNumber + "%");

            return query.getResultList();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}