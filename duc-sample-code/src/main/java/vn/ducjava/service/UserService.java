package vn.ducjava.service;

import org.springframework.data.domain.Sort;
import util.UserStatus;
import vn.ducjava.dto.request.UserRequestDTO;
import vn.ducjava.dto.response.PageResponse;
import vn.ducjava.dto.response.UserDetailResponse;
import vn.ducjava.exception.ResourceNotFoundException;

public interface UserService {
    long saveUser(UserRequestDTO request);

    void updateUser(long userId, UserRequestDTO request) throws ResourceNotFoundException;

    void changeStatus(long userId, UserStatus status) throws ResourceNotFoundException;

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId) throws ResourceNotFoundException;

    PageResponse<?> getAllUsers(int pageNo, int pageSize, String sortBy, Sort.Direction direction);

    PageResponse<?> getAllUsers(int pageNo, int pageSize);

    PageResponse<?> getAllUsersWithSearch(int pageNo, int pageSize, String sortBy,Sort.Direction direction, String search);

    PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String... search);
}
