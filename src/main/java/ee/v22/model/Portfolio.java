package ee.v22.model;

import static javax.persistence.FetchType.EAGER;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ee.v22.model.taxon.Taxon;
import ee.v22.rest.jackson.map.DateTimeDeserializer;
import ee.v22.rest.jackson.map.DateTimeSerializer;

@Entity
public class Portfolio implements Searchable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime created;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updated;

    @ManyToOne
    @JoinColumn(name = "taxon")
    private Taxon taxon;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private User creator;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false)
    private Long views = (long) 0;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "portfolio")
    @OrderColumn(name = "chapterOrder", nullable = false)
    private List<Chapter> chapters;

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "portfolio")
    @OrderBy("added DESC")
    private List<Comment> comments;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "Portfolio_Tag",
            joinColumns = { @JoinColumn(name = "portfolio") },
            inverseJoinColumns = { @JoinColumn(name = "tag") },
            uniqueConstraints = @UniqueConstraint(columnNames = { "portfolio", "tag" }))
    private List<Tag> tags;

    @Lob
    @JsonIgnore
    private byte[] picture;

    @Formula("picture is not null")
    private boolean hasPicture;

    @Enumerated(EnumType.STRING)
    @Column(name = "targetGroup")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Portfolio_TargetGroup", joinColumns = @JoinColumn(name = "portfolio") )
    private List<TargetGroup> targetGroups;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public DateTime getCreated() {
        return created;
    }

    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setCreated(DateTime created) {
        this.created = created;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public DateTime getUpdated() {
        return updated;
    }

    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }

    public Taxon getTaxon() {
        return taxon;
    }

    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public boolean getHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public List<TargetGroup> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(List<TargetGroup> targetGroups) {
        this.targetGroups = targetGroups;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
