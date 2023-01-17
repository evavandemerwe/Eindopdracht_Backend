package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.authorityDtos.AuthorityInputDto;
import nl.novi.breedsoft.dto.authorityDtos.AuthorityOutputDto;
import nl.novi.breedsoft.exception.AuthorityInUseException;
import nl.novi.breedsoft.exception.DuplicateNotAllowedException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.authority.Authority;
import nl.novi.breedsoft.repository.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    /**
     * A method to get all authorities
     * @return a list of authorities in output dto format
     */
    public List<AuthorityOutputDto> getAllAuthorities(){
        List<Authority> authorityList = authorityRepository.findAll();
        return transferToAuthorityOutputDtoList(authorityList);
    }

    /**
     * A method to create a new authority in the database
     * @param authorityInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return the id of the newly created authority
     * @throws DuplicateNotAllowedException throws an exception when the authority already exists
     */
    public Long createAuthority(AuthorityInputDto authorityInputDto){

        Optional<Authority> isAuthorityPresent = authorityRepository.findByAuthority(authorityInputDto.getAuthority());
        if(isAuthorityPresent.isPresent()){
            throw new DuplicateNotAllowedException("Authority already exists. Duplicates are not allowed.");
        }
        Authority newAuthority = new Authority();
        newAuthority.setAuthority(authorityInputDto.getAuthority());

        authorityRepository.save(newAuthority);

        return newAuthority.getId();
    }

    /**
     * A method to delete an authority by authority id
     * You are not allowed to delete an authority that is in use.
     * Trying to do so will result in 'AuthorityInUseException'
     * @param authorityId ID of the authority for which information is requested
     * @throws AuthorityInUseException throws an exception when the authority is still in use
     * @throws RecordNotFoundException throws an exception when a record is not found by id
     */
    public void deleteAuthority(Long authorityId){
        if(authorityRepository.findById(authorityId).isPresent()){
            try {
                authorityRepository.deleteById(authorityId);
            }catch(Exception ex){
                throw new AuthorityInUseException("This authority is still in use. Update your user first before deleting this authority.");
            }
        } else {
            throw new RecordNotFoundException("No authority with given id found");
        }
    }

    //DTO Helper classes
    /**
     * A method to transform a list of authorities to a list of authorities in output dto format
     * @param authorities list with authorities that has to be transformed
     * @return a list of authorities in output dto format
     */
    private List<AuthorityOutputDto>transferToAuthorityOutputDtoList(List<Authority> authorities){
        List<AuthorityOutputDto> authorityOutputDtoList = new ArrayList<>();
        for(Authority authority : authorities){
            AuthorityOutputDto outputDto = transferToAuthorityOutputDto(authority);
            authorityOutputDtoList.add(outputDto);
        }
        return authorityOutputDtoList;
    }

    /**
     * A method that transform an authority to an authority in output dto format
     * @param authority authority that has to be transformed
     * @return an authority in output dto format
     */
    private AuthorityOutputDto transferToAuthorityOutputDto(Authority authority){
        AuthorityOutputDto authorityOutputDto = new AuthorityOutputDto();
        authorityOutputDto.setId(authority.getId());
        authorityOutputDto.setAuthority(authority.getAuthority());

        return authorityOutputDto;
    }
}
