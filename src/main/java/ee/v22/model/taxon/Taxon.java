package ee.v22.model.taxon;

import static javax.persistence.InheritanceType.JOINED;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@Entity
@DiscriminatorColumn(name = "level")
@Inheritance(strategy = JOINED)
@JsonIdentityInfo(generator = JSOGGenerator.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "level")
public abstract class Taxon {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, insertable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public abstract Taxon getParent();

    public boolean containsTaxon(Taxon taxon) {
        if(this.equals(taxon)) {
            return true;
        } else if(getParent() != null) {
            return getParent().containsTaxon(taxon);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 37) //
                .append(name) //
                .append(getClass()) //
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Taxon)) {
            return false;
        }

        // Validate @UniqueConstraint(columnNames = { "name", "level" })
        if (getClass() != obj.getClass()) {
            return false;
        }

        Taxon other = (Taxon) obj;

        return new EqualsBuilder().append(name, other.name) //
                .isEquals();
    }
}
