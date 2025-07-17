package vn.ducjava.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import util.UserStatus;
import vn.ducjava.configuration.Translator;
import vn.ducjava.dto.request.UserRequestDTO;
import vn.ducjava.dto.response.ResponseData;
import vn.ducjava.dto.response.ResponseError;
import vn.ducjava.dto.response.UserDetailResponse;
import vn.ducjava.service.UserService;

@RestController
@RequestMapping("/user") // để controller nhận được các request thì cần phải add annotation này
@Validated
@Slf4j
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Add user", description = "API create new user")
    @PostMapping(value = "/") // thêm mới user, cần gửi đi 1 request body nên tạo thêm 1 request body
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO userDTO) {
        System.out.println("User added successfully=" + userDTO.getFirstName() + " " + userDTO.getLastName());
        try {
            long userId = userService.saveUser(userDTO);
            return new ResponseData<Long>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), userId);
        } catch (Exception e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }
    }

    @Operation(summary = "Update user", description = "API update user")
    @PutMapping("/{userId}") // update toàn bộ 1 user thông qua userId
    public ResponseData<?> updateUser(@PathVariable @Min(1) long userId, @Valid @RequestBody UserRequestDTO userDTO) {
        log.info("Request update userId={}", userId);
        try {
            userService.updateUser(userId, userDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated successfully ");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Change status user", description = "API change status user")
    @PatchMapping("/{userId}")
    public ResponseData<?> changeStatus(@PathVariable @Min(1) long userId, @RequestParam(required = false) UserStatus status) {
        log.info("Request update userId={}", userId);
        try {
            userService.changeStatus(userId, status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated successfully ");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");
        }
    }

    @Operation(summary = "Delete user", description = "API delete user")
    @DeleteMapping("/{userId}") // xóa 1 user thong qua userId
    public ResponseData<?> deleteUser(@PathVariable @Min(1) long userId) {
        log.info("Request delete userId={}", userId);
        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User deleted successfully ");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user fail");
        }
    }

    @Operation(summary = "Get user details", description = "API get user details")
    @GetMapping("/{userId}")
    public ResponseData<UserDetailResponse> getUser(@PathVariable @Min(value = 1) int userId) {
        log.info("Request get user detail, userId={}", userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get user by id", userService.getUser(userId));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get list user per page", description = "Return user by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAlluser(
            @RequestParam(defaultValue = "0") int pageNo,
            @Min(10) @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false)  Sort.Direction direction) {
        log.info("Request get user list");
        return new ResponseData<>(HttpStatus.OK.value(), "Get list users", userService.getAllUsers(pageNo, pageSize, sortBy, direction));
    }

    @GetMapping("/list-without-sort")
    public ResponseData<?> getListuser(@RequestParam(defaultValue = "0") int pageNo,
                                       @RequestParam(defaultValue = "20") int pageSize) {
        log.info("Request get user list");
        return new ResponseData<>(HttpStatus.OK.value(), "Get list users without direction", userService.getAllUsers(pageNo, pageSize));
    }

    @Operation(summary = "Get list user per page", description = "Return user by pageNo and pageSize")
    @GetMapping("/list-with-search")
    public ResponseData<?> getAllUsersWithSearch(
            @RequestParam(defaultValue = "0") int pageNo,
            @Min(10) @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false)  Sort.Direction direction,
            @RequestParam(required = false) String search) {
        log.info("Request get user list and search");
        return new ResponseData<>(HttpStatus.OK.value(), "Get list users", userService.getAllUsersWithSearch(pageNo, pageSize, sortBy, direction, search));
    }

    @Operation(summary = "Get list user per page with criteria search", description = "Return user by pageNo and pageSize")
    @GetMapping("/list-with-search-by-criteria")
    public ResponseData<?> advanceSearchByCriteria(
            @RequestParam(defaultValue = "0") int pageNo,
            @Min(10) @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String... search) {
        log.info("Request advance search user list and search by criteria");
        return new ResponseData<>(HttpStatus.OK.value(), "Get list users", userService.advanceSearchByCriteria(pageNo, pageSize, sortBy, search));
    }
}
