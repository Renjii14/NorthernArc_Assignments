package org.northernarc.productrental.serviceimpl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.northernarc.productrental.dto.DashboardMetricsDTO;
import org.northernarc.productrental.exception.RentalRecordNotFoundException;
import org.northernarc.productrental.model.RentalRecord;
import org.northernarc.productrental.repository.CustomerRepository;
import org.northernarc.productrental.repository.ProductRepository;
import org.northernarc.productrental.repository.RentPaymentRepository;
import org.northernarc.productrental.repository.RentalRecordRepository;
import org.northernarc.productrental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RentPaymentRepository rentPaymentRepository;
    @Autowired
    private final RentalRecordRepository rentalRecordRepository;

    @Override
    public List<RentalRecord> getAllRentals() {
        return rentalRecordRepository.findAll();
    }

    @Override
    public RentalRecord getRentalById(Long rentalId) {
        return rentalRecordRepository.findById(rentalId)
                .orElseThrow(() -> new RentalRecordNotFoundException(rentalId));
    }

    @Override
    public DashboardMetricsDTO getDashboard() {

        long totalCustomers = customerRepository.count();
        long totalProducts = productRepository.count();
        long activeRentals = rentalRecordRepository.countByStatus("ACTIVE");
        long overdueRentals = rentalRecordRepository.countByStatus("OVERDUE");
        Double totalRentCollected= rentPaymentRepository.findTotalRentCollected();
        String topCategory = productRepository.findTopCategoryByRevenue(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse("");
        String highestPayingCustomer = customerRepository.findHighestPayingCustomer(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse("");
        return new DashboardMetricsDTO(
                totalCustomers,
                totalProducts,
                activeRentals,
                overdueRentals,
                totalRentCollected == null ? 0.0 : totalRentCollected,
                topCategory,
                highestPayingCustomer
        );
    }

}
