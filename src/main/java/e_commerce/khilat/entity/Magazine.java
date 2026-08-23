package e_commerce.khilat.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "magazines")
@Where(clause = "deleted = false")
public class Magazine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "year_of_publish")
    private Integer year;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "dt_of_ops")
    private Long dtOfOps;

    @Column(name = "deleted")
    private boolean deleted = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getPdfUrl() { return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getDtOfOps() { return dtOfOps; }
    public void setDtOfOps(Long dtOfOps) { this.dtOfOps = dtOfOps; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}