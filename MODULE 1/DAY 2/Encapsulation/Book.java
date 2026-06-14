public class Book {
    private String title;
    private String author;
    private int pages;

    public String gettitle(String title){
        return title;
    }
    public String getauthor(String author){
        return author;
    }
    public int getpages(int pages){
        return pages;
    }

    public Book(String title,String author,int pages){
        this.title=title;
        this.author=author;
        this.pages=pages;
    }

    public void read(){
        System.out.println("I read the book: " + title + "of " +pages + "--" + author);
    }

    public void getSummary(){
           System.out.println("The summary of the "+ title +" is "+"The Hobbit is the story of Bilbo Baggins, a hobbit who embarks on an adventurous quest with dwarves to reclaim their treasure from the dragon Smaug and discovers his own bravery ");
    }
}
