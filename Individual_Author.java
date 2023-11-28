package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
/**
 * A single person who completed a book all on their own with no outside help.
 */

@Entity
public class Individual_Author extends Authoring_Entity {
    @ManyToMany(mappedBy = "individual_authors")
    private List<Ad_Hoc_Team> ad_hoc_teams = new ArrayList<>();
    /**
     * Individual Author Constructor
     * @param email stores the email of the Individual Author
     * @param name stores the name of the Individual Author
     */
    public Individual_Author(String email, String name) {
        super(email, name);
    }

    /**
     * Default constructor for an Individual Author class
     */
    public Individual_Author() { }

    /* Getter and Setter */

    public List<Ad_Hoc_Team> getAdHocTeams() { return ad_hoc_teams;}

    public void setAdHocTeams(List<Ad_Hoc_Team> ad_hoc_teams) { this.ad_hoc_teams = ad_hoc_teams; }

    /**
     * The string representation of the Individual Author
     * @return string representation of the Individual Author
     */
    @Override
    public String toString () {
        return "Individual Author - Name: " + this.getName() + " Email: " + this.getEmail();
    }
}
