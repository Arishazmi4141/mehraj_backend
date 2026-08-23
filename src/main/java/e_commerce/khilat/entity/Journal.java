package e_commerce.khilat.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "journals")
@Where(clause = "deleted = false")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "dt_of_ops")
    private Long dtOfOps;

    @Column(name = "deleted")
    private boolean deleted = false;

    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    @BatchSize(size = 20)
    private List<JournalImage> journalImages = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getDtOfOps() { return dtOfOps; }
    public void setDtOfOps(Long dtOfOps) { this.dtOfOps = dtOfOps; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public List<JournalImage> getJournalImages() { return journalImages; }
    public void setJournalImages(List<JournalImage> journalImages) { this.journalImages = journalImages; }
}