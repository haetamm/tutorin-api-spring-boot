package tutorin.com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorin.com.constant.Status;
import tutorin.com.constant.TableName;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableName.T_JOB_APPLICATION)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobId", referencedColumnName = "id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutorId", referencedColumnName = "id")
    private User tutor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

}
