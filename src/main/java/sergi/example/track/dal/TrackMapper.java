package sergi.example.track.dal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import sergi.example.mapper.JsonNullableMapper;
import sergi.example.mapper.ReferenceMapper;
import sergi.example.model.HourMinute;
import sergi.example.track.Track;
import sergi.example.track.dto.TrackDTO;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TrackMapper {

    @Mapping(target = "timeSpent", expression = "java(toTimeRepresentation(model.getDuration()))")
    public abstract TrackDTO map(Track model);

    protected HourMinute toTimeRepresentation(long duration) {
        return new HourMinute(duration);
    }
}
