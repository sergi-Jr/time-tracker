package sergi.example.task;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sergi.example.model.BaseEntity;
import sergi.example.task.dto.TaskStatsDTO;
import sergi.example.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NamedNativeQuery(
        name = "stats_native_query_dto",
        query = """
                select t.id as id, DATEDIFF(minute, t.started_at, t.finished_at) as diff
                from tasks as t
                where t.user_id = :userId
                and t.started_at between :start and :end
                and t.finished_at between :start and :end
                order by diff desc;
                """,
        resultSetMapping = "stats_query_dto"
)
@SqlResultSetMapping(
        name = "stats_query_dto",
        classes = @ConstructorResult(
                targetClass = TaskStatsDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = Long.class),
                    @ColumnResult(name = "diff", type = Integer.class),
                }
        )
)
public class Task implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
