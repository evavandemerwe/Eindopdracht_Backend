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

    public List<AuthorityOutputDto> getAllAuthorities(){
        List<Authority> authorityList = authorityRepository.findAll();
        return transferToAuthorityOutputDtoList(authorityList);
    }

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

    // You are not allowed to delete an authority that is in use.
    // Trying to do so will result in 'AuthorityInUseException'
    public void deleteAuthority(Long id){
        if(authorityRepository.findById(id).isPresent()){
            try {
                authorityRepository.deleteById(id);
            }catch(Exception ex){
                throw new AuthorityInUseException("This authority is still in use. Update your user first before deleting this authority.");
            }
        } else {
            throw new RecordNotFoundException("No authority with given id found");
        }
    }
    private List<AuthorityOutputDto>transferToAuthorityOutputDtoList(List<Authority> authorities){
        List<AuthorityOutputDto> authorityOutputDtoList = new ArrayList<>();
        for(Authority authority : authorities){
            AuthorityOutputDto outputDto = transferToAuthorityOutputDto(authority);
            authorityOutputDtoList.add(outputDto);
        }
        return authorityOutputDtoList;
    }


    private AuthorityOutputDto transferToAuthorityOutputDto(Authority authority){
        AuthorityOutputDto authorityOutputDto = new AuthorityOutputDto();
        authorityOutputDto.setId(authority.getId());
        authorityOutputDto.setAuthority(authority.getAuthority());

        return authorityOutputDto;
    }
}
