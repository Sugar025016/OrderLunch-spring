package com.order_lunch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.order_lunch.entity.Schedule;


@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {

	@Modifying
	@Query("DELETE FROM Schedule AS s WHERE s.shop.id = :shopId AND s.type = :type AND s.week in :weeks" )
	void deleteByShopIdAndTypeAndWeek(@Param("shopId") int shopId,@Param("type") int type,@Param("weeks") List<Integer> weeks);
	
}