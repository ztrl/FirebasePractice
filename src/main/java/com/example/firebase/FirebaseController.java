package com.example.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequestMapping("/firebase")
@RequiredArgsConstructor
public class FirebaseController {
    private final FirebaseAuth firebaseAuth;

    @GetMapping
    public ResponseEntity<UserRecord> signIn(String id) throws FirebaseAuthException {
        log.info("id: " + id);
        UserRecord signInWithId = firebaseAuth.getUser(id);
        log.info("signInWithId.getUid(): " + signInWithId.getUid());
        log.info("signInWithId.isEmailVerified(): " + signInWithId.isEmailVerified());
        return ResponseEntity
                .ok()
                .body(signInWithId);
    }

    @PostMapping
    public ResponseEntity<UserRecord> create(String id, String password) throws FirebaseAuthException {
        log.info("id: " + id);
        log.info("password: " + password);
        UserRecord createdUser = firebaseAuth.createUser(new UserRecord.CreateRequest().setUid(id).setPassword(password));
        log.info("createdUser.getUid(): " + createdUser.getUid());
        return ResponseEntity
                .ok()
                .body(createdUser);
    }
}
