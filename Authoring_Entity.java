package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The entity who is responsible for producing their own original book.
 * Either an individual author, writing group or ad hoc team can be inherited from this class.
 */

@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "Authoring_Entity.all",
                query = "SELECT *" +
                        " FROM authoring_entity" +
                        " ORDER BY authoring_entity_name",
                resultClass = Authoring_Entity.class),
        @NamedNativeQuery(name = "Authoring_Entity.select.name",
                query = "SELECT *" +
                        " FROM authoring_entity" +
                        " WHERE authoring_entity_name = ?",
                resultClass = Authoring_Entity.class),
        @NamedNativeQuery(name = "Authoring_Entity.select.email",
                query= "SELECT *" +
                " FROM authoring_entity" +
                " WHERE authoring_entity_email = ?",
                resultClass = Authoring_Entity.class),
        @NamedNativeQuery(name = "Authoring_Entity.count.email",
                query = "SELECT count(*)" +
                        " FROM authoring_entity" +
                        " WHERE authoring_entity_email = ?"),
        @NamedNativeQuery(name = "Authoring_Entity.count.name",
                query = "SELECT count(*)" +
                        " FROM authoring_entity" +
                        " WHERE authoring_entity_name = ?")

})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Authoring_Entity {
    @Id
    /* The email address of a authoring entity which allows direct contact. */
    @Column(name = "AUTHORING_ENTITY_EMAIL", length=30, nullable = false)
    private String email;

    @OneToMany(mappedBy = "authoring_entity_name")
    @JoinColumn(name = "ISBN", nullable = false)
    private List<Book> books = new ArrayList<>();

    /* The name of a authoring entity which uniquely identifies each author type. */
    @Column(name = "AUTHORING_ENTITY_NAME",length=80, nullable = false)
    private String name;

    /* The name person appointed as the main author from a writing group. */
    @Column(length=80)
    private String head_writer;

    /* The year in which the writing group was formed. */
    @Column
    private Integer year_formed;

    public Authoring_Entity() {}

    /**
     * Writing Group constructor which initializes each attribute associated with a given writing group.
     * @param  name, stores the name of the Writing Group
     * @param  email, stores the email address of the Writing Group
     * @param  head_writer, stores the name of the head writer within a Writing Group
     * @param  year_formed, holds the integer represented year in which the Writing Group was formed
     * @return none
     */
    public Authoring_Entity(String email, String name, String head_writer, int year_formed) {
        this.email = email;
        this.name = name;
        this.head_writer = head_writer;
        this.year_formed = year_formed;
    }
    /**
     * Constructor both instances  of an Individual Author or Ad Hoc Team initializes each attribute associated with a given writing group.
     * @param  name, stores the name of either the Individual Author or AdHoc Team
     * @param  email, stores the email address of the either the Individual Author or AdHoc Team
     */
    public Authoring_Entity(String email, String name) {
        this.email = email;
        this.name = name;
        this.year_formed = null;
    }

    // Getters and Setters

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getHeadWriter() { return head_writer; }

    public void setHeadWriter(String head_writer) { this.head_writer = head_writer; }

    public int getYearFormed() { return year_formed; }

    public void setYearFormed(int year_formed) { this.year_formed = year_formed; }

    /**
     * Checks if a Authoring Entity object has a a certain email
     * @param entityManager The entity manager of the Database
     * @param email The email being checked
     * @return True or False depending if the Authoring Entity exists
     */
    public static boolean checkEmail (EntityManager entityManager, String email){
        Integer count = (Integer) entityManager.createNamedQuery("Authoring_Entity.count.email").setParameter(1, email).getSingleResult();
        return(count == 0);
    }

    /**
     * Checks if a Authoring Entity object has a a certain name
     * @param entityManager The entity manager of the Database
     * @param name The name being checked
     * @return True or False depending if the Authoring Entity exists
     */
    public static boolean checkName (EntityManager entityManager, String name){
        Integer count = (Integer) entityManager.createNamedQuery("Authoring_Entity.count.name").setParameter(1, name).getSingleResult();
        return(count == 0);
    }

    /**
     * Gets a Authoring_Entity with a certain email.
     * @param entityManager The entity manager of the Database.
     * @param email
     * @return
     */
    public static Authoring_Entity selectAuthoringEntity(EntityManager entityManager, String email) {
        Authoring_Entity ae = (Authoring_Entity) entityManager.createNamedQuery("Authoring_Entity.select.email").setParameter(1, email).getSingleResult();
        return ae;
    }

    /**
     * Returns a list of all the primary keys of every Authoring Entity in the Database.
     * @param entityManager The Entity Manager of the Database.
     * @return List of Strings that are identifiers.
     */
    public static List<String> authoringEntityPK(EntityManager entityManager) {
        List<Authoring_Entity> ae = entityManager.createNamedQuery("Authoring_Entity.all").getResultList();
        List<Object> o = new ArrayList<Object>();
        List<String> s = new ArrayList<String>();
        for(int i = 0; i < ae.size(); i++) {
            Object pk = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(ae.get(i));
            o.add(pk);
        }

        for(int j = 0; j < ae.size(); j++) {
            s.add("#" + (j + 1) + " PK: " + o.get(j) + " Type: " + ae.get(j).getClass().getSimpleName());
        }
        return s;
    }

    /**
     * The string representation of the Authority Entity
     * @return string representation of the Authoring Entity
     */
    @Override
    public String toString () {
        return "Authoring Entity - Name: " + this.getName() + " Email: " + this.getEmail();
    }
}