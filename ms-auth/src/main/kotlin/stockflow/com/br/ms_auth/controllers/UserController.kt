package stockflow.com.br.ms_auth.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import stockflow.com.br.ms_auth.dtos.RegisterUserDTO
import stockflow.com.br.ms_auth.dtos.UserDTO
import stockflow.com.br.ms_auth.dtos.toModel
import stockflow.com.br.ms_auth.models.toDTO
import stockflow.com.br.ms_auth.services.IUserService

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: IUserService) {
    @PostMapping
    fun register(@RequestBody user: RegisterUserDTO): ResponseEntity<UserDTO> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.save(user.toModel()).toDTO())
    }
}