package sergi.example.track.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sergi.example.model.HourMinute;
import sergi.example.track.CompositeTrackKey;
import sergi.example.track.Track;

import java.time.LocalDateTime;
import java.util.List;

public interface TrackRepository extends JpaRepository<Track, CompositeTrackKey> {

    @Query("""
            select tr from Track tr
            join Task t on tr.taskId = t.id
            where tr.userId = :userId
            and t.startedAt between :start and :end
            and t.finishedAt between :start and :end
            order by tr.duration
            """)
    List<Track> findTrackStatsByUserIdAndDateBetween(long userId, LocalDateTime start, LocalDateTime end);

    @Query("""
            select new sergi.example.model.HourMinute(SUM(tr.duration))
            from Track tr
            join Task t on tr.taskId = t.id
            where tr.userId = :userId
            and t.startedAt between :start and :end
            and t.finishedAt between :start and :end
            """)
    HourMinute getAllTimeSpentByUserIdAndDateBetween(long userId, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("""
            delete from Track tr
            where tr.userId = :userId
            """)
    void deleteAllByUserId(long userId);

    @Query("""
            select tr from Track tr
            where tr.userId = :userId
            """)
    List<Track> findAllByUserId(long userId);

    @Modifying
    @Query("""
            update Track tr set tr.duration =
            (SELECT DATEDIFF(minute, t.startedAt, t.finishedAt) as diff
            from Task t
            where t.id = tr.taskId)
            where tr.duration = 0
            """)
    void setDuration();
}
