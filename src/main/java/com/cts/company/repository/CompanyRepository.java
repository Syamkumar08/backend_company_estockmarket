package com.cts.company.repository;

import com.cts.company.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("select c from Company c where c.companyCode=:companyCode")
    Company findByCompanyCode(@Param("companyCode") String companyCode);
}
