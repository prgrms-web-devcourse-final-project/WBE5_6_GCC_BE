package com.honlife.core.app.model.member.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.MemberImage;


public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
}
