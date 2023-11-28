package csulb.cecs323.model;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import java.util.List;

/**
 * A group of authors who come together in order to write many books with a
 * respective genre or theme.
 */

@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "Writing_Group.all",
                query = "SELECT *" +
                        " FROM authoring_entity" +
                        " WHERE head_writer IS NOT NULL" +
                        " ORDER BY authoring_entity_name",
                resultClass = Writing_Group.class),
})
public class Writing_Group extends Authoring_Entity {
    /**
     * Writing Group Constructor
     * @param email stores the email of the Writing Group
     * @param name stores the name of the Writing Group
     * @param head_writer stores the head writer of the Writing Group
     * @param year_formed stores the year that the Writing Group was formed
     */
    public Writing_Group(String email, String name, String head_writer, int year_formed) {
        super(email, name, head_writer, year_formed);
    }

    /**
     * Default Constructor
     */
    public Writing_Group() { }

    /**
     * Gets a List of all Writing Groups in the Database
     * @param entityManager The Entity Manager of the Database.
     * @return List of Writing Groups
     */
    public static List<Writing_Group> selectAllWritingGroups(EntityManager entityManager) {
        List<Writing_Group> wg = entityManager.createNamedQuery("Writing_Group.all").getResultList();
        return wg;
    }

    /**
     * The string representation of the Writing Group
     * @return string representation of the Writing Group
     */
    @Override
    public String toString () {
        return "Writing Group - Name: " + this.getName() + " Email: " + this.getEmail() +
                " Head Writer: " + this.getHeadWriter() + " Year Formed: " + this.getYearFormed();
    }
}