package com.sprintgether.script.management.server.scriptmanagement.form.user;

import com.sprintgether.script.management.server.scriptmanagement.form.EnumActionARealiser;
import com.sprintgether.script.management.server.scriptmanagement.model.user.EnumStaffType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class StaffForm {
    /*****
     * Through a form on a staff we may want to
     * SAVE, UPDATE or DELETE a record
     * And depending on this paramater that must be always hidden to the user, some
     * other parameter can be null or not
     */
    EnumActionARealiser actionARealiser;
    /***
     * This parameter can only be null if and only if we want to SAVE a record
     * If not it must be initialized with the id of the record that would be affected by the request.
     */
    String idConcernedStaff;
    @NotNull(message = "The staff name cannot be null")
    @NotBlank(message = "The staff name cannot be blank caracter")
    @NotEmpty(message = "The staff name cannot be empty")
    @Size(min=2, max=50, message="The staff name must have at least 2 caracters and at most 50")
    String firstName;
    String lastName;
    @NotNull(message = "The staff type cannot be null")
    String staffType;
    @Email(message = "Check the email enter and ensure that the email structure is respected")
    String email;
    String phoneNumber;
    String address;
    String description;
    /****
     * Field associated with User Model
     */
    @NotNull(message = "The staff username cannot be null")
    @NotBlank(message = "The staff username cannot be blank caracter")
    @NotEmpty(message = "The staff username cannot be empty")
    @Size(min=2, max=50, message="The staff username must have at least 2 caracters and at most 50")
    String username;
    @NotNull(message = "The staff password cannot be null")
    @NotBlank(message = "The staff password cannot be blank caracter")
    @NotEmpty(message = "The staff password cannot be empty")
    @Size(min=2, max=50, message="The staff password must have at least 2 caracters and at most 50")
    String password;

}
