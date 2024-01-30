package music;

import java.util.*;

public class PlaylistLibrary {

    private ArrayList<Playlist> songLibrary; // contains various playlists

    public PlaylistLibrary(ArrayList<Playlist> songLibrary) {
        this.songLibrary = songLibrary;
    }

    public PlaylistLibrary() {
        this(null);
    }

    public Playlist createPlaylist(String filename) {

        
        StdIn.setFile(filename);
        int linecount = 0;

        Playlist p = new Playlist();

        // ArrayList<String> songnodescopy = new ArrayList<String>();
        ArrayList<SongNode> songnodescopy = new ArrayList<SongNode>();
        if (!songnodescopy.isEmpty()){
            songnodescopy.clear();
        }else{ songnodescopy = new ArrayList<SongNode>();
        }
        while (!StdIn.isEmpty()){



            String[] data = StdIn.readLine().split(",");
            String name = data[0];
            String artist = data[1];
            int year = Integer.parseInt(data[2]);
            int pop = Integer.parseInt(data[3]);
            String link = data[4];


            Song sng = new Song(name, artist, year, pop, link);
            SongNode s = new SongNode();
            s.setSong(sng);
            
            
            songnodescopy.add(s);
            // StdOut.println(songnodescopy.get(songnodescopy.size()-1));

            // s.setNext(p.getLast());

            // p.setLast(s);
            
            linecount += 1;

            p.setSize(linecount);


        }

        for (int i = 0; i < linecount-1; i++){
            songnodescopy.get(i).setNext(songnodescopy.get(i+1));
        }
        if (linecount >= 1){
            songnodescopy.get(songnodescopy.size()-1).setNext(songnodescopy.get(0));
        }



        while (!songnodescopy.isEmpty()){
            p.setLast(songnodescopy.get(0));
            songnodescopy.remove(0);
        }

        // StdOut.println("\n" + p.getLast().getNext());

        if (p.getSize() >= 1){return p;}
        else {return new Playlist(null, 0);}

    }

    public void addPlaylist(String filename, int playlistIndex) {
        

        if ( songLibrary == null ) {
            songLibrary = new ArrayList<Playlist>();
        }
        if ( playlistIndex >= songLibrary.size() ) {
            songLibrary.add(createPlaylist(filename));
        } else {
            songLibrary.add(playlistIndex, createPlaylist(filename));
        }        
    }

    public boolean removePlaylist(int playlistIndex) {

        if ( songLibrary == null || playlistIndex >= songLibrary.size() ) {
            return false;
        }

        songLibrary.remove(playlistIndex);
            
        return true;
    }
    
    public void addAllPlaylists(String[] filenames) {
        
        if (songLibrary == null) songLibrary = new ArrayList<Playlist>();

        for (int i = 0; i < filenames.length; i++){
            songLibrary.add(i,createPlaylist(filenames[i]));
        }

    }

    public boolean insertSong(int playlistIndex, int position, Song song) {

        if (songLibrary == null) return false;

        Playlist playlistpointer;

        if ((playlistIndex > songLibrary.size()-1) || (playlistIndex < 0) || (position < 1)){
            return false;
        }else{
            playlistpointer = songLibrary.get(playlistIndex);
        }


        SongNode songpointer;

        if ((playlistpointer.getLast().getNext() == playlistpointer.getLast().getNext().getNext()) && (position == 1)){
            SongNode s = new SongNode();
            s.setSong(song);
            s.setNext(playlistpointer.getLast());
            playlistpointer.getLast().setNext(s);
            playlistpointer.setSize(playlistpointer.getSize()+1);
            return true;
        }

        if (playlistpointer.getLast() == null){
            if (position > 1) return false;
            SongNode s = new SongNode();
            s.setSong(song);
            s.setNext(s);
            playlistpointer.setLast(s);
            playlistpointer.setSize(playlistpointer.getSize()+1);

            return true;
        }
        else{
            songpointer = playlistpointer.getLast();
        }

        if(position > songLibrary.get(playlistIndex).getSize()+1) return false;


        if (position == 0){
            // SongNode s = new SongNode();
            // s.setSong(song);
            // s.setNext(songpointer.getNext());
            // songpointer.setNext(s);
            // playlistpointer.setLast(s);
        }else if(position < 0){
            // SongNode s = new SongNode();
            // s.setSong(song);

            // for (int i = 1; i < position+playlistpointer.getSize(); i++){
            //     songpointer = songpointer.getNext();
            // }

            // s.setNext(songpointer.getNext());
            // songpointer.setNext(s);



        }else if(position >= playlistpointer.getSize()){
            SongNode s = new SongNode();
            s.setSong(song);

            s.setNext(songpointer.getNext());
            songpointer.setNext(s);
            playlistpointer.setLast(s);
        }else{
            SongNode s = new SongNode();
            s.setSong(song);
            
            for (int i = 1; i < position; i++){
                songpointer = songpointer.getNext();
            }

            s.setNext(songpointer.getNext());

            songpointer.setNext(s);
        }
        playlistpointer.setSize(playlistpointer.getSize()+1);




        return true;
    }

