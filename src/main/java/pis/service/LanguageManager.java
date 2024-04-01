/**
 * PIS Projekt 2024
 * LanguageManager.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.service;
import java.util.List;

import pis.data.Language;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Business logic for working with Languages.
 */
@RequestScoped
public class LanguageManager {
    @PersistenceContext
    private EntityManager em;

    /**
     * Returns list of all Languages.
     */
    public List<Language> findAll() {
        return em.createNamedQuery("Language.findAll", Language.class).getResultList();
    }

    /**
     * Returns Language if it exists, otherwise null.
     * @param id Id of the searched Language.
     */
    public Language find(long id) {
        return em.find(Language.class, id);
    }

    /**
     * Returns Language by name if it exists, otherwise null.
     * @param name name of the searched Language.
     */
    public Language findLanguage(String language) {
        Language author = null;
        try {
            Query q = em.createQuery("SELECT l FROM Language l WHERE l.language = :language");
            q.setParameter("language", language);
            return (Language) q.getSingleResult();
        }
        catch(Exception e) {
            System.out.println(e);
            return author;
        }
    }

    /**
     * Add Language to db.
     * @param l Language to add.
     * @return Returns inserted Language.
     */
    @Transactional
    public Language save(Language l) {
        return em.merge(l);
    }

    /**
     * Remove Language from db.
     * @param l Language to remove
     * @return If succeeded or not
     */
    @Transactional
    public void delete(Language l) {
        Language new_l = em.merge(l);
        em.remove(new_l);
    }
}
