public class Movie{
      private String title;
      private String director;
      private int releaseYear;

      public String gettitle(){
        return title;
    
      }
      public String getdirector(){
        return director;
      }

      public int releaseYear(){
        return releaseYear;

      }

       public Movie(String title,String director,int releaseYear){
        this.title=title;
        this.director=director;
        this.releaseYear=releaseYear;
    }

    public void play(){
        System.out.println("I am watching the movie: "+ title+ " directed by "+director+" which was released in the year "+releaseYear);
    }

    public void getRating(){
        System.out.println("What rating will you give for the movie: "+title);
    }
}