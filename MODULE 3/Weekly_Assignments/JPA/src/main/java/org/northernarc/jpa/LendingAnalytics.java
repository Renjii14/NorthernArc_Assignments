package org.northernarc.jpa;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LendingAnalytics {

    private Map<String, LoanApplication> applications =
            new HashMap<>();

    public void loadApplications(List<String> records) {

        if (records == null) {
            return;
        }

        for (String record : records) {

            if (record == null || record.trim().isEmpty()) {
                continue;
            }

            String[] data = record.trim().split("\\|");

            if (data.length != 6) {
                continue;
            }

            String applicationId = data[0].trim();
            String customerName = data[1].trim();
            String lenderName = data[2].trim();
            String loanType = data[3].trim();

            double loanAmount;
            int creditScore;

            try {
                loanAmount = Double.parseDouble(data[4].trim());
                creditScore = Integer.parseInt(data[5].trim());
            } catch (Exception e) {
                continue;
            }

            if (applicationId.isEmpty()
                    || customerName.isEmpty()
                    || lenderName.isEmpty()
                    || loanType.isEmpty()
                    || loanAmount <= 0
                    || creditScore < 300
                    || creditScore > 900) {
                continue;
            }

            LoanApplication current =
                    new LoanApplication(
                            applicationId,
                            customerName,
                            lenderName,
                            loanType,
                            loanAmount,
                            creditScore
                    );

            LoanApplication existing =
                    applications.get(applicationId);

            if (existing == null) {

                applications.put(applicationId, current);
            } else {

                boolean replace = false;

                if (current.getCreditScore()
                        > existing.getCreditScore()) {

                    replace = true;
                } else if (current.getCreditScore()
                        == existing.getCreditScore()) {

                    if (current.getLoanAmount()
                            < existing.getLoanAmount()) {

                        replace = true;
                    } else if (current.getLoanAmount()
                            == existing.getLoanAmount()) {

                        if (current.getCustomerName()
                                .compareTo(
                                        existing.getCustomerName()) < 0) {

                            replace = true;
                        }
                    }
                }

                if (replace) {
                    applications.put(applicationId, current);
                }
            }
        }
    }

    public List<LoanApplication> topCreditProfiles(int n) {

        return applications.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                        LoanApplication::getCreditScore)
                                .reversed()
                                .thenComparing(
                                        LoanApplication::getLoanAmount)
                                .thenComparing(
                                        LoanApplication::getCustomerName)
                )
                .limit(n)
                .toList();
    }

    public Map<String, Double> averageLoanAmountByType() {

        TreeMap<String, Double> result =
                new TreeMap<>();

        applications.values()
                .stream()
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                LoanApplication::getLoanType,
                                java.util.stream.Collectors
                                        .averagingDouble(
                                                LoanApplication::getLoanAmount
                                        )
                        )
                )
                .forEach((k, v) ->
                        result.put(
                                k,
                                Math.round(v * 100.0) / 100.0
                        ));

        return result;
    }

    public Optional<LoanApplication> highestLoanApplication() {

        return applications.values()
                .stream()
                .max(
                        Comparator
                                .comparing(
                                        LoanApplication::getLoanAmount)
                                .thenComparing(
                                        LoanApplication::getCreditScore)
                                .thenComparing(
                                        LoanApplication::getApplicationId,
                                        Comparator.reverseOrder()
                                )
                );
    }

    public Set<String> lendersWithMultipleLoanTypes() {

        TreeSet<String> result =
                new TreeSet<>();

        applications.values()
                .stream()
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                LoanApplication::getLenderName,
                                java.util.stream.Collectors.mapping(
                                        LoanApplication::getLoanType,
                                        java.util.stream.Collectors.toSet()
                                )
                        )
                )
                .forEach((lender, types) -> {

                    if (types.size() > 1) {
                        result.add(lender);
                    }
                });

        return result;
    }

    public Map<String, List<LoanApplication>>
    groupApplicationsByLender() {

        LinkedHashMap<String, List<LoanApplication>>
                result = new LinkedHashMap<>();

        applications.values()
                .stream()
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                LoanApplication::getLenderName
                        )
                )
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {

                    List<LoanApplication> list =
                            entry.getValue()
                                    .stream()
                                    .sorted(
                                            Comparator
                                                    .comparing(
                                                            LoanApplication::getCreditScore)
                                                    .reversed()
                                                    .thenComparing(
                                                            LoanApplication::getLoanAmount
                                                    )
                                    )
                                    .toList();

                    result.put(
                            entry.getKey(),
                            list
                    );
                });

        return result;
    }

    public List<String> suspiciousApplications() {

        Set<String> result =
                new TreeSet<>();

        Map<String, Double> avgLoanAmount =
                applications.values()
                        .stream()
                        .collect(
                                java.util.stream.Collectors.groupingBy(
                                        LoanApplication::getLoanType,
                                        java.util.stream.Collectors
                                                .averagingDouble(
                                                        LoanApplication::getLoanAmount
                                                )
                                )
                        );

        Map<String, Double> avgCreditScore =
                applications.values()
                        .stream()
                        .collect(
                                java.util.stream.Collectors.groupingBy(
                                        LoanApplication::getLoanType,
                                        java.util.stream.Collectors
                                                .averagingDouble(
                                                        LoanApplication::getCreditScore
                                                )
                                )
                        );

        Map<String, Set<String>> customerLenders =
                new HashMap<>();

        for (LoanApplication app : applications.values()) {

            customerLenders
                    .computeIfAbsent(
                            app.getCustomerName()
                                    .toLowerCase(),
                            k -> new HashSet<>()
                    )
                    .add(
                            app.getLenderName()
                                    .toLowerCase()
                    );
        }

        for (LoanApplication app : applications.values()) {

            String customer =
                    app.getCustomerName();

            boolean suspicious = false;

            // Condition 1

            String[] words =
                    customer.toLowerCase()
                            .split("\\s+");

            for (int i = 0; i < words.length - 1; i++) {

                if (words[i]
                        .equals(words[i + 1])) {

                    suspicious = true;
                }
            }

            // Condition 2

            if (customer.toLowerCase()
                    .contains(
                            app.getLenderName()
                                    .toLowerCase())) {

                suspicious = true;
            }

            // Condition 3

            double avgAmount =
                    avgLoanAmount.get(
                            app.getLoanType());

            if (app.getLoanAmount()
                    > avgAmount * 2.5) {

                suspicious = true;
            }

            // Condition 4

            double avgScore =
                    avgCreditScore.get(
                            app.getLoanType());

            if (app.getCreditScore()
                    < avgScore
                    &&
                    app.getLoanAmount()
                            > avgAmount) {

                suspicious = true;
            }

            // Condition 5

            if (words.length > 3) {
                suspicious = true;
            }

            // Condition 6

            if (customerLenders.get(
                            customer.toLowerCase())
                    .size() > 3) {

                suspicious = true;
            }

            // Condition 7

            long samePatternCount =
                    applications.values()
                            .stream()
                            .filter(a ->
                                    a.getLoanType()
                                            .equals(
                                                    app.getLoanType())
                                            &&
                                            a.getLoanAmount()
                                                    == app.getLoanAmount()
                                            &&
                                            a.getCreditScore()
                                                    == app.getCreditScore()
                                            &&
                                            !a.getCustomerName()
                                                    .equalsIgnoreCase(
                                                            customer)
                            )
                            .count();

            if (samePatternCount > 0) {
                suspicious = true;
            }

            // Condition 8

            String sortedName =
                    customer.replaceAll("\\s+", "")
                            .toLowerCase()
                            .chars()
                            .sorted()
                            .collect(
                                    StringBuilder::new,
                                    StringBuilder::appendCodePoint,
                                    StringBuilder::append
                            )
                            .toString();

            long anagramCount =
                    applications.values()
                            .stream()
                            .filter(a ->
                                    a.getLenderName()
                                            .equalsIgnoreCase(
                                                    app.getLenderName()))
                            .filter(a -> {

                                String other =
                                        a.getCustomerName()
                                                .replaceAll("\\s+", "")
                                                .toLowerCase()
                                                .chars()
                                                .sorted()
                                                .collect(
                                                        StringBuilder::new,
                                                        StringBuilder::appendCodePoint,
                                                        StringBuilder::append
                                                )
                                                .toString();

                                return sortedName.equals(other)
                                        &&
                                        !a.getCustomerName()
                                                .equalsIgnoreCase(
                                                        customer);
                            })
                            .count();

            if (anagramCount > 0) {
                suspicious = true;
            }

            if (suspicious) {
                result.add(customer);
            }
        }

        return new ArrayList<>(result);
    }

    public Map<String, Map<String, Optional<LoanApplication>>> loanTypeWiseTopApplicantByLender() {

        return applications.values()
                .stream()
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                LoanApplication::getLoanType,
                                java.util.stream.Collectors.groupingBy(
                                        LoanApplication::getLenderName,
                                        java.util.stream.Collectors
                                                .collectingAndThen(
                                                        java.util.stream.Collectors.maxBy(
                                                                Comparator
                                                                        .comparing(
                                                                                LoanApplication::getCreditScore)
                                                                        .thenComparing(
                                                                                LoanApplication::getLoanAmount)
                                                        ),
                                                        x -> x
                                                )
                                )
                        )
                );
    }
}
