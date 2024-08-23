package tutorin.com.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import tutorin.com.model.JobApplication;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findAllByTutorId(String tutorId);
    List<JobApplication> findAllByJobId(String jobId);

    @Query(value = "SELECT * FROM t_job_application WHERE job_id = :jobId AND tutor_id = :tutorId", nativeQuery = true)
    Optional<JobApplication> findByJobIdAndTutorId(@Param("jobId") String jobId, @Param("tutorId") String tutorId);

}


