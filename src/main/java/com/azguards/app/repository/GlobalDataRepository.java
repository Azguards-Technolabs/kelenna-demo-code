package com.azguards.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.GlobalData;

@Repository
public interface GlobalDataRepository extends JpaRepository<GlobalData, String> {

}
