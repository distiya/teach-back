package distiya.github.com.customerprofilereactive.controller;

import distiya.github.com.customerprofilereactive.dto.RetailProfile;
import distiya.github.com.customerprofilereactive.filter.CustomerProfileFilter;
import distiya.github.com.customerprofilereactive.filter.CustomerProfileIdFilter;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class CustomerProfileController {

    private final RSocketRequester rSocketRequester;

    CustomerProfileController(RSocketRequester rSocketRequester){
        this.rSocketRequester = rSocketRequester;
    }

    @PostMapping(path = "/retail/enquiry",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RetailProfile> getCustomersByRegisteredName(@RequestBody CustomerProfileFilter customerProfileFilter){
        return rSocketRequester
                .route("retail.enquiry")
                .data(customerProfileFilter)
                .retrieveFlux(RetailProfile.class);
    }

    @PostMapping(path = "/retail/enquiry_single",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RetailProfile> getCustomerByCmcpId(@RequestBody CustomerProfileIdFilter customerProfileIdFilter){
        return rSocketRequester
                .route("retail.enquiry.single")
                .data(customerProfileIdFilter)
                .retrieveMono(RetailProfile.class);
    }

    @PostMapping(path = "/retail/delete",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> deleteCustomerByCmcpId(@RequestBody CustomerProfileIdFilter customerProfileIdFilter){
        return rSocketRequester
                .route("retail.delete.single")
                .data(customerProfileIdFilter)
                .retrieveMono(Void.class);
    }

    @PostMapping(path = "/retail/save",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RetailProfile> saveAllCustomers(@RequestBody List<RetailProfile> retailProfiles){
        return rSocketRequester
                .route("retail.save.all")
                .data(Flux.fromIterable(retailProfiles))
                .retrieveFlux(RetailProfile.class);
    }

    @PostMapping(path = "/retail/backpressure",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RetailProfile> getCustomersByRegisteredNameWithBackPressure(@RequestBody CustomerProfileFilter customerProfileFilter){
        return rSocketRequester
                .route("retail.enquiry")
                .data(customerProfileFilter)
                .retrieveFlux(RetailProfile.class)
                .doOnSubscribe(s->s.request(5));
    }

    @PostMapping(path = "/retail/internal_subscribe",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> getCustomersByRegisteredNameWithBackPressureSubscribe(@RequestBody CustomerProfileFilter customerProfileFilter){
        List<String> elements = new ArrayList<>();
        rSocketRequester
                .route("retail.enquiry")
                .data(customerProfileFilter)
                .retrieveFlux(RetailProfile.class)
                .subscribe(new Subscriber<RetailProfile>() {

                    private Subscription s;
                    int onNextAmount;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.s = subscription;
                        this.s.request(2);
                    }

                    @Override
                    public void onNext(RetailProfile retailProfile) {
                        System.out.println(retailProfile.getCmcpId());
                        elements.add(retailProfile.getCmcpId());
                        onNextAmount++;
                        if (onNextAmount % 2 == 0) {
                            try {
                                TimeUnit.SECONDS.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            this.s.request(2);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("CMCP ID List Size After Complete : " + elements.size());
                    }
                });
        System.out.println("CMCP ID List Size Before Returning Mono Void : " + elements.size());
        return Mono.empty().then();
    }
}
