package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A written collection of work, either printed or digitally represented. It can
 * be a literary work, fiction or for educational purposes. It is produced by a publisher
 * and written by a authoring entity.
 */

@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "Book.select.isbn",
                query = "SELECT *" +
                        " FROM book" +
                        " WHERE ISBN = ?",
                resultClass = Book.class),
        @NamedNativeQuery(name = "Book.count.isbn",
                query = "SELECT count(*)" +
                        " FROM book" +
                        " WHERE ISBN = ?"),
        @NamedNativeQuery(name = "Book.all",
                query = "SELECT *" +
                        " FROM book" +
                        " ORDER BY title",
                resultClass = Book.class)
})
@Table(
        name = "BOOK",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"TITLE", "PUBLISHER_NAME"}),
                @UniqueConstraint(columnNames = {"TITLE", "AUTHORING_ENTITY_NAME"})
        }
)
public class Book {

    /** A String that uniquely identifies a Book. */
    @Id
    @Column(length = 17, nullable = false)
    private String ISBN;

    /* The name of a Publisher */
    @ManyToOne
    @JoinColumn(name = "publisher_name", nullable = false)
    private Publisher publisher_name;

    /* The name of a Publisher */
    @ManyToOne
    @JoinColumn(name = "AUTHORING_ENTITY_NAME",  referencedColumnName = "AUTHORING_ENTITY_EMAIL", nullable = false)
    private Authoring_Entity authoring_entity_name;

    /** The title of the Book. */
    @Column(length=60, nullable = false)
    private String title;

    /** The year that the book was published. */
    @Column(nullable = false)
    private int year_published;

    /**
     * Book Constructor
     * @param ISBN stores the ISBN of the Book
     * @param title stores the title of the Book
     * @param year_published stores the year published of the Book
     * @param publisher stores the publisher of the Book
     * @param authoring_entity stores the authoring entity of the Book
     */
    public Book(String ISBN, String title, int year_published, Publisher publisher, Authoring_Entity authoring_entity) {
        this.ISBN = ISBN;
        this.title = title;
        this.year_published = year_published;
        this.publisher_name = publisher;
        this.authoring_entity_name = authoring_entity;
    }

    /**
     * Default Constructor
     */
    public Book() {}

    // Getters and Setters

    public String getISBN() { return ISBN; }

    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getYearPublished() { return year_published; }

    public void setYearPublished(int year_published) { this.year_published = year_published; }

    public Publisher getPublisher() { return publisher_name; }

    public void setPublisher(Publisher publisher) { this.publisher_name = publisher; };

    public Authoring_Entity getAuthoringEntity() { return authoring_entity_name; }

    public void setAuthoringEntity(Authoring_Entity authoring_entity) { this.authoring_entity_name = authoring_entity; }

    /**True or False depending on whether or not the Publisher exists.
     * Checks if there is a Book with a certain ISBN that exists already in the database.
     * @param entityManager The Entity Manager of the Database.
     * @param  ISBN The ISBN being checked.
     * @return True or False depending on whether or not the Book exists.
     */
    public static boolean checkISBN(EntityManager entityManager, String ISBN){
        Integer count = (Integer) entityManager.createNamedQuery("Book.count.isbn").setParameter(1, ISBN).getSingleResult();
        return(count == 0);
    }

    /**
     * Returns a certain Book object with a certain ISBN.
     * @param entityManager The Entity Manager of the Database.
     * @param ISBN The ISBN of the Book being searched for.
     * @return Book with ISBN 'ISBN'.
     */
    public static Book selectBook(EntityManager entityManager, String ISBN) {
        Book b = (Book) entityManager.createNamedQuery("Book.select.isbn").setParameter(1, ISBN).getSingleResult();
        return b;
    }

    /**
     * Returns a list of all Books currently in the Database.
     * @param entityManager The Entity Manager of the Database.
     * @return List of all Book objects.
     */
    public static List<Book> selectAllBooks(EntityManager entityManager) {
        List<Book> b = entityManager.createNamedQuery("Book.all").getResultList();
        return b;
    }

    /**
     * Returns a list of all the primary keys of every Book in the Database.
     * @param entityManager The Entity Manager of the Database.
     * @return List of Strings that are identifiers.
     */
    public static List<String> bookPK(EntityManager entityManager) {
        List<Book> b = entityManager.createNamedQuery("Book.all").getResultList();
        List<Object> o = new ArrayList<Object>();
        List<String> s = new ArrayList<String>();
        for(int i = 0; i < b.size(); i++) {
            Object pk = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(b.get(i));
            o.add(pk);
        }

        for(int j = 0; j < b.size(); j++) {
            s.add("#" + (j + 1) + " PK: " + o.get(j) + " Title: " + b.get(j).getTitle());
        }
        return s;
    }

    /**
     * The string representation of the Book
     * @return string representation of the Book
     */
    @Override
    public String toString () {
        return "Book - ISBN: " + this.getISBN() + " Title: " + this.getTitle() + " Year Published: " + this.getYearPublished();
    }
}