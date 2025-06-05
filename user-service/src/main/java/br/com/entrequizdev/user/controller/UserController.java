package br.com.entrequizdev.user.controller;


import br.com.entrequizdev.user.dto.ChangeUser;
import br.com.entrequizdev.user.dto.CreateUserDto;
import br.com.entrequizdev.user.dto.LoginUserDto;
import br.com.entrequizdev.user.dto.RecoveryJwtTokenDto;
import br.com.entrequizdev.user.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDto loginUserDto) {
        RecoveryJwtTokenDto token = usuarioService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto createUserDto) {
        usuarioService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //Todos tem acesso
    @GetMapping("/test")
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    //somente o usuario tem acesso
    @GetMapping("/test/customer")
    public ResponseEntity<String> getCustomerAuthenticationTest() {
        return new ResponseEntity<>("Cliente autenticado com sucesso", HttpStatus.OK);
    }

    //somente o adm tem acesso
    @GetMapping("/test/administrator")
    public ResponseEntity<String> getAdminAuthenticationTest() {
        return new ResponseEntity<>("Administrador autenticado com sucesso", HttpStatus.OK);
    }

    //mudar todos os dados do usuario
    @PatchMapping("/changeUser/{email}")
    public ResponseEntity<?> mudarDadosUser(@PathVariable("email") String email, @RequestBody ChangeUser changeUser) {
        usuarioService.mudarDadosUsuario(email, changeUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }


//    @GetMapping("/users/me")
//    public ResponseEntity<LoginUserDto> getCurrentUser(@RequestParam String token) {
//        usuarioService.loginUser(token);
//        return new ResponseEntity<>(Htt)
//    }



}