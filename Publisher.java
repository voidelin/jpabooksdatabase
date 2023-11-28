package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A company who publishes books.
 */

@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "Publisher.select.name",
            query = "SELECT *" +
                " FROM publisher" +
                " WHERE publisher_name = ?",
            resultClass = Publisher.class),

        @NamedNativeQuery(name = "Publisher.all",
                query = "SELECT *" +
                        " FROM publisher" +
                        " ORDER BY publisher_name",
                resultClass = Publisher.class),

        @NamedNativeQuery(name = "Publisher.count.name",
                query = "SELECT count(*)" +
                        " FROM publisher" +
                        " WHERE publisher_name = ?"),

        @NamedNativeQuery(name = "Publisher.count.phone",
                query = "SELECT count(*)" +
                        " FROM publisher" +
                        " WHERE phone = ?"),

        @NamedNativeQuery(name = "Publisher.count.email",
                query = "SELECT count(*)" +
                        " FROM publisher" +
                        " WHERE email = ?"),
        })


@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "phone"), @UniqueConstraint(columnNames = "email")})
public class Publisher {
    @Id
    /** A name of a publisher company which uniquely identifies and refers to each individual company */
    @Column(name = "PUBLISHER_NAME", length=80, nullable = false)
    private String name;

    @OneToMany(mappedBy = "publisher_name")
    @JoinColumn(name = "ISBN", nullable = false)
    private List<Book> books = new ArrayList<>();

    /** The email address of a publisher which allows direct contact.*/
    @Column(length=80, nullable = false)
    private String email;

    /** The phone number of a publishing house. */
    @Column (length=24, nullable = false)
    private String phone;

    /**
     * Publisher constructor which initializes each attribute associated with a publisher.
     * @param  name, stores the name of the Publisher
     * @param  email, stores the email of the Publisher
     * @param  phone, stores the phone number of the Publisher
     * @return none
     */
    public Publisher (String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Default Constructor
     */
    public Publisher () {}

    // Getters and Setters

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Checks if there is a Publisher with a certain name that exists already in the database.
     * @param entityManager The Entity Manager of the Database.
     * @param  name The name being checked.
     * @return True or False depending on whether or not the Publisher exists.
     */
    public static boolean checkName(EntityManager entityManager, String name){
        Integer count = (Integer) entityManager.createNamedQuery("Publisher.count.name").setParameter(1, name).getSingleResult();
        return(count == 0);
    }

    /**
     * Checks if there is a Publisher with a certain phone that exists already in the database.
     * @param entityManager The Entity Manager of the Database.
     * @param  phone The phone being checked.
     * @return True or False depending on whether or not the Publisher exists.
     */
    public static boolean checkPhone(EntityManager entityManager, String phone){
        Integer count = (Integer) entityManager.createNamedQuery("Publisher.count.phone").setParameter(1, phone).getSingleResult();
        return(count == 0);
    }

    /**
     * Checks if there is a Publisher with a certain email that exists already in the database.
     * @param entityManager The Entity Manager of the Database.
     * @param  email The email being checked.
     * @return True or False depending on whether or not the Publisher exists.
     */
    public static boolean checkEmail (EntityManager entityManager, String email){
        Integer count = (Integer) entityManager.createNamedQuery("Publisher.count.email").setParameter(1, email).getSingleResult();
        return(count == 0);
    }

    /**
     * Returns a certain Publisher object with a certain name.
     * @param entityManager The Entity Manager of the Database.
     * @param name The name of the Publisher being searched for.
     * @return Publisher with name 'name'.
     */
    public static Publisher selectPublisher(EntityManager entityManager, String name) {
        Publisher p = (Publisher) entityManager.createNamedQuery("Publisher.select.name").setParameter(1, name).getSingleResult();
        return p;
    }

    /**
     * Returns a list of all Publishers currently in the Database.
     * @param entityManager The Entity Manager of the Database.
     * @return List of Publisher objects.
     */
    public static List<Publisher> selectAllPublishers(EntityManager entityManager) {
        List<Publisher> p = entityManager.createNamedQuery("Publisher.all").getResultList();
        return p;
    }

    /**
     * Returns a list of all the primary keys of every Publisher in the Database.
     * @param entityManager The Entity Manager of the Database.
     * @return List of Objects that are identifiers.
     */
    public static List<Object> publisherPK(EntityManager entityManager) {
        List<Publisher> p = entityManager.createNamedQuery("Publisher.all").getResultList();
        List<Object> o = new ArrayList<>();
        for(int i = 0; i < p.size(); i++) {
            Object pk = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(p.get(i));
            o.add(pk);
        }
        return o;
    }

    /**
     * The string representation of the Publisher
     * @return string representation of the Publisher
     */
    @Override
    public String toString () {
        return "Publisher - Name: " + this.getName() + " Email: " + this.getEmail() + " Phone #: " + this.getPhone();
    }


}