package sergi.example.task.dal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import sergi.example.mapper.JsonNullableMapper;
import sergi.example.mapper.ReferenceMapper;
import sergi.example.task.Task;
import sergi.example.task.dto.TaskCreateDTO;
import sergi.example.task.dto.TaskDTO;
import sergi.example.task.dto.TaskUpdateDTO;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "user.id", target = "userId")
    public abstract TaskDTO map(Task model);

    @Mapping(source = "userId", target = "user")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);
}
