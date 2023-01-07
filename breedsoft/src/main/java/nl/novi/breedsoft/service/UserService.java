package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.userDtos.UserInputDto;
import nl.novi.breedsoft.dto.userDtos.UserOutputDto;
import nl.novi.breedsoft.dto.userDtos.UserPatchDto;
import nl.novi.breedsoft.exception.DuplicateNotAllowedException;
import nl.novi.breedsoft.exception.PasswordComplexityException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.authority.Authority;
import nl.novi.breedsoft.model.authority.User;
import nl.novi.breedsoft.repository.AuthorityRepository;
import nl.novi.breedsoft.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
    }

    public List<UserOutputDto> getAllUsers(){
        List<User> userList = userRepository.findAll();
        return transferToUserOutputDtoList(userList);
    }

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

    public void deleteUser(Long id){
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No user with given id found");
        }
    }

    //PATCH will only update an existing object,
    //with the properties mapped in the request body (that are not null).
    public UserOutputDto patchUser(Long id, UserPatchDto userPatchDto){
        Optional<User> userFound = userRepository.findById(id);

        if(userFound.isPresent()){
            User updatedUser = userRepository.getReferenceById(id);
            if(userPatchDto.getUsername() != null){
                updatedUser.setUsername(userPatchDto.getUsername());
            }
            if(userPatchDto.getPassword() != null){
                String passwordComplexityRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
                if (userPatchDto.getPassword().matches(passwordComplexityRegex)){
                    updatedUser.setPassword(userPatchDto.getUsername());
                }else{
                    throw new PasswordComplexityException("Password must contain at least one digit, one uppercase character, one lowercase character, a special character and must be between 8 and character long.");
                }
            }
            if(userPatchDto.getAuthorities() != null){
                List<Authority> authorities = userPatchDto.getAuthorities();
                List<Authority> foundAuthorities = new ArrayList<Authority>();
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

    private List<UserOutputDto> transferToUserOutputDtoList(List<User> users){
        List<UserOutputDto> userOutputDtoList = new ArrayList<>();
        for(User user : users){
            UserOutputDto outputDto = transferToUserOutputDto(user);
            userOutputDtoList.add(outputDto);
        }
        return userOutputDtoList;
    }

    private UserOutputDto transferToUserOutputDto(User user){
        UserOutputDto userOutputDto = new UserOutputDto();
        userOutputDto.setId(user.getId());
        userOutputDto.setUsername(user.getUsername());
        userOutputDto.setAuthorities(user.getAuthorities());

        return  userOutputDto;
    }

    private Authority getCompleteAuthorityID(Long authorityId){
        if(authorityId == 0){
            throw new RecordNotFoundException("Missing authority ID.");
        }
        Optional<Authority> authority = authorityRepository.findById(authorityId);
        return(authority.isPresent()) ? authority.get() : null;
    }
}
