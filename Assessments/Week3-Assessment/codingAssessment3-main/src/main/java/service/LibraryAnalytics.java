package service;

import entity.Book;

import java.util.*;
import java.util.stream.Collectors;

public class LibraryAnalytics {

    private Map<String, Book> books = new HashMap<>();

    public void loadBooks(List<String> records) {

        if(records==null){
            return;
        }
        for(String record:records){
            if(record==null||record.trim().isEmpty()){
                continue;
            }
            String[] data=record.trim().split("\\|");
            if(data.length!=6){
                continue;
            }
            String bookId=data[0].trim();
            String title=data[1].trim();
            String author=data[2].trim();
            String category=data[3].trim();
            int borrowCount = 0;
            double rating=0.0;
            try{
                borrowCount=Integer.parseInt(data[4].trim());
                rating=Double.parseDouble(data[5].trim());
            }
            catch(NumberFormatException e){
                continue;
            }

            if(bookId.isEmpty()||title.isEmpty()||author.isEmpty()||category.isEmpty()||rating < 0 || rating > 5
                    || borrowCount < 0){
                continue;
            }
            Book current=new Book(bookId,title,author,category,borrowCount,rating);
            Book existing=books.get(bookId);
            if(existing==null) {
                books.put(bookId, current);
            }
            else{
                boolean replace=false;
                if(current.getRating()> existing.getRating()){
                    replace=true;
                }
                else if(current.getRating()== existing.getRating()){
                    if(current.getBorrowCount()> existing.getBorrowCount()){
                        replace=true;
                    } else if (current.getBorrowCount()== existing.getBorrowCount()) {
                        if(current.getTitle().compareTo(existing.getTitle())<0){
                            replace=true;
                        }

                    }
                }
                if(replace){
                    books.put(bookId,current);
                }
            }

        }
    }

    public List<Book> topRatedBooks(int n) {

        if(n <= 0) {
            return new ArrayList<>();
        }

        return books.values()
                .stream()
                .sorted(Comparator.comparingDouble(Book::getRating)
                                .reversed()
                                .thenComparingInt(Book::getBorrowCount)
                                .reversed()
                                .thenComparing(Book::getTitle)
                )
                .limit(n)
                .toList();
    }

    public Map<String, Double> averageRatingByCategory() {

        TreeMap<String,Double> map=new TreeMap<>();
        books.values().stream()
                .collect(Collectors.groupingBy(Book::getCategory, Collectors.averagingDouble(Book::getRating)))
                .forEach((k,v)-> map.put(k,Math.round(v*100.0)/100.0));
        return map;

    }

    public Optional<Book> mostBorrowedBook() {

            return books.values().stream()
                    .max(Comparator.comparing(Book::getBorrowCount)
                            .thenComparing(Book::getRating)
                            .thenComparing(Book::getTitle,Comparator.reverseOrder())
                    );
    }


    public Set<String> authorsWithMultipleCategories() {

        TreeSet<String> result = new TreeSet<>();

        books.values().stream().collect(Collectors.groupingBy(Book::getAuthor,Collectors.mapping(Book::getCategory, Collectors.toSet())))
                .forEach((author, categories) -> {

                    if(categories.size() > 1) {
                        result.add(author);
                    }
                });

        return result;
    }
    public Map<String,List<Book>> groupBooksByAuthor() {

        LinkedHashMap<String,List<Book>> map =
                new LinkedHashMap<>();

        books.values()
                .stream()
                .collect(Collectors.groupingBy(Book::getAuthor))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {

                    List<Book> list =
                            entry.getValue()
                                    .stream()
                                    .sorted(Comparator.comparingDouble(Book::getRating).reversed()
                                            .thenComparing(Comparator.comparingInt(Book::getBorrowCount).reversed()))
                                    .toList();

                    map.put(entry.getKey(), list);
                });

        return map;
    }

    public List<String> suspiciousBooks() {

        Set<String> result = new TreeSet<>();

        Map<String,Double> avgBorrowCount = books.values().stream().collect(Collectors.groupingBy(Book::getCategory, Collectors.averagingInt(Book::getBorrowCount)));

        Map<String,Double> avgRating =books.values().stream().collect(Collectors.groupingBy(Book::getCategory, Collectors.averagingDouble(Book::getRating)));
        for(Book b : books.values()) {

            String title = b.getTitle();

            boolean suspicious = false;

            String[] words =
                    title.toLowerCase().split("\\s+");

            for(int i = 0; i < words.length - 1; i++) {

                if(words[i].equals(words[i + 1])) {
                    suspicious = true;
                }
            }
            if(title.toLowerCase()
                    .contains(
                            b.getAuthor().toLowerCase()
                    )) {

                suspicious = true;
            }

            double categoryAvgBorrow =
                    avgBorrowCount.get(
                            b.getCategory()
                    );

            if(b.getBorrowCount()
                    > 3 * categoryAvgBorrow) {

                suspicious = true;
            }

            double categoryAvgRating =
                    avgRating.get(
                            b.getCategory()
                    );

            if(suspicious) {
                result.add(title);
            }
        }

        return new ArrayList<>(result);

    }

    public Map<String, Map<String, Book>> categoryWiseTopRatedBookByEachAuthor() {

        return books.values()
                .stream()
                .collect(Collectors.groupingBy(Book::getCategory,
                        Collectors.collectingAndThen(Collectors.groupingBy(Book::getAuthor,
                                        Collectors.collectingAndThen(
                                                        Collectors.maxBy(
                                                                Comparator.comparingDouble(
                                                                        Book::getRating
                                                                )
                                                        ),
                                                        Optional::get
                                                )
                                        ),
                                        x -> x
                                )
                        )
                );
    }
}

