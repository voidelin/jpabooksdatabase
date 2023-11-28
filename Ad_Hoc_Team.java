package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A group of authors who come together for a single specific
 * purpose of writing a book, and will later disband.
 */

@Entity
public class Ad_Hoc_Team extends Authoring_Entity {
    @ManyToMany
    @JoinTable(
            name = "ad_hoc_teams_member",
            joinColumns = @JoinColumn(name = "ad_hoc_team_authoring_entity_email"),
            inverseJoinColumns = @JoinColumn(name = "individual_author_authoring_entity_email"))
    private List<Individual_Author> individual_authors = new ArrayList<>();

    /**
     * Ad Hoc Team Constructor
     * @param email stores the email of the Ad Hoc Team
     * @param name stores the name of the Ad Hoc Team
     */
    public Ad_Hoc_Team(String email, String name) {
        super(email, name);
    }

    /**
     * Default Constructor
     */
    public Ad_Hoc_Team() { }

    // Getters and Setters

    public List<Individual_Author> getIndividualAuthors() { return individual_authors; }

    public void setIndividualAuthors(List<Individual_Author> individual_authors) { this.individual_authors = individual_authors; }

    /**
     * Assigns Individual Authors to the Ad Hoc Team.
     * @param individual_author The Individual Author being assigned.
     */
    public void addToIndividualAuthors(Individual_Author individual_author) {
        this.individual_authors.add(individual_author);
    }

    /**
     * The string representation of the Ad Hoc Team
     * @return string representation of the Ad Hoc Team
     */
    @Override
    public String toString () {
        return "Ad Hoc Team - Name: " + this.getName() + " Email: " + this.getEmail();
    }
}
