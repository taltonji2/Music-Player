package view;

public class Song {

    private String songTitle;
    private String artist;
    private int year;

    public Song() {
        this.songTitle = null;
        this.artist = null;
        this.year = 0;
    }

    public Song(String songTitle, String artist, int year) {
        this.songTitle = songTitle;
        this.artist = artist;
        this.year = year;
    }

    public String getSong() {
        return songTitle;
    }

    public void setSong(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
    

