package org.example.server.repository;

import org.example.server.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: astidhiyaa
 * @date: 02/05/25
 */
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
}