    public boolean removeSong(int playlistIndex, Song song) {

        if ((songLibrary == null) || (songLibrary.get(playlistIndex).getLast() == null) || (playlistIndex > songLibrary.size()-1) || (playlistIndex < 0)) return false;


        SongNode songpointer = songLibrary.get(playlistIndex).getLast().getNext();
        SongNode t = songLibrary.get(playlistIndex).getLast();
        

        if ((song.equals(songpointer.getSong()) && (songpointer.getSong() == songpointer.getNext().getSong()))){
            songpointer.setNext(null);
            songLibrary.get(playlistIndex).setLast(null);
            songLibrary.get(playlistIndex).setSize(songLibrary.get(playlistIndex).getSize()-1);
            return true;
        }

        if (song.equals(t.getNext().getSong()) && (songLibrary.get(playlistIndex).getSize() < 2)){
            t.setNext(t);
            songLibrary.get(playlistIndex).setSize(songLibrary.get(playlistIndex).getSize()-1);
            songLibrary.get(playlistIndex).setLast(t);
            return true;
        }

        if (song.equals(t.getNext().getSong()) && (songLibrary.get(playlistIndex).getSize() >= 2)){
            t.setNext(t.getNext().getNext());
            songLibrary.get(playlistIndex).setSize(songLibrary.get(playlistIndex).getSize()-1);
            songLibrary.get(playlistIndex).setLast(t);
            return true;
        }
        



        for (int i = 0; i < songLibrary.get(playlistIndex).getSize()-2; i++){
            songpointer = songpointer.getNext();
            if (song.equals(songpointer.getNext().getSong())){
                songpointer.setNext(songpointer.getNext().getNext());
                songLibrary.get(playlistIndex).setSize(songLibrary.get(playlistIndex).getSize()-1);
                for (int j = 0; j < songLibrary.get(playlistIndex).getSize()-i-2; j++){
                    songpointer = songpointer.getNext();
                }
                songLibrary.get(playlistIndex).setLast(songpointer);
                return true;
            }
        }

        
        
    
        
        

        return false; // update the return value
    }

    /**
     * This method reverses the playlist located at playlistIndex
     * 
     * Each node in the circular linked list will point to the element that 
     * came before it.
     * 
     * After the list is reversed, the playlist located at playlistIndex will 
     * reference the first SongNode in the original playlist (new last).
     * 
     * @param playlistIndex the playlist to reverse
     */
    public void reversePlaylist(int playlistIndex) {
        // WRITE YOUR CODE HERE


        if ((songLibrary == null) || (songLibrary.get(playlistIndex).getLast() == null) || (playlistIndex > songLibrary.size()-1) || (playlistIndex < 0)){ return;}


        SongNode prev = null;
        SongNode current = songLibrary.get(playlistIndex).getLast().getNext();
        SongNode next = null;

        while (current != null){
            next = current.getNext();
            current.setNext(prev);
            prev = current;
            current = next;
        }

        songLibrary.get(playlistIndex).setLast(prev);


    }

