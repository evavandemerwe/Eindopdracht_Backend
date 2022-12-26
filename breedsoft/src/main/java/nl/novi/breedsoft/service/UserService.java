package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.UserInputDto;
import nl.novi.breedsoft.dto.UserOutputDto;
import nl.novi.breedsoft.exception.DuplicateNotAllowedException;
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
        List<User> userList =userRepository.findAll();
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

    private User transferToUser(UserInputDto userInputDto){
        User user = new User();
        user.setUsername(userInputDto.getUsername());
        user.setPassword(userInputDto.getPassword());

        if(userInputDto.getAuthorities() != null) {
            user.setAuthorities(userInputDto.getAuthorities());
        }

        return user;
    }

    private List<UserOutputDto> transferToUserOutputDtoList(List<User> users){
        List<UserOutputDto> userDtoList = new ArrayList<>();
        for(User user : users){
            UserOutputDto outputDto = transferToUserOutputDto(user);
            userDtoList.add(outputDto);
        }
        return userDtoList;
    }
    private UserOutputDto transferToUserOutputDto(User user){
        UserOutputDto userOutputDto = new UserOutputDto();
        userOutputDto.setUsername(user.getUsername());
        userOutputDto.setAuthorities(user.getAuthorities());

        return  userOutputDto;
    }

    private Authority getCompleteAuthorityID(Long authorityId){
        if(authorityId== 0){
            throw new RecordNotFoundException("Missing authority ID");
        }
        Optional<Authority> authority = authorityRepository.findById(authorityId);
        return(authority.isPresent()) ? authority.get() : null;
    }
}
