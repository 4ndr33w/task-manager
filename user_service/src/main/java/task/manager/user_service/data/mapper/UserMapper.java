package task.manager.user_service.data.mapper;

import org.mapstruct.*;
import org.mapstruct.MappingConstants.ComponentModel;
import task.manager.user_service.data.dto.request.UserRequestDto;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;
import task.manager.user_service.data.entity.User;

@Mapper(
        componentModel = ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    User toEntity(UserRequestDto requestDto);
    UserResponseDto toResponse(User user);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toUpdatedEntity(UserUpdateDto updateDto, @MappingTarget User user);
}