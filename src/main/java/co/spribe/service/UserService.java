package co.spribe.service;

import co.spribe.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  Optional<User> findById(Long userId);

  Page<User> findAllPageable(Pageable pageable);
}
