package com.bnext.api.controllers.impl;

import com.bnext.api.controllers.UserController;
import com.bnext.api.dtos.ContactRequestDto;
import com.bnext.api.dtos.ContactResponseDto;
import com.bnext.api.dtos.UserRequestDto;
import com.bnext.api.dtos.UserResponseDto;
import com.bnext.api.exceptions.InvalidDataException;
import com.bnext.api.exceptions.ResourceNotFoundException;
import com.bnext.api.services.UserService;
import com.bnext.api.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Clase donde se implementan todos los endpoints relacionados con usuarios y sus contactos.
 */
@RestController
@RequestMapping(CommonConstants.PATH_USERS)
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint que obtiene todos los usuarios
     * @return ResponseEntity <List<UserResponseDto>> Listado de todos los usuarios existentes.
     */
    @Override
    @GetMapping()
    public ResponseEntity <List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    /**
     * Endpoint que crea un nuevo usuario.
     * @param userRequestDto Objeto con los datos del nuevo usuario a crear.
     * @return ResponseEntity <UserResponseDto> El nuevo usuario creado
     */
    @PostMapping()
    public ResponseEntity <UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequestDto));
    }

    /**
     * Endpoint para obtener un usuario a través de su id
     * @param userId Id del usuario a buscar
     * @return ResponseEntity <UserResponseDto> Usuario encontrado.
     * @throws ResourceNotFoundException Si el id de usuario no existe.
     */
    @GetMapping(CommonConstants.PATH_VARIABLE_ID)
    public ResponseEntity <UserResponseDto> getUserById(@PathVariable(value = CommonConstants.ID) Long userId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok().body(userService.findByUserId(userId));
    }

    /**
     * Endpoint que actualiza un usuario
     * @param userId Id del usuario a actualizar.
     * @param userRequestDto Objeto con los nuevos datos del usuario a actualizar.
     * @return ResponseEntity <UserResponseDto> Usuario con los nuevos datos actualizados.
     * @throws ResourceNotFoundException Si el id de usuario no existe.
     */
    @PutMapping(CommonConstants.PATH_VARIABLE_ID)
    public ResponseEntity <UserResponseDto> updateUser(@PathVariable(value = CommonConstants.ID) Long userId,
                                                      @Valid @RequestBody UserRequestDto userRequestDto)
            throws ResourceNotFoundException {
        return ResponseEntity.ok().body(userService.saveUser(userId, userRequestDto));
    }

    /**
     * Endpoint que elimina primero los contactos de un usuario y después el usuario.
     * @param userId Id del usuario a eliminar
     * @return ResponseEntity Texto indicando si el borrado ha sido correcto.
     * @throws ResourceNotFoundException Si el id de usuario no existe.
     */
    @DeleteMapping(CommonConstants.PATH_VARIABLE_ID)
    public ResponseEntity deleteUser(@PathVariable(value = CommonConstants.ID) Long userId)
            throws ResourceNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.ok().body(CommonConstants.DELETED_USER_MESSAGE);
    }

    /**
     * Endpoint que actualiza los contactos de un usuario. Primero elimina los contactos existentes
     * y después inserta las nuevos contactos.
     * @param userId Id del usuario a actualizar sus contactos
     * @param contactsRequestDto Listado de nuevos contactos a actualizar.
     * @return ResponseEntity <List<ContactResponseDto>> Listado de los nuevos contactos del usuario
     * @throws ResourceNotFoundException Si el id de usuario no existe.
     * @throws InvalidDataException Si alguno de los número de teléfono proporcionados no es válido.
     */
    @PostMapping(CommonConstants.PATH_VARIABLE_ID + CommonConstants.PATH_CONTACTS)
    public ResponseEntity <List<ContactResponseDto>> saveContacts(@PathVariable(value = CommonConstants.ID) Long userId,
                                                                  @Valid @RequestBody List<ContactRequestDto> contactsRequestDto)
            throws ResourceNotFoundException, InvalidDataException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUserContacts(userId, contactsRequestDto));
    }

    /**
     * Endpoint que obtiene los contactos de un usuario.
     * @param userId Id del usuario para obtener sus contactos.
     * @return ResponseEntity <List<ContactResponseDto>> Listado de contactos del usuario.
     * @throws ResourceNotFoundException Si el id de usuario no existe.
     */
    @GetMapping(CommonConstants.PATH_VARIABLE_ID + CommonConstants.PATH_CONTACTS)
    public ResponseEntity <List<ContactResponseDto>> findContacts(@PathVariable(value = CommonConstants.ID) Long userId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.findContactsByUserId(userId));
    }

    /**
     * Enpoint que obtiene los contactos comunes entre dos usuarios.
     * @param userId1 Id de un usuario para obtener sus contactos.
     * @param userId2 Id de otro usuario para obtener sus contactos.
     * @return ResponseEntity<List<ContactResponseDto>> Listado de contactos comunes entre los dos usuarios.
     * @throws ResourceNotFoundException Si alguno de los ids de usuario no existe.
     */
    @GetMapping(CommonConstants.PATH_CONTACTS)
    public ResponseEntity<List<ContactResponseDto>> getCommonContacts(
            @RequestParam Long userId1, @RequestParam Long userId2)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getCommonsContacts(userId1, userId2));
    }
}