package nl.novi.breedsoft.controller;

import jakarta.validation.Valid;
import nl.novi.breedsoft.dto.authorityDtos.AuthorityInputDto;
import nl.novi.breedsoft.dto.authorityDtos.AuthorityOutputDto;
import nl.novi.breedsoft.service.AuthorityService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;
import static nl.novi.breedsoft.utility.UriResponseUtility.createUri;

@RestController
@RequestMapping("authorities")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    //Because authorities do not contain a lot of information we only need to get, create and delete them

    /**
     * GET method to request a list of all authorities
     * @return ResponseEntity with OK http status code and a list with all authorities
     */
    @GetMapping("")
    public ResponseEntity<Iterable<AuthorityOutputDto>> getAllAuthorities(){
        return ResponseEntity.ok(authorityService.getAllAuthorities());
    }

    /**
     * POST method to create a new authority
     * @param authorityInputDtoDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with created http status code and URI pointing to the newly created entity
     * or bindingResultError if there is an error in the binding
     */
    @PostMapping("")
    public ResponseEntity<Object> createAuthority(
            @Valid @RequestBody AuthorityInputDto authorityInputDtoDto,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return bindingResultError(bindingResult);
        }else{
            Long createdId = authorityService.createAuthority(authorityInputDtoDto);
            URI uri = createUri(createdId, "/authorities/");
            return ResponseEntity.created(uri).body("Authority is successfully created!");
        }
    }

    /**
     * DELETE method to delete an authority based on authorityId
     * @param authorityId The id of the authority to be deleted
     * @return ResponseEntity with no content http status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAuthority(@PathVariable("id") Long authorityId){
        authorityService.deleteAuthority(authorityId);
        return ResponseEntity.noContent().build();
    }
}
