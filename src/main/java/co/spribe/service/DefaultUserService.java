package co.spribe.service;

import co.spribe.model.User;
import co.spribe.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultUserService implements UserService {

  private final UserRepository userRepository;

  @Override
  public Optional<User> findById(Long userId) {
    return userRepository.findById(userId);
  }

  @Override
  public Page<User> findAllPageable(Pageable pageable) {
    return userRepository.findAll(pageable);
  }
}
