package ee.v22.utils;

import static ee.v22.guice.GuiceInjector.getInjector;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class DbUtils {

    public static EntityTransaction getTransaction() {
        return getEntityManager().getTransaction();
    }

    public static EntityManager getEntityManager() {
        return getInjector().getInstance(EntityManager.class);
    }

    public static void closeEntityManager() {
        getInjector().getInstance(EntityManager.class).close();
    }

    public static void emptyCache() {
        EntityManager em = getInjector().getInstance(EntityManager.class);
        EntityTransaction transaction = em.getTransaction();
        if (transaction.isActive() && !transaction.getRollbackOnly()) {
            em.flush();
            em.clear();
        }
    }

    /**
     * Rollsback transaction if transaction is marked as rollback only. Commit
     * otherwise.
     * <p>
     * If transaction is not active anymore, nothing is done.
     */
    public static void closeTransaction() {
        EntityTransaction transaction = getTransaction();
        if (transaction.isActive()) {
            if (transaction.getRollbackOnly()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        }
    }
}
