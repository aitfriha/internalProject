package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.VoucherType;

@Repository
public interface VoucherTypeRepository extends MongoRepository<VoucherType, String> {
    VoucherType findVoucherTypeByCode(String code);
    VoucherType findVoucherTypeByName(String name);
    VoucherType findVoucherTypeBy_id(String id);
}
