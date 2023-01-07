package nl.novi.breedsoft.controller;

import jakarta.validation.Valid;
import nl.novi.breedsoft.dto.authorityDtos.AuthorityInputDto;
import nl.novi.breedsoft.dto.authorityDtos.AuthorityOutputDto;
import nl.novi.breedsoft.service.AuthorityService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;

@RestController
@RequestMapping("authorities")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    //Because authorities do not contain a lot of information we only need to get, create and delete them

    @GetMapping("")
    public ResponseEntity<Iterable<AuthorityOutputDto>> getAllAuthorities(){
        return ResponseEntity.ok(authorityService.getAllAuthorities());
    }

    @PostMapping("")
    public ResponseEntity<Object> createAuthority(@Valid @RequestBody AuthorityInputDto authorityInputDtoDto, BindingResult br){
        if(br.hasErrors()){
            return bindingResultError(br);
        }else{
            Long createdId = authorityService.createAuthority(authorityInputDtoDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/authorities/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Authority is successfully created!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAuthority(@PathVariable("id") Long id){
        authorityService.deleteAuthority(id);
        return ResponseEntity.noContent().build();
    }
}
