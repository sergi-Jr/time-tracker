package sergi.example.track;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@IdClass(CompositeTrackKey.class)
@NoArgsConstructor
public class Track {

    @Id
    private long userId;

    @Id
    private long taskId;

    private long duration;
}
