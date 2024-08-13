package tutorin.com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorin.com.constant.Gender;
import tutorin.com.constant.TableName;

import java.sql.Date;
import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableName.T_JOB)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", referencedColumnName = "id")
    private User student;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "subject", length = 100)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "education", length = 50)
    private String education;

    @Column(name = "deadline")
    private Date deadline;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "salary", length = 100)
    private String salary;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updatedAt")
    private Timestamp updatedAt;

    // Constructors, equals, and hashCode methods

}
