package com.azguards.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azguards.app.bean.Level;

public interface LevelRepository extends JpaRepository<Level, String> {
	List<Level> findAllByOrderBySequenceNoAsc();
}
