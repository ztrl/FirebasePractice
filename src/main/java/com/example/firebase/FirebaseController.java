package com.example.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequestMapping("/firebase")
@RequiredArgsConstructor
public class FirebaseController {
    private final FirebaseAuth firebaseAuth;

    @ExceptionHandler(FirebaseAuthException.class)
    public ResponseEntity<String> error(FirebaseAuthException exception) {
        log.error(exception.getAuthErrorCode().name());
        log.error(exception.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(exception.getMessage());
    }

    @PostMapping("/login")
    public ResponseEntity<UserRecord> signIn(String id) throws FirebaseAuthException {
        log.info("signIn id: " + id);
        UserRecord signInWithId = firebaseAuth.getUser(id);
        log.info("signInWithId.getUid(): " + signInWithId.getUid());
        log.info("signInWithId.isEmailVerified(): " + signInWithId.isEmailVerified());
        return ResponseEntity
                .ok()
                .body(signInWithId);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserRecord> create(String id, String password) throws FirebaseAuthException {
        log.info("create id: " + id);
        log.info("create password: " + password);
        UserRecord createdUser = firebaseAuth.createUser(new UserRecord.CreateRequest().setUid(id).setPassword(password));
        log.info("createdUser.getUid(): " + createdUser.getUid());
        return ResponseEntity
                .ok()
                .body(createdUser);
    }

    @PatchMapping
    public ResponseEntity<UserRecord> update(String id, String email, String phoneNumber) throws FirebaseAuthException {
        log.info("update id: " + id);
        UserRecord updatedUser = firebaseAuth.updateUser(
                new UserRecord.UpdateRequest(id)
                        .setEmail(email)
                        .setPhoneNumber(phoneNumber));
        return ResponseEntity
                .ok()
                .body(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(String id) throws FirebaseAuthException {
        log.info("delete id: " + id);
        firebaseAuth.deleteUser(id);
        HttpStatus status = HttpStatus.OK;
        UserRecord user = null;

        try {
            user = firebaseAuth.getUser(id);
            status = HttpStatus.BAD_REQUEST;
        } catch (FirebaseAuthException e) {
            log.error(e.getAuthErrorCode().name());
        }

        return ResponseEntity
                .status(status)
                .body("user " + id + " deleted");
    }
}
