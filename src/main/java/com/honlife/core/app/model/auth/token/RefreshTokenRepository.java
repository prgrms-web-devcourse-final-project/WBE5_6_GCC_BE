package com.honlife.core.app.model.auth.token;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.honlife.core.app.model.auth.token.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByAtId(String atId);
    void deleteByAtId(String atId);
}
