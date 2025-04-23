package co.spribe.controller;

import co.spribe.controller.dto.PageDto;
import co.spribe.controller.dto.UserDto;
import co.spribe.mapper.PageMapper;
import co.spribe.mapper.UserMapper;
import co.spribe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final PageMapper pageMapper;
  private final UserMapper userMapper;

  @GetMapping
  public PageDto<UserDto> getUsersPaginated(
      @PageableDefault(sort = "costWithFee", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<UserDto> unitDtoPage = userService.findAllPageable(pageable)
        .map(userMapper::toDto);
    return pageMapper.toDto(unitDtoPage);
  }

}
