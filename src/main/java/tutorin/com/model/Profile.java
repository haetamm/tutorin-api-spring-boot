package tutorin.com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorin.com.constant.TableName;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableName.T_PROFILE)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "postcode", length = 20)
    private String postcode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
}
