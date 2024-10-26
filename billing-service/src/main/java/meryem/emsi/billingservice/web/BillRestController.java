package meryem.emsi.billingservice.web;

import meryem.emsi.billingservice.Service.CustomerRestClient;
import meryem.emsi.billingservice.Service.ProductRestClient;
import meryem.emsi.billingservice.entities.Bill;
import meryem.emsi.billingservice.repository.BillRepository;
import meryem.emsi.billingservice.repository.ProductItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestController {
    private BillRepository billRepository;
    private ProductItemRepository productItemRepository;
    private CustomerRestClient customerRestClient;
    private ProductRestClient productRestClient;

    public BillRestController(ProductItemRepository productItemRepository,BillRepository billRepository,
                              CustomerRestClient customerRestClient,
                              ProductRestClient productRestClient) {
        this.productItemRepository = productItemRepository;
        this.productRestClient=productRestClient;
        this.customerRestClient=customerRestClient;
        this.billRepository=billRepository;
    }
    @GetMapping("/fullBill/{id}")
    public Bill bill(@PathVariable Long id){
        Bill bill=billRepository.findById(id)
                .orElse(null);
        bill.setCustomer(customerRestClient.findCustomerById(bill.getCustomerId()));
        bill.getProductItems().forEach(pi->{
            pi.setProduct(productRestClient.findProductById(pi.getProductId()));
        });
        return bill;
    }
}
