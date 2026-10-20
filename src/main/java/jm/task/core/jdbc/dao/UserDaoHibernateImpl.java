package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Consumer;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }
    private void executeInTransaction(Consumer<Session> action) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                action.accept(session);

                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }

                throw new RuntimeException("Transaction failed", e);
            }
        }
    }

    @Override
    public void createUsersTable() {
        executeInTransaction(session ->
                session.createNativeQuery("""
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(50),
                lastName VARCHAR(50),
                age TINYINT
            )
            """).executeUpdate()
        );
    }

    @Override
    public void dropUsersTable() {
        executeInTransaction(session ->
                session.createNativeQuery("DROP TABLE IF EXISTS users")
                        .executeUpdate()
        );
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeInTransaction(session ->
                session.persist(new User(name, lastName, age))
        );
    }

    @Override
    public void removeUserById(long id) {
        executeInTransaction(session -> {
            User user = session.get(User.class, id);

            if (user != null) {
                session.remove(user);
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        executeInTransaction(session ->
                session.createQuery("DELETE FROM User").executeUpdate()
        );
    }
    }

}
