package nl.novi.breedsoft.controller;

import jakarta.validation.Valid;
import nl.novi.breedsoft.dto.userDtos.UserInputDto;
import nl.novi.breedsoft.dto.userDtos.UserOutputDto;
import nl.novi.breedsoft.dto.userDtos.UserPatchDto;
import nl.novi.breedsoft.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;
import static nl.novi.breedsoft.utility.createUriResponse.createUri;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET method to get a list of all users from the database
     * @return ResponseEntity with OK http status code and a list with all users
     */
    @GetMapping("")
    public ResponseEntity<Iterable<UserOutputDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * POST method to create a new user in the database
     * @param userInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with created http status code and URI pointing to the newly created entity,
     * or bindingResultError if there is an error in the binding
     */
    @PostMapping("")
    public ResponseEntity<Object> createUser(
            @Valid @RequestBody UserInputDto userInputDto,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if(bindingResult.hasErrors()){
            return bindingResultError(bindingResult);
        }else{
            Long createdId = userService.createUser(userInputDto);
            URI uri = createUri(createdId, "/users/");
            return ResponseEntity.created(uri).body("User is successfully created!");
        }
    }

    /**
     * PATCH method that updates a user only when the user exists in the database
     * @param userId ID of the user for which information is requested
     * @param userPatchDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with OK http status code and the updated user,
     * or bindingResultError if there is an error in the binding
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchUser(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UserPatchDto userPatchDto,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            UserOutputDto userOutputDto = userService.patchUser(userId, userPatchDto);
            return ResponseEntity.ok().body(userOutputDto);
        }
    }

    /**
     * DELETE method to delete a user from the database by id
     * @param userId ID of the person for which information is requested
     * @return ResponseEntity with no content http status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
