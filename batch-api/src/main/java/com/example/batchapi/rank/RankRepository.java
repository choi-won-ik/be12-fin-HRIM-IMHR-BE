package com.example.batchapi.rank;

import com.example.batchapi.rank.model.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Long> {
}
