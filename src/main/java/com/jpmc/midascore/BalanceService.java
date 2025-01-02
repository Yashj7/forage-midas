package com.jpmc.midascore;

import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class BalanceService {

    private final UserRepository userRepository;

    public BalanceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/balance")
    public ResponseEntity<Balance> getUserBalance(@RequestParam("userId") Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        BigDecimal balanceAmount = (user != null) ? BigDecimal.valueOf(user.getBalance()) : BigDecimal.ZERO;

        Balance balance = new Balance(balanceAmount.floatValue());
        return ResponseEntity.ok(balance);
    }
}