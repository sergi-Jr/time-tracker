package sergi.example.user.dal;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import sergi.example.mapper.JsonNullableMapper;
import sergi.example.mapper.ReferenceMapper;
import sergi.example.user.User;
import sergi.example.user.dto.UserCreateDTO;
import sergi.example.user.dto.UserDTO;
import sergi.example.user.dto.UserUpdateDTO;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    public abstract User map(UserCreateDTO dto);

    public abstract UserDTO map(User model);

    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);
}
