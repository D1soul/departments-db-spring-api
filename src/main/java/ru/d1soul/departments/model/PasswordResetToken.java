package ru.d1soul.departments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.d1soul.departments.model.User;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "password_reset_token")
public class PasswordResetToken implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String  token;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "users_id", referencedColumnName = "id")
    private User user;

    public void setExpiryDate(int minutes){
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, minutes);
        this.expiryDate = now.getTime();
    }

    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }

}
