package task.manager.user_service.data.mapper.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import task.manager.user_service.data.dto.request.UserRequestDto;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.entity.User;
import task.manager.user_service.data.enums.UserRole;
import task.manager.user_service.data.mapper.UserMapper;

import java.time.Instant;
import java.util.Set;

@Component
public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User toEntity(UserRequestDto requestDto) {
        User user = delegate.toEntity(requestDto);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setRoles(Set.of(UserRole.USER));

        return user;
    }

    @Override
    public User toUpdatedEntity(UserUpdateDto updateDto, User user) {
        user.setUpdatedAt(Instant.now());

        return user;
    }
}