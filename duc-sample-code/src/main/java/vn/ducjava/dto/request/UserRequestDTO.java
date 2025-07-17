package vn.ducjava.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import util.*;
import validator.EnumValue;
import validator.GenderSubset;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static util.Gender.*;

public class UserRequestDTO implements Serializable {
    @NotBlank(message = "firstName must be mot blank")
    private String firstName;

    @NotNull(message = "lastName must be not null" )
    private String lastName;

//    @Pattern(regexp = "^\\d{10}$", message = "phone invalid format")
    @PhoneNumber
    private String phone;

    @Email(message = "email invalid format")
    private String email;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dateOfBirth;

    @NotEmpty(message = "permission must be not empty")
    private List<String> permission;

    @NotNull(message = "username must be not null")
    private String username;

    @NotNull(message = "password must be not null")
    private String password;

    @EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|NONE$")
    private UserStatus status;

    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    private Gender gender;

    @NotNull(message = "type must be not null")
    @EnumValue(name = "type", enumClass = UserType.class)
    private String type;

    @NotEmpty(message = "addresses can not empty")
    private Set<AddressDTO> addresses;


    // constructor
    public UserRequestDTO(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;

    }

    // getter
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Date getDate() {
        return dateOfBirth;
    }

    public String getType() {
        return type;
    }

    public Gender getGender() {
        return gender;
    }

    public Set<AddressDTO> getAddresses() {
        return addresses;
    }

    public List<String> getPermission() {
        return permission;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserStatus getStatus() {
        return status;
    }

}
