/** CECS 323 JPA Project - Books
 * @author Victoria Macali
 * @author Anthony Reyes
 * @author Linda Trinh
 */

package csulb.cecs323.app;

import csulb.cecs323.model.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class BookUI {
   private static final Logger LOGGER = Logger.getLogger(BookUI.class.getName());

   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("BookUI");
      EntityManager manager = factory.createEntityManager();

      boolean quit = false;
      Scanner in = new Scanner(System.in);

      System.out.println("\nWelcome to the Book database");
      System.out.println("----------------------------------");

      LOGGER.fine("Begin of Transaction");
      EntityTransaction tx = manager.getTransaction();

      while (!quit) {
         int choice;
         tx.begin();
         System.out.println("\n1. Add a new object");
         System.out.println("2. List all the information about a specific object");
         System.out.println("3. Delete a book");
         System.out.println("4. Update a book");
         System.out.println("5. List the primary key of all rows of a specific object");
         System.out.println("6. Quit");
         choice = in.nextInt();
         switch(choice){
            case 1:
               System.out.println("\nWhat type of object would you like to add?");
               System.out.println("1. Authoring Entity");
               System.out.println("2. Publisher");
               System.out.println("3. Book");
               choice = in.nextInt();
               switch(choice){
                  case 1:
                     System.out.println("\nWhat type of Authoring Entity would you like to add?");
                     System.out.println("1. Writing Group");
                     System.out.println("2. Individual Author");
                     System.out.println("3. Ad Hoc Team");
                     System.out.println("4. Individual Author to an existing Ad Hoc Team");
                     choice = in.nextInt();
                     switch(choice){
                        case 1:
                           addWritingGroup(manager);
                           break;
                        case 2:
                           addIndividualAuthor(manager);
                           break;
                        case 3:
                           addAdHocTeam(manager);
                           break;
                        case 4:
                           addIndividualAuthorToAdHocTeam(manager);
                           break;
                        default:
                           System.out.println("Invalid entry!");
                           break;
                     }
                     break;
                  case 2:
                     addPublisher(manager);
                     break;
                  case 3:
                     addBook(manager);
                     break;
                  default:
                     System.out.println("Invalid entry!");
                     break;
               }
               break;
            case 2:
               System.out.println("\nWhich object would you like to see?");
               System.out.println("1 - Publisher");
               System.out.println("2 - Book");
               System.out.println("3 - Writing Group");
               choice = in.nextInt();
               switch(choice){
                  case 1:
                     listPublisher(manager);
                     break;
                  case 2:
                     listBook(manager);
                     break;
                  case 3:
                     listWritingGroup(manager);
                     break;
                  default:
                     System.out.println("Invalid entry!");
                     break;
               }
               break;
            case 3:
               deleteBook(manager);
               break;
            case 4:
               updateBook(manager);
               break;
            case 5:
               System.out.println("\nWhich row of primary keys would you like to see?");
               System.out.println("1 - Publisher");
               System.out.println("2 - Book");
               System.out.println("3 - Authoring Entities");
               choice = in.nextInt();
               switch(choice){
                  case 1:
                     listPublisherPK(manager);
                     break;
                  case 2:
                     listBookPK(manager);
                     break;
                  case 3:
                     listAuthoringEntityPK(manager);
                     break;
                  default:
                     break;
               }
               break;
            case 6:
               System.out.println("\nQuitting program ... ");
               quit = true;
               break;
            default:
               System.out.println("Invalid entry!");
               break;
         }
         tx.commit();
         LOGGER.fine("End of Transaction");
      }

      manager.close();
   }

   /**
    * Adds a Publisher object to the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void addPublisher(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter publisher name: ");
      String name = in.nextLine();
      while(! Publisher.checkName(manager, name)) {
         System.out.print("The name entered already exists, please try again: ");
         name = in.nextLine();
      }
      System.out.print("Enter publisher email: ");
      String email = in.nextLine();
      while(! Publisher.checkEmail(manager, email)) {
         System.out.print("The email entered already exists, please try again: ");
         email = in.nextLine();
      }
      System.out.print("Enter publisher phone: ");
      String phone = in.nextLine();
      while(! Publisher.checkPhone(manager, phone)) {
         System.out.print("The phone entered already exists, please try again: ");
         phone = in.nextLine();
      }

      Publisher p = new Publisher(name, email, phone);
      LOGGER.info("Persisting: " + p);
      manager.persist(p);
      LOGGER.info("Persisted object after flush (non-null id): " + p);
   }

   /**
    * Deletes a Publisher object from the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void deletePublisher(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter the name of the publisher you wish to delete: ");
      String name = in.nextLine();
      while(Publisher.checkName(manager, name)) {
         System.out.print("That publisher does not exist, please try again: ");
         name = in.nextLine();
      }
      Publisher delete = Publisher.selectPublisher(manager, name);
      manager.remove(delete);
   }

   /**
    * Lists a certain Publisher from the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void listPublisher(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter the name of the publisher you wish to see: ");
      String name = in.nextLine();
      while(Publisher.checkName(manager, name)) {
         System.out.print("That publisher does not exist, please try again: ");
         name = in.nextLine();
      }
      Publisher p = Publisher.selectPublisher(manager, name);
      System.out.println("\nThe information of " + p.getName() + ":");
      System.out.println(p);
   }

   /**
    * Lists all the Primary Keys of the Publisher objects from the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void listPublisherPK(EntityManager manager) {
      List<Object> o = Publisher.publisherPK(manager);
      for(int i = 0; i < o.size(); i ++) {
         System.out.println("#" + (i + 1) + " PK: " + o.get(i));
      }
   }

   /**
    * Adds an Individual_Author object to the Database.
    * @param manager The Entity Manager of the Database.
           */
   public static void addIndividualAuthor(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter individual author name: ");
      String name = in.nextLine();
      System.out.print("Enter individual author email: ");
      String email = in.nextLine();
      while(! Authoring_Entity.checkEmail(manager, email)) {
         System.out.print("The email entered already exists, please try again: ");
         email = in.nextLine();
      }

      Individual_Author ia = new Individual_Author(email, name);
      LOGGER.info("Persisting: " + ia);
      manager.persist(ia);
      LOGGER.info("Persisted object after flush (non-null id): " + ia);
   }

   /**
    * Adds an Ad_Hoc_Team object to the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void addAdHocTeam(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter ad hoc team name: ");
      String name = in.nextLine();
      System.out.print("Enter ad hoc team email: ");
      String email = in.nextLine();
      while(! Authoring_Entity.checkEmail(manager, email)) {
         System.out.print("The email entered already exists, please try again: ");
         email = in.nextLine();
      }

      Ad_Hoc_Team aht = new Ad_Hoc_Team(email, name);
      LOGGER.info("Persisting: " + aht);
      manager.persist(aht);
      LOGGER.info("Persisted object after flush (non-null id): " + aht);
   }

   /**
    * Adds an Writing_Group object to the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void addWritingGroup(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter writing group name: ");
      String name = in.nextLine();
      System.out.print("Enter writing group email: ");
      String email = in.nextLine();
      while(! Authoring_Entity.checkEmail(manager, email)) {
         System.out.print("The email entered already exists, please try again: ");
         email = in.nextLine();
      }
      System.out.print("Enter head writer name: ");
      String head_writer = in.nextLine();
      System.out.print("Enter year formed: ");
      Integer year_formed = in.nextInt();

      Writing_Group wg = new Writing_Group(email, name, head_writer, year_formed);
      LOGGER.info("Persisting: " + wg);
      manager.persist(wg);
      LOGGER.info("Persisted object after flush (non-null id): " + wg);
   }

   /**
    * Adds an Individual_Author object to an Ad_Hoc_Team object.
    * @param manager The Entity Manager of the Database.
    */
   public static void addIndividualAuthorToAdHocTeam(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter the email of the Individual Author you wish to add: ");
      String ia_email = in.nextLine();
      while(Authoring_Entity.checkEmail(manager, ia_email)) {
         System.out.print("That Individual Author does not exist, please try again: ");
         ia_email = in.nextLine();
      }
      System.out.print("Enter the email of the Ad Hoc Team you wish to add to: ");
      String aht_email = in.nextLine();
      while(Authoring_Entity.checkEmail(manager, aht_email)) {
         System.out.print("That Ad Hoc Team does not exist, please try again: ");
         aht_email = in.nextLine();
      }
      Individual_Author ia = (Individual_Author) Authoring_Entity.selectAuthoringEntity(manager, ia_email);
      Ad_Hoc_Team aht = (Ad_Hoc_Team) Authoring_Entity.selectAuthoringEntity(manager, aht_email);

      aht.addToIndividualAuthors(ia);

      System.out.println(ia.getName() + " was added to " + aht.getName());
   }

   /**
    * Lists a certain Writing Group from the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void listWritingGroup(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter the email of the writing group you wish to see: ");
      String email = in.nextLine();
      while(Authoring_Entity.checkEmail(manager, email)) {
         System.out.print("That writing group does not exist, please try again: ");
         email = in.nextLine();
      }
      Writing_Group wg = (Writing_Group) Authoring_Entity.selectAuthoringEntity(manager, email);
      System.out.println("\nThe information of " + wg.getName() + ":");
      System.out.println(wg);
   }

   /**
    * Deletes an Authoring_Entity from the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void deleteAuthoringEntity(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter the email of the authoring entity you wish to delete: ");
      String email = in.nextLine();
      while(Authoring_Entity.checkEmail(manager, email)) {
         System.out.print("That publisher does not exist, please try again: ");
         email = in.nextLine();
      }
      Authoring_Entity delete = Authoring_Entity.selectAuthoringEntity(manager, email);
      manager.remove(delete);
   }

   /**
    * Adds a Book object to the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void addBook(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter ISBN: ");
      String ISBN = in.nextLine();
      System.out.print("Enter title: ");
      String title = in.nextLine();

      System.out.print("Enter the name of the publisher: ");
      String name = in.nextLine();
      while(Publisher.checkName(manager, name)) {
         System.out.print("That publisher does not exist, please try again: ");
         name = in.nextLine();
      }
      Publisher p = Publisher.selectPublisher(manager, name);

      System.out.print("Enter the email of the authoring entity: ");
      String email = in.nextLine();
      while(Authoring_Entity.checkEmail(manager, email)) {
         System.out.print("That authoring entity does not exist, please try again: ");
         email = in.nextLine();
      }
      Authoring_Entity ae = Authoring_Entity.selectAuthoringEntity(manager, email);

      System.out.print("Enter year published: ");
      Integer year_published = in.nextInt();

      Book b = new Book(ISBN, title, year_published, p, ae);
      LOGGER.info("Persisting: " + b);
      manager.persist(b);
      LOGGER.info("Persisted object after flush (non-null id): " + b);

      System.out.println("Book " + b.getTitle() + " has been added to the Database.");
   }

   /**
    * Lists a certain Book from the Database
    * @param manager The Entity Manager of the Database.
    */
   public static void listBook(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter the ISBN of the book you wish to see: ");
      String ISBN = in.nextLine();
      while(Book.checkISBN(manager, ISBN)) {
         System.out.print("That book does not exist, please try again: ");
         ISBN = in.nextLine();
      }
      Book b = Book.selectBook(manager, ISBN);
      Publisher p = b.getPublisher();
      Authoring_Entity ae = b.getAuthoringEntity();
      System.out.println("\nThe information of " + b.getTitle() + ":");
      System.out.println(b + "\n" + p + "\n" + ae);
   }

   /**
    * Deletes a Book object from the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void deleteBook(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter the ISBN of the book you wish to delete: ");
      String ISBN = in.nextLine();
      while(Book.checkISBN(manager, ISBN)) {
         System.out.print("That book does not exist, please try again: ");
         ISBN = in.nextLine();
      }
      Book delete = Book.selectBook(manager, ISBN);
      manager.remove(delete);
      System.out.println(delete.getTitle() + " has been removed from the Database.");
   }

   /**
    * Updates a Book object's Authoring_Entity.
    * @param manager The Entity Manager of the Database.
    */
   public static void updateBook(EntityManager manager) {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter the ISBN of the book you wish to update: ");
      String ISBN = in.nextLine();
      while(Book.checkISBN(manager, ISBN)) {
         System.out.print("That book does not exist, please try again: ");
         ISBN = in.nextLine();
      }

      Book update = Book.selectBook(manager, ISBN);
      Authoring_Entity old = update.getAuthoringEntity();
      System.out.print("Enter the email of the new authoring entity: ");
      String email = in.nextLine();
      while(Authoring_Entity.checkEmail(manager, email)) {
         System.out.print("That authoring entity does not exist, please try again: ");
         email = in.nextLine();
      }
      Authoring_Entity ae = Authoring_Entity.selectAuthoringEntity(manager, email);
      update.setAuthoringEntity(ae);
      manager.merge(update);
      System.out.println(update.getTitle() + " has been updated.\n" + old.getName() + " has been replaced with " + ae.getName());
   }

   /**
    * Lists all the Primary Keys as well as the titles of the Book objects from the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void listBookPK(EntityManager manager) {
      List<String> s = Book.bookPK(manager);
      for(int i = 0; i < s.size(); i++) {
         System.out.println(s.get(i));
      }
   }

   /**
    * Lists all the Primary Keys as well as the data types of Authoring_Entity objects from the Database.
    * @param manager The Entity Manager of the Database.
    */
   public static void listAuthoringEntityPK(EntityManager manager) {
      List<String> s = Authoring_Entity.authoringEntityPK(manager);
      for(int i = 0; i < s.size(); i++) {
         System.out.println(s.get(i));
      }
   }
}
