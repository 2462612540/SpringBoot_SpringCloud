package com.tensquare.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.base.pojo.City;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface CityDao extends JpaRepository<City,String>,JpaSpecificationExecutor<City>{
	
}
