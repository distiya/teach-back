package distiya.github.com.customerprofilereactive.repository;

import distiya.github.com.customerprofilereactive.entity.TblRetail;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface TblRetailRepository extends ReactiveCrudRepository<TblRetail,String> {

    @Query("SELECT * FROM tbl_retail WHERE registered_name_lower LIKE CONCAT(:registeredName,'%') ORDER BY cash_depository_number ASC LIMIT 500")
    Flux<TblRetail> findByRegisteredName(String registeredName);

    @Query("SELECT * FROM tbl_retail WHERE cmcp_id = :cmcpId")
    Mono<TblRetail> findByCmcpId(String cmcpId);

    @Query("DELETE FROM tbl_retail WHERE cmcp_id = :cmcpId")
    Mono<Void> deleteByCmcpId(String cmcpId);
}
