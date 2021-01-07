package dk.lundogbendsen.sprinbootcourse.persistence.education.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditedEntity {
    @Id
    @GeneratedValue
    private Long id;

    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date updatedDate;
}
