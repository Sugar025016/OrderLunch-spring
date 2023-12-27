package com.order_lunch.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;

import com.order_lunch.entity.Shop;

@Repository
public interface IShopRepository extends JpaRepository<Shop, Integer> {

	Shop getShopById(Integer id);

	// @Query("SELECT s FROM Shop s " +
	// "LEFT JOIN s.category c " +
	// "LEFT JOIN s.tabs t " +
	// "LEFT JOIN t.products p " +
	// "WHERE s.isDelete = false " +
	// "AND s.isDisable = false " +
	// "AND (:city IS NULL OR s.address.city = :city) " +
	// "AND (:area IS NULL OR s.address.area = :area) " +
	// "AND (:categoryId IS NULL OR c.id = :categoryId)" +
	// "AND (:other IS NULL OR c.name like %:other% OR p.name like %:other% OR
	// s.name like %:other%)")
	// Set<Shop> findByAddress_CityAndAddress_AreaAndCategory_IdAndCategory_name(
	// @Param("city") String city,
	// @Param("area") String area,
	// @Param("categoryId") Integer categoryId,
	// @Param("other") String other);

	@Query("SELECT s FROM Shop s " +
			"LEFT JOIN s.category c " +
			"LEFT JOIN s.address a " +
			"LEFT JOIN s.tabs t " +
			"LEFT JOIN t.products p " +
			"WHERE s.isDelete = false " +
			"AND s.isDisable = false " +
			"AND (:city IS NULL OR a.addressData.city = :city) " +
			"AND (:area IS NULL OR a.addressData.area = :area) " +
			"AND (:categoryId IS NULL OR c.id = :categoryId)" +
			"AND (:other IS NULL OR c.name like %:other% OR p.name like %:other% OR s.name like %:other%)")
	Set<Shop> findByAddress_CityAndAddress_AreaAndCategory_IdAndCategory_name(
			@Param("city") String city,
			@Param("area") String area,
			@Param("categoryId") Integer categoryId,
			@Param("other") String other);

	// @Query(value = "SELECT NEW com.order_lunch.model.response.BackstageShopResponse(s) FROM Shop s " +
	// 		"LEFT JOIN s.category c " +
	// 		"LEFT JOIN s.address a " +
	// 		"LEFT JOIN s.tabs t " +
	// 		"LEFT JOIN t.products p " +
	// 		"WHERE (:city IS NULL OR a.addressData.city = :city) " +
	// 		"AND (:area IS NULL OR a.addressData.area = :area) " +
	// 		"AND (:categoryId IS NULL OR c.id = :categoryId)" +
	// 		"AND (:other IS NULL OR c.name like %:other% OR p.name like %:other% OR s.name like %:other%)" +
	// 		"AND (s.isDelete = false)" +
	// 		"group by s.id", countQuery = "SELECT count(s) FROM Shop s " +
	// 				"LEFT JOIN s.category c " +
	// 				"LEFT JOIN s.address a " +
	// 				"LEFT JOIN s.tabs t " +
	// 				"LEFT JOIN t.products p " +
	// 				"WHERE (:city IS NULL OR a.addressData.city = :city) " +
	// 				"AND (:area IS NULL OR a.addressData.area = :area) " +
	// 				"AND (:categoryId IS NULL OR c.id = :categoryId)" +
	// 				"AND (:other IS NULL OR c.name like %:other% OR p.name like %:other% OR s.name like %:other%)" +
	// 				"AND (s.isDelete = false)" +
	// 				"group by s.id")
	// Page<BackstageShopResponse> findByAddress_CityAndAddress_AreaAndCategory_IdAndCategory_name(
	// 		@Param("city") String city,
	// 		@Param("area") String area,
	// 		@Param("categoryId") Integer categoryId,
	// 		@Param("other") String other,
	// 		Pageable pageable);



	@Query(value = "SELECT s FROM Shop s " +
			"LEFT JOIN s.category c " +
			"LEFT JOIN s.address a " +
			"LEFT JOIN s.tabs t " +
			"LEFT JOIN t.products p " +
			"WHERE (:city IS NULL OR a.addressData.city = :city) " +
			"AND (:area IS NULL OR a.addressData.area = :area) " +
			"AND (:categoryId IS NULL OR c.id = :categoryId)" +
			"AND (:other IS NULL OR c.name like %:other% OR p.name like %:other% OR s.name like %:other%)" +
			"AND (s.isDelete = false)" +
			"group by s.id", countQuery = "SELECT count(s) FROM Shop s " +
					"LEFT JOIN s.category c " +
					"LEFT JOIN s.address a " +
					"LEFT JOIN s.tabs t " +
					"LEFT JOIN t.products p " +
					"WHERE (:city IS NULL OR a.addressData.city = :city) " +
					"AND (:area IS NULL OR a.addressData.area = :area) " +
					"AND (:categoryId IS NULL OR c.id = :categoryId)" +
					"AND (:other IS NULL OR c.name like %:other% OR p.name like %:other% OR s.name like %:other%)" +
					"AND (s.isDelete = false)" +
					"group by s.id")
	Page<Shop> findByAddress_CityAndAddress_AreaAndCategory_IdAndCategory_name(
			@Param("city") String city,
			@Param("area") String area,
			@Param("categoryId") Integer categoryId,
			@Param("other") String other,
			Pageable pageable);

			

	Optional<Shop> findByIdAndIsDeleteIsFalse(int id);

	// Set<Shop> findAllByLove_Id(int userId);

	List<Shop> findFirst6ByNameLikeAndIsDeleteIsFalse(String name);

	List<Shop> getShopsByUserId(int id);

	Optional<Shop> getShopsByIdAndUserId(int id, int userId);

	Optional<Shop> findByIdAndLovesId(int shopId, int userId);
}