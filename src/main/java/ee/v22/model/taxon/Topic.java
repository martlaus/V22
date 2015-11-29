package ee.v22.model.taxon;

import static javax.persistence.FetchType.EAGER;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DiscriminatorValue("TOPIC")
public class Topic extends Taxon {

    @OneToMany(fetch = EAGER, mappedBy = "topic")
    private Set<Subtopic> subtopics;

    @ManyToOne
    @JoinColumn(name = "subject")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "domain")
    private Domain domain;

    @ManyToOne
    @JoinColumn(name = "module")
    private Module module;

    public Set<Subtopic> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(Set<Subtopic> subtopics) {
        this.subtopics = subtopics;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @JsonIgnore
    @Override
    public Taxon getParent() {
        return subject != null ? subject : domain;
    }
}
