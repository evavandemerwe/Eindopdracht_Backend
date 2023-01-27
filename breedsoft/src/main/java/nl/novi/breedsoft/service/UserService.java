package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.userDtos.UserInputDto;
import nl.novi.breedsoft.dto.userDtos.UserOutputDto;
import nl.novi.breedsoft.dto.userDtos.UserPatchDto;
import nl.novi.breedsoft.exception.AuthorityInUseException;
import nl.novi.breedsoft.exception.DuplicateNotAllowedException;
import nl.novi.breedsoft.exception.PasswordComplexityException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.authority.Authority;
import nl.novi.breedsoft.model.authority.User;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.repository.AuthorityRepository;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder encoder;
    private final PersonRepository personRepository;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder encoder,
                       PersonRepository personRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
        this.personRepository = personRepository;
    }

    /**
     * A method for retrieval of all users from the database
     * @return a list of all users in output dto format
     */
    public List<UserOutputDto> getAllUsers(){
        List<User> userList = userRepository.findAll();
        return transferToUserOutputDtoList(userList);
    }

    /**
     * A method to create a new user in the database
     * @param userInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return the ID of the user created in the database
     * @throws DuplicateNotAllowedException throws an exception when the username already exist in the database
     * @throws RecordNotFoundException throws an exception when given authority is not found in the database
     */
    public Long createUser(UserInputDto userInputDto){

        Optional<User> isUserPresent = userRepository.findByUsername(userInputDto.getUsername());
        if(isUserPresent.isPresent()){
            throw new DuplicateNotAllowedException("Username already exists. Duplicates are not allowed.");
        }

        User newUser = new User();
        newUser.setUsername(userInputDto.getUsername());

        newUser.setPassword(encoder.encode(userInputDto.getPassword()));

        List<Authority> userAuthorities = new ArrayList<>();
        for (Authority authority : userInputDto.getAuthorities()) {
            Optional<Authority> authorityFound = authorityRepository.findById(authority.getId());
            if(authorityFound.isEmpty()){
                throw new RecordNotFoundException("Authority " + authority.getId() + " is not found");
            }
            userAuthorities.add(authorityFound.get());
        }
        newUser.setAuthorities(userAuthorities);

        userRepository.save(newUser);

        return newUser.getId();
    }

    /**
     * A method (PATCH) will only update an existing object,
     * with the properties mapped in the request body (that are not null).
     * We do NOT update veterinarian appointment and medical data here.
     * @param userId ID of the user for which an update is requested
     * @param userPatchDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return the updated user in output dto format
     */
    public UserOutputDto patchUser(Long userId, UserPatchDto userPatchDto){
        Optional<User> userFound = userRepository.findById(userId);

        if(userFound.isPresent()){
            User updatedUser = userRepository.getReferenceById(userId);
            if(userPatchDto.getUsername() != null){
                updatedUser.setUsername(userPatchDto.getUsername());
            }
            if(userPatchDto.getPassword() != null){
                String passwordComplexityRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,20}$";
                if (userPatchDto.getPassword().matches(passwordComplexityRegex)){
                    updatedUser.setPassword(userPatchDto.getUsername());
                }else{
                    throw new PasswordComplexityException("Password must contain at least one digit, one uppercase character, one lowercase character, a special character and must be between 8 and character long.");
                }
            }
            if(userPatchDto.getAuthorities() != null){
                List<Authority> authorities = userPatchDto.getAuthorities();
                List<Authority> foundAuthorities = new ArrayList<>();
                for(Authority authority: authorities){
                    if(authority.getId() == 0){
                        throw new RecordNotFoundException("Missing authority ID.");
                    }
                    Optional<Authority> optionalAuthority = authorityRepository.findById(authority.getId());
                    if(optionalAuthority.isPresent()) {
                        foundAuthorities.add(optionalAuthority.get());
                    } else {
                      throw new RecordNotFoundException("Given authority is not found.");
                    }
                }
                updatedUser.setAuthorities(foundAuthorities);
            }
            userRepository.save(updatedUser);
            return transferToUserOutputDto(updatedUser);
        } else {
            throw new RecordNotFoundException("User is not found.");
        }
    }

    /**
     * A method for deleting a user from the database by id
     * @param userId ID of the user for which deletion is requested
     * @throws RecordNotFoundException throws an exception when no user with given id is found in the database
     */
    public void deleteUser(Long userId){
        if(userRepository.findById(userId).isPresent()){
            try {
                //Delete user from person
                Person person = personRepository.findByUserId(userId);
                if(person!= null) {
                    person.setUser(null);
                    personRepository.save(person);
                }

                //Delete user
                userRepository.deleteById(userId);
            }catch(Exception ex){
                throw new AuthorityInUseException("This user is still in use.");
            }
        } else {
            throw new RecordNotFoundException("No user with given id found");
        }
    }

    //DTO helper classes
    /**
     * A method to transform a list with users to a list of users in output dto format
     * @param userList list of users to be transformed
     * @return a list of users in output dto format
     */
    private List<UserOutputDto> transferToUserOutputDtoList(List<User> userList){
        List<UserOutputDto> userOutputDtoList = new ArrayList<>();
        for(User user : userList){
            UserOutputDto outputDto = transferToUserOutputDto(user);
            userOutputDtoList.add(outputDto);
        }
        return userOutputDtoList;
    }

    /**
     * A method to transform a user to a user in output dto format
     * @param user user to be transformed
     * @return a user in output dto format
     */
    private UserOutputDto transferToUserOutputDto(User user){
        UserOutputDto userOutputDto = new UserOutputDto();
        userOutputDto.setId(user.getId());
        userOutputDto.setUsername(user.getUsername());
        userOutputDto.setAuthorities(user.getAuthorities());

        return  userOutputDto;
    }


}
