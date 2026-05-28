package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Position;
import util.JPAUtil;

import java.util.List;

public class PositionDAO {

    public void save(Position position) {
        EntityManager em = null;

        try {
            em = JPAUtil.getEntityManagerMysql();
            em.getTransaction().begin();

            if (position.getId() == null) {
                em.persist(position);
            } else {
                em.merge(position);
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

            Position position = em.find(Position.class, id);

            if (position != null) {
                em.remove(position);
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

    public List<Position> findAll() {
        EntityManager em = null;

        try {
            em = JPAUtil.getEntityManagerMysql();

            TypedQuery<Position> query = em.createQuery(
                    "SELECT p FROM Position p ORDER BY p.id DESC",
                    Position.class
            );

            return query.getResultList();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Position> findByIdNumber(String idNumber) {
        EntityManager em = null;

        try {
            em = JPAUtil.getEntityManagerMysql();

            TypedQuery<Position> query = em.createQuery(
                    "SELECT p FROM Position p WHERE p.idNumber LIKE :idNumber ORDER BY p.id DESC",
                    Position.class
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