    /**
     * This method merges two playlists.
     * 
     * Both playlists have songs in decreasing popularity order. The resulting 
     * playlist will also be in decreasing popularity order.
     * 
     * You may assume both playlists are already in decreasing popularity 
     * order. If the songs have the same popularity, add the song from the 
     * playlist with the lower playlistIndex first.
     * 
     * After the lists have been merged:
     *  - store the merged playlist at the lower playlistIndex
     *  - remove playlist at the higher playlistIndex 
     * 
     * 
     * @param playlistIndex1 the first playlist to merge into one playlist
     * @param playlistIndex2 the second playlist to merge into one playlist
     */
    public void mergePlaylists(int playlistIndex1, int playlistIndex2){
        // WRITE YOUR CODE HERE

        if ((songLibrary == null) || (playlistIndex1 > songLibrary.size()-1) || (playlistIndex2 > songLibrary.size()-1) || (playlistIndex1 < 0) || (playlistIndex2 < 0))return; 

        if ( (songLibrary.get(playlistIndex1).getSize() < 1)){
            removePlaylist(playlistIndex1);
            return;
        }
        if (songLibrary.get(playlistIndex2).getSize() < 1){
            removePlaylist(playlistIndex2);
            return;
        }
        

        Playlist mergeplaylist = songLibrary.get(playlistIndex1);
        Playlist oldplaylist = songLibrary.get(playlistIndex2);

        boolean z = false;
            
        
        if (playlistIndex1 > playlistIndex2){
            mergeplaylist = songLibrary.get(playlistIndex2);
            oldplaylist = songLibrary.get(playlistIndex1);
            z = true;
        }

        Playlist merged = new Playlist();
        merged.setSize(songLibrary.get(playlistIndex1).getSize() + songLibrary.get(playlistIndex2).getSize());

        SongNode p = mergeplaylist.getLast().getNext();
        SongNode q = oldplaylist.getLast().getNext();
        SongNode newhead = null;
        SongNode sorting = null; 
        // SongNode tail = mergeplaylist.getLast();
        // SongNode tail2 = oldplaylist.getLast();
        // // SongNode head = tail.getNext();
        // // SongNode head2 = tail2.getNext();

        mergeplaylist.getLast().setNext(null);
        oldplaylist.getLast().setNext(null);

        if (p == null) merged = oldplaylist;
        if (q == null) merged = mergeplaylist;


        if ((p != null) && (q != null)){
            if (p.getSong().getPopularity() >= q.getSong().getPopularity()){
                sorting = p;
                p = sorting.getNext();
            }else {
                sorting = q; 
                q = sorting.getNext();
            }
        }

        newhead = sorting;

        while ((p != null) && (q != null)){
            if(p.getSong() == q.getSong()){
                sorting.setNext(sorting.getNext().getNext());
            }
            if (p.getSong().getPopularity() >= q.getSong().getPopularity()){
                sorting.setNext(p);
                sorting = p;
                p = sorting.getNext();
            // }else if (p.getSong().getPopularity() == q.getSong().getPopularity()){
            //     sorting.setNext(q);
            }else {
                sorting.setNext(q);
                sorting = q;
                q = sorting.getNext();
            }
        }
        if (p == null){ sorting.setNext(q);}
        if (q == null){ sorting.setNext(p);}


        mergeplaylist.setLast(null);
        mergeplaylist.setSize(mergeplaylist.getSize()+oldplaylist.getSize());
        oldplaylist.setLast(null);

        if (z){
            removePlaylist(playlistIndex1);
        }else{
            removePlaylist(playlistIndex2);
        }

        SongNode t = newhead;

        for (int i = 0; i < mergeplaylist.getSize()-1; i++){
            t = t.getNext();
        }
        t.setNext(newhead);


        mergeplaylist.setLast(t);
        // for (int i = 0; i < songLibrary.size(); i++){
        //     if (songLibrary.get(i).getLast() != (mergeplaylist.getLast())){
        //         removePlaylist(i);
        //     }
        // }

    }

