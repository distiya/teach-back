package distiya.github.com.customerprofilereactive.controller;

import distiya.github.com.customerprofilereactive.dto.RetailProfile;
import distiya.github.com.customerprofilereactive.entity.TblRetail;
import distiya.github.com.customerprofilereactive.filter.CustomerProfileFilter;
import distiya.github.com.customerprofilereactive.filter.CustomerProfileIdFilter;
import distiya.github.com.customerprofilereactive.repository.TblRetailRepository;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
@Timed
public class CustomerProfileController {

    @Autowired
    private TblRetailRepository tblRetailRepository;

    @MessageMapping("retail.enquiry.single")
    Mono<RetailProfile> requestResponse(CustomerProfileIdFilter customerProfileIdFilter) {
        return tblRetailRepository.findByCmcpId(customerProfileIdFilter.getCmcpId())
                .map(this::convertToRetailProfile);
    }

    @MessageMapping("retail.enquiry")
    Flux<RetailProfile> requestStream(CustomerProfileFilter customerProfileFilter) {
       return tblRetailRepository.findByRegisteredName(customerProfileFilter.getRegisteredName().toLowerCase())
               .map(this::convertToRetailProfile);
    }

    @MessageMapping("retail.save.all")
    Flux<RetailProfile> requestChannel(Flux<RetailProfile> retailProfileFlux) {
        return tblRetailRepository.saveAll(retailProfileFlux.map(this::convertToRetailEntity)).map(this::convertToRetailProfile);
    }

    @MessageMapping("retail.delete.single")
    Mono<Void> fireForget(CustomerProfileIdFilter customerProfileIdFilter) {
        return tblRetailRepository.deleteByCmcpId(customerProfileIdFilter.getCmcpId());
    }

    private RetailProfile convertToRetailProfile(TblRetail tblRetail){
        log.info("Calling in reactive to convert from TblRetail to RetailProfile");
        RetailProfile retailProfile = new RetailProfile();
        retailProfile.setAlias(tblRetail.getAlias());
        retailProfile.setCashDepositoryNumber(tblRetail.getCashDepositoryNumber());
        retailProfile.setRegisteredName(tblRetail.getRegisteredName());
        retailProfile.setNameLine1(tblRetail.getNameLine1());
        retailProfile.setNameLine2(tblRetail.getNameLine2());
        retailProfile.setCmcpId(tblRetail.getCmcpId());
        retailProfile.setDateOfBirth(tblRetail.getDateOfBirth());
        retailProfile.setFamilyName(tblRetail.getFamilyName());
        return retailProfile;
    }

    private TblRetail convertToRetailEntity(RetailProfile retailProfile){
        log.info("Calling in reactive to convert from TblRetail to RetailProfile");
        TblRetail tblRetail = new TblRetail();
        tblRetail.setAlias(retailProfile.getAlias());
        tblRetail.setCashDepositoryNumber(retailProfile.getCashDepositoryNumber());
        tblRetail.setRegisteredName(retailProfile.getRegisteredName());
        tblRetail.setNameLine1(retailProfile.getNameLine1());
        tblRetail.setNameLine2(retailProfile.getNameLine2());
        tblRetail.setCmcpId(retailProfile.getCmcpId());
        tblRetail.setDateOfBirth(retailProfile.getDateOfBirth());
        tblRetail.setFamilyName(retailProfile.getFamilyName());
        return tblRetail;
    }
}
