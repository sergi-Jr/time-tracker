package sergi.example.user.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sergi.example.exception.ResourceNotFoundException;
import sergi.example.user.User;
import sergi.example.user.dto.UserCreateDTO;
import sergi.example.user.dto.UserDTO;
import sergi.example.user.dto.UserUpdateDTO;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDTO create(UserCreateDTO data) {
        User model = userMapper.map(data);
        userRepository.save(model);
        return userMapper.map(model);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO data) {
        User model = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found, id: " + id));
        userMapper.update(data, model);
        userRepository.save(model);
        return userMapper.map(model);
    }
}
