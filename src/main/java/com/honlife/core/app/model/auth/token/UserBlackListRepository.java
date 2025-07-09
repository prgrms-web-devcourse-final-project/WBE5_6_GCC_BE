package com.honlife.core.app.model.auth.token;

import org.springframework.data.repository.CrudRepository;
import com.honlife.core.app.model.auth.token.entity.UserBlackList;

public interface UserBlackListRepository extends CrudRepository<UserBlackList, String> {
    void deleteByEmail(String email);
}