    /**
     * This method shuffles a specified playlist using the following procedure:
     * 
     * 1. Create a new playlist to store the shuffled playlist in.
     * 
     * 2. While the size of the original playlist is not 0, randomly generate a number 
     * using StdRandom.uniformInt(1, size+1). Size contains the current number
     * of items in the original playlist.
     * 
     * 3. Remove the corresponding node from the original playlist and insert 
     * it into the END of the new playlist (1 being the first node, 2 being the 
     * second, etc). 
     * 
     * 4. Update the old playlist with the new shuffled playlist.
     *    
     * @param index the playlist to shuffle in songLibrary
     */
    public void shufflePlaylist(int playlistIndex) {
        // WRITE YOUR CODE HERE

        Playlist original = songLibrary.get(playlistIndex);

        if (original.getSize() < 1) return;

        Playlist newp = new Playlist();
        newp.setSize(original.getSize());

        ArrayList<SongNode> z = new ArrayList<SongNode>(original.getSize());

        int num = 0;

        SongNode a = original.getLast();
        SongNode last = original.getLast();

        while(original.getSize() != 0){
            num = StdRandom.uniformInt(original.getSize()+1);
            for(int i = 0; i < num; i++){
                a = a.getNext();
            }
            
            SongNode t = new SongNode(a.getNext().getSong(), null);

            a.setNext(a.getNext().getNext());
            for (int i = 0; i < num; i++){
                original.setLast(original.getLast().getNext());
            }
            original.getLast().setNext(original.getLast().getNext());

            z.add(t);

            if (last.getSong() == t.getSong()){
                for (int i = 0; i < num; i++){
                    last = last.getNext();
                }
            }
            while (original.getLast().getSong() != last.getSong()){
                original.setLast(original.getLast().getNext());
            }
            
            last = original.getLast();

            a = original.getLast();
            
            original.setSize(original.getSize()-1);

        }
        original.setLast(null);
        original.setSize(z.size());
        original.setLast(z.get(0));

        SongNode t = original.getLast();

        for (int i = 1; i < z.size(); i++){
            t.setNext(z.get(i));
            t = t.getNext();
            
        }

        t.setNext(z.get(0));

        for (int i = 0; i < z.size(); i++){
            z.remove(i);
        }

        for (int i = 0; i < original.getSize()-1; i++){
            original.setLast(original.getLast().getNext());
        }


    }

    /**
     * This method sorts a specified playlist using linearithmic sort.
     * 
     * Set the playlist located at the corresponding playlistIndex
     * in decreasing popularity index order.
     * 
     * This method should  use a sort that has O(nlogn), such as with merge sort.
     * 
     * @param playlistIndex the playlist to shuffle
     */
    public void sortPlaylist ( int playlistIndex ) {

        // WRITE YOUR CODE HERE
        
    }

    /**
     * ****DO NOT**** UPDATE THIS METHOD
     * Plays playlist by index; can use this method to debug.
     * 
     * @param playlistIndex the playlist to print
     * @param repeats number of times to repeat playlist
     * @throws InterruptedException
     */
    public void playPlaylist(int playlistIndex, int repeats) {
        /* DO NOT UPDATE THIS METHOD */

        final String NO_SONG_MSG = " has no link to a song! Playing next...";
        if (songLibrary.get(playlistIndex).getLast() == null) {
            StdOut.println("Nothing to play.");
            return;
        }

        SongNode ptr = songLibrary.get(playlistIndex).getLast().getNext(), first = ptr;

        do {
            StdOut.print("\r" + ptr.getSong().toString());
            if (ptr.getSong().getLink() != null) {
                StdAudio.play(ptr.getSong().getLink());
                for (int ii = 0; ii < ptr.getSong().toString().length(); ii++)
                    StdOut.print("\b \b");
            }
            else {
                StdOut.print(NO_SONG_MSG);
                try {
                    Thread.sleep(2000);
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
                for (int ii = 0; ii < NO_SONG_MSG.length(); ii++)
                    StdOut.print("\b \b");
            }

            ptr = ptr.getNext();
            if (ptr == first) repeats--;
        } while (ptr != first || repeats > 0);
    }

    /**
     * ****DO NOT**** UPDATE THIS METHOD
     * Prints playlist by index; can use this method to debug.
     * 
     * @param playlistIndex the playlist to print
     */
    public void printPlaylist(int playlistIndex) {
        StdOut.printf("%nPlaylist at index %d (%d song(s)):%n", playlistIndex, songLibrary.get(playlistIndex).getSize());
        if (songLibrary.get(playlistIndex).getLast() == null) {
            StdOut.println("EMPTY");
            return;
        }
        SongNode ptr;
        for (ptr = songLibrary.get(playlistIndex).getLast().getNext(); ptr != songLibrary.get(playlistIndex).getLast(); ptr = ptr.getNext() ) {
            StdOut.print(ptr.getSong().toString() + " -> ");
        }
        if (ptr == songLibrary.get(playlistIndex).getLast()) {
            StdOut.print(songLibrary.get(playlistIndex).getLast().getSong().toString() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    public void printLibrary() {
        if (songLibrary.size() == 0) {
            StdOut.println("\nYour library is empty!");
        } else {
                for (int ii = 0; ii < songLibrary.size(); ii++) {
                printPlaylist(ii);
            }
        }
    }

    /*
     * Used to get and set objects.
     * DO NOT edit.
     */
     public ArrayList<Playlist> getPlaylists() { return songLibrary; }
     public void setPlaylists(ArrayList<Playlist> p) { songLibrary = p; }
}
